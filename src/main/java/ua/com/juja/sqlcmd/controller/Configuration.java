package ua.com.juja.sqlcmd.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {
    private Properties properties;

    public Configuration() {
        try(InputStream is = this.getClass().getClassLoader().getResourceAsStream("config/sqlcmd.properties")) {
            properties = new Properties();
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getServerName(){
        return properties.getProperty("database.server.name");
    }

    public String getPort(){
        return properties.getProperty("database.port");
    }

    public String getDriver(){
        return properties.getProperty("database.jdbc.driver");
    }

    public String getDatabaseName(){
        return properties.getProperty("database.name");
    }

    public String getDatabaseUsername(){
        return properties.getProperty("database.username");
    }

    public String getDatabasePassword(){
        return properties.getProperty("database.password");
    }
}
