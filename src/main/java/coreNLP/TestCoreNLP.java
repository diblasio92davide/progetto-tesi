package coreNLP;

import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.PropertiesUtils;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import utility.FileService;
import utility.TagmeUtils;

public class TestCoreNLP {

    public static void main(String[] args) throws IOException {

        StanfordCoreNLP pipeline = new StanfordCoreNLP(
                PropertiesUtils.asProperties(
                        "annotators", "tokenize,ssplit,pos,lemma,ner",
                        "ssplit.isOneSentence", "true",
                        "tokenize.language", "en", "entitySubclassification", "iob2"));
        BufferedReader reader = new BufferedReader(new FileReader((args[0])));
        BufferedWriter writer = new BufferedWriter(new FileWriter(args[1]));
        while (reader.ready()) {
            String textDocument = reader.readLine();
            Annotation document = new Annotation(textDocument);
            pipeline.annotate(document);
            Annotator annotator = new Annotator(document);
            String nerout = annotator.ner();
            writer.write(nerout);
            writer.newLine();
            System.out.println(TagmeUtils.process(textDocument));
        }
        reader.close();
        writer.close();
    }

}
