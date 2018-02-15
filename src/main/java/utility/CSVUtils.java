package utility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVUtils {
	
	private static final char DEFAULT_SEPARATOR = '\t';
	
	/**
	 * The
     * main requires six arguments:
     * 1) input file stat_cum_CPD(csv)
     * 2) input file stat_point_CPD(csv)
     * 3) output file(csv) with pVlaue=0.01 of the input file 1)
     * 4) output file(csv) with pVlaue=0.05 of the input file 1)
     * 5) output file(csv) with pVlaue=0.01 of the input file 2)
     * 6) output file(csv) with pVlaue=0.05 of the input file 2)
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		try {
			cambiamentoSemantico(args[0], 0.01, args[2]);
			cambiamentoSemantico(args[0], 0.05, args[3]);
			cambiamentoSemantico(args[1], 0.01, args[4]);
			cambiamentoSemantico(args[1], 0.05, args[5]);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String cambiamentoSemantico(String csvFile, double pValue, String outputCsvFile) throws IOException
	{
		StringBuilder output = new StringBuilder();
		BufferedWriter writer = new BufferedWriter(new FileWriter(outputCsvFile));
		
		BufferedReader scanner = new BufferedReader(new FileReader(csvFile));
	
		while(scanner.ready())
		{
			List<String> line = parseLine(scanner.readLine());
			String outputLine = sogliaPValue(line, pValue);
			if(outputLine != null)
				writer.append(outputLine+"\n");
		}
		scanner.close();
		writer.close();
		return output.toString();
	}
	
	private static List<String> parseLine(String line)
	{
		List<String> lista = new ArrayList<>();
		int length = line.length();
		
		for(int i=0; i<length; i++)
		{
			StringBuilder elem = new StringBuilder();
			for(int j=i; line.charAt(j)!=DEFAULT_SEPARATOR && j!=length-1; j++, i++)
			{
				elem.append(line.charAt(j));
				if(j+1 == length-1)
					elem.append(line.charAt(j+1));
			}
			lista.add(elem.toString());
		}
		
		return lista;
	}
	
	private static String sogliaPValue(List<String> line, double pValue)
	{
		if(line.get(0).contains("E_"))
		{
			String output = "";
			List<String> values = new ArrayList<>();
		
			for(int i=1; i<line.size(); i=i+2)
			{
				double filePValue = Double.parseDouble(line.get(i+1));
				if(filePValue > pValue)
				{
					values.add(line.get(i));
					values.add(line.get(i+1));
				}
			}
			
			if(!values.isEmpty())
			{
				output+=line.get(0);
				for(int i=0; i<values.size(); i++)
				{
					output+="\t" + values.get(i);
				}
				return output;
			}
			else
				return null;
		}
		else
			return null;
	}
	
}
