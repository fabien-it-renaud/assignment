package searchengine;

import java.util.List;


public interface DocumentTokenizer {    
    public List<Token> tokenize(Document doc);
}
