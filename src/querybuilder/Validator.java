package querybuilder;

public interface Validator {
    boolean validate(String text);
    void printFailedRules();
    String getFailedRules();
}
