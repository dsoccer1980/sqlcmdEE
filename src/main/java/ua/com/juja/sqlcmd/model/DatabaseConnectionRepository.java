package ua.com.juja.sqlcmd.model;

import org.springframework.data.repository.CrudRepository;
import ua.com.juja.sqlcmd.model.entity.DatabaseConnection;

public interface DatabaseConnectionRepository extends CrudRepository<DatabaseConnection, Integer> {

    DatabaseConnection findByUserNameAndDbName(String userName, String dbName);
}
