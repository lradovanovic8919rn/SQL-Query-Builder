package querybuilder;

public class CompilerImp implements Compiler{

    private String text;
    private String table;
    private StringBuilder completeQuery;

    /*
    Primer upita:
    var query = new Query("Departments").OrderBy("manager_id").Where("department_id", ">", 50).OrWhere("department_name","like","C%")
    Generisani SQL:
    SELECT * FROM [Departments] WHERE [department_id] > 50 OR [department_name] like 'C%' ORDER BY [manager_id]
    */
    public CompilerImp() {
        this.completeQuery = new StringBuilder();

    }

    @Override
    public void compile(String text) {
        this.text = text;

        String[] parts = text.split("\\.");
        String[] tableName = parts[0].split("\"");
        table = tableName[1];
        completeQuery.append("SELECT * FROM " + table + " ");
        System.out.println("Q: " + completeQuery);
        System.out.println(table);

        String temp = text.substring(text.indexOf(".") + 1);
        String[] newParts = temp.split("\\)\\.");

        for(int i=0; i<newParts.length; i++){
            //System.out.println(newParts[i]);
            compilePart(newParts[i]);
        }
    }
    @Override
    public void compilePart(String part) {
        /*
        select("job_title","prosecnaplata"
        avg("salary","prosecnaplata"
        groupby("job_title"
        join("jobs"
        on("jobs.job_id","=","employees.job_id"
        whereinq("employee_id", a)
         */
        part = part.substring(0, part.length()-1);
        String[] query = part.split("\\(");
        queryBuilder(query[0], query[1]);
        //System.out.println(part);
    }

    //q: select
    //args: "job_title","prosecnaplata"
    @Override
    public void queryBuilder(String q, String args) {
        //System.out.println("Q: " + q);
        //System.out.println("ARGS: " + args);

        switch(q){ //TODO implementirati

            case "select": //moze vise od 1arg
                //args je samo kolona/e
                System.out.println("select: " + q + " " + args);
                completeQuery.replace(7, 7, args);
                for(int i=0; i<completeQuery.length(); i++) {
                    if (completeQuery.charAt(i) == '"') {
                        completeQuery.setCharAt(i, ' ');
                    } else if (completeQuery.charAt(i) == '*') {
                        completeQuery.setCharAt(i, ' ');
                    }
                }
                System.out.println("Q: " + completeQuery);
                break;

            //sortiranje
            case "orderby": //moze vise od 1arg
                //args je samo kolona/e
                System.out.println("orderby: " + q + " " + args);
                break;
            case "orderbydesc": //moze vise od 1arg
                //args je samo kolona/e
                System.out.println("orderbydesc: " + q + " " + args);
                break;

            //filtriranje
            case "where":
                //args je kolona, operator, kriterijum
                System.out.println("where: " + q + " " + args);
                break;
            case "orwhere":
                //args je kolona, operator, kriterijum
                System.out.println("orwhere: " + q + " " + args);
                break;
            case "andwhere":
                //args je kolona, operator, kriterijum
                System.out.println("andwhere: " + q + " " + args);
                break;
            case "wherebetween":
                //args je kolona, int1, int2
                System.out.println("wherebetween: " + q + " " + args);
                break;
            case "wherein":
                //args je kolona
                System.out.println("wherein: " + q + " " + args);
                break;
            case "parametarlist":
                //args je lista parametara p1, p2, p3...
                System.out.println("parametarlist: " + q + " " + args);
                break;

            //spajanje tabela
            case "join":
                //args je tabela a posle toga sigurno dolazi case "on"
                System.out.println("join: " + q + " " + args);
                break;
            case "on":
                //args je kolona1, operator, kolona2
                System.out.println("on: " + q + " " + args);
                break;

            //stringovne operacije
            case "whereendswith":
                //args je kolona, patern
                System.out.println("whereendswith: " + q + " " + args);
                break;
            case "wherestartswith":
                //args je kolona, patern
                System.out.println("wherestartswith: " + q + " " + args);
                break;
            case "wherecontains":
                //args je kolona, patern
                System.out.println("wherecontains: " + q + " " + args);
                break;

            //funkcije agregacije
            //alias je opcioni argument
            case "avg":
                //args je kolona, alias
                System.out.println("avg: " + q + " " + args);
                break;
            case "count":
                //args je kolona, alias
                System.out.println("count: " + q + " " + args);
                break;
            case "min":
                //args je kolona, alias
                System.out.println("min: " + q + " " + args);
                break;
            case "max":
                //args je kolona, alias
                System.out.println("max: " + q + " " + args);
                break;
            case "groupby": //moze vise od 1 arg
                //args je samo kolona/e
                System.out.println("groupby: " + q + " " + args);
                break;
            case "having":
                //args je alias, operator, kriterijum
                System.out.println("having: " + q + " " + args);
                break;
            case "andhaving":
                //args je alias, operator, kriterijum
                System.out.println("andhaving: " + q + " " + args);
                break;
            case "orhaving":
                //args je alias, operator, kriterijum
                System.out.println("orhaving: " + q + " " + args);
                break;

            //podupiti
            case "whereinq":
                //args je kolona, query
                System.out.println("whereinq: " + q + " " + args);
                break;
            case "whereeqq":
                //args je kolona, query
                System.out.println("whereeqq: " + q + " " + args);
                break;
        }
    }



}
