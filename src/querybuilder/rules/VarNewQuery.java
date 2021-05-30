package querybuilder.rules;

public class VarNewQuery implements Rules {

    @Override
    public boolean checkTheRule(String s) {
        String[] string = s.split(" ");
        if(string[0].equals("var") && string[2].equals("=") &&
                string[3].equals("new") && string[4].contains("Query")){
            return true;
        }
        return false;
    }

    @Override
    public String getErrorMessage() {
        return "Var New Query rule failed";
    }

}
