package searchengine;

import java.util.Collections;
import java.util.Map;
import java.util.List;


/**
 * A Document is simply a list of words.
 * 
 * The maximal size of a document is Integer.MAX_VALUE
 * 
 */
public class Document implements Comparable<Document> {
    private final String content;
    private List<Token> tokens;
    private static int nbDocuments = 0;
    private final Integer id;
    private final String name;
    private Map<Token, Double> tokensFrequency;
    
    /**
     * 
     * @param content the content of the document
     * @param name the name of the document
     */
    public Document(String content, String name) {
        this.content = content.trim();
        this.id = nbDocuments;
        this.name = name;
        this.tokens = Collections.EMPTY_LIST;
        Document.nbDocuments += 1;
        this.tokensFrequency = Collections.EMPTY_MAP;
    }
    
    /**
     * Creates a document without specifying a name. In this case, a unique
     * identifier (integer) is assigned to the name.
     * @param content the content of the document
     */
    public Document(String content) {
        this.content = content.trim();
        this.id = nbDocuments;
        this.name = String.valueOf(id);
        this.tokens = Collections.EMPTY_LIST;
        Document.nbDocuments += 1;
        this.tokensFrequency = Collections.EMPTY_MAP;
    }
    
    public void tokenize(DocumentTokenizer docTok) {
        this.tokens = docTok.tokenize(this);
        this.tokensFrequency = 
                DocumentStatistics.computeTokensFrequency(tokens);
    }
    
    /**
     * Compare two documents according to their identifier
     * 
     * @param other the other document compared
     * @return 0 if the two identifier are equal (only true when comparing
     * a document with itself); a value greater than 0 if this document's id is
     * greater than other's id; a value less than 0 if this document's id
     * is less than other's id
     * */
    @Override
    public int compareTo(Document other) {
        // This has to be a total strict order so operations on Set can work
        return this.id.compareTo(other.id);
    }
    
    /**
     * 
     * @return The list of tokens in the document i.e. all the words
     * (duplicate are __not__removed)
     */
    public List<Token> getTokens() {
        return this.tokens;
    }
    
     /**
     * 
     * @return The whole content of the document, as a string
     */
    public String getContent() {
        return this.content;
    }
    
    /**
     * 
     * @return the unique identifier of the document
     */
    public int getId() {
        return this.id;
    }
    
    /**
     * 
     * @return the name of the document
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * Returns "Document " concatenated with the document's name
     * @return a string representation of the document
     */
    @Override
    public String toString() {
        return ("Document " + this.name); 
    }
    
    /**
     * 
     * @param token
     * @return the frequency of the token in the document that is 
     * the number of occurrences of the token in the document divided by
     * the total length of the document
     */
    public double getTokenFrequency(Token token) {
        if (this.tokensFrequency.containsKey(token)) {
                return this.tokensFrequency.get(token);
        } else {
            return 0;
        }
    }
}
