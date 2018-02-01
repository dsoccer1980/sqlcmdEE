package ua.com.juja.sqlcmd.integraion;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.service.Service;


import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class MockIntegrationTest {
    private WebDriver driver;
    private SpringMockerJettyRunner runner;
    private Service service;

    @Before
    public void startServer() throws Exception {
        runner = new SpringMockerJettyRunner("src/main/webapp", "/sqlcmd");
        driver = new HtmlUnitDriver(true);
    }

    @After
    public void stop() throws Exception {
        runner.stop();
    }

    @Test
    public void test() throws Exception {
        // given
        runner.mockBean("service");
        runner.start();
        service = runner.getBean("service");

        when(service.commandsList()).thenReturn(Arrays.asList("help", "tables"));

        DatabaseManager manager = mock(DatabaseManager.class);
        when(service.connect(anyString(), anyString(), anyString()))
                .thenReturn(manager);

        when(service.tables(manager))
                .thenReturn(new LinkedHashSet<String>(Arrays.asList("users", "test")));

        // when
        driver.get(runner.getUrl());

        driver.findElement(By.id("dbname")).sendKeys("sqlcmd");
        driver.findElement(By.id("username")).sendKeys("postgres");
        driver.findElement(By.id("password")).sendKeys("postgres");
        driver.findElement(By.id("connect")).click();

        // then
        assertLinks("[help, tables]",
                "[/sqlcmd/help, /sqlcmd/tables]");

        // when
        driver.get(getBase() + "/sqlcmd/tables");

        // then
        assertLinks("[users, test, Menu]",
                "[/sqlcmd/find?table=users, " +
                "/sqlcmd/find?table=test, " +
                "/sqlcmd/menu]"
        );
    }

    private void assertLinks(String expectedNames, String expectedUrls) {
        List<WebElement> elements = driver.findElements(By.tagName("a"));
        List<String> names = new LinkedList<>();
        List<String> urls = new LinkedList<>();
        for (WebElement element : elements) {
            names.add(element.getText());
            urls.add(element.getAttribute("href"));
        }
        assertEquals(expectedNames, names.toString());
        String base = getBase();
        assertEquals(expectedUrls, urls.toString()
                .replaceAll(base, ""));
    }

    private String getBase() {
        return "http://localhost:" + runner.getPort();
    }


}