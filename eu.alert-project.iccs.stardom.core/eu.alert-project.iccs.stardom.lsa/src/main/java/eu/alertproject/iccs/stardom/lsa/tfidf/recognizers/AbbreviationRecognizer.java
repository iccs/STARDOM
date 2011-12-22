package eu.alertproject.iccs.stardom.lsa.tfidf.recognizers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import eu.alertproject.iccs.stardom.lsa.tfidf.tokenizers.Token;
import eu.alertproject.iccs.stardom.lsa.tfidf.tokenizers.TokenType;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Rule to identify Abbreviation entities. This is a multi-pass rule that does
 * the following checks:
 * (1) Check for abbreviations embedded with numbers, eg 3.0pct. This will 
 *     result in two tokens 3.0 (NUMBER) and pct (ABBREVIATION). The first
 *     token will have ___ appended to it to signify that it should be joined
 *     to the token following it.
 * (2) Check for abbreviations with periods, eg. U.S.
 * (3) Check for abbreviations without periods, only if the first character
 *     is uppercase, ie US or MD.
 * In all cases, the abbreviation is looked up against the co_abbrev (enc_type=a)
 * table to ensure a correct match.
 * @author Sujit Pal
 * @version $Revision$
 */
public class AbbreviationRecognizer implements IRecognizer {

  private final Log log = LogFactory.getLog(getClass());
  
  private JdbcTemplate jdbcTemplate;
  private Pattern abbrevEmbeddedInWordPattern;
  private Pattern abbrevWithPeriodsPattern;
  private Pattern abbrevWithAllCapsPattern;
  
  private Set<String> abbreviations = new HashSet<String>();

  public AbbreviationRecognizer() {
    super();
  }



  public void init() throws Exception {
    abbrevEmbeddedInWordPattern = Pattern.compile("\\d+(\\.\\d+)*(\\w+)"); // eg 3.3pct
    abbrevWithPeriodsPattern = Pattern.compile("\\w(\\.\\w)+"); // U.S
    abbrevWithAllCapsPattern = Pattern.compile("[A-Z]+"); // MD, USA
  }

  public List<Token> recognize(List<Token> tokens) {
    List<Token> recognizedTokens = new LinkedList<Token>();
    for (Token token : tokens) {
      TokenType type = token.getType();
      if (type != TokenType.WORD) {
        // we only apply abbreviation recognition rules to WORD tokens, so
        // if this is not a word, its a pass-through for this rule set.
        recognizedTokens.add(token);
        continue;
      }
      String word = token.getValue();
      // match abbreviations embedded in numbers
      Matcher abbrevEmbeddedInWordMatcher = abbrevEmbeddedInWordPattern.matcher(word);
      if (abbrevEmbeddedInWordMatcher.matches()) {
        String abbrevPart = abbrevEmbeddedInWordMatcher.group(2);
        String numberPart = word.substring(0, word.indexOf(abbrevPart));
        if (isAbbreviation(abbrevPart)) {
          recognizedTokens.add(new Token(numberPart + "___", TokenType.NUMBER));
          recognizedTokens.add(new Token(abbrevPart, TokenType.ABBREVIATION));
          continue;
        }
      }
      // match if word contains embedded periods
      Matcher abbrevWithPeriodsMatcher = abbrevWithPeriodsPattern.matcher(word);
      if (abbrevWithPeriodsMatcher.matches()) {
        if (isAbbreviation(word)) {
          token.setType(TokenType.ABBREVIATION);
          recognizedTokens.add(token);
          continue;
        }
      }
      // match if word is all uppercase
      Matcher abbrevWithAllCapsMatcher = abbrevWithAllCapsPattern.matcher(word);
      if (abbrevWithAllCapsMatcher.matches()) {
        // embed periods in the potential abbreviation, and check for both 
        // the original and the period embedded word against our database list
        char[] wordchars = word.toCharArray();
        List<Character> wordChars = new ArrayList<Character>();
        for (int i = 0; i < wordchars.length; i++) {
          wordChars.add(wordchars[i]);
        }
        String periodEmbeddedWord = StringUtils.join(wordChars.iterator(), ".");
        if (isAbbreviation(word) || isAbbreviation(periodEmbeddedWord)) {
          token.setType(TokenType.ABBREVIATION);
          recognizedTokens.add(token);
          continue;
        }
      }
      // if we came this far, none of our tests matched, so we cannot mark
      // this token as an abbreviation...pass through
      recognizedTokens.add(token);
    }
    return recognizedTokens;
  }

  @SuppressWarnings("unchecked")
  private boolean isAbbreviation(String abbrevPart) {
//    List<Map<String,Object>> rows = jdbcTemplate.queryForList(      "select enc_name from co_abbrev where enc_type = ? and enc_name = ?",       new String[] {"a", StringUtils.lowerCase(abbrevPart)});
//    for (Map<String,Object> row : rows) {
//      return true;
//    }
//    return false;
      return false;
  }
}
