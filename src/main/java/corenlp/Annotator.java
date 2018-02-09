package corenlp;

public class Annotator {

    //private Annotation document;

    public Annotator() {
    }

    /*public Annotator(Annotation document) {
        this.document = document;
    }

    public Annotation getDocument() {
        return document;
    }

    public void setDocument(Annotation document) {
        this.document = document;
    }*/

    /*public void tokenization() {
        String textFile = "";
        for (Sentence sent : document.sentences()) {
            textFile += "Testo analizzato:\r\n" + sent
                    + "\r\nTokenizazione:\r\n" + sent.words() + "\r\n\r\n";
        }

        FileService.scriviFile("Tokenization.txt", textFile);
    }

    public void sentenceSplitting() {
        String textFile = "";

        textFile += "Testo analizzato:\r\n" + document
                + "\r\nDivisione delle frasi:\r\n" + document.sentences() + "\r\n\r\n";

        FileService.scriviFile("Sentence Splitting.txt", textFile);
    }

    public void partOfSpeechTagging() {
        String textFile = "";
        for (Sentence sent : document.sentences()) {
            textFile += "Testo analizzato:\r\n" + sent
                    + "\r\nParte del tagging vocale:\r\n" + sent.posTags() + "\r\n\r\n";
        }

        FileService.scriviFile("Part of speech tagging.txt", textFile);
    }

    public void lemmatization() {
        String textFile = "";
        for (Sentence sent : document.sentences()) {
            textFile += "Testo analizzato:\r\n" + sent
                    + "\r\nLemmatizzazione:\r\n" + sent.lemmas() + "\r\n\r\n";
        }

        FileService.scriviFile("Lemmatization.txt", textFile);
    }

    public void namedEntityRecognition() {
        String textFile = "";
        for (Sentence sent : document.sentences()) {
            textFile += "Testo analizzato:\r\n" + sent
                    + "\r\nRiconoscimento entita':\r\n" + sent.nerTags() + "\r\n\r\n";
        }

        FileService.scriviFile("Named Entity Recognition.txt", textFile);

    }

    public void ner() {
        String textFile = "";
        for (Sentence sent : document.sentences()) {
            textFile += "Frase analizzata:\r\n" + sent
                    + "\r\nRielaborazione della frase con riconoscimento entit�:\r\n";

            for (int i = 0; i < sent.nerTags().size(); i++) {
                if (!sent.nerTag(i).equals("O")) {
                    textFile += sent.word(i) + "_" + sent.nerTag(i) + " ";
                } else {
                    textFile += sent.word(i) + " ";
                }
            }

            textFile += "\r\n\r\n";
        }

        FileService.scriviFile("ner.txt", textFile);

    }*/
    /*public String ner() {
        List<CoreMap> sentences = document.get(SentencesAnnotation.class);
        StringBuilder sb = new StringBuilder();
        for (CoreMap sentence : sentences) {
            // traversing the words in the current sentence
            // a CoreLabel is a CoreMap with additional token-specific methods
            List<String> entity = new ArrayList<String>();
            String type = "";
            for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
                String ne = token.get(NamedEntityTagAnnotation.class);
                if (ne.equals("O")) {
                    if (!entity.isEmpty()) {
                        for (int i = 0; i < entity.size(); i++) {
                            sb.append(entity.get(i));
                            sb.append("_");
                        }
                        sb.append(type);
                        sb.append(" ");
                        entity.clear();
                        type = "";
                    }
                    sb.append(token.word()).append(" ");
                } else {
                    type = ne;
                    entity.add(token.word());
                }
            }
            if (!entity.isEmpty()) {
                for (int i = 0; i < entity.size(); i++) {
                    sb.append(entity.get(i));
                    sb.append("_");
                }
                sb.append(type);
                sb.append(" ");
                entity.clear();
                type = "";
            }
            sb.append("\n");
        }
        return sb.toString().trim();
    }*/

}
