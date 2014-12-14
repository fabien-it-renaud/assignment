/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package searchengine;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author renaud
 */
public class SearchEngineTest {

    /**
     * Test the examples in the assignment
     */
    @Test
    public void exampleInAssignment() {
        Document d1, d2, d3;
        d1 = new Document("the brown fox jumped over the brown dog", "one");
        d2 = new Document("the lazy brown dog sat in the corner", "two");
        d3 = new Document("the red fox bit the lazy dog", "three");
        List<Document> docs = new ArrayList<>();
        docs.add(d1);
        docs.add(d2);
        docs.add(d3);
        
        SearchEngine gugul = new SearchEngine(docs);
        
        List<Document> resultBrown = new ArrayList<>();
        resultBrown.add(d1);
        resultBrown.add(d2);
        assertTrue(gugul.search("brown").equals(resultBrown));
        
        List<Document> resultFox = new ArrayList<>();
        resultFox.add(d3);
        resultFox.add(d1);
        assertTrue(gugul.search("fox").equals(resultFox));
    }
    
    
    /**
     * Test searching the empty string
     */
    @Test
    public void testEmptyString() {
        Document d1, d2, d3;
        d1 = new Document("the brown fox jumped over the brown dog");
        d2 = new Document("");
        d3 = new Document("abc     def");
        List<Document> docs = new ArrayList<>();
        docs.add(d1);
        docs.add(d2);
        docs.add(d3);
        
        SearchEngine gugul = new SearchEngine(docs);
        
        List<Document> resultEmpty = new ArrayList<>();
        assertTrue(gugul.search("").equals(resultEmpty));
    }
}
