package querybuilder.rules;

public class HavingAliasRule implements Rules {
    @Override
    public boolean checkTheRule(String string) {
        String temp = string.substring(string.indexOf(".") + 1);
        String[] newParts = temp.split("\\)\\.");

        for (String newPart : newParts) {
            if (newPart.contains("Having(") || newPart.contains("AndHaving(") || newPart.contains("OrHaving(")) {
                String[] alias = newPart.split("\"");
                //System.out.println("alias1: " + alias[1]);
                for (String np : newParts) {
                    if(np.contains("Avg(") || np.contains("Count(") || np.contains("Min(") || np.contains("Max(")){
                        String[] aAlias = np.split("\"");
                        //System.out.println("a3: " + aAlias[3]);
                        if(aAlias[3].equals(alias[1])) return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public String getErrorMessage() {
        return "HavingAlias rule failed! You need to have same alias in Having and aggregate function!";
    }
}
