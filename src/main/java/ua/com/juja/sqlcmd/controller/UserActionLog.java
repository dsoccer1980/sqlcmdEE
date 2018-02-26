package ua.com.juja.sqlcmd.controller;

import ua.com.juja.sqlcmd.model.entity.UserAction;


public class UserActionLog {

    private String action;
    private String userName;
    private String database;

    public UserActionLog() {
        // do nothing
    }

    public UserActionLog(UserAction action) {
        this.database = action.getDatabaseConnection().getDatabase();
        this.userName = action.getDatabaseConnection().getUserName();
        this.action = action.getAction();
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }
}
