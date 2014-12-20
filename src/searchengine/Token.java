package searchengine;

public interface Token extends Comparable<Token> {
    public String getContent();
    @Override
    public int compareTo(Token other);
    @Override
    public int hashCode();
    @Override
    public boolean equals(Object other);
}
