package ua.com.juja.sqlcmd.model.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "database_connection")
public class DatabaseConnection {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "db_name")
    private String database;;

    @JoinColumn(name = "database_connection_id")
    @OneToMany(fetch = FetchType.LAZY)
    private List<UserAction> userActions;

    public List<UserAction> getUserActions() {
        return userActions;
    }

    public void setUserActions(List<UserAction> userActions) {
        this.userActions = userActions;
    }

    public DatabaseConnection() {
        //do nothing
    }

    public DatabaseConnection(String userName, String database) {
        this.userName = userName;
        this.database = database;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setId(int id) {
        this.id = id;
    }


}
