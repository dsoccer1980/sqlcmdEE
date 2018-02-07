package ua.com.juja.sqlcmd.model;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserActionsDaoImpl implements UserActionsDao {

    private JdbcTemplate template;

    @Autowired
    @Qualifier(value="logDataSource")
    public void setDataSource(DataSource dataSource) {
        template = new JdbcTemplate(dataSource);
    }


    @Override
    public void log(String userName, String dbName, String action) {
        template.update("INSERT INTO user_actions(user_name, db_name, action)" +
                                 " VALUES(?, ?, ?)", userName, dbName, action);
    }

    @Override
    public List<UserAction> getAll(String userName) {
        return template.query("SELECT * FROM user_actions WHERE user_name = ?", new String[]{userName},
                new RowMapper<UserAction>() {
            @Override
            public UserAction mapRow(ResultSet resultSet, int rowNum) throws SQLException {
                UserAction result = new UserAction();
                result.setId(resultSet.getInt("id"));
                result.setUserName(resultSet.getString("user_name"));
                result.setDbName(resultSet.getString("db_name"));
                result.setAction(resultSet.getString("action"));
                return result;
            }
        });
    }
}
