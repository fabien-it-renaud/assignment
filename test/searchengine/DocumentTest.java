/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package searchengine;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;
import java.util.ArrayList;

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
