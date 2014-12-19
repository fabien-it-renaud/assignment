package searchengine;

import java.util.ArrayList;
import java.util.List;



public class DocumentTokenizer {
            
    // Simply split words according to spaces between them
    public static List<String> tokenize(String content) {
        String[] tokensTmp = content.split(" +");
        List<String> tokens = new ArrayList<>();
        
        // This is consuming a lot of time
        for (String token : tokensTmp) {
            tokens.add(token.toLowerCase());
        }
        
        return tokens;
    }
}
