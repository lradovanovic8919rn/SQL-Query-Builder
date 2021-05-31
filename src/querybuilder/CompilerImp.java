package querybuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompilerImp implements Compiler {

    private String text;
    private String table;
    private StringBuilder completeQuery;
    private List<String> queryList;
    private final List<String> tempList;
    private final List<String> aliasList;
    private String selectName;
    private String andWhere;
    private String orWhere;
    private String whereIn;
    private String joinOn;
    private String avgAlias;
    private String countAlias;
    private String minAlias;
    private String maxAlias;
    private String tempAvg;
    private String tempCount;
    private String tempMin;
    private String tempMax;
    private String andHavingHelp;
    private String orHavingHelp;
    private String varName;
    private final Map<String, String> varMap;

    /*
    Primer upita:
    var query = new Query("Departments").OrderBy("manager_id").Where("department_id", ">", 50).OrWhere("department_name","like","C%")
    Generisani SQL:
    SELECT * FROM [Departments] WHERE [department_id] > 50 OR [department_name] like 'C%' ORDER BY [manager_id]
    */
    public CompilerImp() {
        this.completeQuery = new StringBuilder();
        this.queryList = new ArrayList<>();
        this.aliasList = new ArrayList<>();
        this.tempList = new ArrayList<>();
        this.varMap = new HashMap<>();
    }

    @Override
    public String compile(String text) {
        this.text = text;
        andWhere = " ";
        orWhere = " ";
        tempAvg = " ";
        tempCount = " ";
        tempMin = " ";
        tempMax = " ";
        andHavingHelp = " ";
        orHavingHelp = " ";
        String[] var = text.split(" ");
        varName = var[1];
        String[] parts = text.split("\\.");
        String[] tableName = parts[0].split("\"");
        table = tableName[1];
        completeQuery.append("select * from ").append(table).append(" ");
        System.out.println("Q: " + completeQuery);
        System.out.println("table name: " + table + " ");
        System.out.println("-----------------------");

        String temp = text.substring(text.indexOf(".") + 1);
        String[] newParts = temp.split("\\)\\.");

        for (String newPart : newParts) {
            //System.out.println(newParts[i]);
            compilePart(newPart);
        }

        checkQueryOrder();
        varMap.put(varName, completeQuery.toString());
        //System.out.println(varMap);
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
        part = part.substring(0, part.length() - 1);
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
        StringBuilder sb2 = new StringBuilder();
        String s;
        String avg;
        String count;
        String min;
        String max;
        switch (q) {
            case "Select" -> { //moze vise od 1arg
                //args je samo kolona/e
                //System.out.println("select: " + q + " " + args);
                StringBuilder temp = new StringBuilder();
                temp.append(args);
                for (int i = 0; i < temp.length(); i++) {
                    if (temp.charAt(i) == '"') {
                        temp.setCharAt(i, ' ');
                    }
                }
                this.selectName = temp.toString();
                //System.out.println(temp);
                //queryList.add("select " + selectName);
                int tempHelp = 0;
                String ths = null;
                String ahs = null;
                for (String sh : tempList) {
                    if (!sh.equals(" ")) {
                        tempHelp = 1;
                        ths = sh;
                    }
                }
                if (tempHelp == 0) {
                    completeQuery.replace(0, completeQuery.length(), "select " + selectName + " from " + table);
                }
                if (tempHelp == 1) {
                    for (String shs : aliasList) {
                        if (ths.contains(shs)) {
                            ahs = shs;
                        }
                    }
                    if (ahs != null) {
                        if (selectName.contains(ahs)) {
                            selectName = selectName.replace(ahs, ths);
                            completeQuery.replace(0, completeQuery.length(), "select " + selectName + " from " + table);
                        } else {
                            completeQuery.replace(0, completeQuery.length(), "select " + selectName + "," + ths + " from " + table);
                        }
                    }
                }
                System.out.println(completeQuery);
                System.out.println("aaaaaaaaaaaaa" + ahs);
                System.out.println("tempppppppppppp" + ths);
            }

            //sortiranje
            case "OrderBy" -> { //moze vise od 1arg
                sb = new StringBuilder();
                System.out.println(args);
                sb.append(args);
                for (int i = 0; i < sb.length(); i++) {
                    if ((sb.charAt(i) == '"')) {
                        sb.setCharAt(i, ' ');
                    }
                }
                String orderstr = " order by " + sb + " asc ";
                queryList.add(orderstr);
                System.out.println("orderby: " + q + " " + args);
            }
            case "OrderByDesc" -> { //moze vise od 1arg
                sb = new StringBuilder();
                System.out.println(args);
                sb.append(args);
                for (int i = 0; i < sb.length(); i++) {
                    if ((sb.charAt(i) == '"')) {
                        sb.setCharAt(i, ' ');
                    }
                }
                String orderdsc = " order by " + sb + " desc ";
                queryList.add(orderdsc);
                System.out.println("orderbydesc: " + q + " " + args);
            }

            //filtriranje
            case "Where" -> {
                //args je kolona, operator, kriterijum
                sb.delete(0, sb.length());
                System.out.println(args);
                sb.append(args);
                for (int i = 0; i < sb.length(); i++) {
                    if ((sb.charAt(i) == '"') || (sb.charAt(i) == ',')) {
                        sb.setCharAt(i, ' ');
                    }
                }
                System.out.println(" where: " + q + " " + args);
                System.out.println(args);
                if (!andWhere.equals(" ")) {
                    sb.append(andWhere);
                }
                if (!orWhere.equals(" ")) {
                    sb.append(orWhere);
                }
                s = " where " + sb;
                queryList.add(s);
            }
            case "OrWhere" -> {
                sb = new StringBuilder();
                sb.append(args);
                for (int i = 0; i < sb.length(); i++) {
                    if ((sb.charAt(i) == '"') || (sb.charAt(i) == ',')) {
                        sb.setCharAt(i, ' ');
                    }
                }
                int flag = 0;
                for (String str : queryList) {
                    if ((str.contains(" where ")) && !(str.contains(" between ")) && !(str.contains(" in ") && !(str.contains(" like ")))) {
                        flag++;
                        String s2 = str + " or " + sb;
                        queryList.remove(str);
                        queryList.add(s2);
                    }
                }
                if (flag == 0) {
                    sb2.delete(0, sb2.length());
                    sb2.append(orWhere).append(" or ").append(sb);
                    orWhere = sb2.toString();

                }
                System.out.println("orwhere: " + q + " " + args);
            }
            case "AndWhere" -> {
                sb = new StringBuilder();
                sb.append(args);
                for (int i = 0; i < sb.length(); i++) {
                    if ((sb.charAt(i) == '"') || (sb.charAt(i) == ',')) {
                        sb.setCharAt(i, ' ');
                    }
                }
                String str2 = null;
                int flag2 = 0;
                for (String str : queryList) {
                    if ((str.contains(" where ")) && !(str.contains(" between ")) && !(str.contains(" in "))) {
                        flag2++;
                        if (str.contains(" or ")) {
                            for (int i = 0; i < str.length(); i++) {
                                if ((str.charAt(i) == ' ') && (str.charAt(i + 1) == 'o') && (str.charAt(i + 2) == 'r') && (str.charAt(i + 3) == ' ')) {
                                    String s1 = str.substring(i);
                                    String s2 = str.substring(0, i);
                                    str2 = s2 + " and" + sb + " " + s1;
                                }
                            }

                        } else {
                            s = " and " + sb;
                            str2 = str + s;

                        }
                        queryList.remove(str);
                        queryList.add(str2);
                    }

                }
                if (flag2 == 0) {
                    sb2.delete(0, sb2.length());
                    sb2.append(" and ").append(sb).append(andWhere);
                    andWhere = sb2.toString();

                }
                System.out.println("andwhere: " + q + " " + args);
            }
            case "WhereBetween" -> {
                sb = new StringBuilder();
                sb.append(args);
                for (int i = 0; i < sb.length(); i++) {
                    if ((sb.charAt(i) == '"')) {
                        sb.setCharAt(i, ' ');
                    }
                }
                String col = null;
                String st = null;
                String to = null;
                int ind = 0;
                int zarez = 0;
                for (int i = 0; i < (sb.toString()).length(); i++) {
                    if ((sb.toString()).charAt(i) == ',') {
                        zarez++;
                        if (zarez == 1) {
                            ind = i + 1;
                            col = (sb).substring(0, i);
                        }
                        if (zarez == 2) {
                            st = (sb).substring(ind, i);
                            to = (sb).substring(i + 1);
                        }
                    }
                }
                String sf = " where " + col + " between " + st + " and " + to;
                queryList.add(sf);
                System.out.println("wherebetween: " + q + " " + args + ' ' + zarez + ' ' + sb);
            }
            case "WhereIn" -> {
                sb = new StringBuilder();
                sb.append(args);
                for (int i = 0; i < sb.length(); i++) {
                    if ((sb.charAt(i) == '"')) {
                        sb.setCharAt(i, ' ');
                    }
                }
                whereIn = " where " + sb + " in ";
                System.out.println("wherein: " + q + " " + args);
            }
            case "ParametarList" -> {
                sb = new StringBuilder();
                sb.append(args);
                for (int i = 0; i < sb.length(); i++) {
                    if ((sb.charAt(i) == '"')) {
                        sb.setCharAt(i, ' ');
                    }
                }
                whereIn = whereIn + "(" + sb + ")";
                queryList.add(whereIn);
                System.out.println("parametarlist: " + q + " " + args);
                System.out.println(whereIn);
            }

            //spajanje tabela
            case "Join" -> {
                sb = new StringBuilder();
                System.out.println(args);
                sb.append(args);
                for (int i = 0; i < sb.length(); i++) {
                    if ((sb.charAt(i) == '"')) {
                        sb.setCharAt(i, ' ');
                    }
                }
                joinOn = " join " + sb;
                System.out.println("join: " + q + " " + args);
            }
            case "On" -> {
                sb = new StringBuilder();
                System.out.println(args);
                sb.append(args);
                for (int i = 0; i < sb.length(); i++) {
                    if ((sb.charAt(i) == '"') || (sb.charAt(i) == ',')) {
                        sb.setCharAt(i, ' ');
                    }
                }
                joinOn = joinOn + " on (" + sb + ")";
                queryList.add(joinOn);
                System.out.println("on: " + q + " " + args);
            }

            //stringovne operacije
            case "WhereEndsWith" -> {
                //args je kolona, patern
                System.out.println("whereendswith: " + q + " " + args);
                sb = new StringBuilder();
                sb.append(args);
                for (int i = 0; i < sb.length(); i++) {
                    if (sb.charAt(i) == '"') {
                        sb.setCharAt(i, ' ');
                    }
                }
                //ime kolone , '%nesto' -> where ime_kolone like '%nesto'
                String[] wewSplit = sb.toString().split(",");
                String whereEndsWith = " where " + wewSplit[0] + " like " + wewSplit[1];
                System.out.println(whereEndsWith);
                queryList.add(whereEndsWith);
            }
            case "WhereStartsWith" -> {
                //args je kolona, patern
                System.out.println("wherestartswith: " + q + " " + args);
                sb = new StringBuilder();
                sb.append(args);
                for (int i = 0; i < sb.length(); i++) {
                    if (sb.charAt(i) == '"') {
                        sb.setCharAt(i, ' ');
                    }
                }
                //ime kolone , 'nesto%' -> where ime_kolone like 'nesto%'
                String[] wswSplit = sb.toString().split(",");
                String whereStartsWith = " where " + wswSplit[0] + " like " + wswSplit[1];
                System.out.println(whereStartsWith);
                queryList.add(whereStartsWith);
            }
            case "WhereContains" -> {
                //args je kolona, patern
                System.out.println("wherecontains: " + q + " " + args);
                sb = new StringBuilder();
                sb.append(args);
                for (int i = 0; i < sb.length(); i++) {
                    if (sb.charAt(i) == '"') {
                        sb.setCharAt(i, ' ');
                    }
                }
                //ime kolone , '%nesto%' -> where ime_kolone like '%nesto%'
                String[] wcSplit = sb.toString().split(",");
                String whereContains = " where " + wcSplit[0] + " like " + wcSplit[1];
                System.out.println(whereContains);
                queryList.add(whereContains);
            }

            //funkcije agregacije
            //alias je opcioni argument
            case "Avg" -> {
                //args je kolona, alias
                System.out.println("avg: " + q + " " + args);
                sb = new StringBuilder();
                sb.append(args);
                for (int i = 0; i < sb.length(); i++) {
                    if (sb.charAt(i) == '"') {
                        sb.setCharAt(i, ' ');
                    }
                }
                //ime kolone , alias -> avg ime_kolone
                if (sb.toString().contains(",")) {
                    String[] avgSplit = sb.toString().split(",");
                    avg = " avg(" + avgSplit[0] + ")";
                    avgAlias = avgSplit[1];

                    if ((text.contains(".Select(")) && !(completeQuery.toString().contains("select * "))) {
                        if (completeQuery.toString().contains(avgAlias.trim())) {
                            if (!completeQuery.toString().contains("as \"" + avgAlias + "\"")) {

                                String cc = completeQuery.toString().replace(avgAlias, avg + " as \"" + avgAlias + "\" ");
                                completeQuery = new StringBuilder();
                                completeQuery.append(cc);
                            }

                        } else if (!completeQuery.toString().contains(avgAlias.trim())) {
                            for (int i = 0; i < completeQuery.toString().length(); i++) {
                                if ((completeQuery.toString().charAt(i) == ' ') && (completeQuery.toString().charAt(i + 1) == 'f') && (completeQuery.toString().charAt(i + 2) == 'r') && (completeQuery.toString().charAt(i + 3) == 'o') && (completeQuery.toString().charAt(i + 4) == 'm') && (completeQuery.toString().charAt(i + 5) == ' ')) {
                                    String ss1 = completeQuery.substring(0, i + 1);
                                    String ss2 = completeQuery.substring(i + 1, completeQuery.toString().length());
                                    completeQuery = new StringBuilder();
                                    completeQuery.append(ss1).append(",").append(avg).append(" as \"").append(avgAlias).append("\" ").append(ss2);
                                    //completeQuery.append("," + avg + " as \"" + avgAlias + "\" ");
                                    //completeQuery.append(ss2);
                                    break;
                                }
                            }
                        }
                    }

                    if ((text.contains(".Select(")) && (completeQuery.toString().contains("select * "))) {
                        if (tempAvg.equals(" ")) {
                            tempAvg = avg + " as \"" + avgAlias + " \" ";

                        }
                    }
                    if ((!text.contains(".Select(")) && (completeQuery.toString().contains("select * "))) {
                        String cc = completeQuery.toString().replace("*", avg + " as \"" + avgAlias + " \"");
                        completeQuery = new StringBuilder();
                        completeQuery.append(cc);
                    }

                    System.out.println("avg: " + avg);
                    System.out.println(completeQuery.toString());
                    //queryList.add(avg);
                } else { //znaci da nema alias
                    avg = " avg(" + sb + ")";
                    if (completeQuery.toString().contains("select * ")) {
                        String ss = completeQuery.toString().replace("select *", "select " + avg + " ");
                        completeQuery = new StringBuilder();
                        completeQuery.append(ss);
                        System.out.println(ss);
                        System.out.println(completeQuery.toString());
                    } else {
                        for (int i = 0; i < completeQuery.toString().length(); i++) {
                            if ((completeQuery.toString().charAt(i) == ' ') && (completeQuery.toString().charAt(i + 1) == 'f') && (completeQuery.toString().charAt(i + 2) == 'r') && (completeQuery.toString().charAt(i + 3) == 'o') && (completeQuery.toString().charAt(i + 4) == 'm') && (completeQuery.toString().charAt(i + 5) == ' ')) {
                                String ss1 = completeQuery.substring(0, i + 1);
                                String ss2 = completeQuery.substring(i + 1, completeQuery.toString().length());
                                completeQuery = new StringBuilder();
                                completeQuery.append(ss1).append(" , ").append(avg).append(" ").append(ss2);
                                //completeQuery.append(" , " + avg + " ");
                                //completeQuery.append(ss2);
                                break;
                            }
                        }
                    }
                    if ((text.contains(".Select(")) && (completeQuery.toString().contains("select * "))) {
                        if (tempAvg.equals(" ")) {
                            tempAvg = avg + " ";
                        }
                    }
                }
                aliasList.add(avgAlias);
                tempList.add(tempAvg);
            }
            case "Count" -> {
                //args je kolona, alias
                System.out.println("count: " + q + " " + args);
                sb = new StringBuilder();
                sb.append(args);
                for (int i = 0; i < sb.length(); i++) {
                    if (sb.charAt(i) == '"') {
                        sb.setCharAt(i, ' ');
                    }
                }
                if (sb.toString().contains(",")) {
                    String[] countSplit = sb.toString().split(",");
                    count = " count(" + countSplit[0] + ")";
                    countAlias = countSplit[1];

                    if ((text.contains(".Select(")) && !(completeQuery.toString().contains("select * "))) {
                        if (completeQuery.toString().contains(countAlias.trim())) {
                            if (!completeQuery.toString().contains("as \"" + countAlias + "\"")) {

                                String cc = completeQuery.toString().replace(countAlias, count + " as \"" + countAlias + "\" ");
                                completeQuery = new StringBuilder();
                                completeQuery.append(cc);
                            }

                        } else if (!completeQuery.toString().contains(countAlias.trim())) {
                            for (int i = 0; i < completeQuery.toString().length(); i++) {
                                if ((completeQuery.toString().charAt(i) == ' ') && (completeQuery.toString().charAt(i + 1) == 'f') && (completeQuery.toString().charAt(i + 2) == 'r') && (completeQuery.toString().charAt(i + 3) == 'o') && (completeQuery.toString().charAt(i + 4) == 'm') && (completeQuery.toString().charAt(i + 5) == ' ')) {
                                    String ss1 = completeQuery.substring(0, i + 1);
                                    String ss2 = completeQuery.substring(i + 1, completeQuery.toString().length());
                                    completeQuery = new StringBuilder();
                                    completeQuery.append(ss1).append(",").append(count).append(" as \"").append(countAlias).append("\" ").append(ss2);
                                    //completeQuery.append("," + count + " as \"" + countAlias + "\" ");
                                    //completeQuery.append(ss2);
                                    break;
                                }
                            }
                        }
                    }

                    if ((text.contains(".Select(")) && (completeQuery.toString().contains("select * "))) {
                        if (tempCount.equals(" ")) {
                            tempCount = count + " as \"" + countAlias + " \" ";

                        }
                    }
                    if ((!text.contains(".Select(")) && (completeQuery.toString().contains("select * "))) {
                        String cc = completeQuery.toString().replace("*", count + " as \"" + countAlias + " \"");
                        completeQuery = new StringBuilder();
                        completeQuery.append(cc);
                    }
                    System.out.println("count: " + count);
                } else { //znaci da nema alias
                    count = " count(" + sb + ")";
                    if (completeQuery.toString().contains("select * ")) {
                        String ss = completeQuery.toString().replace("select *", "select " + count + " ");
                        completeQuery = new StringBuilder();
                        completeQuery.append(ss);

                        System.out.println(ss);

                        System.out.println(completeQuery.toString());
                    } else {
                        for (int i = 0; i < completeQuery.toString().length(); i++) {
                            if ((completeQuery.toString().charAt(i) == ' ') && (completeQuery.toString().charAt(i + 1) == 'f') && (completeQuery.toString().charAt(i + 2) == 'r') && (completeQuery.toString().charAt(i + 3) == 'o') && (completeQuery.toString().charAt(i + 4) == 'm') && (completeQuery.toString().charAt(i + 5) == ' ')) {
                                String ss1 = completeQuery.substring(0, i + 1);
                                String ss2 = completeQuery.substring(i + 1, completeQuery.toString().length());
                                completeQuery = new StringBuilder();
                                completeQuery.append(ss1).append(" , ").append(count).append(" ").append(ss2);
                                //completeQuery.append(" , " + count + " ");
                                //completeQuery.append(ss2);
                                break;
                            }
                        }
                    }
                    if ((text.contains(".Select(")) && (completeQuery.toString().contains("select * "))) {
                        if (tempCount.equals(" ")) {
                            tempCount = count + " ";
                        }
                    }
                }
                aliasList.add(countAlias);
                tempList.add(tempCount);
            }
            case "Min" -> {
                //args je kolona, alias
                System.out.println("min: " + q + " " + args);
                sb = new StringBuilder();
                sb.append(args);
                for (int i = 0; i < sb.length(); i++) {
                    if (sb.charAt(i) == '"') {
                        sb.setCharAt(i, ' ');
                    }
                }
                if (sb.toString().contains(",")) {
                    String[] minSplit = sb.toString().split(",");
                    min = " min(" + minSplit[0] + ")";
                    minAlias = minSplit[1];

                    if ((text.contains(".Select(")) && (!completeQuery.toString().contains("select * "))) {
                        if (completeQuery.toString().contains(minAlias)) {
                            if (!completeQuery.toString().contains("as \"" + minAlias + "\"")) {
                                String cc;
                                String ssss = minAlias;
                                System.out.println("Alijaaaaaaaaaaaaaaaaaaaas" + ssss);
                                System.out.println("Alijaaaaaaaaaaaaaaaaaaaas " + completeQuery);
                                cc = completeQuery.toString().replace(minAlias, min + " as \"" + ssss + "\" ");
                                System.out.println("Alijaaaaaaaaaaaaaaaaaaaas " + ssss);
                                //System.out.println("Alijaaaaaaaaaaaaaaaaaaaas "+completeQuery);
                                System.out.println("ccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccc" + cc);

                                completeQuery = new StringBuilder();
                                completeQuery.append(cc);
                                System.out.println("Alijaaaaaaaaaaaaaaaaaaaas " + completeQuery);
                            }

                        } else if (!completeQuery.toString().contains(minAlias)) {
                            for (int i = 0; i < completeQuery.toString().length(); i++) {
                                if ((completeQuery.toString().charAt(i) == ' ') && (completeQuery.toString().charAt(i + 1) == 'f') && (completeQuery.toString().charAt(i + 2) == 'r') && (completeQuery.toString().charAt(i + 3) == 'o') && (completeQuery.toString().charAt(i + 4) == 'm') && (completeQuery.toString().charAt(i + 5) == ' ')) {
                                    String ss1 = completeQuery.substring(0, i + 1);
                                    String ss2 = completeQuery.substring(i + 1, completeQuery.toString().length());
                                    completeQuery = new StringBuilder();
                                    completeQuery.append(ss1).append(",").append(min).append(" as \"").append(minAlias).append("\" ").append(ss2);
                                    //completeQuery.append("," + min + " as \"" + minAlias + "\" ");
                                    //completeQuery.append(ss2);
                                    break;
                                }
                            }
                        }
                    }

                    if ((text.contains(".Select(")) && (completeQuery.toString().contains("select * "))) {
                        if (tempMin.equals(" ")) {
                            tempMin = min + " as \"" + minAlias + " \" ";
                        }
                    }
                    if ((!text.contains(".Select(")) && (completeQuery.toString().contains("select * "))) {
                        String cc = completeQuery.toString().replace("*", min + " as \"" + minAlias + " \"");
                        completeQuery = new StringBuilder();
                        completeQuery.append(cc);
                    }

                    System.out.println("min: " + min);
                } else { //znaci da nema alias
                    min = " min(" + sb + ")";
                    if (completeQuery.toString().contains("select * ")) {
                        String ss = completeQuery.toString().replace("select *", "select " + min + " ");
                        completeQuery = new StringBuilder();
                        completeQuery.append(ss);

                        System.out.println(ss);
                        System.out.println(completeQuery.toString());
                    } else {
                        for (int i = 0; i < completeQuery.toString().length(); i++) {
                            if ((completeQuery.toString().charAt(i) == ' ') && (completeQuery.toString().charAt(i + 1) == 'f') && (completeQuery.toString().charAt(i + 2) == 'r') && (completeQuery.toString().charAt(i + 3) == 'o') && (completeQuery.toString().charAt(i + 4) == 'm') && (completeQuery.toString().charAt(i + 5) == ' ')) {
                                String ss1 = completeQuery.substring(0, i + 1);
                                String ss2 = completeQuery.substring(i + 1, completeQuery.toString().length());
                                completeQuery = new StringBuilder();
                                completeQuery.append(ss1).append(" , ").append(min).append(" ").append(ss2);
                                //completeQuery.append(" , " + min + " ");
                                //completeQuery.append(ss2);
                                break;
                            }
                        }
                    }
                    if ((text.contains(".Select(")) && (completeQuery.toString().contains("select * "))) {
                        if (tempMin.equals(" ")) {
                            tempMin = min + " ";
                        }
                    }
                }
                tempList.add(tempMin);
                aliasList.add(minAlias);
            }
            case "Max" -> {
                //args je kolona, alias
                System.out.println("max: " + q + " " + args);
                sb = new StringBuilder();
                sb.append(args);
                for (int i = 0; i < sb.length(); i++) {
                    if (sb.charAt(i) == '"') {
                        sb.setCharAt(i, ' ');
                    }
                }
                if (sb.toString().contains(",")) {
                    String[] maxSplit = sb.toString().split(",");
                    max = " max(" + maxSplit[0] + ")";
                    maxAlias = maxSplit[1];
                    System.out.println("max: " + max);
                    if ((text.contains(".Select(")) && (!completeQuery.toString().contains("select * "))) {
                        if (completeQuery.toString().contains(maxAlias)) {
                            if (!completeQuery.toString().contains("as \"" + maxAlias + "\"")) {
                                String cc;
                                String ssss = maxAlias;
                                System.out.println("Alijaaaaaaaaaaaaaaaaaaaas" + ssss);
                                System.out.println("Alijaaaaaaaaaaaaaaaaaaaas " + completeQuery);
                                cc = completeQuery.toString().replace(maxAlias, max + " as \"" + ssss + "\" ");
                                System.out.println("Alijaaaaaaaaaaaaaaaaaaaas " + ssss);
                                //System.out.println("Alijaaaaaaaaaaaaaaaaaaaas "+completeQuery);
                                System.out.println("ccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccc" + cc);

                                completeQuery = new StringBuilder();
                                completeQuery.append(cc);
                                System.out.println("Alijaaaaaaaaaaaaaaaaaaaas " + completeQuery);
                            }

                        } else if (!completeQuery.toString().contains(maxAlias)) {
                            for (int i = 0; i < completeQuery.toString().length(); i++) {
                                if ((completeQuery.toString().charAt(i) == ' ') && (completeQuery.toString().charAt(i + 1) == 'f') && (completeQuery.toString().charAt(i + 2) == 'r') && (completeQuery.toString().charAt(i + 3) == 'o') && (completeQuery.toString().charAt(i + 4) == 'm') && (completeQuery.toString().charAt(i + 5) == ' ')) {
                                    String ss1 = completeQuery.substring(0, i + 1);
                                    String ss2 = completeQuery.substring(i + 1, completeQuery.toString().length());
                                    completeQuery = new StringBuilder();
                                    completeQuery.append(ss1).append(",").append(max).append(" as \"").append(maxAlias).append("\" ").append(ss2);
                                    //completeQuery.append("," + max + " as \"" + maxAlias + "\" ");
                                    //completeQuery.append(ss2);
                                    break;
                                }
                            }
                        }
                    }

                    if ((text.contains(".Select(")) && (completeQuery.toString().contains("select * "))) {
                        if (tempMax.equals(" ")) {
                            tempMax = max + " as \"" + maxAlias + " \" ";
                        }
                    }
                    if ((!text.contains(".Select(")) && (completeQuery.toString().contains("select * "))) {
                        String cc = completeQuery.toString().replace("*", max + " as \"" + maxAlias + " \"");
                        completeQuery = new StringBuilder();
                        completeQuery.append(cc);
                    }
                } else { //znaci da nema alias
                    max = " max(" + sb + ")";
                    if (completeQuery.toString().contains("select * ")) {
                        String ss = completeQuery.toString().replace("select *", "select " + max + " ");
                        completeQuery = new StringBuilder();
                        completeQuery.append(ss);

                        System.out.println(ss);
                        System.out.println(completeQuery.toString());
                    } else {
                        for (int i = 0; i < completeQuery.toString().length(); i++) {
                            if ((completeQuery.toString().charAt(i) == ' ') && (completeQuery.toString().charAt(i + 1) == 'f') && (completeQuery.toString().charAt(i + 2) == 'r') && (completeQuery.toString().charAt(i + 3) == 'o') && (completeQuery.toString().charAt(i + 4) == 'm') && (completeQuery.toString().charAt(i + 5) == ' ')) {
                                String ss1 = completeQuery.substring(0, i + 1);
                                String ss2 = completeQuery.substring(i + 1, completeQuery.toString().length());
                                completeQuery = new StringBuilder();
                                completeQuery.append(ss1).append(" , ").append(max).append(" ").append(ss2);
                                //completeQuery.append(" , " + max + " ");
                                //completeQuery.append(ss2);
                                break;
                            }
                        }
                    }
                    if ((text.contains(".Select(")) && (completeQuery.toString().contains("select * "))) {
                        if (tempMax.equals(" ")) {
                            tempMax = max + " ";
                        }
                    }
                }
                aliasList.add(maxAlias);
                tempList.add(tempMax);
            }
            case "GroupBy" -> { //moze vise od 1 arg
                //args je samo kolona/e
                System.out.println("C" + q + " " + args);
                sb = new StringBuilder();
                sb.append(args);
                for (int i = 0; i < sb.length(); i++) {
                    if (sb.charAt(i) == '"') {
                        sb.setCharAt(i, ' ');
                    }
                }
                for (String strtr : aliasList) {
                    if ((sb.toString().contains(strtr)) && (!strtr.equals(" "))) {
                        String novi = sb.toString().replace(strtr, " \" " + strtr + " \" ");
                        sb = new StringBuilder();
                        sb.append(novi);
                    }
                }
                String groupBy = " group by " + sb + " ";
                queryList.add(groupBy);
            }
            case "Having" -> {
                //args je alias, operator, kriterijum
                System.out.println("having: " + q + " " + args);
                sb = new StringBuilder();
                sb.append(args);
                for (int i = 0; i < sb.length(); i++) {
                    if ((sb.charAt(i) == '"') || (sb.charAt(i) == ',')) {
                        sb.setCharAt(i, ' ');
                    }
                }
                String hv = " having " + sb + " ";
                if (!andHavingHelp.equals(" ")) {
                    hv = hv + andHavingHelp;
                    System.out.println("1");
                }
                if (!orHavingHelp.equals(" ")) {
                    hv = hv + orHavingHelp;
                    System.out.println("222222222222" + hv);

                }
                queryList.add(hv);
            }
            case "AndHaving" -> {
                //args je alias, operator, kriterijum
                System.out.println("andhaving: " + q + " " + args);
                sb = new StringBuilder();
                sb.append(args);
                for (int i = 0; i < sb.length(); i++) {
                    if ((sb.charAt(i) == '"') || (sb.charAt(i) == ',')) {
                        sb.setCharAt(i, ' ');
                    }
                }
                int flagah = 0;
                for (String strrr : queryList) {
                    if (strrr.contains(" having ")) {
                        flagah++;
                        String strrr2 = "";
                        if (strrr.contains(" or ")) {
                            for (int i = 0; i < strrr.length(); i++) {
                                if ((strrr.charAt(i) == ' ') && (strrr.charAt(i + 1) == 'o') && (strrr.charAt(i + 2) == 'r') && (strrr.charAt(i + 3) == ' ')) {
                                    String s2 = strrr.substring(i);
                                    String s1 = strrr.substring(0, i);
                                    strrr2 = s1 + " and" + sb + " " + s2;
                                    System.out.println("ceo " + strrr2);
                                    System.out.println("prvi " + s1);
                                    System.out.println("drugi" + s2);
                                }
                            }

                        } else {
                            s = " and " + sb;
                            strrr2 = strrr + s;
                        }
                        queryList.remove(strrr);
                        queryList.add(strrr2);
                    }
                }
                if (flagah == 0) {
                    andHavingHelp = " and " + sb + " ";
                    System.out.println("saddasdasdsadsadaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" + andHavingHelp);
                }
            }
            case "OrHaving" -> {
                //args je alias, operator, kriterijum
                System.out.println("orhaving: " + q + " " + args);
                sb = new StringBuilder();
                sb.append(args);
                for (int i = 0; i < sb.length(); i++) {
                    if ((sb.charAt(i) == '"') || (sb.charAt(i) == ',')) {
                        sb.setCharAt(i, ' ');
                    }
                }
                int flagoh = 0;
                for (String str : queryList) {
                    if (str.contains(" having ")) {
                        flagoh++;
                        String s2 = str + " or " + sb;
                        System.out.println("Ovaj se ubacuje u novi: " + s2);
                        queryList.remove(str);
                        queryList.add(s2);
                    }
                }
                if (flagoh == 0) {
                    orHavingHelp = " or " + sb;
                    System.out.println(sb);
                    System.out.println("saddasdasdsadsadaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" + orHavingHelp);
                }
            }

            //podupiti
            case "WhereInQ" -> {
                //args je kolona, query
                System.out.println("whereinq: " + q + " " + args);
                sb = new StringBuilder();
                sb.append(args);
                for (int i = 0; i < sb.length(); i++) {
                    if ((sb.charAt(i) == '"')) {
                        sb.setCharAt(i, ' ');
                    }
                }
                //ime kolone , a -> where ime_kolone in a
                String[] wiqSplit = sb.toString().split(",");
                //wiqSplit[1] -> nadji u mapi
                String whereInQ = " where " + wiqSplit[0] + " in (" + varMap.get(wiqSplit[1].trim()) + ")";
                System.out.println(whereInQ);
                queryList.add(whereInQ);
            }
            case "WhereEqQ" -> {
                //args je kolona, query
                System.out.println("whereeqq: " + q + " " + args);
                sb = new StringBuilder();
                sb.append(args);
                for (int i = 0; i < sb.length(); i++) {
                    if (sb.charAt(i) == '"') {
                        sb.setCharAt(i, ' ');
                    }
                }
                //ime kolone , a -> where ime_kolone = a
                String[] weqSplit = sb.toString().split(",");
                String whereEqQ = " where " + weqSplit[0] + " = (" + varMap.get(weqSplit[1].trim()) + ") ";
                System.out.println(whereEqQ);
                queryList.add(whereEqQ);
            }
            /*
            default:
                //znaci da ima samo Query
                completeQuery.append("* from " + table);
                System.out.println("default: " + completeQuery);
                break;
                */
        }
    }

    //queryName: select
    //counter: 1
    private void checkQueryOrder() {
        //String test = "select job_title, avg(salary) from jobs join employees using (job_id) where employee_id in (select employee_id from job_history) group by job_title";
        /*for(int i=0; i<queryList.size(); i++){
            if(!queryList.get(i).contains("select")){
                completeQuery.append(" * ");
            } else {
                completeQuery.append(selectName);
            }
        }*/
        for (String s : queryList) {
            if (s.contains(" join ")) {
                completeQuery.append(s);
            }
        }
        for (String s : queryList) {
            if (s.contains(" where ")) {
                completeQuery.append(s);
            }
        }
        for (String s : queryList) {
            if (s.contains(" group by ")) {
                completeQuery.append(s);
            }
        }
        for (String s : queryList) {
            if (s.contains(" having ")) {
                completeQuery.append(s);
            }
        }
        for (String s : queryList) {
            if (s.contains(" order by ")) {
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
        varName = "";
    }

    @Override
    public String getTable() {
        return table;
    }

}
