package querybuilder.rules;

public class JoinArgsRule implements Rules{
    @Override
    public boolean checkTheRule(String string) {
        String[] var = string.split(" ");
        String[] parts = string.split("\\.");
        String temp = string.substring(string.indexOf(".") + 1);
        String[] newParts = temp.split("\\)\\.");
        String[] tableName = parts[0].split("\"");
        String table = tableName[1];

        for(int i=0;i< newParts.length;i++) {
            if (newParts[i].contains("Join(")) {
                String part = newParts[i].substring(0, newParts[i].length() - 1);
                String[] query = part.split("\\(");
                StringBuilder sb = new StringBuilder();
                sb.append(query[1]);
                for (int j = 0; j < sb.length(); j++) {
                    if ((sb.charAt(j) == '"')) {
                        sb.setCharAt(j, ' ');
                    }
                }
                if(sb.toString().trim().equals(table.trim())){
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public String getErrorMessage() {
        return "JoinArgs rule failed!You must Join().On() other table!";
    }
}
