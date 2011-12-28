package eu.alertproject.iccs.stardom.lsa.tfidf.recognizers;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eu.alertproject.iccs.stardom.lsa.tfidf.tokenizers.Token;
import eu.alertproject.iccs.stardom.lsa.tfidf.tokenizers.TokenType;

/**
 * Identifies Punctuations in the returned text. A BreakIterator will treat
 * both punctuation and whitespace as word boundaries. This is usually the
 * first extractor in a chain, so it reduces the number of unknowns. Adapted from JTMT
 * @author Kostas Christidis
 * @version $Revision$
 */
public class BoundaryRecognizer implements IRecognizer {

  private Pattern whitespacePattern;
  private Pattern punctuationPattern;
  
  /**
   * @see com.mycompany.jrocker.recognizers.IRecognizer#init()
   */
  public void init() throws Exception {
    this.whitespacePattern = Pattern.compile("\\s+");
    this.punctuationPattern = Pattern.compile("\\p{Punct}");
  }

  /**
   * @see com.mycompany.jrocker.recognizers.IRecognizer#extractEntities(java.util.List)
   */
  public List<Token> recognize(List<Token> tokens) {
    List<Token> extractedTokens = new LinkedList<Token>();
    for (Token token : tokens) {
      String value = token.getValue();
      TokenType type = token.getType();
      if (type != TokenType.UNKNOWN) {
        // we already know what this is, continue
        extractedTokens.add(token);
        continue;
      }
      // whitespace
      Matcher whitespaceMatcher = whitespacePattern.matcher(value);
      if (whitespaceMatcher.matches()) {
        extractedTokens.add(new Token(value, TokenType.WHITESPACE));
        continue;
      }
      // punctuation
      Matcher punctuationMatcher = punctuationPattern.matcher(value);
      if (punctuationMatcher.matches()) {
        extractedTokens.add(new Token(value, TokenType.PUNCTUATION));
        continue;
      }
      // we came this far, then its still UNKNOWN
      extractedTokens.add(token);
    }
    return extractedTokens;
  }
}
