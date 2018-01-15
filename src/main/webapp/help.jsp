
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html lang="en">
<head>
    <title>Help</title>
</head>
<body>


Существующие комманды:  <br>

        connect|databaseName|userName|password <br>
         - подключиться к базе данных, с которой будем работать <br>

        tables <br>
         - вывод списка всех таблиц базы данных, к которой подключились <br>

        clear|tableName <br>
         - очистка всей таблицы <br>

        insert|tableName|column1|value1|column2|value2|...|columnN|valueN <br>
         - создание записи в таблице <br>

        create|tableName|column1|column2|...|columnN <br>
         - создание таблицы <br>

        update|tableName|column1|value1|column2|value2|...|columnN|valueN <br>
         - обновить запись, установив значение column2 = value2,..,columnN = valueN, для которой соблюдается условие column1 = value1 <br>

        find|tableName <br>
         - получить содержимое таблицы 'tableName' <br>

        drop|tableName <br>
         - удалить таблицу <br>

        delete|tableName|column|value <br>
         - удалить запись в таблице <br>

        help <br>
         - вывод существующих команд на экран <br>

<a href=menu>Menu</a>



</body>
</html>
