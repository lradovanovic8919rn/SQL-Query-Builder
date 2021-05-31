package querybuilder.rules;

import java.util.ArrayList;
import java.util.List;

public class SelectAggregateRule implements Rules {
    @Override
    public boolean checkTheRule(String string) {
        String temp = string.substring(string.indexOf(".") + 1);
        String[] newParts = temp.split("\\)\\.");
        List<String> selectArgs = new ArrayList<>();


        for (String newPart : newParts) {
            if (newPart.contains("Select(")) {
                String[] args = newPart.split("\"");
                for(int i=0; i<args.length; i++){
                    if(i%2!=0){
                        //System.out.println("arg"+i+": " + args[i]);
                        selectArgs.add(args[i]);
                    }
                }
                for (String np : newParts) {
                    if(np.contains("Avg(") || np.contains("Count(") || np.contains("Min(") || np.contains("Max(")){
                        String[] aAlias = np.split("\"");
                        //System.out.println("a3: " + aAlias[3]);
                        if(selectArgs.contains(aAlias[3])) return true;
                    }
                    if(np.contains("GroupBy(")){
                        String[] aAlias = np.split("\"");
                        //System.out.println("a1: " + aAlias[1]);
                        if(selectArgs.contains(aAlias[1])) return true;
                    }
                }
            }else{
                return true;
            }

        }
        return false;
    }

    @Override
    public String getErrorMessage() {
        return "SelectAggregate rule failed! You need to have aggregate function in your select!";
    }
}
