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
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 *
 * @author pierpaolo
 */
public class TagmeUtils {

    public static final String token = "f28fd60a-f58d-4ae6-8d5a-be2e9b481be3-843339462";

    public static String process(String text) throws UnsupportedEncodingException, MalformedURLException, IOException {
        String urlstr = "https://tagme.d4science.org/tagme/tag?lang=en&gcube-token=" + token + "&text=";
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

}
