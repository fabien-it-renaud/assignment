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
     * @param size   the number of tokens
     * @return       a map associating to each token its frequency
     */
    public static Map<String, Double> 
      computeTokensFrequency(List<String> tokens, int size) {
        Map<String, Double> tokensFrequency = new HashMap<>();
        
        for (String token : tokens) {
            if (tokensFrequency.containsKey(token)) {
                continue;
            }
            double nbOccs = Collections.frequency(tokens, token);
            // The division is safe since if we are there is at least one token
            double freq = nbOccs / size;
            tokensFrequency.put(token, freq);
        }
        
        return tokensFrequency;
    }
}
