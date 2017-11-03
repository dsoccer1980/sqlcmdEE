package ua.com.juja.sqlcmd.model;

/**
 * Created by denis on 03.11.2017.
 */
public interface DataSet {
    void put(String name, Object value);

    Object[] getValues();

    String[] getNames();
}
