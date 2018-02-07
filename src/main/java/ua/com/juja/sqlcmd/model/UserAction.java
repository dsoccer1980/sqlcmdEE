package ua.com.juja.sqlcmd.model;

public class UserAction {

    private String userName;
    private String dbName;
    private String action;
    private int id;

    public UserAction() {
        //do nothing
    }

    public UserAction(String userName, String dbName, String action) {
        this.userName = userName;
        this.dbName = dbName;
        this.action = action;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setId(int id) {
        this.id = id;
    }
}
