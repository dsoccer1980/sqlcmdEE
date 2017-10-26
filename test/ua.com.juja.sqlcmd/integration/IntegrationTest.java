package ua.com.juja.sqlcmd.integration;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ua.com.juja.sqlcmd.controller.Main;

import java.io.*;

import static org.junit.Assert.assertEquals;


/**
 * Created by denis on 24.10.2017.
 */
public class IntegrationTest {

    private ConfigurableInputStream in;
    private ByteArrayOutputStream out;

    @Before
    public void setup(){
        out=new ByteArrayOutputStream();
        in = new ConfigurableInputStream();

        System.setIn(in);
        System.setOut(new PrintStream(out));
    }



    public String getData() {
        try {
            String result = new String(out.toByteArray(), "UTF-8");
            return result;
        } catch (UnsupportedEncodingException e) {
            return e.getMessage();
        }
    }


    @Test
    public void testHelp() {
        //given
        in.add("help");
        in.add("exit");

        //when
        Main.main(new String[0]);

        //then
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                "Существующие комманды:\r\n" +
                "\tconnect|databaseName|userName|password\r\n" +
                "\t\t - Подключиться к базе данных, с которой будем работать\r\n" +
                "\tlist\r\n" +
                "\t\t - Вывод списка всех таблиц базы данных, к которой подключились\r\n" +
                "\tclear|tableName\r\n" +
                "\t\t - очистка всей таблицы\r\n" +
                "\tcreate|tableName|column1|value1|column2|value2|...|columnN|valueN\r\n" +
                "\t\t - создание записи в таблице\r\n" +
                "\tfind|tableName\r\n" +
                "\t\t - Получить содержимое таблицы 'tableName'\r\n" +
                "\thelp\r\n" +
                "\t\t - Вывод существующих команд на экран\r\n" +
                "\texit\r\n" +
                "\t\t - Выход из программы\r\n" +
                "Введи команду или help для помощи:\r\n" +
                "До скорой встречи!\r\n", getData());
    }

    @Test
    public void testExit() {
        //given
        in.add("exit");

        //when
        Main.main(new String[0]);
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                "До скорой встречи!\r\n", getData());
    }

    @Test
    public void testListWithoutConnect() {
        //given
        in.add("list");
        in.add("exit");

        //when
        Main.main(new String[0]);
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                "Вы не можете пользоваться командой 'list', пока не подключитесь с помощью команды connect|databaseName|userName|password\r\n" +
                "Введи команду или help для помощи:\r\n" +
                "До скорой встречи!\r\n", getData());
    }

    @Test
    public void testFindUserWithoutConnect() {
        //given
        in.add("find|users");
        in.add("exit");

        //when
        Main.main(new String[0]);
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                "Вы не можете пользоваться командой 'find|users', пока не подключитесь с помощью команды connect|databaseName|userName|password\r\n" +
                "Введи команду или help для помощи:\r\n" +
                "До скорой встречи!\r\n", getData());
    }

    @Test
    public void testUnsupported() {
        //given
        in.add("unsupported");
        in.add("exit");

        //when
        Main.main(new String[0]);
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                "Вы не можете пользоваться командой 'unsupported', пока не подключитесь с помощью команды connect|databaseName|userName|password\r\n" +
                "Введи команду или help для помощи:\r\n" +
                "До скорой встречи!\r\n", getData());
    }

    @Test
    public void testUnsupportedAfterConnect() {
        //given
        in.add("connect|sqlcmd|postgres|postgres");
        in.add("unsupported");
        in.add("exit");

        //when
        Main.main(new String[0]);
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                //connect
                "Успех!\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //unsupported
                "Несуществующая команда:unsupported\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //exit
                "До скорой встречи!\r\n", getData());
    }

    @Test
    public void testListAfterConnect() {
        //given
        in.add("connect|sqlcmd|postgres|postgres");
        in.add("list");
        in.add("exit");

        //when
        Main.main(new String[0]);
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                //connect
                "Успех!\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //list
                "[users, test]\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //exit
                "До скорой встречи!\r\n", getData());
    }

//    @Test
//    public void testFindWithErrorAfterConnect() {
//        //given
//        in.add("connect|sqlcmd|postgres|postgres");
//        in.add("find|nonexistst");
//        in.add("exit");
//
//        //when
//        Main.main(new String[0]);
//        assertEquals("Привет юзер!\r\n" +
//                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
//                //connect
//                "Успех!\r\n" +
//                "Введи команду или help для помощи:\r\n" +
//                //list
//                "[users, test]\r\n" +
//                "Введи команду или help для помощи:\r\n" +
//                //exit
//                "До скорой встречи!\r\n", getData());
//    }

    @Test
    public void testFindWithoutDataAfterConnect() {
        //given
        in.add("connect|sqlcmd|postgres|postgres");
        in.add("clear|users");
        in.add("find|users");
        in.add("exit");

        //when
        Main.main(new String[0]);
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                //connect
                "Успех!\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //clear|users
                "Таблица users была успешно очищена.\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //find|users
                "-----------------\r\n" +
                "|name|password|id|\r\n" +
                "-----------------\r\n" +
                "-----------------\r\n" +  //TODO
                "Введи команду или help для помощи:\r\n" +
                //exit
                "До скорой встречи!\r\n", getData());
    }

    @Test
    public void testConnectAfterConnect() {
        //given
        in.add("connect|sqlcmd|postgres|postgres");
        in.add("list");
        in.add("connect|sqlcmd|postgres|postgres");
        in.add("list");
        in.add("exit");

        //when
        Main.main(new String[0]);
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                //connect
                "Успех!\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //list
                "[users, test]\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //connect
                "Успех!\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //list
                "[users, test]\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //exit
                "До скорой встречи!\r\n", getData());
    }

    @Test
    public void testConnectWithError() {
        //given
        in.add("connect|sqlcmd|");
        in.add("exit");

        //when
        Main.main(new String[0]);
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                //connect
                "Неудача по причине:Неверное количество параметров, разделенных знаком '|', ожидается 4, а есть 2\r\n" +
                "Повтори попытку.\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //exit
                "До скорой встречи!\r\n", getData());
    }

    @Test
    public void testFindAfterConnectWithData() {
        //given
        in.add("connect|sqlcmd|postgres|postgres");
        in.add("clear|users");
        in.add("create|users|id|13|name|Stiven|password|*****");
        in.add("create|users|id|14|name|Eva|password|+++++");
        in.add("find|users");
        in.add("exit");

        //when
        Main.main(new String[0]);
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                //connect
                "Успех!\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //clear|users
                "Таблица users была успешно очищена.\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //create|users|id|13|name|Stiven|password|*****
                "Запись {names:[id, name, password], values:[13, Stiven, *****]} в таблице users была успешно создана.\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //create|users|id|14|name|Eva|password|+++++
                "Запись {names:[id, name, password], values:[14, Eva, +++++]} в таблице users была успешно создана.\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //find|users
                "-----------------\r\n" +
                "|name|password|id|\r\n" +
                "-----------------\r\n" +
                "|Stiven|*****|13|\r\n" +
                "|Eva|+++++|14|\r\n" +
                "-----------------\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //exit
                "До скорой встречи!\r\n", getData());
    }
}
