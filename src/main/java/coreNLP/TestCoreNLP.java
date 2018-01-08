package coreNLP;

import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.PropertiesUtils;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONException;

import utility.JsonUtils;
import utility.TagmeUtils;

public class TestCoreNLP {

    public static void main(String[] args) throws IOException, JSONException {

        StanfordCoreNLP pipeline = new StanfordCoreNLP(
                PropertiesUtils.asProperties(
                        "annotators", "tokenize,ssplit,pos,lemma,ner",
                        "ssplit.isOneSentence", "true",
                        "tokenize.language", "en", "entitySubclassification", "iob2"));
        BufferedReader reader = new BufferedReader(new FileReader((args[0])));
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
    }

}
