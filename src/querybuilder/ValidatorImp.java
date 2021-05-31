package querybuilder;

import lombok.NoArgsConstructor;
import querybuilder.rules.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class ValidatorImp implements Validator {

    private final List<Rules> rules = new ArrayList<>();
    private final List<Rules> failedRules = new ArrayList<>();

    @Override
    public boolean validate(String text) {
        //ovde cemo da dodajemo pravila u listu:
        this.rules.add(new VarNewQueryRule());
        this.rules.add(new HavingGroupByRule());
        this.rules.add(new JoinOnRule());
        this.rules.add(new WhereInParamRule());
        this.rules.add(new HavingAmountRule());
        this.rules.add(new WhereAmountRule());
        this.rules.add(new WhereInOrderRule());
        this.rules.add(new JoinOnOrderRule());
        this.rules.add(new JoinArgsRule());
        this.rules.add(new HavingAliasRule());
        this.rules.add(new SelectAggregateRule());
        //TODO napraviti jos pravila

        int passed = 0;
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

        return false;
    }

    @Override
    public void printFailedRules() {
        for (Rules r : failedRules) {
            System.out.println(r.getErrorMessage());
        }
    }

}
