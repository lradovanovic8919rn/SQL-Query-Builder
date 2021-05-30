package querybuilder.rules;

public interface Rules {
    boolean checkTheRule(String string);
    String getErrorMessage();
}
