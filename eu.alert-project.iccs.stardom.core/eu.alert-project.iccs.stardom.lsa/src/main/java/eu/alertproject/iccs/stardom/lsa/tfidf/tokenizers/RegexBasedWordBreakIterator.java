package eu.alertproject.iccs.stardom.lsa.tfidf.tokenizers;

import java.text.BreakIterator;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

/**
 * A word break iterator that allows its default behavior for the Locale 
 * to be overriden by supplied regular expression rules.
 * @author Sujit Pal
 * @version $Revision$
 */
public class RegexBasedWordBreakIterator extends BreakIterator {

  private BreakIterator wordBreakIterator = BreakIterator.getWordInstance();
  private String text;
  private int textLength;
  private boolean inXml;
  private boolean inHyphenatedToken;

  /**
   * Entry point to set the text into the BreakIterator.
   */
  @Override
  public void setText(String newText) {
    this.text = newText;
    this.textLength = text.length();
    this.setText(new StringCharacterIterator(newText));
  }
  
  /**
   * We are only interested in overriding next(), since that is the only method
   * our WordTokenizer depends on. A more complete implementation should try
   * to extend all the methods below. In our case, all we do is delegate to 
   * the underlying BreakIterator.wordInstance for the other methods.
   * @see java.text.BreakIterator#next()
   */
  @Override
  public int next() {
    int current = wordBreakIterator.current();
    if (current == BreakIterator.DONE) {
      return current;
    }
    int next = 0;
    for (;;) {
      next = wordBreakIterator.next();
      if (next <= 0 || (next + 2) >= textLength) {
        break;
      }
      if (wordBreakIterator.isBoundary(next)) {
        char currentChar = text.charAt(next);
        if (currentChar == '<') {
          // when '<' encountered, return the token so far, then set the flag
          inXml = true;
          break;
        }
        if (currentChar == '-') {
          // when a '-' is encountered, we need to keep going until we encounter
          // a whitespace boundary. But we don't return the token in this case.
          inHyphenatedToken = true;
        }
        if (inXml) {
          // xml pattern tokenization, keep accumulating until the closing
          // tag is encountered, then advance the wordBreakIterator to the 
          // next token and return
          if (currentChar != '>') {
            current = next;
            continue;
          }
          // reset the inXml when '>' encountered
          next = wordBreakIterator.next();
          current = next;
          inXml = false;
          break;
        } else if (inHyphenatedToken) {
          while (! Character.isWhitespace(currentChar)) {
            next = wordBreakIterator.next();
            if (next >= textLength) {
              break;
            }
            currentChar = text.charAt(next);
            current = next;
            continue;
          }
          inHyphenatedToken = false;
          break;  
        } else {
          if (Character.isLetterOrDigit(currentChar)) {
            // if the next boundary starts with a letter or digit, there is
            // not much confusion as to whether it is a boundary or not.
            break;
          }
          char precedingChar = text.substring(next - 1, next).charAt(0);
          char followingChar = text.substring(next + 1, next + 2).charAt(0); 
          System.out.println("text[" + next + "]=" + currentChar + ", preceding char=" + precedingChar + ", following char=" + followingChar);
          // Default rule: If the boundary is not a letter or digit, it can be one of 
          // the many punctuation chars. If the character before and after the boundary 
          // character is alphanumeric, then we consider this a boundary.
          if (Character.isLetterOrDigit(precedingChar) && 
              Character.isLetterOrDigit(followingChar)) {
            break;
          }
          current = next;
        }
      } else {
        break;
      }
    }
    return next;
  }
  
  /**
   * @see java.text.BreakIterator#current()
   */
  @Override
  public int current() {
    return wordBreakIterator.current();
  }

  /**
   * @see java.text.BreakIterator#first()
   */
  @Override
  public int first() {
    return wordBreakIterator.first();
  }

  /**
   * @see java.text.BreakIterator#following(int)
   */
  @Override
  public int following(int offset) {
    return wordBreakIterator.following(offset);
  }

  /**
   * @see java.text.BreakIterator#last()
   */
  @Override
  public int last() {
    return wordBreakIterator.last();
  }

  /**
   * @see java.text.BreakIterator#next(int)
   */
  @Override
  public int next(int n) {
    return wordBreakIterator.next(n);
  }

  /**
   * @see java.text.BreakIterator#previous()
   */
  @Override
  public int previous() {
    return wordBreakIterator.previous();
  }

  /**
   * @see java.text.BreakIterator#getText()
   */
  @Override
  public CharacterIterator getText() {
    return wordBreakIterator.getText();
  }

  /**
   * @see java.text.BreakIterator#setText(java.text.CharacterIterator)
   */
  @Override
  public void setText(CharacterIterator newText) {
    wordBreakIterator.setText(newText);
  }
}
