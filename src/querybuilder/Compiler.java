package querybuilder;

public interface Compiler {
    void compile(String text);
    void compilePart(String part);
    void queryBuilder(String q, String args);

}
