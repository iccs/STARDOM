package eu.alertproject.iccs.stardom.lsa.tfidf.tokenizers;

/**
 * List of all possible token types. Default is UNKNOWN.
 * @author Sujit Pal
 * @version $Revision$
 */
public enum TokenType {
  ABBREVIATION, 
  COMBINED, 
  PHRASE, 
  EMOTICON, 
  INTERNET, 
  WORD,
  STOP_WORD,
  CONTENT_WORD,
  NUMBER, 
  WHITESPACE,
  PUNCTUATION, 
  PLACE, 
  ORGANIZATION,
  MARKUP, 
  UNKNOWN
}
