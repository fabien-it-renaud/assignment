package searchengine;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;


/**
 * A Document is simply a string of words without any punctuation.
 * The content is simply tokenized with spaces as delimiters.
 * 
 * The maximal size of a document is Integer.MAX_VALUE
 * 
 */
public class Document implements Comparable<Document> {
    private final String content;
    private List<String> tokens;
    private static int nbDocuments = 0;
    private final Integer id;
    private final String name;
    private HashMap<String, Double> tokensFrequency;
    private final int numberOfTokens;
    
    /**
     * 
     * @param content the content of the document
     * @param name the name of the document
     */
    public Document(String content, String name) {
        this.name = name;
        this.content = content.trim();
        this.id = nbDocuments;
        Document.nbDocuments += 1;
        this.tokenize();
        this.numberOfTokens = this.tokens.size();
        this.computeTokensFrequency();
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
        Document.nbDocuments += 1;
        this.tokenize();
        this.numberOfTokens = this.tokens.size();
        this.computeTokensFrequency();
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
    
    
    // Simply split words according to spaces between them
    private void tokenize() {
        String[] tokensTmp = content.split(" +");
        // This is consuming a lot of time
        this.tokens = new ArrayList<>();
        for (String token : tokensTmp) {
            this.tokens.add(token.toLowerCase());
        }
    }
    
    private void computeTokensFrequency() {
        this.tokensFrequency = new HashMap<>();
        
        for (String token : this.tokens) {
            if (this.tokensFrequency.containsKey(token)) {
                continue;
            }
            double nbOccs = Collections.frequency(this.tokens, token);
            // The division is safe since if we are there is at least one token
            double freq = nbOccs / this.numberOfTokens;
            tokensFrequency.put(token, freq);
        }
    }
    
    /**
     * Dumps the frequency table of all tokens
    */
    public void printTokensFrequency() {
        for (String tok : this.tokens) {
            System.out.println(tok + ": " +  this.getTokenFrequency(tok));
        }
    }
    
    /**
     * 
     * @return The list of tokens in the document i.e. all the words
     * (duplicate are __not__removed)
     */
    public List<String> getTokens() {
        return this.tokens;
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
    public double getTokenFrequency(String token) {
        if (this.tokensFrequency.containsKey(token)) {
                return this.tokensFrequency.get(token);
        } else {
            return 0;
        }
    }
}
