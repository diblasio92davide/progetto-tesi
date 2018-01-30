package utility;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonUtils {
	
	/*
	 * Questo metodo prende in considerazione Tagme con il valore rho predefinito di 0.1
	 */
	public static String rhoFisso(String textDocument, String jsonSTR) throws IOException, ParseException
	{
		JSONParser jParser = new JSONParser();
		Object ob = jParser.parse(jsonSTR);
		
		JSONObject jsonOBJ = (JSONObject)ob;
		int startTag, endTag,
			start = 0, end = 0;
		double rho;
		String title;
		JSONArray annotations = (JSONArray)jsonOBJ.get("annotations");
		StringBuilder output = new StringBuilder("");
		
		for(int i=0; i<annotations.size(); i++)
		{
			startTag = ((Long)((JSONObject)annotations.get(i)).get("start")).intValue();
			endTag = ((Long)((JSONObject)annotations.get(i)).get("end")).intValue();
			rho = (Double)((JSONObject)annotations.get(i)).get("rho");
			
			if(startTag > 0)
			{
				end = startTag;
				System.out.println(start + " " + end);
				output.append(textDocument.substring(start, end));
			}
			
			if(rho >= 0.1)
			{
				title = (String)((JSONObject)annotations.get(i)).get("title");
				output.append(title);
			}
			else
				output.append(textDocument.substring(startTag, endTag));
			
			start = endTag;
		}
		
		if(start < textDocument.length())
			output.append(textDocument.substring(start));

		return output.toString();
	}
	
	/*
	 * Questo metodo prende in considerazione Tagme con il valore minimo di rho passato in input
	 */
	public static String rhoMinimo(String textDocument, String jsonSTR, double rhoVAR) throws IOException, ParseException
	{
		JSONParser jParser = new JSONParser();
		Object ob = jParser.parse(jsonSTR);
		
		JSONObject jsonOBJ = (JSONObject)ob;
		int startTag, endTag,
			start = 0, end = 0;
		double rho;
		String title;
		JSONArray annotations = (JSONArray)jsonOBJ.get("annotations");
		StringBuilder output = new StringBuilder("");
		
		for(int i=0; i<annotations.size(); i++)
		{
			startTag = ((Long)((JSONObject)annotations.get(i)).get("start")).intValue();
			endTag = ((Long)((JSONObject)annotations.get(i)).get("end")).intValue();
			rho = (Double)((JSONObject)annotations.get(i)).get("rho");
			
			if(startTag > 0)
			{
				end = startTag;
				System.out.println(start + " " + end);
				output.append(textDocument.substring(start, end));
			}
			
			if(rho >= rhoVAR)
			{
				title = (String)((JSONObject)annotations.get(i)).get("title");
				output.append(title);
			}
			else
				output.append(textDocument.substring(startTag, endTag));
			
			start = endTag;
		}
		
		if(start < textDocument.length())
			output.append(textDocument.substring(start));

		return output.toString();
	}
	
	/*
	 * Questo metodo è per la lettura di file json NON di prova, poichè diversamente dal file
	 * json di prova che mi ha inviato, che tra l'altro ho  modificato mettendo tutto
	 * sulla stessa riga come avviene nel resto dei json che mi ha inviato, il primo oggetto
	 * che incontra è un JSONArray e non direttamente un JSONObject come avviene nel file di
	 * prova
	 */
	public static String getBody(String textDocumentJSON) throws FileNotFoundException, IOException, ParseException
	{
		JSONParser jParser = new JSONParser();
		
		Object ob = jParser.parse(textDocumentJSON);
		JSONArray jArray = (JSONArray)ob;
		
		String text = "";
		
		for(int i=0; i<jArray.size(); i++)
		{
			String body = "";
			JSONObject news = (JSONObject)jArray.get(i);
			JSONArray notizia = (JSONArray)news.get("title");
			
			String title = (String)notizia.get(0);
			
			JSONArray bodyArray = (JSONArray)news.get("body");
			for(int j=0; j<bodyArray.size(); j++)
			{
				body += bodyArray.get(j);
			}
			
			text += title + " - " + body + "\n";
		}
		
		return text;
		
	}
	
	/*
	 * Questo metodo è per la lettura di file json di prova
	 */
	public static String getBodyProva(String textDocumentJSON) throws FileNotFoundException, IOException, ParseException
	{
		JSONParser jParser = new JSONParser();
		
		Object ob = jParser.parse(textDocumentJSON);
		JSONObject news = (JSONObject)ob;
		
		String text = "";
		String body = "";
			
		JSONArray notizia = (JSONArray)news.get("title");
			
		String title = (String)notizia.get(0);
			
		JSONArray bodyArray = (JSONArray)news.get("body");
		for(int j=0; j<bodyArray.size(); j++)
		{
			body += bodyArray.get(j);
		}
			
		text += title + " - " + body + "\n";
		
		return text;
		
	}
}
