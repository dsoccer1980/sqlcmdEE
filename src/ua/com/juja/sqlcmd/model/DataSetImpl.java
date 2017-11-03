package ua.com.juja.sqlcmd.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;


public class DataSetImpl implements DataSet {

    static class Data {
        private String name;
        private Object value;

        public Data(String name, Object value) {
            this.name = name;
            this.value = value;
        }


        public String getName() {
            return name;
        }

        public Object getValue() {
            return value;
        }
    }

    public Data[] data = new Data[100];  //TODO
    public int index = 0;

    @Override
    public void put(String name, Object value) {
        data[index++] = new Data(name,value);

    }

    @Override
    public Object[] getValues() {
        Object[] result = new Object[index];
        for (int i = 0; i < index; i++) {
            result[i] = data[i].getValue();
        }
        return result;
    }

    @Override
    public Set<String> getNames() {
        Set<String> result = new LinkedHashSet<>();

        for (int i = 0; i < index; i++) {
            result.add(data[i].getName());
        }
        return result;
    }

    @Override
    public String toString() {
        return "{" +
                "names:" + getNames().toString() + ", " +
                "values:" + Arrays.toString(getValues()) +
                "}";
    }
}
