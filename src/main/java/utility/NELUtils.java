/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author pierpaolo
 */
public class NELUtils {

    /**
     * TAGME application token and user agent for http requests
     */
    public static final String TOKEN = "f28fd60a-f58d-4ae6-8d5a-be2e9b481be3-843339462";

    private static final String USER_AGENT = "Mozilla/5.0";

    /**
     * Call tagme using HTTP GET
     * @param text Text to process
     * @param longtext Flag for processing long text
     * @return TAGME json
     * @throws UnsupportedEncodingException
     * @throws MalformedURLException
     * @throws IOException
     */
    public static String tagmeGET(String text, boolean longtext) throws UnsupportedEncodingException, MalformedURLException, IOException {
        String urlstr = "https://tagme.d4science.org/tagme/tag?lang=en&gcube-token=" + TOKEN + "&text=";
        if (longtext) {
            urlstr = "https://tagme.d4science.org/tagme/tag?lang=en&gcube-token=" + TOKEN + "&long_text=5&text=";
        }
        urlstr += URLEncoder.encode(text, "utf-8");
        URL url = new URL(urlstr);
        InputStream inputStream = url.openStream();
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        while (reader.ready()) {
            sb.append(reader.readLine());
            sb.append("\n");
        }
        reader.close();
        inputStream.close();
        return sb.toString();
    }

    /**
     * Call tagme using HTTP POST
     * @param text Text to process
     * @param longtext Flag for processing long text
     * @return TAGME json
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public static String tagmePOST(String text, boolean longtext) throws UnsupportedEncodingException, IOException {
        String url = "https://tagme.d4science.org/tagme/tag";
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(600 * 1000)
                .setConnectionRequestTimeout(600 * 1000)
                .setSocketTimeout(600 * 1000).build();
        HttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("User-Agent", USER_AGENT);
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("lang", "en"));
        urlParameters.add(new BasicNameValuePair("gcube-token", TOKEN));
        urlParameters.add(new BasicNameValuePair("text", text));
        if (longtext) {
            urlParameters.add(new BasicNameValuePair("long_text", "5"));
        }
        httpPost.setEntity(new UrlEncodedFormEntity(urlParameters));
        HttpResponse response = httpClient.execute(httpPost);
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
            sb.append("\n");
        }
        reader.close();
        return sb.toString();
    }

    //check intersection between two spots in the text
    private static boolean intersec(int s1, int e1, int s2, int e2) {
        if (e1 < s2) {
            return false;
        }
        return (s1 >= s2 && e1 <= e2) || (s1 <= s2 && e1 >= e2) || (s1 <= s2 && e1 <= e2) || (s1 >= s2 && e1 >= e2);
    }

    //check if the annotation is valid. An annotation is valid if it overcomes the threshold and it is the best annotation (length+rho)
    private static boolean validAnnotation(JSONArray annotations, int index, int start, int end, double threshold) {
        boolean valid = true;
        for (int i = index - 1; i >= 0; i--) {
            JSONObject spot = (JSONObject) annotations.get(i);
            double rho = 0;
            if (spot.get("rho") instanceof Double) {
                rho = (Double) spot.get("rho");
            } else if (spot.get("rho") instanceof Long) {
                rho = ((Long) spot.get("rho")).doubleValue();
            }
            if (rho >= threshold) {
                Long cstart = (Long) spot.get("start");
                Long cend = (Long) spot.get("end");
                if (!intersec(start, end, cstart.intValue(), cend.intValue())) {
                    break;
                } else {
                    if ((end - start) <= (cend - cstart)) {
                        valid = false;
                        break;
                    }
                }
            }
        }
        if (valid) {
            for (int i = index + 1; i < annotations.size(); i++) {
                JSONObject spot = (JSONObject) annotations.get(i);
                double rho = 0;
                if (spot.get("rho") instanceof Double) {
                    rho = (Double) spot.get("rho");
                } else if (spot.get("rho") instanceof Long) {
                    rho = ((Long) spot.get("rho")).doubleValue();
                }
                if (rho >= threshold) {
                    Long cstart = (Long) spot.get("start");
                    Long cend = (Long) spot.get("end");
                    if (!intersec(start, end, cstart.intValue(), cend.intValue())) {
                        break;
                    } else {
                        if ((end - start) < (cend - cstart)) {
                            valid = false;
                            break;
                        }
                    }
                }
            }
        }
        return valid;
    }

    /**
     * Process the tagme json and replace the text with entities
     * @param text Original content
     * @param strJson tagme json
     * @param threshold rho threshold
     * @return New text
     * @throws ParseException
     */
    public static String processJSON(String text, String strJson, double threshold) throws ParseException {
        StringBuilder sb = new StringBuilder();
        JSONParser parser = new JSONParser();
        JSONObject response = (JSONObject) parser.parse(strJson);
        JSONArray annotations = (JSONArray) response.get("annotations");
        int lastIdx = 0;
        for (int i = 0; i < annotations.size(); i++) {
            JSONObject spot = (JSONObject) annotations.get(i);
            double rho = 0;
            if (spot.get("rho") instanceof Double) {
                rho = (Double) spot.get("rho");
            } else if (spot.get("rho") instanceof Long) {
                rho = ((Long) spot.get("rho")).doubleValue();
            }
            if (rho >= threshold) {
                Long start = (Long) spot.get("start");
                Long end = (Long) spot.get("end");
                if (validAnnotation(annotations, i, start.intValue(), end.intValue(), threshold)) {
                    if (spot.get("title") != null) {
                        String title = (String) spot.get("title").toString().replace(" ", "_");
                        sb.append(text.substring(lastIdx, start.intValue()));
                        sb.append(title);
                        lastIdx = end.intValue();
                    }
                }
            }
        }
        if (lastIdx < text.length()) {
            sb.append(text.substring(lastIdx));
        }
        return sb.toString();
    }

