package eu.alertproject.iccs.stardom.lsa.tfidf;



import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.sql.DataSource;

import org.apache.commons.collections15.Bag;
import org.apache.commons.collections15.bag.HashBag;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

import Jama.Matrix;
import eu.alertproject.iccs.stardom.lsa.tfidf.recognizers.*;
import eu.alertproject.iccs.stardom.lsa.tfidf.recognizers.RecognizerChain;
import eu.alertproject.iccs.stardom.lsa.tfidf.tokenizers.Token;
import eu.alertproject.iccs.stardom.lsa.tfidf.tokenizers.TokenType;
import eu.alertproject.iccs.stardom.lsa.tfidf.tokenizers.WordTokenizer;



/**
 * Generate the word occurence vector for a document collection.
 */
public class VectorGenerator {

  private DataSource dataSource;
  
  private Map<Integer,String> wordIdValueMap = 
    new HashMap<Integer,String>();
  private Map<Integer,String> documentIdNameMap = 
    new HashMap<Integer,String>();
  private Matrix matrix;

  @Required
  public void setDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public void generateVector(Map<String,Reader> documents) 
      throws Exception {
    Map<String,Bag<String>> documentWordFrequencyMap = 
      new HashMap<String,Bag<String>>();
    SortedSet<String> wordSet = new TreeSet<String>();
    Integer docId = 0;
    for (String key : documents.keySet()) {
      String text = getText(documents.get(key));
      Bag<String> wordFrequencies = getWordFrequencies(text);
      wordSet.addAll(wordFrequencies.uniqueSet());
      documentWordFrequencyMap.put(key, wordFrequencies);
      documentIdNameMap.put(docId, key);
      docId++;
    }
    // create a Map of ids to words from the wordSet
    int wordId = 0;
    for (String word : wordSet) {
      wordIdValueMap.put(wordId, word);
      wordId++;
    }
    // we need a documents.keySet().size() x wordSet.size() matrix to hold
    // this info
    int numDocs = documents.keySet().size();
    int numWords = wordSet.size();
    double[][] data = new double[numWords][numDocs];
    for (int i = 0; i < numWords; i++) {
      for (int j = 0; j < numDocs; j++) {
        String docName = documentIdNameMap.get(j);
        Bag<String> wordFrequencies = 
          documentWordFrequencyMap.get(docName);
        String word = wordIdValueMap.get(i);
        int count = wordFrequencies.getCount(word);
        data[i][j] = count;
      }
    }
    matrix = new Matrix(data);
  }

  public Matrix getMatrix() {
    return matrix;
  }
  
  public String[] getDocumentNames() {
    String[] documentNames = new String[documentIdNameMap.keySet().size()];
    for (int i = 0; i < documentNames.length; i++) {
      documentNames[i] = documentIdNameMap.get(i);
    }
    return documentNames;
  }
  
  public String[] getWords() {
    String[] words = new String[wordIdValueMap.keySet().size()];
    for (int i = 0; i < words.length; i++) {
      String word = wordIdValueMap.get(i);
      if (word.contains("|||")) {
        // phrases are stored with length for other purposes, strip it off
        // for this report.
        word = word.substring(0, word.indexOf("|||"));
      }
      words[i] = word;
    }
    return words;
  }

  private Bag<String> getWordFrequencies(String text) 
      throws Exception {
    Bag<String> wordBag = new HashBag<String>();
    WordTokenizer wordTokenizer = new WordTokenizer();
    wordTokenizer.setText(text);
    List<Token> tokens = new ArrayList<Token>();
    Token token = null;
    while ((token = wordTokenizer.nextToken()) != null) {
      tokens.add(token);
    }
    RecognizerChain recognizerChain = new RecognizerChain(
        Arrays.asList(new IRecognizer[] {
        new BoundaryRecognizer(),
        new AbbreviationRecognizer(dataSource),
        new PhraseRecognizer(dataSource),
        new StopwordRecognizer(),
        new ContentWordRecognizer()
    }));
    recognizerChain.init();
    List<Token> recognizedTokens = recognizerChain.recognize(tokens);
    for (Token recognizedToken : recognizedTokens) {
      if (recognizedToken.getType() == TokenType.ABBREVIATION ||
          recognizedToken.getType() == TokenType.PHRASE ||
          recognizedToken.getType() == TokenType.CONTENT_WORD) {
        // lowercase words to treat Human and human as the same word
        wordBag.add(StringUtils.lowerCase(recognizedToken.getValue()));
      }
    }
    return wordBag;
  }

  private String getText(Reader reader) throws Exception {
    StringBuilder textBuilder = new StringBuilder();
    char[] cbuf = new char[1024];
    int len = 0;
    while ((len = reader.read(cbuf, 0, 1024)) != -1) {
      textBuilder.append(ArrayUtils.subarray(cbuf, 0, len));
    }
    reader.close();
    return textBuilder.toString();
  }
}
