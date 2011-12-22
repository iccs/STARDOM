package eu.alertproject.iccs.stardom.lsa.tfidf.recognizers;

import java.util.LinkedList;
import java.util.List;

import eu.alertproject.iccs.stardom.lsa.tfidf.tokenizers.Token;

/**
 * Uses a set of rules (usually a regular expression or dictionary lookup) to
 * identify a word as a certain token type. All words returned from the tokenizer
 * start off as token type UNKNOWN. Rules are fired in sequence to identify the
 * type, most specific to least specific. Once a word is identified as a particular
 * entity, we don't process the rule chain any further.
 * @author Sujit Pal
 * @version $Revision$
 */
public class RecognizerChain implements IRecognizer {

  private List<IRecognizer> recognizers;
  
  public RecognizerChain(List<IRecognizer> recognizers) {
    super();
    setRecognizers(recognizers);
  }
  
  public void setRecognizers(List<IRecognizer> recognizers) {
    this.recognizers = recognizers;
  }

  public void init() throws Exception {
    for (IRecognizer recognizer : recognizers) {
      recognizer.init();
    }
  }
  
  /**
   * Applies a chain of IEntityExtractor implementations to the input Token
   * List and transforms it into another Token List.
   * @param a List of Tokens.
   * @return another List of Tokens.
   */
  public List<Token> recognize(final List<Token> tokens) {
    List<Token> recognizedTokens = new LinkedList<Token>();
    recognizedTokens.addAll(tokens);
    for (IRecognizer recognizer : recognizers) {
      recognizedTokens = recognizer.recognize(recognizedTokens);
    }
    return recognizedTokens;
  }
}
