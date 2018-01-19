package ua.com.juja.sqlcmd.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.com.juja.sqlcmd.model.DatabaseManager;

@Component
public class DataBaseManagerFactory {

    @Autowired
    private DatabaseManager databaseManager;

    public DatabaseManager createDatabaseManager(){
        try {
           return (DatabaseManager)Class.forName(databaseManager.getClass().getName()).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnsupportedOperationException();
        }
    }

    public void setDatabaseManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }
}
