package querybuilder.rules;

public class VarNewQueryRule implements Rules {
    @Override
    public boolean checkTheRule(String s) {
        String[] string = s.split(" ");
        return string[0].equals("var") && string[2].equals("=") && string[3].equals("new") && string[4].contains("Query");
    }

    @Override
    public String getErrorMessage() {
        return "Var New Query rule failed! You need to have var yourvarname = Query";
    }

}
