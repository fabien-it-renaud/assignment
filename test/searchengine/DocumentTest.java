package searchengine;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;


public class DocumentTest {
    
    private boolean noEmptyString(Document doc) {
        List<Token> tokens = doc.getTokens();
        for (Token tok : tokens) {
            String content = tok.getContent();
            if (content.equals("")) {
                return false;
            }
        }
        
        return true;
    }
    
    @Test
    public void testGetTokensEmptyString() {
        Document d1 = new Document(" abc    def     ghi  ");
        Document d2 = new Document("");

        assertTrue(noEmptyString(d1));
        assertTrue(noEmptyString(d2));
    }
}
