package searchengine;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Compute statistics on the content of a document 
 */
public class DocumentStatistics {
    
    /**
     * 
     * @param tokens the tokenized content of the document
     * @return       a map associating to each token its frequency
     */
    public static Map<Token, Double> 
      computeTokensFrequency(List<Token> tokens) {
        Map<Token, Double> tokensFrequency = new HashMap<>();
        
        for (Token token : tokens) {
            if (tokensFrequency.containsKey(token)) {
                continue;
            }
            double nbOccs = Collections.frequency(tokens, token);
            // The division is safe since if we are there is at least one token
            double freq = nbOccs / tokens.size();
            tokensFrequency.put(token, freq);
        }
        
        return tokensFrequency;
    }
}
