package querybuilder;

public class ValidatorImp implements Validator{

    private String text;

    //var b = new Query("jobs").Select("job_title","prosecnaPlata").Avg("salary","prosecnaPlata").GroupBy("job_title")
    // .Join("jobs").On("jobs.job_id","=","employees.job_id").WhereInQ("employee_id", a)
    public ValidatorImp(String text) {
        this.text = text;
        validate(text);
    }

    public ValidatorImp() {

    }

    @Override
    public void validate(String text) {
        //TODO validacija
      //  CompilerImp c = new CompilerImp(text);
    }

}
