package querybuilder.rules;

public class HavingAmountRule implements Rules{
    @Override
    public boolean checkTheRule(String string) {

        int counter=0;

        String[] var = string.split(" ");
        String[] parts = string.split("\\.");
        String temp = string.substring(string.indexOf(".") + 1);
        String[] newParts = temp.split("\\)\\.");

        for (int i = 0; i < newParts.length; i++) {
            if((newParts[i].contains("Having("))&&(!newParts[i].contains("AndHaving("))&&(!newParts[i].contains("AndHaving("))){
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
        return "HavingAmount rule failed!You can call Having() only once!";
    }
}
