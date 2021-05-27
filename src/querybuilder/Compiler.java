package querybuilder;

public interface Compiler {
    String compile(String text);
    void compilePart(String part);
    void queryBuilder(String q, String args);
    void reset();
}
