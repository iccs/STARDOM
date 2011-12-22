package eu.alertproject.iccs.stardom.lsa.tfidf.tokenizers;

import java.io.File;
import java.text.BreakIterator;

import org.apache.commons.io.FileUtils;

import com.ibm.icu.text.RuleBasedBreakIterator;

/**
 * Tokenizes the input into paragraphs, using ICU4J's rule-based break
 * iterator. Rule file is adapted from the rule file used internally by
 * the RBBI sentence tokenizer.
 * @author Sujit Pal 
 * @version $Revision$
 */
public class ParagraphTokenizer {

  private String text;
  private int index = 0;
  private RuleBasedBreakIterator breakIterator;
  
  public ParagraphTokenizer() throws Exception {
    this("src/main/resources/paragraph_break_rules.txt");
  }

  public ParagraphTokenizer(String rulesfile) throws Exception {
    super();
    this.breakIterator = new RuleBasedBreakIterator(
      FileUtils.readFileToString(new File(rulesfile), "UTF-8"));
  }
  
  public void setText(String text) {
    this.text = text;
    this.breakIterator.setText(text);
    this.index = 0;
  }
  
  public String nextParagraph() {
    int end = breakIterator.next();
    if (end == BreakIterator.DONE) {
      return null;
    }
    String sentence = text.substring(index, end);
    index = end;
    return sentence;
  }
}
