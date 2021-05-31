package querybuilder.rules;

import java.util.Arrays;

public class HavingGroupBy implements Rules {
    @Override
    public boolean checkTheRule(String s) {

        int hFlag=0;
        int gFlag=0;

            if (s.contains("Having(")){
                hFlag++;
            }
            if (s.contains("GroupBy(")){
                gFlag++;
            }

        if ((gFlag == 0) &&(hFlag>0)){
            return false;
        }
        System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa "+hFlag+" "+gFlag);
        return true;
    }

    @Override
    public String getErrorMessage() {
        return "HavingGroupBy rule failed!You have to have group by when using having!";
    }
}
