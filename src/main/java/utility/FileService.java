package utility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileService {
	
	public static String leggiFile(String nomeFile)
	{
		try {
			BufferedReader buffer = new BufferedReader(
					new FileReader(nomeFile));
			
		String contenuto = "", riga;
		riga = buffer.readLine();
		
		while(riga != null)
		{
			contenuto += riga + " ";
			riga = buffer.readLine();
		}
		
		buffer.close();
		return contenuto;
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static void scriviFile(String nomeFile, String contenuto)
	{
		try {
			BufferedWriter buffer = new BufferedWriter(
					new FileWriter(nomeFile));
			
			PrintWriter out = new PrintWriter(buffer);
			out.println(contenuto);
			
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
