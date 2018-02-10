package ua.com.juja.sqlcmd.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ua.com.juja.sqlcmd.model.entity.UserAction;

import java.util.List;

public interface UserActionRepository extends CrudRepository<UserAction, Integer> {

    @Query(value = "SELECT ua FROM UserAction ua WHERE ua.databaseConnection.userName = :userName")
    List<UserAction> findByUserName(String userName);
}
