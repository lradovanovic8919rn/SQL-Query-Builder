package querybuilder;

public class Compiler {

    private String text;
    /*
    Primer upita:
    var query = new Query("Departments").OrderBy("manager_id").Where("department_id", ">", 50).OrWhere("department_name","like","C%")
    Generisani SQL:
    SELECT * FROM [Departments] WHERE [department_id] > 50 OR [department_name] like 'C%' ORDER BY [manager_id]
    */
    public Compiler(String text) {
        this.text = text;
        compile(text);
    }

    private void compile(String text) {
        String[] parts = text.split("\\.");
        String[] tableName = parts[0].split("\"");
        String table = tableName[1];
        System.out.println(table);

        String temp = text.substring(text.indexOf(".") + 1);
        String[] newParts = temp.split("\\)\\.");

        for(int i=0; i<newParts.length; i++){
            //System.out.println(newParts[i]);
            compilePart(newParts[i]);
        }
    }

    private void compilePart(String part) {
        /*
        select("job_title","prosecnaplata"
        avg("salary","prosecnaplata"
        groupby("job_title"
        join("jobs"
        on("jobs.job_id","=","employees.job_id"
        whereinq("employee_id", a)
         */
        System.out.println(part);
    }

}
