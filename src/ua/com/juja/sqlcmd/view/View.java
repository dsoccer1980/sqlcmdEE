package ua.com.juja.sqlcmd.view;

/**
 * Created by denis on 18.10.2017.
 */
public interface View {

    void write(String message);

    String read();
}
