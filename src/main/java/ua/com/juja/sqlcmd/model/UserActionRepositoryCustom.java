package ua.com.juja.sqlcmd.model;

public interface UserActionRepositoryCustom {

    void saveAction(String databaseName, String userName, String action);
}