    /**
     *
     * @param strJson
     * @param threshold
     * @return
     * @throws ParseException
     */
    public static String processJSONEntity(String strJson, double threshold) throws ParseException {
        StringBuilder sb = new StringBuilder();
        JSONParser parser = new JSONParser();
        JSONObject response = (JSONObject) parser.parse(strJson);
        JSONArray annotations = (JSONArray) response.get("annotations");
        for (int i = 0; i < annotations.size(); i++) {
            JSONObject spot = (JSONObject) annotations.get(i);
            double rho = 0;
            if (spot.get("rho") instanceof Double) {
                rho = (Double) spot.get("rho");
            } else if (spot.get("rho") instanceof Long) {
                rho = ((Long) spot.get("rho")).doubleValue();
            }
            if (rho >= threshold) {
                Long start = (Long) spot.get("start");
                Long end = (Long) spot.get("end");
                if (validAnnotation(annotations, i, start.intValue(), end.intValue(), threshold)) {
                    if (spot.get("title") != null) {
                        String title = (String) spot.get("title").toString().replace(" ", "_");
                        sb.append(title);
                        sb.append(" ");
                    }
                }
            }
        }
        return sb.toString();
    }

    /**
     * Process the tagme json and replace the text with entities
     * @param text Original content
     * @param strJson tagme json
     * @param threshold rho threshold
     * @param analyzer the analyzer used for tokenizing the text
     * @return New tokenized text
     * @throws ParseException
     * @throws IOException
     */
    public static String processJSONandTokenize(String text, String strJson, double threshold, Analyzer analyzer) throws ParseException, IOException {
        StringBuilder sb = new StringBuilder();
        JSONParser parser = new JSONParser();
        JSONObject response = (JSONObject) parser.parse(strJson);
        JSONArray annotations = (JSONArray) response.get("annotations");
        int lastIdx = 0;
        for (int i = 0; i < annotations.size(); i++) {
            JSONObject spot = (JSONObject) annotations.get(i);
            double rho = 0;
            if (spot.get("rho") instanceof Double) {
                rho = (Double) spot.get("rho");
            } else if (spot.get("rho") instanceof Long) {
                rho = ((Long) spot.get("rho")).doubleValue();
            }
            if (rho >= threshold) {
                Long start = (Long) spot.get("start");
                Long end = (Long) spot.get("end");
                if (validAnnotation(annotations, i, start.intValue(), end.intValue(), threshold)) {
                    if (spot.get("title") != null) {
                        String title = (String) spot.get("title").toString().replace(" ", "_");
                        List<String> tokens = Utils.getTokens(analyzer, "nel", new StringReader(text.substring(lastIdx, start.intValue())));
                        for (String token : tokens) {
                            sb.append(token);
                            sb.append(" ");
                        }
                        sb.append(title);
                        sb.append(" ");
                        lastIdx = end.intValue();
                    }
                }
            }
        }
        if (lastIdx < text.length()) {
            List<String> tokens = Utils.getTokens(analyzer, "nel", new StringReader(text.substring(lastIdx)));
            for (String token : tokens) {
                sb.append(token);
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        String text = "Barack Obama was a USA president and have not hair.";
        String json = tagmeGET(text, false);
        System.out.println(processJSON(text, json, 0.15));
        System.out.println(processJSONandTokenize(text, json, 0.2, new StandardAnalyzer()));
        json = tagmePOST(text, true);
        System.out.println(processJSON(text, json, 0.15));
        System.out.println(processJSONandTokenize(text, json, 0.2, new StandardAnalyzer()));
    }

}
