package searchengine;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;


public class DocumentTest {
    
    @Test
    public void testGetTokensEmptyString() {
        Document d1 = new Document(" abc    def     ghi  ");
        Document d2 = new Document("");

        List<Token> tokensDoc1 = d1.getTokens();
        for (Token tok : tokensDoc1) {
            String content = tok.getContent();
            assertFalse(content.equals(""));
        }
        
        List<Token> tokensDoc2 = d2.getTokens();
        for (Token tok : tokensDoc2) {
            String content = tok.getContent();
            assertFalse(content.equals(""));
        }
    }
    
}
