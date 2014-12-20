package searchengine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Simply splits words according to spaces between them
 */
public class DocumentTokenizerSimple implements DocumentTokenizer {
    
    /**
     * 
     * @param doc 
     * @return the list of tokens which were delimited by spaces
     */
    @Override
    public List<Token> tokenize(Document doc) {
        String content = doc.getContent();
        
        String[] tokensTmp = content.split(" +");
        List<Token> tokens = new ArrayList<>();
        
        String[] singletonEmpty = {""};
        if (Arrays.equals(tokensTmp, singletonEmpty)) {
            // If the entire document is empty
            return tokens;
        } else {
            // This is consuming a lot of time
            for (String token : tokensTmp) {
                Token tokenLower = new TokenSimple(token.toLowerCase());
                tokens.add(tokenLower);
            }
            return tokens;
        }
    }   
}
