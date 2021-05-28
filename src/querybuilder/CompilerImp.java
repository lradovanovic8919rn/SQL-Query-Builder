package querybuilder;

import java.util.ArrayList;
import java.util.List;

public class CompilerImp implements Compiler{

    private String text;
    private String table;
    private StringBuilder completeQuery;
    private List<String> queryList;
    private String selectName;
    private String andWhere;
    private String orWhere;
    private String whereIn;


    /*
    Primer upita:
    var query = new Query("Departments").OrderBy("manager_id").Where("department_id", ">", 50).OrWhere("department_name","like","C%")
    Generisani SQL:
    SELECT * FROM [Departments] WHERE [department_id] > 50 OR [department_name] like 'C%' ORDER BY [manager_id]
    */
    public CompilerImp() {
        this.completeQuery = new StringBuilder();
        this.queryList = new ArrayList();
    }

    @Override
    public String compile(String text) {

        this.text = text;
        andWhere=" ";
        orWhere=" ";
        String[] parts = text.split("\\.");
        String[] tableName = parts[0].split("\"");
        table = tableName[1];
        completeQuery.append("select * from "+ table+" ");
        System.out.println("Q: " + completeQuery);
        System.out.println("table name: " + table+" ");
        System.out.println("-----------------------");

        String temp = text.substring(text.indexOf(".") + 1);
        String[] newParts = temp.split("\\)\\.");

        for(int i=0; i<newParts.length; i++){
            //System.out.println(newParts[i]);
            compilePart(newParts[i]);
        }
        checkQueryOrder();

        return completeQuery.toString();

        //return "select * from jobs where max_salary>=9001";
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
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2= new StringBuilder();

        String s;

        switch(q){ //TODO implementirati

            case "Select": //moze vise od 1arg
                //args je samo kolona/e
                //System.out.println("select: " + q + " " + args);
                StringBuilder temp = new StringBuilder();
                temp.append(args);
                for(int i=0; i<temp.length(); i++) {
                    if(temp.charAt(i) == '"') {
                        temp.setCharAt(i, ' ');
                    }
                }
                this.selectName = temp.toString();
                //System.out.println(temp);
                //queryList.add("select " + selectName);
                completeQuery.replace(0,completeQuery.length(),"select "+selectName+" from "+table);
                break;

            //sortiranje
            case "OrderBy": //moze vise od 1arg
                //args je samo kolona/e
                System.out.println("orderby: " + q + " " + args);
                break;
            case "OrderByDesc": //moze vise od 1arg
                //args je samo kolona/e
                System.out.println("orderbydesc: " + q + " " + args);
                break;

            //filtriranje
            case "Where":
                //args je kolona, operator, kriterijum
                sb.delete(0,sb.length());
                System.out.println(args);
                sb.append(args);
                for(int i=0; i<sb.length(); i++) {
                    if((sb.charAt(i) == '"')||(sb.charAt(i) == ',')) {
                        sb.setCharAt(i, ' ');
                    }
                }
                System.out.println(" where: " + q + " " + args);
                System.out.println(args);

                if(andWhere!=" "){
                   sb.append(andWhere);
                }
                if(orWhere!=" "){
                    sb.append(orWhere);
                }

                s=" where "+sb.toString();
                queryList.add(s);
                break;
            case "OrWhere":
                sb=new StringBuilder();;
                sb.append(args);
                for(int i=0; i<sb.length(); i++) {
                    if((sb.charAt(i) == '"')||(sb.charAt(i) == ',')) {
                        sb.setCharAt(i, ' ');
                    }
                }
                int flag=0;
                for(String str:queryList) {
                    if ((str.contains(" where "))&&!(str.contains(" between "))&&!(str.contains(" in "))) {
                        flag++;
                        String s2=str+" or "+sb.toString();
                        queryList.remove(str);
                        queryList.add(s2);
                    }

                }
                if (flag==0){
                    sb2.delete(0,sb2.length());
                    sb2.append(orWhere);
                    sb2.append(" or "+sb.toString());
                    orWhere=sb2.toString();

                }

                System.out.println("orwhere: " + q + " " + args);

                break;
            case "AndWhere":
                sb=new StringBuilder();;
                sb.append(args);
                for(int i=0; i<sb.length(); i++) {
                    if((sb.charAt(i) == '"')||(sb.charAt(i) == ',')) {
                        sb.setCharAt(i, ' ');
                    }
                }
                String str2=null;
                int flag2=0;
                for(String str:queryList){
                    if((str.contains(" where "))&&!(str.contains(" between "))&&!(str.contains(" in "))){
                        flag2++;
                        if(str.contains(" or ")){
                            for(int i=0;i<str.length();i++){
                                if ((str.charAt(i) == ' ')&&(str.charAt(i+1) =='o')&&(str.charAt(i+2)=='r')&&(str.charAt(i+3)==' ')){
                                    String s1=str.substring(i);
                                    String s2=str.substring(0,i);
                                    String s3=s1+" and"+sb.toString()+" "+s2;
                                    str2=s3;
                                }
                            }

                        }else{
                            s=" and "+sb.toString();
                            str2=str+s;

                        }
                        queryList.remove(str);
                        queryList.add(str2);
                    }

                }
                if (flag2==0){
                    sb2.delete(0,sb2.length());
                    sb2.append(" and "+sb.toString());
                    sb2.append(andWhere);
                    andWhere=sb2.toString();

                }
                System.out.println("andwhere: " + q + " " + args);
                break;
            case "WhereBetween":
                sb=new StringBuilder();;
                sb.append(args);
                for(int i=0; i<sb.length(); i++) {
                    if((sb.charAt(i) == '"')) {
                        sb.setCharAt(i, ' ');
                    }
                }
                String col=null;
                String st=null;
                String to=null;
                int ind=0;
                int zarez=0;
                for(int i=0;i<(sb.toString()).length();i++){
                    if((sb.toString()).charAt(i)==','){
                        zarez++;
                        if (zarez==1){
                            ind=i+1;
                            col=(sb.toString()).substring(0,i);
                        }
                       if(zarez==2){
                           st=(sb.toString()).substring(ind,i);
                           to=(sb.toString()).substring(i+1);
                       }
                    }
                }

                String sf=" where "+col+" between "+st+" and "+to;
                queryList.add(sf);
                System.out.println("wherebetween: " + q + " " + args+' ' + zarez+' '+sb.toString());
                break;
            case "WhereIn":
                sb=new StringBuilder();;
                sb.append(args);
                for(int i=0; i<sb.length(); i++) {
                    if((sb.charAt(i) == '"')) {
                        sb.setCharAt(i, ' ');
                    }
                }
                whereIn=" where "+sb.toString()+" in ";
                System.out.println("wherein: " + q + " " + args);
                break;
            case "ParametarList":
                sb=new StringBuilder();;
                sb.append(args);
                for(int i=0; i<sb.length(); i++) {
                    if((sb.charAt(i) == '"')) {
                        sb.setCharAt(i, ' ');
                    }
                }
                whereIn=whereIn+"("+sb.toString()+")";
                queryList.add(whereIn);
                System.out.println("parametarlist: " + q + " " + args);
                System.out.println(whereIn);
                break;

            //spajanje tabela
            case "Join":
                //args je tabela a posle toga sigurno dolazi case "on"
                System.out.println("join: " + q + " " + args);
                break;
            case "On":
                //args je kolona1, operator, kolona2
                System.out.println("on: " + q + " " + args);
                break;

            //stringovne operacije
            case "WhereEndsWith":
                //args je kolona, patern
                System.out.println("whereendswith: " + q + " " + args);
                break;
            case "WhereStartsWith":
                //args je kolona, patern
                System.out.println("wherestartswith: " + q + " " + args);
                break;
            case "WhereContains":
                //args je kolona, patern
                System.out.println("wherecontains: " + q + " " + args);
                break;

            //funkcije agregacije
            //alias je opcioni argument
            case "Avg":
                //args je kolona, alias
                System.out.println("avg: " + q + " " + args);
                break;
            case "Count":
                //args je kolona, alias
                System.out.println("count: " + q + " " + args);
                break;
            case "Min":
                //args je kolona, alias
                System.out.println("min: " + q + " " + args);
                break;
            case "Max":
                //args je kolona, alias
                System.out.println("max: " + q + " " + args);
                break;
            case "GroupBy": //moze vise od 1 arg
                //args je samo kolona/e
                System.out.println("groupby: " + q + " " + args);
                break;
            case "Having":
                //args je alias, operator, kriterijum
                System.out.println("having: " + q + " " + args);
                break;
            case "AndHaving":
                //args je alias, operator, kriterijum
                System.out.println("andhaving: " + q + " " + args);
                break;
            case "OrHaving":
                //args je alias, operator, kriterijum
                System.out.println("orhaving: " + q + " " + args);
                break;

            //podupiti
            case "WhereInQ":
                //args je kolona, query
                System.out.println("whereinq: " + q + " " + args);
                break;
            case "WhereEqQ":
                //args je kolona, query
                System.out.println("whereeqq: " + q + " " + args);
                break;

          /*  default:
                //znaci da ima samo Query
                completeQuery.append("* from " + table);
                System.out.println("default: " + completeQuery);
                break;*/
        }

    }

    //queryName: select
    //counter: 1
    private void checkQueryOrder() {
        String test = "select job_title, avg(salary) from jobs join employees using (job_id) where employee_id in (select employee_id from job_history) group by job_title";
        //TODO proveriti raspored u listi
        /*for(int i=0; i<queryList.size(); i++){
            if(!queryList.get(i).contains("select")){
                completeQuery.append(" * ");
            } else {
                completeQuery.append(selectName);
            }
        }*/
        for(String s:queryList){
            if( s.contains(" where ")) {
                completeQuery.append(s);
            }
        }

        System.out.println("SQL: " + completeQuery);
    }

    @Override
    public void reset() {
        text = "";
        table = "";
        completeQuery = new StringBuilder();
        queryList = new ArrayList<>();
        selectName = "";
    }

}
