package querybuilder.rules;

public class HavingAmountRule implements Rules {
    @Override
    public boolean checkTheRule(String string) {
        int counter = 0;
        String temp = string.substring(string.indexOf(".") + 1);
        String[] newParts = temp.split("\\)\\.");

        for (String newPart : newParts) {
            if ((newPart.contains("Having(")) && (!newPart.contains("AndHaving(")) && (!newPart.contains("AndHaving("))) {
                counter++;
            }
        }
        return (counter == 0) || (counter == 1);
    }

    @Override
    public String getErrorMessage() {
        return "HavingAmount rule failed! You can call Having() only once!";
    }
}
