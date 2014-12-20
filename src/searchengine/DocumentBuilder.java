package searchengine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;


public class DocumentBuilder {
    
    private static File[] listAllFiles(File directory) {
        File [] emptyDir = {};
        try {
            if (directory.isDirectory()) {
                return directory.listFiles();
            } else {
                return emptyDir;
            }
        } catch (SecurityException e) {
            System.err.println("Impossible to read the directory " + directory);
            return emptyDir;       
        }
    }
    
    public static List<Document> buildFromDirectory(String dir) {
        File directory = new File(dir);
        List<Document> docs = new ArrayList<>();
        
        File[] fList = listAllFiles(directory);
        for (File file : fList) {
            if (file.isFile()) {
                byte[] bytes;
                try {
                    bytes = Files.readAllBytes(file.toPath());
                    String content = new String(bytes);
                    Document d = new Document(content, file.getName());
                    docs.add(d);
                } catch (IOException e) {
                    System.err.println("Impossible to read the file " + 
                            file.getName());
                }
           }
        }
        
        return docs;
    }
    
}
