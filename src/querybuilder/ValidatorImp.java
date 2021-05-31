package querybuilder;

import lombok.NoArgsConstructor;
import querybuilder.rules.HavingGroupBy;
import querybuilder.rules.Rules;
import querybuilder.rules.VarNewQuery;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class ValidatorImp implements Validator {

    private List<Rules> rules = new ArrayList<>();
    private int passed;
    private List<Rules> failedRules = new ArrayList<>();

    @Override
    public boolean validate(String text) {
        //ovde cemo da dodajemo pravila u listu:
        this.rules.add(new VarNewQuery());
        this.rules.add(new HavingGroupBy());
        //TODO napraviti jos pravila

        passed = 0;
        for (Rules r : rules) {
            if (r.checkTheRule(text)) {
                passed++;
            } else {
                //System.out.println(r.getErrorMessage());
                failedRules.add(r);
            }
        }

        if (passed == rules.size()) {
            System.out.println("All rules passed!");
            return true;
        }

        return  false;
    }

    @Override
    public void printFailedRules() {
        for (Rules r : failedRules) {
            System.out.println(r.getErrorMessage());
        }
    }

}
