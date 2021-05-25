package querybuilder;

public class Validator {

    private String text;

    //var b = new Query("jobs").Select("job_title","prosecnaPlata").Avg("salary","prosecnaPlata").GroupBy("job_title")
    // .Join("jobs").On("jobs.job_id","=","employees.job_id").WhereInQ("employee_id", a)
    public Validator(String text) {
        this.text = text;
        validate(text);
    }

    private void validate(String text) {
        //TODO validacija
        Compiler c = new Compiler(text);
    }

}
