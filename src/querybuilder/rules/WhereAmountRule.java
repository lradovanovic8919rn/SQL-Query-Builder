package querybuilder.rules;

public class WhereAmountRule implements Rules {
    @Override
    public boolean checkTheRule(String string) {

        int counter = 0;

        String[] var = string.split(" ");
        String[] parts = string.split("\\.");
        String temp = string.substring(string.indexOf(".") + 1);
        String[] newParts = temp.split("\\)\\.");

        for (String newPart : newParts) {
            if (((newPart.contains("Where(")) && (!newPart.contains("AndWhere(")) && (!newPart.contains("OrWhere("))) || (newPart.contains("WhereEqQ(")) || (newPart.contains("WhereInQ(")) || (newPart.contains("WhereStartsWith(")) || (newPart.contains("WhereContains(")) || (newPart.contains("WhereEndsWith("))) {
                counter++;
            }
        }
        return (counter == 0) || (counter == 1);
    }

    @Override
    public String getErrorMessage() {
        return "WhereAmount rule failed!You can call use where only once per query!";
    }
}
