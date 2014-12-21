package searchengine;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.Set;
import java.util.Collections;
import java.util.Map;
import java.util.List;

import java.util.Scanner;


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
    private final DocumentTokenizer docTok;

   
    /* The inverted index maps each token contained in the corpus to the 
    * names of the documents where they appear. Documents are so far unordered.
    */
    private final Map<Token, Set<Document>> invertedIndex;
    
    /* As we will have to go through all the unordered inverted index 
    * to sort it, it is simpler to recreate it ordered
    */
    private Map<Token, List<Document>> sortedInvertedIndex;
  
    
    /**
     * 
     * @param documents The documents in which the search can be made
     * @param docTok Specifies how the documents are tokenized
     */
    public SearchEngine(List<Document> documents, DocumentTokenizer docTok) {
        this.documents = documents;
        this.docTok = docTok;
        this.numDocs = this.documents.size();
        this.invertedIndex = new HashMap<>(this.numDocs);
        this.createInvertedIndex();
        this.sort();
    }
    
    /**
     * 
     * @return the inverted index where documents are sorted according to tf-idf
     */
    public Map<Token, List<Document>> getSortedInvertedIndex() {
        return this.sortedInvertedIndex;
    }
    
      
    /**
     * Creates the inverted index where documents are unsorted
     */
    private void createInvertedIndex() {
        for (Document doc : this.documents) {
            doc.tokenize(this.docTok);
            for (Token token : doc.getTokens()) {
                Set<Document> setOfDocs = this.invertedIndex.get(token);
                 
                if (setOfDocs == null) {
                    setOfDocs = new TreeSet<>();
                    this.invertedIndex.put(token, setOfDocs);
                }
                
                setOfDocs.add(doc);
            }
        }
        
        /* In case of a document was empty 
        The fact that we are using a simple token should be taken in the
        search engine constructor
        */
        Token emptyTok = new TokenSimple("");
        this.invertedIndex.remove(emptyTok);
    }
    
    /* A pair of a token and the document where it appears with the possibility
    of being sorted according to tf-idf  
    Ultimately this should be replaced by a single comparaison function
    which can handle the current context.
    */
    private static class TokenInDoc implements Comparable<TokenInDoc> {
        private final Token token;
        private final Document document;
        // The number of documents where the token appears (including
        // the current one)
        private final double numDocsWithToken;
                
        public TokenInDoc(Token token, Document document, int docsWithToken) {
            this.token = token;
            this.document = document;
            this.numDocsWithToken = (double)docsWithToken;
        }
        
        public Document getDocument() {
            return this.document;
        }
        
        public Token getToken() {
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
        
        for (Token token : this.invertedIndex.keySet()) {
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
        Token tok = new TokenSimple(token.toLowerCase());
        List<Document> docs = this.sortedInvertedIndex.get(tok);
        if (docs == null) {
            return Collections.EMPTY_LIST;
        } else {
            return docs;
        }
    }
    
    
    /**
     * Gives a simple presentation of the search engine that is: the number 
     * of documents indexed as well as the different occurrences of each token
     * in the corresponding documents
     * 
     * @return a presentation of the search engine
     */ 
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        String NEW_LINE = System.getProperty("line.separator");
        
        result.append(this.numDocs + " documents indexed" + NEW_LINE);
        
        for (Token token : this.sortedInvertedIndex.keySet()) {
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
     * @param docTok
     */
    public static void 
      searchInDirectory(String directory, DocumentTokenizer docTok) {
        DocumentBuilder docBuild = new DocumentBuilder(docTok);
        List<Document> docs = 
                docBuild.buildFromDirectory(directory);
        SearchEngine searchEng = new SearchEngine(docs, docTok);
        
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
            System.err.println("Something went wrong during the search");
        } 
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        searchInDirectory(".", new DocumentTokenizerSimple());
    }
    
}
