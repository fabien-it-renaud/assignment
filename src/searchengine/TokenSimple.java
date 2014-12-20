package searchengine;


public class TokenSimple implements Token {
    private final String token;
    
    public TokenSimple(String token) {
            this.token = token;
    }
    
    @Override
    public int compareTo(Token other) {
        return this.token.compareTo(other.getContent());
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else {
            if (obj instanceof Token) {
                Token other = (Token)obj;
                return this.token.equals(other.getContent());
            } else {
                return false;
            }
        } 
    }
    
    @Override
    public int hashCode() {
        return this.token.hashCode();
    }
    
    @Override
    public String getContent() {
        return this.token;
    }
    
    @Override
    public String toString() {
        return this.token;
    }
    
}
