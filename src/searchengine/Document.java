package searchengine;

import java.util.HashMap;
        
/**
 *
 * @author renaud
 */
public class Document implements Comparable<Document> {
    private final String content;
    private String[] tokens;
    private static int nbDocuments = 0;
    private final Integer name;  // TODO: Generalize this
    private HashMap<String, Double> tokensFrequency;
    private final int numberOfTokens;
    
    public Document(String content) {
        this.content = content;
        this.name = nbDocuments;
        Document.nbDocuments += 1;
        this.tokenize();
        this.numberOfTokens = this.tokens.length;
        this.computeTokensFrequency();
    }
    
    @Override
    public int compareTo(Document other) {
        // This has to be a total strict order so operations on Set can work
        return this.name.compareTo(other.name);
    }
    
    private void tokenize() {
        this.tokens = content.split(" ");
    }
    
    private void computeTokensFrequency() {
        this.tokensFrequency = new HashMap<>();
        for (String token : tokens) {
            Double n = tokensFrequency.get(token);
            if (n == null)
                n = 1.0;
            else
                n += 1;
            tokensFrequency.put(token, n);
        }
 
        for (String token : tokens) {
            Double n = tokensFrequency.get(token);
            tokensFrequency.put(token, n/(double)this.numberOfTokens);
        }
        
        
    }
    
    public String[] getTokens() {
        return this.tokens;
    }
    
    public int getName() {
        return this.name;
    }
    
    public double getTokenFrequency(String token) {
        return this.tokensFrequency.get(token);
    }
}
