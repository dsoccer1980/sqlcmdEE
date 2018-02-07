package ua.com.juja.sqlcmd.model;

import java.util.List;

public interface UserActionsDao {

    void log(String userName, String dbName, String action);
    List<UserAction> getAll(String userName);
}
