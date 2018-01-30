package coreNLP;

import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.PropertiesUtils;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.parser.ParseException;

import utility.JsonUtils;
import utility.TagmeUtils;

public class TestCoreNLP {

    public static void main(String[] args) throws IOException {

        StanfordCoreNLP pipeline = new StanfordCoreNLP(
                PropertiesUtils.asProperties(
                        "annotators", "tokenize,ssplit,pos,lemma,ner",
                        "ssplit.isOneSentence", "true",
                        "tokenize.language", "en", "entitySubclassification", "iob2"));
        BufferedWriter writer = new BufferedWriter(new FileWriter(args[1]));
        BufferedWriter writer1 = new BufferedWriter(new FileWriter(args[2]));
        
        /*Argomenti passati al main in questo ordine:
         * 0 - "Input.txt"
         * 1 - "Entity Type.txt"
         * 2 - "RHO Fisso.txt"
         * 3 - "RHO a scelta.txt"
         * 4 - "items-prova.json"
         * 5 - "items-1820.json"
         */
        
        
        /*
         * Nel codice sottostante ho utilizzato il metodo getBodyProva per estrarre il
         * contenuto del file prova json relativo al titolo e al body come mi aveva richiesto.
         * 1° problema - Nonostante stampi bene l'ouput del metodo ner(), questo non lo scrive
         * 	  nel file Entity Type.txt
         * 2° problema - Nel metodo rhoFisso di JsonUtils mi solleva un'eccezione riguardante
         * 	  gli indici di start e end dell'output TagmeUtils.process(textBody): ho stampato
         * 	  ogni cosa, ma mi sono reso conto che stampando anche gli indici in effetti
         * 	  stranamente ad un certo punto del ciclo il valore dell'end di un jsonObject
         * 	  è maggiore dello start del jsonObject successivo.
         * 3° problema - Ho provato anche la lettura di uno dei file json contenente tutte
         * 	  le notizie su ogni riga. Per fare ciò il metodo giusto per leggere da questo file
         *    è getBody() e non getBodyProva(), il motivo l'ho scritto nella classe JsonUtils
         *    proprio sopra questo metodo. I problemi che riscontro è la grande lentezza nel
         *    leggere questo file(figuriamoci nella lettura di tutti i file json che mi ha mandato)
         *    e soprattutto gli errori relativi alla scrittura su file perchè la lunghezza di ogni riga
         *    dei file json supera i valori consentiti per scrivere su file.
         */
    	try {
    		BufferedReader reader = new BufferedReader(new FileReader((args[4])));
    		
    		while(reader.ready())
    		{
    			String textDocumentJSON = reader.readLine();
    			String textBody = JsonUtils.getBodyProva(textDocumentJSON);
    			Annotation document = new Annotation(textBody);
                pipeline.annotate(document);
                Annotator annotator = new Annotator(document);
                
                String nerout = annotator.ner();
                System.out.println("Stampa metodo di stanfordNLP");
                System.out.println(nerout);
                writer.write(nerout);
                writer.newLine();
                
                System.out.println("\nStampa del contentuto del body del file JSON");
                System.out.println(textBody);
                
                System.out.println("Stampa output del metodo process di TagmeUtils");
                System.out.println(TagmeUtils.process(textBody));
                String rhoFisso = JsonUtils.rhoFisso(textBody, TagmeUtils.process(textBody));
                writer1.write(rhoFisso);
                writer1.newLine();
    		}
    		
    		reader.close();
    		writer.close();
    		writer1.close();
            
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    	
    	
    	/*CODICE SCRITTO IN PRECEDENZA ANALIZZANDO IL FILE "Input.txt" FUNZIONANTE
    	
    	
    	/*BufferedReader reader = new BufferedReader(new FileReader((args[0])));
        BufferedWriter writer = new BufferedWriter(new FileWriter(args[1]));
        BufferedWriter writer1 = new BufferedWriter(new FileWriter(args[2]));
        BufferedWriter writer2 = new BufferedWriter(new FileWriter(args[3]));
        
        while (reader.ready()) {
            String textDocument = reader.readLine();
            Annotation document = new Annotation(textDocument);
            pipeline.annotate(document);
            Annotator annotator = new Annotator(document);
            
            String nerout = annotator.ner();
            writer.write(nerout);
            writer.newLine();
            
            String rhoFisso = JsonUtils.rhoFisso(textDocument, TagmeUtils.process(textDocument));
            writer1.write(rhoFisso);
            writer1.newLine();
            
            String rhoMinimo = JsonUtils.rhoMinimo(textDocument, TagmeUtils.process(textDocument), 0.01);
            writer2.write(rhoMinimo);
            writer2.newLine();
            
            //PROVE DI STAMPA
            System.out.print(TagmeUtils.process(textDocument));
            System.out.println(rhoFisso);
            System.out.println(rhoMinimo);
            System.out.println();
            
        }
        
        reader.close();
        writer.close();
        writer1.close();
        writer2.close();
        */
        
    }

}
