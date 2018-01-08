package utility;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtils {
	
	public static String rhoFisso(String textDocument, String jsonSTR) throws JSONException, IOException
	{
		JSONObject jsonOBJ = new JSONObject(jsonSTR);
		int startTag, endTag,
			start = 0, end = 0;
		double rho;
		String title;
		JSONArray annotations = (JSONArray)jsonOBJ.get("annotations");
		StringBuilder output = new StringBuilder("");
		
		for(int i=0; i<annotations.length(); i++)
		{
			startTag = ((JSONObject)annotations.get(i)).getInt("start");
			endTag = ((JSONObject)annotations.get(i)).getInt("end");
			rho = ((JSONObject)annotations.get(i)).getDouble("rho");
			
			if(startTag > 0)
			{
				end = startTag;
				output.append(textDocument.substring(start, end));
			}
			
			if(rho >= 0.1)
			{
				title = ((JSONObject)annotations.get(i)).getString("title");
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
	
	public static String rhoMinimo(String textDocument, String jsonSTR, double rhoVAR) throws JSONException, IOException
	{
		JSONObject jsonOBJ = new JSONObject(jsonSTR);
		int startTag, endTag,
			start = 0, end = 0;
		double rho;
		String title;
		JSONArray annotations = (JSONArray)jsonOBJ.get("annotations");
		StringBuilder output = new StringBuilder("");
		
		for(int i=0; i<annotations.length(); i++)
		{
			startTag = ((JSONObject)annotations.get(i)).getInt("start");
			endTag = ((JSONObject)annotations.get(i)).getInt("end");
			rho = ((JSONObject)annotations.get(i)).getDouble("rho");
			
			if(startTag > 0)
			{
				end = startTag;
				output.append(textDocument.substring(start, end));
			}
			
			if(rho >= rhoVAR)
			{
				title = ((JSONObject)annotations.get(i)).getString("title");
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
}
