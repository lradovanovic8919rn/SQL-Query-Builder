package querybuilder;

import lombok.NoArgsConstructor;
import querybuilder.rules.Rules;
import querybuilder.rules.VarNewQuery;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class ValidatorImp implements Validator {

    private List<Rules> rules = new ArrayList<>();
    private int passed;

    @Override
    public void validate(String text) {
        //ovde cemo da dodajemo pravila u listu:
        this.rules.add(new VarNewQuery());
        //TODO napraviti jos pravila

        passed = 0;
        for (Rules r : rules) {
            if (r.checkTheRule(text)) {
                passed++; //koliko pravila je prosao
            } else {
                System.out.println(r.getErrorMessage());
            }
        }
        if (passed == rules.size()) System.out.println("All rules passed!");
    }

}
