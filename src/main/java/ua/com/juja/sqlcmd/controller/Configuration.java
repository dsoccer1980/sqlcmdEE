package ua.com.juja.sqlcmd.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by denisuser on 20.11.2017.
 */
public class Configuration {
    private Properties properties;

    public Configuration() {
        try(InputStream is = Main.class.getClassLoader().getResourceAsStream("config/sqlcmd.properties")) {
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
}
