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
    
    @Test
    public void testCaseDoesNotMatter() {
        Document d1, d2, d3;
        d1 = new Document("the brown fox jumped over the brown dog", "one");
        d2 = new Document("the lazy brown dog sat in the corner", "two");
        d3 = new Document("the red FOX bit the lazy dog", "three");
        List<Document> docs = Arrays.asList(d1, d2, d3);
        
        SearchEngine gugul = new SearchEngine(docs);
        
        List<Document> resultBrown = Arrays.asList(d1, d2);
        assertTrue(gugul.search("BROWN").equals(resultBrown));
        
        List<Document> resultFox = Arrays.asList(d3, d1);
        assertTrue(gugul.search("fox").equals(resultFox));
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
    
    @Test
    public void biggerTest() {
        Document d1, d2, d3, d4, d5, d6, d7;
        d1 = new Document("the brown fox jumped over the brown dog while the lazy brown dog sat in the corner", "one");
        d2 = new Document("brown is a color", "two");
        d3 = new Document("lazy is a behavior", "three");
        d4 = new Document("lazy and brown are adjectives which are not often associated", "four");
        d5 = new Document("if the adjective brown appears in a sentence it is not important", "five");
        d6 = new Document("dog is an animal", "six");
        d7 = new Document("a dog eats a fox", "seven");
        
        List<Document> docs = Arrays.asList(d1, d2, d3, d4, d5, d6, d7);
        
        List<Document> resultBrown = Arrays.asList(d2, d1, d4, d5);
        /* Notice that even though tf is the same for dog in d6 and d7,
        documents should appear in this order */
        List<Document> resultDog = Arrays.asList(d6, d7, d1);
        List<Document> resultFox = Arrays.asList(d7, d1);
        
        SearchEngine gugul = new SearchEngine(docs);  
        
        assertTrue(gugul.search("brown").equals(resultBrown));
        assertTrue(gugul.search("dog").equals(resultDog));
        assertTrue(gugul.search("fox").equals(resultFox));
        
                
    }
}
