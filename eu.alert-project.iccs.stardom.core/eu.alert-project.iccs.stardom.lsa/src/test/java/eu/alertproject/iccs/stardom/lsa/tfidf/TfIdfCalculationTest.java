package eu.alertproject.iccs.stardom.lsa.tfidf;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.util.LinkedHashMap;
import java.util.Map;



import org.apache.commons.lang.StringUtils;
import org.apache.commons.math.linear.RealMatrix;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * Test class for generating term/document matrices using various methods.
 * @author Sujit Pal
 * @version $Revision: 55 $
 */
public class TfIdfCalculationTest {

  private VectorGenerator vectorGenerator;
  private Map<String,Reader> documents;
  
  @Before
  public void setUp() throws Exception {
    vectorGenerator = new VectorGenerator();
    documents = new LinkedHashMap<String,Reader>();
    BufferedReader reader = new BufferedReader(
      new FileReader("src/test/resources/indexing_test.txt"));
    String line = null;
    while ((line = reader.readLine()) != null) {
      String[] docTitleParts = StringUtils.split(line, "::");
      documents.put(docTitleParts[0], new StringReader(docTitleParts[1]));
    }
  }
  
  @Test
  public void testVectorGeneration() throws Exception {
    vectorGenerator.generateVector(documents);
    prettyPrintMatrix("Occurences", vectorGenerator.getMatrix(), 
      vectorGenerator.getDocumentNames(), vectorGenerator.getWords(), 
      new PrintWriter(System.out, true));
  }
  
  @Test
  public void testTfIndexer() throws Exception {
    vectorGenerator.generateVector(documents);
    TfIndexer indexer = new TfIndexer();
    RealMatrix tfMatrix = indexer.transform(vectorGenerator.getMatrix());
    prettyPrintMatrix("Term Frequency", tfMatrix, 
      vectorGenerator.getDocumentNames(), vectorGenerator.getWords(), 
      new PrintWriter(System.out, true));
  }
  
  @Test
  public void testIdfIndexer() throws Exception {
    vectorGenerator.generateVector(documents);
    IdfIndexer indexer = new IdfIndexer();
    RealMatrix idfMatrix = indexer.transform(vectorGenerator.getMatrix());
    prettyPrintMatrix("Inverse Document Frequency", idfMatrix,
      vectorGenerator.getDocumentNames(), vectorGenerator.getWords(),
      new PrintWriter(System.out, true));
  }
  
  @Test
  public void testLsiIndexer() throws Exception {
    vectorGenerator.generateVector(documents);
    LsiIndexer indexer = new LsiIndexer();
    RealMatrix lsiMatrix = indexer.transform(vectorGenerator.getMatrix());
    prettyPrintMatrix("Latent Semantic (LSI)", lsiMatrix,
      vectorGenerator.getDocumentNames(), vectorGenerator.getWords(),
      new PrintWriter(System.out, true));
  }
  
  private void prettyPrintMatrix(String legend, RealMatrix matrix, 
      String[] documentNames, String[] words, PrintWriter writer) {
    writer.printf("=== words ===%n");
      for (int i = 0; i < words.length; i++) {
      writer.printf("%15s", words[i]);
      }
    writer.printf("=== %s ===%n", legend);
    writer.printf("%15s", " ");
    for (int i = 0; i < documentNames.length; i++) {
      writer.printf("%8s", documentNames[i]);
    }
    writer.println();
    for (int i = 0; i < words.length; i++) {
      writer.printf("%15s", words[i]);
      for (int j = 0; j < documentNames.length; j++) {
        writer.printf("%8.4f", matrix.getEntry(i, j));
      }
      writer.println();
    }
    writer.flush();
  }
}
