package corenlp;

import java.io.IOException;


public class TestCoreNLP {

    public static void main(String[] args) throws IOException {

        /*StanfordCoreNLP pipeline = new StanfordCoreNLP(
                PropertiesUtils.asProperties(
                        "annotators", "tokenize,ssplit,pos,lemma,ner",
                        "ssplit.isOneSentence", "true",
                        "tokenize.language", "en", "entitySubclassification", "iob2"));
        BufferedWriter writer = new BufferedWriter(new FileWriter(args[1]));
        BufferedWriter writer1 = new BufferedWriter(new FileWriter(args[2]));

        try {
            BufferedReader reader = new BufferedReader(new FileReader((args[4])));

            while (reader.ready()) {
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
        }*/

    }

}
