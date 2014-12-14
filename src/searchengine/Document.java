package searchengine;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


/**
 *
 * @author renaud
 */
public class Document implements Comparable<Document> {
    private final String content;
    private List<String> tokens;
    private static int nbDocuments = 0;
    private final Integer id;
    private final String name;
    private HashMap<String, Double> tokensFrequency;
    private final int numberOfTokens;
    
    public Document(String content, String name) {
        this.name = name;
        this.content = content.trim();
        this.id = nbDocuments;
        Document.nbDocuments += 1;
        this.tokenize();
        this.numberOfTokens = this.tokens.size();
        this.computeTokensFrequency();
    }
    
    public Document(String content) {
        this.content = content.trim();
        this.id = nbDocuments;
        this.name = String.valueOf(id);
        Document.nbDocuments += 1;
        this.tokenize();
        this.numberOfTokens = this.tokens.size();
        this.computeTokensFrequency();
    }
    
    @Override
    public int compareTo(Document other) {
        // This has to be a total strict order so operations on Set can work
        return this.id.compareTo(other.id);
    }
    
    private void tokenize() {
        this.tokens = Arrays.asList(content.split(" +"));
    }
    
    private void computeTokensFrequency() {
        this.tokensFrequency = new HashMap<>();
        
        for (String token : this.tokens) {
            if (this.tokensFrequency.containsKey(token))
                break;
            double nbOccs = Collections.frequency(this.tokens, token);
            double freq = nbOccs / this.numberOfTokens;
            tokensFrequency.put(token, freq);
        }
    }
    
    public List<String> getTokens() {
        return this.tokens;
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    @Override
    public String toString() {
        return ("Document " + this.name); 
    }
    
    public double getTokenFrequency(String token) {
        if (this.tokensFrequency.containsKey(token))
                return this.tokensFrequency.get(token);
        else
            return 0;
    }
}
