package eu.alertproject.iccs.stardom.lsa.tfidf.tokenizers;

import java.io.File;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ibm.icu.text.BreakIterator;
import com.ibm.icu.text.RuleBasedBreakIterator;

/**
 * Tokenize a block of text into its component words. This is a simple wrapper
 * over BreakIterator.
 * @author Sujit Pal
 * @version $Revision$
 */
public class WordTokenizer {
  
  private final Log log = LogFactory.getLog(getClass());

  @SuppressWarnings("unchecked")
  private final static Map<Integer,TokenType> RULE_ENTITY_MAP = 
    ArrayUtils.toMap(new Object[][] {
      {new Integer(0), TokenType.UNKNOWN},
      {new Integer(100), TokenType.NUMBER},
      {new Integer(200), TokenType.WORD},
      {new Integer(500), TokenType.ABBREVIATION},
      {new Integer(501), TokenType.WORD},
      {new Integer(502), TokenType.INTERNET},
      {new Integer(503), TokenType.INTERNET},
      {new Integer(504), TokenType.MARKUP},
      {new Integer(505), TokenType.EMOTICON},
      {new Integer(506), TokenType.INTERNET},
      {new Integer(507), TokenType.INTERNET}
  });
  
  private String text;
  private int index = 0;
  private RuleBasedBreakIterator breakIterator;
  
  public WordTokenizer() throws Exception {
    this("src/main/resources/word_break_rules.txt");
  }

  public WordTokenizer(String rulesfile) throws Exception {
    super();
    this.breakIterator = new RuleBasedBreakIterator(
      FileUtils.readFileToString(new File(rulesfile), "UTF-8"));
  }
  
  public void setText(String text) {
    this.text = text;
    this.breakIterator.setText(text);
    this.index = 0;
  }
  
  public Token nextToken() throws Exception {
    for (;;) {
      int end = breakIterator.next();
      if (end == BreakIterator.DONE) {
        return null;
      }
      String nextWord = text.substring(index, end);
//      log.debug("next=" + nextWord + "[" + breakIterator.getRuleStatus() + "]");
      index = end;
//      return new Token(nextWord, RULE_ENTITY_MAP.get(breakIterator.getRuleStatus()));
         return new Token(nextWord, null);
    }
  }
}
