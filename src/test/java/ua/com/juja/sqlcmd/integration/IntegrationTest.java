package ua.com.juja.sqlcmd.integration;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import ua.com.juja.sqlcmd.controller.Configuration;
import ua.com.juja.sqlcmd.controller.Main;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class IntegrationTest {
    private ConfigurableInputStream in;
    private ByteArrayOutputStream out;
    private Configuration configuration;
    private String tableName1 = "test_db1";
    private String tableName2 = "test_db2";

    @Before
    public void setup() {
        out = new ByteArrayOutputStream();
        in = new ConfigurableInputStream();

        System.setIn(in);
        System.setOut(new PrintStream(out));

        configuration = new Configuration();
    }

    private String getData() {
        try {
            return new String(out.toByteArray(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return e.getMessage();
        }
    }

    @Test
    public void test01Help() {
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
                "\t\t - подключиться к базе данных, с которой будем работать\r\n" +
                "\ttables\r\n" +
                "\t\t - вывод списка всех таблиц базы данных, к которой подключились\r\n" +
                "\tclear|tableName\r\n" +
                "\t\t - очистка всей таблицы\r\n" +
                "\tinsert|tableName|column1|value1|column2|value2|...|columnN|valueN\r\n" +
                "\t\t - создание записи в таблице\r\n" +
                "\tcreate|tableName|column1|column2|...|columnN\r\n" +
                "\t\t - создание таблицы\r\n" +
                "\tupdate|tableName|column1|value1|column2|value2|...|columnN|valueN\r\n" +
                "\t\t - обновить запись, установив значение column2 = value2,..,columnN = valueN, для которой соблюдается условие column1 = value1\r\n" +
                "\tfind|tableName\r\n" +
                "\t\t - получить содержимое таблицы 'tableName'\r\n" +
                "\tdrop|tableName\r\n" +
                "\t\t - удалить таблицу\r\n" +
                "\tdelete|tableName|column|value\r\n" +
                "\t\t - удалить запись в таблице\r\n" +
                "\thelp\r\n" +
                "\t\t - вывод существующих команд на экран\r\n" +
                "\texit\r\n" +
                "\t\t - выход из программы\r\n" +
                "Введи команду или help для помощи:\r\n" +
                "До скорой встречи!\r\n", getData());
    }

    @Test
    public void test02Exit() {
        //given
        in.add("exit");

        //when
        Main.main(new String[0]);
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                "До скорой встречи!\r\n", getData());
    }

    @Test
    public void test03ListWithoutConnect() {
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
    public void test04FindUserWithoutConnect() {
        //given
        in.add("find|testDB");
        in.add("exit");

        //when
        Main.main(new String[0]);
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                "Вы не можете пользоваться командой 'find|testDB', пока не подключитесь с помощью команды connect|databaseName|userName|password\r\n" +
                "Введи команду или help для помощи:\r\n" +
                "До скорой встречи!\r\n", getData());
    }

    @Test
    public void test05Unsupported() {
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
    public void test06UnsupportedAfterConnect() {
        //given
        in.add("connect|" + getDatabaseUsernamePassword());
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
    public void test07CreateTable1() {
        //given
        in.add("connect|" + getDatabaseUsernamePassword());
        in.add("drop|" + tableName1);
        in.add("yes");
        in.add("create|" + tableName1 + "|id");
        in.add("find|" + tableName1);
        in.add("exit");

        //when
        Main.main(new String[0]);
        String data = getData();
        data = data.replaceFirst("Введи команду или help для помощи:\r\n" +
                "Неудача по причине:Таблицы " + tableName1 + " не существует\r\n" +
                "Повтори попытку.\r\n" +
                "Введи команду или help для помощи:\r\n" +
                "Несуществующая команда:yes\r\n","");
        data = data.replaceFirst("Введи команду или help для помощи:\r\n" +
                "Вы уверены, что хотите удалить таблицу: " + tableName1 + ". yes/no\\?\r\n" +
                "Таблица " + tableName1 + " была успешно удалена.\r\n","");

        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                //connect
                "Успех!\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //create|test3|id|name|password
                "Таблица " + tableName1 + " была успешно создана.\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //find|test3
                "+----------------+\r\n" +
                "+  id            +\r\n" +
                "+----------------+\r\n" +
                "+----------------+\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //exit
                "До скорой встречи!\r\n", data);
    }

    @Test
    public void test08CreateTable2() {
        //given
        in.add("connect|" + getDatabaseUsernamePassword());
        in.add("drop|" + tableName2);
        in.add("yes");
        in.add("create|" + tableName2 + "|name|password|id");
        in.add("find|" + tableName2);
        in.add("exit");

        //when
        Main.main(new String[0]);
        String data = getData();
        data = data.replaceFirst("Введи команду или help для помощи:\r\n" +
                "Неудача по причине:Таблицы " + tableName2 + " не существует\r\n" +
                "Повтори попытку.\r\n" +
                "Введи команду или help для помощи:\r\n" +
                "Несуществующая команда:yes\r\n","");
        data = data.replaceFirst("Введи команду или help для помощи:\r\n" +
                "Вы уверены, что хотите удалить таблицу: " + tableName2 + ". yes/no\\?\r\n" +
                "Таблица " + tableName2 + " была успешно удалена.\r\n","");

        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                //connect
                "Успех!\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //create|test3|id|name|password
                "Таблица " + tableName2 + " была успешно создана.\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //find|test3
                "+--------------+--------------+----------------+\r\n" +
                "+  name        +  password    +  id            +\r\n" +
                "+--------------+--------------+----------------+\r\n" +
                "+--------------+--------------+----------------+\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //exit
                "До скорой встречи!\r\n", data);
    }

    @Test
    public void test09CreateWithWrongParameter() {
        //given
        in.add("connect|" + getDatabaseUsernamePassword());
        in.add("create|testDB");
        in.add("exit");

        //when
        Main.main(new String[0]);
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                //connect
                "Успех!\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //clear|testDB|something
                "Неудача по причине:Формат команды 'create|tableName|column1|column2|...|columnN', а ты прислал: 'create|testDB'\r\n" +
                "Повтори попытку.\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //exit
                "До скорой встречи!\r\n", getData());
    }

    @Test
    public void test10CreateWithSqlException() {
        //given
        in.add("connect|" + getDatabaseUsernamePassword());
        in.add("create|-testDB|id");
        in.add("exit");

        //when
        Main.main(new String[0]);
        // fail();
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                //connect
                "Успех!\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //clear|testDB|something
                "Неудача по причине:ERROR: syntax error at or near \"-\"\n" +
                "  Position: 14\r\n" +
                "Повтори попытку.\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //exit
                "До скорой встречи!\r\n", getData());
    }

    @Test
    public void test11ListAfterConnect() {
        //given
        in.add("connect|" + getDatabaseUsernamePassword());
        in.add("tables");
        in.add("exit");

        //when
        Main.main(new String[0]);
        String data = getData();
        String reg = "Привет юзер!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: connect\\|database\\|username\\|password\r\n" +
                //connect
                "Успех!\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //tables
                "\\[.*test_db1, test_db2, .*\\]\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //exit
                "До скорой встречи!\r\n";

        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(data);
        assertTrue(matcher.matches());
    }

    @Test
    public void test12FindWithoutDataAfterConnect() {
        //given
        in.add("connect|" + getDatabaseUsernamePassword());
        in.add("create|" + tableName2 + "|name|password|id");
        in.add("clear|" + tableName2);
        in.add("yes");
        in.add("find|" + tableName2);
        in.add("exit");

        //when
        Main.main(new String[0]);
        String data = getData();
        data = data.replaceFirst("Введи команду или help для помощи:\r\n" +
                "Таблица " + tableName2 + " была успешно создана.\r\n","");
        data = data.replaceFirst("Введи команду или help для помощи:\r\n" +
                "Неудача по причине:Таблица " + tableName2 + " уже существует\r\n" +
                "Повтори попытку.\r\n","");
        assertEquals(String.format("Привет юзер!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                //connect
                "Успех!\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //clear|testDB
                "Вы уверены, что хотите очистить таблицу: %1$s. yes/no?\r\n" +
                //yes
                "Таблица %1$s была успешно очищена.\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //find|testDB
                "+--------------+--------------+----------------+\r\n" +
                "+  name        +  password    +  id            +\r\n" +
                "+--------------+--------------+----------------+\r\n" +
                "+--------------+--------------+----------------+\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //exit
                "До скорой встречи!\r\n", tableName2), data);
    }

    @Test
    public void test13ConnectAfterConnect() {
        //given
        in.add("connect|" + getDatabaseUsernamePassword());
        in.add("create|" + tableName1 + "|name");
        in.add("create|" + tableName2 + "|name|password|id");
        in.add("tables");
        in.add("connect|" + getDatabaseUsernamePassword());
        in.add("tables");
        in.add("exit");

        //when
        Main.main(new String[0]);
        String data = getData();
        data = data.replaceFirst("Введи команду или help для помощи:\r\n" +
                "Таблица " + tableName1 + " была успешно создана.\r\n","");
        data = data.replaceFirst("Введи команду или help для помощи:\r\n" +
                "Неудача по причине:Таблица " + tableName1 + " уже существует\r\n" +
                "Повтори попытку.\r\n","");
        data = data.replaceFirst("Введи команду или help для помощи:\r\n" +
                "Таблица " + tableName2 + " была успешно создана.\r\n","");
        data = data.replaceFirst("Введи команду или help для помощи:\r\n" +
                "Неудача по причине:Таблица " + tableName2 + " уже существует\r\n" +
                "Повтори попытку.\r\n","");
        String reg = "Привет юзер!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: connect\\|database\\|username\\|password\r\n" +
                //connect
                "Успех!\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //tables
                "\\[.*test_db1, test_db2.*\\]\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //connect
                "Успех!\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //tables
                "\\[.*test_db1, test_db2.*\\]\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //exit
                "До скорой встречи!\r\n";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(data);
        assertTrue(matcher.matches());
    }

    @Test
    public void test14ConnectWithError() {
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
    public void test15FindAfterConnectWithData() {
        //given
        in.add("connect|" + getDatabaseUsernamePassword());
        in.add("clear|" + tableName2);
        in.add("yes");
        in.add("insert|" + tableName2 + "|id|13|name|Stiven|password|*****");
        in.add("insert|" + tableName2 + "|id|14|name|Eva|password|+++++");
        in.add("find|" + tableName2);
        in.add("exit");

        //when
        Main.main(new String[0]);
        assertEquals(String.format("Привет юзер!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                //connect
                "Успех!\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //clear|testDB
                "Вы уверены, что хотите очистить таблицу: %1$s. yes/no?\r\n" +
                //yes
                "Таблица %1$s была успешно очищена.\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //insert|testDB|id|13|name|Stiven|password|*****
                "Запись {names:[id, name, password], values:[13, Stiven, *****]} в таблице '%1$s' была успешно создана.\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //insert|testDB|id|14|name|Eva|password|+++++
                "Запись {names:[id, name, password], values:[14, Eva, +++++]} в таблице '%1$s' была успешно создана.\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //find|testDB
                "+--------------+--------------+----------------+\r\n" +
                "+  name        +  password    +  id            +\r\n" +
                "+--------------+--------------+----------------+\r\n" +
                "+  Stiven      +  *****       +  13            +\r\n" +
                "+  Eva         +  +++++       +  14            +\r\n" +
                "+--------------+--------------+----------------+\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //exit
                "До скорой встречи!\r\n", tableName2), getData());
    }

    @Test
    public void test16ClearWithError() {
        //given
        in.add("connect|" + getDatabaseUsernamePassword());
        in.add("clear|" + tableName2 + "|something");
        in.add("exit");

        //when
        Main.main(new String[0]);
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                //connect
                "Успех!\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //clear|testDB|something
                "Неудача по причине:Формат комманды 'clear|tableName', а ты ввел: clear|" + tableName2 + "|something\r\n" +
                "Повтори попытку.\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //exit
                "До скорой встречи!\r\n", getData());
    }

    @Test
    public void test17ClearWithCancel() {
        //given
        in.add("connect|" + getDatabaseUsernamePassword());
        in.add("clear|" + tableName2);
        in.add("no");
        in.add("exit");

        //when
        Main.main(new String[0]);
        assertEquals(String.format("Привет юзер!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                //connect
                "Успех!\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //clear|testDB
                "Вы уверены, что хотите очистить таблицу: %s. yes/no?\r\n" +
                //no
                "Команда по очистке таблице отменена.\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //exit
                "До скорой встречи!\r\n", tableName2), getData());
    }

    @Test
    public void test18ClearWithWrongAnswersToAskingClearTableOrNot() {
        //given
        in.add("connect|" + getDatabaseUsernamePassword());
        in.add("clear|"  + tableName2);
        in.add("smth");
        in.add("no");
        in.add("exit");

        //when
        Main.main(new String[0]);
        assertEquals(String.format("Привет юзер!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                //connect
                "Успех!\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //clear|testDB
                "Вы уверены, что хотите очистить таблицу: %s. yes/no?\r\n" +
                //smth
                "Нужно ввести yes или no, а введено: smth\r\n" +
                //no
                "Команда по очистке таблице отменена.\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //exit
                "До скорой встречи!\r\n", tableName2), getData());
    }

    @Test
    public void test19ClearIfTableNotExists() {
        //given
        String tableName = "testDBNotExists";
        in.add("connect|" + getDatabaseUsernamePassword());
        in.add("clear|" + tableName);
        in.add("exit");

        //when
        Main.main(new String[0]);
        String data = getData();
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                //connect
                "Успех!\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //delete
                "Неудача по причине:Таблицы testDBNotExists не существует\r\n" +
                "Повтори попытку.\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //exit
                "До скорой встречи!\r\n", data);
    }

    @Test
    public void test20InsertWithOddCountParameters() {
        //given
        in.add("connect|" + getDatabaseUsernamePassword());
        in.add("insert|testDB|something");
        in.add("exit");

        //when
        Main.main(new String[0]);
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                //connect
                "Успех!\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //insert|testDB|something
                "Неудача по причине:Должно быть четное количество параметров в формате 'insert|tableName|column1|value1|column2|value2|...|columnN|valueN', а ты прислал: 'insert|testDB|something'\r\n" +
                "Повтори попытку.\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //exit
                "До скорой встречи!\r\n", getData());
    }

    @Test
    public void test21InsertWithCountParametersLess3() {
        //given
        in.add("connect|" + getDatabaseUsernamePassword());
        in.add("insert|testDB");
        in.add("exit");

        //when
        Main.main(new String[0]);
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                //connect
                "Успех!\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //insert|testDB|something
                "Неудача по причине:Формат комманды 'insert|tableName|column1|value1|column2|value2|...|columnN|valueN', а ты прислал: 'insert|testDB'\r\n" +
                "Повтори попытку.\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //exit
                "До скорой встречи!\r\n", getData());
    }

    @Test
    public void test22InsertWithSqlException() {
        //given
        in.add("connect|" + getDatabaseUsernamePassword());
        in.add("insert|" + tableName2 +"|name-|13");
        in.add("exit");

        //when
        Main.main(new String[0]);
        // fail();
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                //connect
                "Успех!\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //clear|testDB|something
                "Неудача по причине:ERROR: syntax error at or near \"-\"\n" +
                "  Position: 27\r\n" +
                "Повтори попытку.\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //exit
                "До скорой встречи!\r\n", getData());
    }

    @Test
    public void test23UpdateTable() {
        //given
        in.add("connect|" + getDatabaseUsernamePassword());
        in.add("clear|" + tableName2);
        in.add("yes");
        in.add("insert|" + tableName2 + "|name|Stiven|password|*****|id|13");
        in.add("update|" + tableName2 +"|id|13|name|StivenNew|password|passwordNew");
        in.add("exit");

        //when
        Main.main(new String[0]);
        String data = getData();
        assertEquals(String.format("Привет юзер!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                //connect
                "Успех!\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //clear
                "Вы уверены, что хотите очистить таблицу: %1$s. yes/no?\r\n" +
                "Таблица %s была успешно очищена.\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //insert
                "Запись {names:[name, password, id], values:[Stiven, *****, 13]} в таблице '%1$s' была успешно создана.\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //update
                "Запись {names:[name, password], values:[StivenNew, passwordNew]} в таблице '%1$s' была успешно обновлена.\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //exit
                "До скорой встречи!\r\n",tableName2), data);
    }

    @Test
    public void test24UpdateTableWithCountParametersLess5() {
        //given
        in.add("connect|" + getDatabaseUsernamePassword());
        in.add("create|" + tableName2 + "|name|password|id");
        in.add("update|" + tableName2 +"|name");
        in.add("exit");

        //when
        Main.main(new String[0]);
        String data = getData();
        data = data.replaceFirst("Введи команду или help для помощи:\r\n" +
                "Таблица " + tableName2 + " была успешно создана.\r\n","");
        data = data.replaceFirst("Введи команду или help для помощи:\r\n" +
                "Неудача по причине:Таблица " + tableName2 + " уже существует\r\n" +
                "Повтори попытку.\r\n","");
        assertEquals(String.format("Привет юзер!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                //connect
                "Успех!\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //update
                "Неудача по причине:Формат комманды 'update|tableName|column1|value1|column2|value2|...|columnN|valueN', а ты прислал: 'update|%s|name'\r\n" +
                "Повтори попытку.\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //exit
                "До скорой встречи!\r\n", tableName2), data);
    }

    @Test
    public void test25UpdateTableWithOddCountParameters() {
        //given
        in.add("connect|" + getDatabaseUsernamePassword());
        in.add("update|" + tableName2 +"|name|Somename|password");
        in.add("exit");

        //when
        Main.main(new String[0]);
        String data = getData();
        data = data.replaceFirst("Введи команду или help для помощи:\r\n" +
                "Таблица " + tableName2 + " была успешно создана.\r\n","");
        data = data.replaceFirst("Введи команду или help для помощи:\r\n" +
                "Неудача по причине:Таблица " + tableName2 + " уже существует\r\n" +
                "Повтори попытку.\r\n","");
        assertEquals(String.format("Привет юзер!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                //connect
                "Успех!\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //update
                "Неудача по причине:Должно быть четное количество параметров в формате 'update|tableName|column1|value1|column2|value2|...|columnN|valueN', а ты прислал: 'update|%s|name|Somename|password'\r\n" +
                "Повтори попытку.\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //exit
                "До скорой встречи!\r\n", tableName2), data);
    }

    @Test
    public void test26UpdateTableNotExists() {
        //given
        String tableName = "testDBNotExists";
        in.add("connect|" + getDatabaseUsernamePassword());
        in.add("update|" + tableName +"|id|13|name|Somename");
        in.add("exit");

        //when
        Main.main(new String[0]);
        String data = getData();
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                //connect
                "Успех!\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //update
                "Неудача по причине:Таблицы testDBNotExists не существует\r\n" +
                "Повтори попытку.\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //exit
                "До скорой встречи!\r\n", data);
    }

    @Test
    public void test27UpdateWithSqlException() {
        //given
        in.add("connect|" + getDatabaseUsernamePassword());
        in.add("update|" + tableName2 + "|name-|13|id|13");
        in.add("exit");

        //when
        Main.main(new String[0]);
        // fail();
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                //connect
                "Успех!\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //clear|testDB|something
                "Неудача по причине:ERROR: syntax error at or near \"=\"\n" +
                "  Position: 41\r\n" +
                "Повтори попытку.\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //exit
                "До скорой встречи!\r\n", getData());
    }

    @Test
    public void test28DeleteRow() {
        //given
        in.add("connect|" + getDatabaseUsernamePassword());
        in.add("clear|" + tableName2);
        in.add("yes");
        in.add("insert|" + tableName2 + "|name|Stiven|password|*****|id|13");
        in.add("delete|" + tableName2 +"|id|13");
        in.add("find|" + tableName2);
        in.add("exit");

        //when
        Main.main(new String[0]);
        String data = getData();
        assertEquals(String.format("Привет юзер!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                //connect
                "Успех!\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //clear
                "Вы уверены, что хотите очистить таблицу: %1$s. yes/no?\r\n" +
                "Таблица %s была успешно очищена.\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //insert
                "Запись {names:[name, password, id], values:[Stiven, *****, 13]} в таблице '%1$s' была успешно создана.\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //update
                "Запись [id, 13] в таблице '%1$s' была успешно удалена.\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //find
                "+--------------+--------------+----------------+\r\n" +
                "+  name        +  password    +  id            +\r\n" +
                "+--------------+--------------+----------------+\r\n" +
                "+--------------+--------------+----------------+\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //exit
                "До скорой встречи!\r\n",tableName2), data);
    }

    @Test
    public void test29DeleteRowWithWrongCountParameters() {
        //given
        in.add("connect|" + getDatabaseUsernamePassword());
        in.add("create|" + tableName2 + "|name|password|id");
        in.add("delete|" + tableName2 +"|name");
        in.add("exit");

        //when
        Main.main(new String[0]);
        String data = getData();
        data = data.replaceFirst("Введи команду или help для помощи:\r\n" +
                "Таблица " + tableName2 + " была успешно создана.\r\n","");
        data = data.replaceFirst("Введи команду или help для помощи:\r\n" +
                "Неудача по причине:Таблица " + tableName2 + " уже существует\r\n" +
                "Повтори попытку.\r\n","");
        assertEquals(String.format("Привет юзер!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                //connect
                "Успех!\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //update
                "Неудача по причине:Формат комманды 'delete|tableName|column|value', а ты прислал: 'delete|%s|name'\r\n" +
                "Повтори попытку.\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //exit
                "До скорой встречи!\r\n", tableName2), data);
    }

    @Test
    public void test30DeleteIfTableNotExists() {
        //given
        String tableName = "testDBNotExists";
        in.add("connect|" + getDatabaseUsernamePassword());
        in.add("delete|" + tableName +"|id|13");
        in.add("exit");

        //when
        Main.main(new String[0]);
        String data = getData();
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                //connect
                "Успех!\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //delete
                "Неудача по причине:Таблицы testDBNotExists не существует\r\n" +
                "Повтори попытку.\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //exit
                "До скорой встречи!\r\n", data);
    }

    @Test
    public void test31DeleteIfRowNotExists() {
        //given
        in.add("connect|" + getDatabaseUsernamePassword());
        in.add("delete|" + tableName2 + "|name-|13");
        in.add("exit");

        //when
        Main.main(new String[0]);
        // fail();
        assertEquals(String.format("Привет юзер!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                //connect
                "Успех!\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //clear|testDB|something
                "Неудача по причине:Данной записи в таблице %s не существует\r\n" +
                "Повтори попытку.\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //exit
                "До скорой встречи!\r\n", tableName2), getData());
    }

    @Test
    public void test32DropTableWithWrongParameters() {
        //given
        in.add("connect|" + getDatabaseUsernamePassword());
        in.add("drop|test|smth");
        in.add("exit");

        //when
        Main.main(new String[0]);
        String data = getData();
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                //connect
                "Успех!\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //drop
                "Неудача по причине:Формат комманды 'drop|tableName', а ты ввел: drop|test|smth\r\n" +
                "Повтори попытку.\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //exit
                "До скорой встречи!\r\n", data);
    }

    @Test
    public void test33DropTableWithCancel() {
        //given
        in.add("connect|" + getDatabaseUsernamePassword());
        in.add("drop|" + tableName2);
        in.add("no");
        in.add("exit");

        //when
        Main.main(new String[0]);
        String data = getData();
        assertEquals(String.format("Привет юзер!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                //connect
                "Успех!\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //drop
                "Вы уверены, что хотите удалить таблицу: %s. yes/no?\r\n" +
                "Команда по удалению таблицы отменена.\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //exit
                "До скорой встречи!\r\n", tableName2), data);
    }

    @Test
    public void test34DropIfTableNotExists() {
        //given
        String tableName = "testDBNotExists";
        in.add("connect|" + getDatabaseUsernamePassword());
        in.add("drop|" + tableName);
        in.add("exit");

        //when
        Main.main(new String[0]);
        String data = getData();
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                //connect
                "Успех!\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //delete
                "Неудача по причине:Таблицы testDBNotExists не существует\r\n" +
                "Повтори попытку.\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //exit
                "До скорой встречи!\r\n", data);
    }

    @Test
    public void test35DropWithWrongAnswersToAskingClearTableOrNot() {
        //given
        in.add("connect|" + getDatabaseUsernamePassword());
        in.add("drop|" + tableName2);
        in.add("smth");
        in.add("no");
        in.add("exit");

        //when
        Main.main(new String[0]);
        assertEquals(String.format("Привет юзер!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                //connect
                "Успех!\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //clear|testDB
                "Вы уверены, что хотите удалить таблицу: %s. yes/no?\r\n" +
                //smth
                "Нужно ввести yes или no, а введено: smth\r\n" +
                //no
                "Команда по удалению таблицы отменена.\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //exit
                "До скорой встречи!\r\n", tableName2), getData());
    }

    @Test
    public void test36DropTable1() {
        //given
        in.add("connect|" + getDatabaseUsernamePassword());
        in.add("drop|" + tableName1);
        in.add("yes");
        in.add("exit");

        //when
        Main.main(new String[0]);
        String data = getData();
        assertEquals(String.format("Привет юзер!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                //connect
                "Успех!\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //clear|testDB
                "Вы уверены, что хотите удалить таблицу: %1$s. yes/no?\r\n" +
                //yes
                "Таблица %1$s была успешно удалена.\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //exit
                "До скорой встречи!\r\n", tableName1), data);
    }

    @Test
    public void test37DropTable2() {
        //given
        in.add("connect|" + getDatabaseUsernamePassword());
        in.add("drop|" + tableName2);
        in.add("yes");
        in.add("exit");

        //when
        Main.main(new String[0]);
        String data = getData();
        assertEquals(String.format("Привет юзер!\r\n" +
                "Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: connect|database|username|password\r\n" +
                //connect
                "Успех!\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //clear|testDB
                "Вы уверены, что хотите удалить таблицу: %s. yes/no?\r\n" +
                //yes
                "Таблица %1$s была успешно удалена.\r\n" +
                "Введи команду или help для помощи:\r\n" +
                //exit
                "До скорой встречи!\r\n", tableName2), data);
    }

    private String getDatabaseUsernamePassword() {
        return String.format("%s|%s|%s", configuration.getDatabaseName(), configuration.getDatabaseUsername(), configuration.getDatabasePassword());
    }
}
