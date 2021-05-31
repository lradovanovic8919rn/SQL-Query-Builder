package querybuilder.rules;

public class QueryJoinRule implements Rules {
    @Override
    public boolean checkTheRule(String string) {
        String temp = string.substring(string.indexOf(".") + 1);
        String[] newParts = temp.split("\\)\\.");
        //System.out.println("QUERYJOINRULE");
        String[] querySplit = string.split(" ");
        String tableName = querySplit[4].split("\"")[1];
        //System.out.println(tableName);

        for (String newPart : newParts) {
            /*
            for(int i=0; i<newParts.length; i++){
                System.out.println("newParts"+i+": " + newParts[i]);
            }
            */
            if (newPart.contains("Join(")) {
                String[] joinTableName = newPart.split("\"");
                //System.out.println("join: " + joinTableName[1]);
                if(joinTableName[1].equals(tableName)) return false;
            }
        }
        return true;
    }

    @Override
    public String getErrorMessage() {
        return "QueryJoin rule failed! You need to have different tables in Query and Join!";
    }
}
