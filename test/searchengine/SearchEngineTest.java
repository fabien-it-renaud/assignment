package searchengine;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import org.junit.Test;
import static org.junit.Assert.*;


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
        List<Document> docs = Arrays.asList(d1, d2, d3);
        
        SearchEngine gugul = new SearchEngine(docs);
        
        List<Document> resultBrown = Arrays.asList(d1, d2);
        assertTrue(gugul.search("brown").equals(resultBrown));
        
        List<Document> resultFox = Arrays.asList(d3, d1);
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
        List<Document> docs = Arrays.asList(d1, d2, d3);
        
        SearchEngine gugul = new SearchEngine(docs);
        
        List<Document> resultEmpty = new ArrayList<>();
        assertTrue(gugul.search("").equals(resultEmpty));
    }
    
    /**
     * Test searching a string which is not contained in any document
     */
    @Test
    public void testAbsbsentString() {
        Document d1, d2, d3;
        d1 = new Document("the brown fox jumped over the brown dog");
        d2 = new Document("the lazy brown dog sat in the corner", "two");
        d3 = new Document("the red fox bit the lazy dog", "three");
     
        List<Document> docs = Arrays.asList(d1, d2, d3);
        
        SearchEngine gugul = new SearchEngine(docs);
        
        List<Document> resultEmpty = new ArrayList<>();
        assertTrue(gugul.search("foxbit").equals(resultEmpty));
    }
    
    /*
    A search engine should not index two times the same document
    */
    @Test
    public void testIndexingSameDocument() {
        Document d = new Document("single document", "one");
        List<Document> docs = Arrays.asList(d, d, d);
        
        List<Document> result = Arrays.asList(d);
        
        SearchEngine gugul = new SearchEngine(docs);        
        assertTrue(gugul.search("single").equals(result));
        
    }
}
