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
 *
 * @author renaud
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
    }
    
    /* A pair of a token and the document where it appears with the possibility
    of being sorted according to tf-idf  
    Ultimately this should be replaced by a single comparaison function
    which can handle the current context.
    */
    private class TokenInDoc implements Comparable<TokenInDoc> {
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
     * Searches a token within the list of documents
     * 
     * @param token The token we want to search
     * @return the ordered list of documents, according to tf-idf 
     *         where the token appears
     */
    public List<Document> search(String token) {
        List<Document> docs = this.sortedInvertedIndex.get(token);
        if (docs == null) 
            return (new ArrayList<>());
        else
            return docs;
    }
    
    
    // For debugging purposes 
    private void printSortedInvertedIndex() {
        for (String token : this.sortedInvertedIndex.keySet()) {
            System.out.print("Token " + token + " appears in documents ");
            for (Document doc : this.sortedInvertedIndex.get(token)) {
                System.out.print(" " + doc.getName());
            }
            System.out.println("");
        }
    }

    
    /**
     * Interactively searches for tokens in documents in the current directory
     */
    public static void searchDocumentCurrentDirectory(){
        List<Document> docs = new ArrayList<>();
        
        File directory = new File(".");
        File[] fList = directory.listFiles();
        for (File file : fList) {
            if (file.isFile()) {
                byte[] bytes;
                try {
                    bytes = Files.readAllBytes(file.toPath());
                    String content = new String(bytes);
                    Document d = new Document(content, file.getName());
                    docs.add(d);
                } catch (IOException e) {
                    System.out.println("Impossible to read the file " + 
                            file.getName());
                }
           }
        }
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
    
    
    public static void exampleInAssignment() {
        Document d1, d2, d3, d4, d5, d6;
        d1 = new Document("the brown fox jumped over the brown dog", "one");
        d2 = new Document("the lazy brown dog sat in the corner", "two");
        d3 = new Document("the red fox bit the lazy dog", "three");
        List<Document> docs = new ArrayList<>();
        docs.add(d1);
        docs.add(d2);
        docs.add(d3);
        
        SearchEngine gugul = new SearchEngine(docs);
        
        System.out.println(gugul.search("brown"));
        System.out.println(gugul.search("fox"));
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        exampleInAssignment();
        searchDocumentCurrentDirectory();
    }
    
}
