package querybuilder.rules;

public class JoinOnRule implements Rules {
    @Override
    public boolean checkTheRule(String s) {
        int joinFlag = 0;
        int onFlag = 0;
        if (s.contains("Join(")) {
            joinFlag++;
        }
        if (s.contains("On(")) {
            onFlag++;
        }
        return (((joinFlag == 0) && (onFlag == 0))) || ((joinFlag == 1) && (onFlag == 1));
    }

    @Override
    public String getErrorMessage() {
        return "JoinOn rule failed! You have to use both Join() and On() commands!";
    }
}
