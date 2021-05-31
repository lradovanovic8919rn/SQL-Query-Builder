package querybuilder.rules;

public class WhereAmountRule implements Rules{
    @Override
    public boolean checkTheRule(String string) {

        int counter=0;

        String[] var = string.split(" ");
        String[] parts = string.split("\\.");
        String temp = string.substring(string.indexOf(".") + 1);
        String[] newParts = temp.split("\\)\\.");

        for (int i = 0; i < newParts.length; i++) {
            if(((newParts[i].contains("Where("))&&(!newParts[i].contains("AndWhere("))&&(!newParts[i].contains("OrWhere(")))||(newParts[i].contains("WhereEqQ("))||(newParts[i].contains("WhereInQ("))||(newParts[i].contains("WhereStartsWith("))||(newParts[i].contains("WhereContains("))||(newParts[i].contains("WhereEndsWith("))){
                counter++;
            }
        }
        if((counter==0)||(counter==1)){
            return true;
        }
        return false;
    }

    @Override
    public String getErrorMessage() {
        return "WhereAmount rule failed!You can call use where only once per query!";
    }
}
