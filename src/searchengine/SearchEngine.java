package searchengine;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.Set;
import java.util.Collections;
import java.util.Map;
import java.util.List;

import java.util.Scanner;
import java.io.File;
import java.nio.file.Files;
import java.io.IOException;


/**
 * A SearchEngine can search for tokens in a list of documents.
 * To obtain an efficient search, an inverted index (associating tokens to
 * the documents where they appear) is created. It may thus take time
 * to initialize a SearchEngine.
 * Furthermore the documents associated to each token are sorted
 * according to tf-idf statistic (http://en.wikipedia.org/wiki/Tf-idf)
 */
public class SearchEngine {
    private final List<Document> documents;
    private final int numDocs; // The number of documents in the corpus

   
    /* The inverted index maps each token contained in the corpus to the 
    * names of the documents where they appear. Documents are so far unordered.
    */
    private final Map<String, Set<Document>> invertedIndex;
    
    /* As we will have to go through all the unordered inverted index 
    * to sort it, it is simpler to recreate it ordered
    */
    private Map<String, List<Document>> sortedInvertedIndex;
  
    
    /**
     * 
     * @param documents The documents in which the search can be made
     */
    public SearchEngine(List<Document> documents) {
        this.documents = documents;
        this.numDocs = this.documents.size();
        this.invertedIndex = new HashMap<>(this.numDocs);
        this.createInvertedIndex();
        this.sort();
    }
    
    /**
     * 
     * @return the inverted index where documents are sorted according to tf-idf
     */
    public Map<String, List<Document>> getSortedInvertedIndex() {
        return this.sortedInvertedIndex;
    }
    
      
    /**
     * Creates the inverted index where documents are unsorted
     */
    private void createInvertedIndex() {
        for (Document doc : this.documents) {
            for (String token : doc.getTokens()) {
                Set<Document> setOfDocs = this.invertedIndex.get(token);
                 
                if (setOfDocs == null) {
                    setOfDocs = new TreeSet<>();
                    this.invertedIndex.put(token, setOfDocs);
                }
                
                setOfDocs.add(doc);
            }
        }
        
        /* In case of a document was empty */
        this.invertedIndex.remove("");
    }
    
    /* A pair of a token and the document where it appears with the possibility
    of being sorted according to tf-idf  
    Ultimately this should be replaced by a single comparaison function
    which can handle the current context.
    */
    private static class TokenInDoc implements Comparable<TokenInDoc> {
        private final String token;
        private final Document document;
        // The number of documents where the token appears (including
        // the current one)
        private final double numDocsWithToken ;
                
        public TokenInDoc(String token, Document document, int docsWithToken) {
            this.token = token;
            this.document = document;
            this.numDocsWithToken = (double)docsWithToken;
        }
        
        public Document getDocument() {
            return this.document;
        }
        
        public String getToken() {
            return this.token;
        }
        
        private double computeTfIdf() {
            double termFrequency = this.document.getTokenFrequency(this.token);
            double inverseDocFreq = 1.0;
            /* The idf part does not play any role in the ordering
                   Math.log(((double)numDocs) / (1 + numDocsWithToken));
            */
            return termFrequency * inverseDocFreq;
        }
                
        @Override
        public int compareTo(TokenInDoc other) {
            
            return -Double.compare(this.computeTfIdf(), other.computeTfIdf());
        }
            
    }
    
   /**
    * Sort for each token the documents where the token appears
    * according to tf-idf. 
    * All the sorting is done before the first search can
    * be made. It is slow to start but fast afterwards.
    * 
    */
    private void sort() {
        /* Sorting takes times since we want to rely on Java sorting algorithm
         to avoid costly debugging and testing of a home made sorting algorithm
        
        This means that sorting goes like this:
        For each token we create from its set of associated documents an array
           of pairs TokenInDoc (a pair of a token and the document 
           it appears in).
           - Sort this array since TokenInDoc has all the information to compute
                tf-idf.
           - Remove all the unnecessary tokens so once initialiazed, 
              the inverted index does not take too much space in memory.
        */
        this.sortedInvertedIndex = new HashMap<>();
        
        for (String token : this.invertedIndex.keySet()) {
            List<TokenInDoc> tokDocList = new ArrayList<>();   
            
            Set<Document> setOfDocuments = this.invertedIndex.get(token);
            int setSize = setOfDocuments.size();
            for (Document doc : setOfDocuments) {
                tokDocList.add(new TokenInDoc(token, doc, setSize));
            }
            
            Collections.sort(tokDocList);
            
            List<Document> sortedDocs = new ArrayList<>();
            this.sortedInvertedIndex.put(token, sortedDocs);
            for (TokenInDoc tokDoc : tokDocList) {
                sortedDocs.add(tokDoc.getDocument());                        
            }          
            
        }
    }
    
    /**
     * Searches a token within the list of documents and returns the documents
     * where it appears, ordered by their tf-idf score.
     * If a token has the same tf-idf in two documents, documents are listed 
     * in the order in which they were given to the constructor.
     * 
     * @param token The token we want to search
     * @return the ordered list of documents, according to tf-idf 
     *         where the token appears
     */
    public List<Document> search(String token) {
        List<Document> docs = this.sortedInvertedIndex.get(token.toLowerCase());
        if (docs == null) {
            return (new ArrayList<>());
        } else {
            return docs;
        }
    }
    
    
    /**
     * Dump of the entire sorted inverted index
     * 
     * @return the sorted inverted index, as a string
     */ 
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        String NEW_LINE = System.getProperty("line.separator");
        
        for (String token : this.sortedInvertedIndex.keySet()) {
            result.append("Token " + token + " appears in documents:");
            for (Document doc : this.sortedInvertedIndex.get(token)) {
                result.append(" "+ doc.getName());
            }
            result.append(NEW_LINE);
        }
        
        return result.toString();
    }

    
    /**
     * Interactively searches for tokens in documents in the current directory
     * 
     * @param directory
     */
    public static void searchInDirectory(String directory) {
        List<Document> docs = DocumentBuilder.buildFromDirectory(directory);
        SearchEngine searchEng = new SearchEngine(docs);
        
        Scanner sc = new Scanner(System.in);
        String token;
        System.out.println("Type the word you are looking for, "
                + "or return to exit");
        try {
            while (sc.hasNextLine() && 
                !(token = sc.nextLine()).isEmpty()) {
                System.out.println(searchEng.search(token));
            }
        } catch (Exception e) {
            System.out.println("Something went wrong during the search");
        } 
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        searchInDirectory(".");
    }
    
}
