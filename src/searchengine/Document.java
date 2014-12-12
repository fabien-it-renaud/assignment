package searchengine;

import java.util.HashMap;
import java.util.List;
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
    private final Integer name;  // TODO: Generalize this
    private HashMap<String, Double> tokensFrequency;
    private final int numberOfTokens;
    
    public Document(String content) {
        this.content = content;
        this.name = nbDocuments;
        Document.nbDocuments += 1;
        this.tokenize();
        this.numberOfTokens = this.tokens.size();
        this.computeTokensFrequency();
    }
    
    @Override
    public int compareTo(Document other) {
        // This has to be a total strict order so operations on Set can work
        return this.name.compareTo(other.name);
    }
    
    private void tokenize() {
        this.tokens = Arrays.asList(content.split(" "));
    }
    
    private void computeTokensFrequency() {
        this.tokensFrequency = new HashMap<>();
        
        
        for (String token : this.tokens) {
            if (this.tokensFrequency.containsKey(token))
                break;
            double nbOccs = Collections.frequency(this.tokens, token);
            double freq = nbOccs / this.numberOfTokens;
            tokensFrequency.put(token, freq);
            System.out.println("For token " + token + " the frequency is " +
                    freq);
        }
        
        
    }
    
    public List<String> getTokens() {
        return this.tokens;
    }
    
    public int getName() {
        return this.name;
    }
    
    public double getTokenFrequency(String token) {
        if (this.tokensFrequency.containsKey(token))
                return this.tokensFrequency.get(token);
        else
            return 0;
    }
}
