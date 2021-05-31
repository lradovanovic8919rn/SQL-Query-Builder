package querybuilder.rules;

public class WhereInOrderRule implements Rules {
    @Override
    public boolean checkTheRule(String string) {
        String temp = string.substring(string.indexOf(".") + 1);
        String[] newParts = temp.split("\\)\\.");
        if (string.contains("WhereIn(")) {
            for (int i = 0; i < newParts.length; i++) {
                if ((newParts[i].contains("WhereIn(")) && (i != newParts.length - 1)) {
                    if (newParts[i + 1].contains("ParametarList(")) {
                        return true;
                    }
                }
            }
        }
        return !string.contains("WhereIn(");
    }

    @Override
    public String getErrorMessage() {
        return "WhereInOrder rule failed! You have to use WhereIn().ParametarList() in this exact order!";
    }
}
