package eu.alertproject.iccs.stardom.lsa.tfidf.tokenizers;


/**
 * Models a word token. Also contains information about the type of entity
 * the token represents.
 * @author Sujit Pal
 * @version $Revision$
 */
public class Token {

  public Token() {
    super();
  }
  
  public Token(String value, TokenType type) {
    this();
    setValue(value);
    setType(type);
  }
  
  private String value;
  private TokenType type;
  
  public String getValue() {
    return value;
  }
  
  public void setValue(String value) {
    this.value = value;
  }
  
  public TokenType getType() {
    return type;
  }
  
  public void setType(TokenType type) {
    this.type = type;
  }
  
  @Override
  public String toString() {
    return value + " (" + type + ")";
  }
}
