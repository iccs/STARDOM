package eu.alertproject.iccs.stardom.lsa.tfidf.recognizers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import eu.alertproject.iccs.stardom.lsa.tfidf.tokenizers.Token;
import eu.alertproject.iccs.stardom.lsa.tfidf.tokenizers.TokenType;

import org.apache.commons.lang.StringUtils;

/**
 * A recognizer that recognizes common stop words. Special stopwords may
 * be passed in through the non-default constructor.
 * @author Sujit Pal
 * @version $Revision$
 */
public class StopwordRecognizer implements IRecognizer {

  private static final String DEFAULT_STOPWORDS = 
    "a about add ago after all also an and another any are as at be " +
    "because been before being between big both but by came can come " +
    "could did do does due each else end far few for from get got had " +
    "has have he her here him himself his how if in into is it its " +
    "just let lie like low make many me might more most much must " +
    "my never no nor not now of off old on only or other our out over " +
    "per pre put re said same see she should since so some still such " +
    "take than that the their them then there these they this those " +
    "through to too under up use very via want was way we well were " +
    "what when where which while who will with would yes yet you your";

  private Set<String> stopwords = new HashSet<String>();
  
  public StopwordRecognizer() {
    super();
  }
  
  public StopwordRecognizer(String[] stopwords) {
    this.stopwords.addAll(Arrays.asList(stopwords));
  }
  
  public void init() throws Exception {
    if (stopwords.size() == 0) {
      String[] stopwordArray = StringUtils.split(DEFAULT_STOPWORDS, " ");
      stopwords.addAll(Arrays.asList(stopwordArray));
    }
  }

  public List<Token> recognize(List<Token> tokens) {
    List<Token> recognizedTokens = new ArrayList<Token>();
    for (Token token : tokens) {
      if (token.getType() == TokenType.WORD) {
        if (stopwords.contains(StringUtils.lowerCase(token.getValue()))) {
          token.setType(TokenType.STOP_WORD);
        }
      }
      recognizedTokens.add(token);
    }
    return recognizedTokens;
  }

}
