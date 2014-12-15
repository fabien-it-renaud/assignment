package searchengine;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

/**
 *
 * @author renaud
 */
public class DocumentTest {
    
    @Test
    public void testGetTokensEmptyString() {
        Document d = new Document(" abc    def     ghi  ");        

        List<String> tokens = d.getTokens();
        assertTrue(!(tokens.contains("")));   
    }
    
}
