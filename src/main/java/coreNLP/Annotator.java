package coreNLP;

import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;
import utility.FileService;

public class Annotator {
	
	private Document document;
	
	public Annotator(){}
	
	public Annotator(Document document)
	{
		this.setDocument(document);
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}
	
	public void tokenization()
	{
		String textFile = "";
		for (Sentence sent : document.sentences())
		{
			textFile += "Testo analizzato:\r\n" + sent
					+ "\r\nTokenizazione:\r\n" + sent.words() + "\r\n\r\n";
		}
		
		FileService.scriviFile("Tokenization.txt", textFile);
	}
	
	public void sentenceSplitting()
	{
		String textFile = "";

		textFile += "Testo analizzato:\r\n" + document
				+ "\r\nDivisione delle frasi:\r\n" + document.sentences() + "\r\n\r\n";
		
		FileService.scriviFile("Sentence Splitting.txt", textFile);
	}
	
	public void partOfSpeechTagging()
	{
		String textFile = "";
		for (Sentence sent : document.sentences())
		{
			textFile += "Testo analizzato:\r\n" + sent
					+ "\r\nParte del tagging vocale:\r\n" + sent.posTags() + "\r\n\r\n";
		}
		
		FileService.scriviFile("Part of speech tagging.txt", textFile);
	}
	
	public void lemmatization()
	{
		String textFile = "";
		for (Sentence sent : document.sentences())
		{
			textFile += "Testo analizzato:\r\n" + sent
					+ "\r\nLemmatizzazione:\r\n" + sent.lemmas() + "\r\n\r\n";
		}
		
		FileService.scriviFile("Lemmatization.txt", textFile);
	}
	
	public void namedEntityRecognition()
	{
		String textFile = "";
		for (Sentence sent : document.sentences())
		{
			textFile += "Testo analizzato:\r\n" + sent
					+ "\r\nRiconoscimento entita':\r\n" + sent.nerTags() + "\r\n\r\n";
		}
		
		FileService.scriviFile("Named Entity Recognition.txt", textFile);
		
	}
	
	public void NER()
	{
		String textFile = "";
		for (Sentence sent : document.sentences())
		{
			textFile += "Frase analizzata:\r\n" + sent
					+ "\r\nRielaborazione della frase con riconoscimento entità:\r\n";
			
			for(int i=0; i<sent.nerTags().size(); i++)
			{
				if(!sent.nerTag(i).equals("O"))
				{
					textFile += sent.word(i) + "_" + sent.nerTag(i) + " ";
				}
				else
				{
					textFile += sent.word(i) + " ";
				}
			}
			
			textFile += "\r\n\r\n";
		}
		
		FileService.scriviFile("NER.txt", textFile);
		
	}
}
