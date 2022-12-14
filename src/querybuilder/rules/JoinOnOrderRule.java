package querybuilder.rules;

public class JoinOnOrderRule implements Rules {
    @Override
    public boolean checkTheRule(String string) {
        String temp = string.substring(string.indexOf(".") + 1);
        String[] newParts = temp.split("\\)\\.");
        if (string.contains("Join(")) {
            for (int i = 0; i < newParts.length; i++) {
                if ((newParts[i].contains("Join(")) && (i != newParts.length - 1)) {
                    if (newParts[i + 1].contains("On(")) {
                        return true;
                    }
                }
            }
        }
        return !string.contains("Join(");
    }

    @Override
    public String getErrorMessage() {
        return "JoinOnOrder rule failed! You have to use Join().On() in this exact order!";
    }
}
