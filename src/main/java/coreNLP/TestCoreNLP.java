package coreNLP;

import edu.stanford.nlp.simple.Document;
import utility.FileService;

public class TestCoreNLP {

public static void main(String[] args) {
	
	String textDocument = FileService.leggiFile("Input.txt");
	Document doc = new Document(textDocument);
	
	Annotator annotator = new Annotator(doc);
	
	//tokenizzazione del contenuto del file "Input.txt" sul file "Tokenization.txt"
	annotator.tokenization();
	
	//divisione delle frasi del contenuto del file "Input.txt" sul file "Sentence Splitting.txt
	annotator.sentenceSplitting();
	
	//parte del tagging vocale del contenuto del file "Input.txt" sul file "Part of speech tagging.txt"
	annotator.partOfSpeechTagging();
	
	//lemmatizzazione del contenuto del file "Input.txt" sul file "Lemmatization.txt"
	annotator.lemmatization();
	
	//riconoscimento delle entità con nome del contenuto del file "Input.txt" sul file "Named Entity Recognition.txt"
	annotator.namedEntityRecognition();
	
	//lavoro tesi: affiancare al nome la sua entità. Output "NER.txt"
	annotator.NER();
	}

}
