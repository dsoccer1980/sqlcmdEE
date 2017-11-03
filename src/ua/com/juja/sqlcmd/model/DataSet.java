package ua.com.juja.sqlcmd.model;

import java.util.Set;


public interface DataSet {
    void put(String name, Object value);

    Object[] getValues();

    Set<String> getNames();
}
