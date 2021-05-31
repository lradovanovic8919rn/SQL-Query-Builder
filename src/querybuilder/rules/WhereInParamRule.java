package querybuilder.rules;

public class WhereInParamRule implements Rules {
    @Override
    public boolean checkTheRule(String s) {
        int wiFlag = 0;
        int paramFlag = 0;
        if (s.contains("WhereIn(")) {
            wiFlag++;
        }
        if (s.contains("ParametarList(")) {
            paramFlag++;
        }
        return (((wiFlag == 0) && (paramFlag == 0))) || ((wiFlag == 1) && (paramFlag == 1));
    }

    @Override
    public String getErrorMessage() {
        return "WhereInParametarList rule failed!You have to use both WhereIn() and PrarametarList() commands!";
    }
}
