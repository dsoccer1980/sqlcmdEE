package ua.com.juja.sqlcmd.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "user_actions")
public class UserAction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "action")
    private String action;

    @JoinColumn(name = "database_connection_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private DatabaseConnection databaseConnection;

    public UserAction() {
        //do nothing
    }

    public UserAction(String action, DatabaseConnection databaseConnection) {
        this.action = action;
        this.databaseConnection = databaseConnection;
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

    public int getId() {
        return id;
    }

    public DatabaseConnection getDatabaseConnection() {
        return databaseConnection;
    }

    public void setDatabaseConnection(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }
}
