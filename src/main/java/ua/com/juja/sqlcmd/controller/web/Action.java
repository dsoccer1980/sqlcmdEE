package ua.com.juja.sqlcmd.controller.web;

import ua.com.juja.sqlcmd.service.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public interface Action {
    boolean canProcess(String url);
    void doGet(HttpServletRequest req, HttpServletResponse resp)  throws ServletException, IOException;
    void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;
    void setService(Service service);
}
