package ua.com.juja.sqlcmd.model;

import org.springframework.beans.factory.annotation.Autowired;
import ua.com.juja.sqlcmd.model.entity.DatabaseConnection;
import ua.com.juja.sqlcmd.model.entity.UserAction;

public class UserActionRepositoryImpl implements UserActionRepositoryCustom {

    @Autowired
    private UserActionRepository userActions;

    @Autowired
    private DatabaseConnectionRepository databaseConnections;

    @Override
    public void saveAction(String databaseName, String userName, String action) {
        DatabaseConnection databaseConnection = databaseConnections.findByUserNameAndDbName(userName, databaseName);
        if (databaseConnection == null) {
            databaseConnection = databaseConnections.save(new DatabaseConnection(userName, databaseName));
        }
        userActions.save(new UserAction(action, databaseConnection));
    }
}
