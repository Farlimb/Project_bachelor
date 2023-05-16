package com.example.id_system;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SeleniumTests {
    static WebDriver driver;

    @BeforeAll
    public static void setUpClass(){
        System.setProperty("webdriver.chrome.driver", "src/test/java/com/example/bakalarka_jpa/Driver/chromedriver.exe");
        System.setProperty("webdriver.http.factory", "jdk-http-client");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }


    @BeforeEach
    public void setUp(){
        driver = new ChromeDriver();
    }


    @Test public void uprava(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(500) /*timeout in seconds*/);
        DeleteByParams(wait,"SkuskaJózsef", "Gyula", "Bratislava", "Moskovská 2");
        DeleteByParams(wait,"SkuskaJóžef", "Ďula", "Košice", "nahodnaulica 4");
        DeleteByParams(wait,"SkuskaJózsef", "Gyula", "Ružemberok", "Moskovenská 2");
        DeleteByParams(wait,"SkuskaJóžef", "Ďula", "Košice - Západ", "Letná 9");
        DeleteByParams(wait,"SkuskaJózsef", "Gyula", "Košice", "Kotbuská 8");
        DeleteByParams(wait,"SkuskaJóžef", "Ďula", "Košice", "Tomášikova 4");
        DeleteByParams(wait,"SkuskaJózsef", "Gyula", "Bratislava", "Moskovská 2");
        DeleteByParams(wait,"SkuskaJóžef", "Ďula", "Košice", "nahodnaulica 4");
        DeleteByParams(wait,"SkuskaJózsef", "Gyula", "Ružemberok", "Moskovenská 2");
        DeleteByParams(wait,"SkuskaJóžef", "Ďula", "Košice - Západ", "Letná 9");
        DeleteByParams(wait,"SkuskaJózsef", "Gyula", "Košice", "Kotbuská 8");
        DeleteByParams(wait,"SkuskaJóžef", "Ďula", "Košice", "Tomášikova 4");
    }
    @Test
    public void verifyCreateAndDelete(){
        var expectedURL = "http://localhost:8080/createnew.html";
        WebElement nameInput;
        WebElement create;
        WebElement ulicaInput;
        WebElement obecInput;
        WebElement priezviskoInput;

        driver.navigate().to("http://localhost:8080/createnew.html");
        //assert driver.getTitle().equals("Submit Data to JPA Server");
        nameInput = driver.findElement(By.id("meno"));
        priezviskoInput = driver.findElement(By.id("priezvisko"));
        obecInput = driver.findElement(By.id("obec"));
        ulicaInput = driver.findElement(By.id("ulica"));
        create = driver.findElement(By.className("item-button"));

        nameInput.sendKeys("");
        priezviskoInput.sendKeys("");
        obecInput.sendKeys("");
        ulicaInput.sendKeys("");
        create.click();
        assert expectedURL.equals(driver.getCurrentUrl()); //kontrola prázdneho vstupu

        nameInput.sendKeys("  ");
        priezviskoInput.sendKeys("  ");
        obecInput.sendKeys("  ");
        ulicaInput.sendKeys("   ");
        create.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(50) /*timeout in seconds*/);
        assert wait.until(ExpectedConditions.alertIsPresent())!=null; //kontrola vstupu iba s medzerami
        driver.switchTo().alert().accept();

        driver.navigate().to("http://localhost:8080/createnew.html"); // vytvorenie záznamu
        var searchURL = "http://localhost:8080/searchResults.html";
        //assert driver.getTitle().equals("Submit Data to JPA Server");
        nameInput = driver.findElement(By.id("meno"));
        priezviskoInput = driver.findElement(By.id("priezvisko"));
        obecInput = driver.findElement(By.id("obec"));
        ulicaInput = driver.findElement(By.id("ulica"));
        create = driver.findElement(By.className("item-button"));
        nameInput.sendKeys("Tomáš");
        priezviskoInput.sendKeys("Pietrík");
        obecInput.sendKeys("Ružomberok");
        ulicaInput.sendKeys("Moskovská 2");
        create.click();
        wait = new WebDriverWait(driver, Duration.ofMillis(5000));
        wait.until(ExpectedConditions.titleIs("Results"));
        assert searchURL.equals(driver.getCurrentUrl());

        var change = driver.findElement(By.className("changeBtn")); //vymazanie vytvoreného záznamu
        change.click();
        wait.until(ExpectedConditions.titleIs("Selected Record"));
        var delete = driver.findElement(By.className("item-button"));
        delete.click();
        driver.switchTo().alert().accept();
        wait.until(ExpectedConditions.titleIs("Submit Data to JPA Server"));
        assert "http://localhost:8080/index.html".equals(driver.getCurrentUrl());
    }
    @Test
    public void updateTest(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(500));
        CreateByParams("Fehervári", "Kováč", "Ružomberok", "Vymyslená 2");
        findByParams("Fehervári", "Kováč", "Ružomberok", "Vymyslená 2","");
        var edit = driver.findElement(By.cssSelector("tbody tr:first-child td.rowBorderEnd button.changeBtn"));
        edit.click();
        wait.until(ExpectedConditions.titleIs("Selected Record"));
        var id = driver.findElement(By.id("nanoId")).getText();
        WebElement nameInput = driver.findElement(By.id("meno"));
        WebElement priezviskoInput = driver.findElement(By.id("priezvisko"));
        WebElement obecInput = driver.findElement(By.id("obec"));
        WebElement ulicaInput = driver.findElement(By.id("ulica"));
        nameInput.sendKeys("skuskaMeno");
        priezviskoInput.sendKeys("skuskaPriezvisko");
        var changeRecordBtn = driver.findElement(By.cssSelector("change-button"));
        changeRecordBtn.click();
        findByParams("", "", "", "",id);
        var text = driver.findElements(By.cssSelector("tbody tr:nth-child(1) td.match")).isEmpty();
        assert !text;
        text = driver.findElements(By.cssSelector("tbody tr:nth-child(2) td.match")).isEmpty();
        assert text;
        DeleteByParams(wait, "Fehervári", "Kovács", "Ružomberok", "Vymyslená 2");
        DeleteByParams(wait, "skuskaMeno", "skuskaPriezvisko", "Ružomberok", "Vymyslená 2");
    }
    @Test
    public void verifySearch(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(500));
        CreateByParams("Fehervári", "Kováč", "Ružomberok", "Vymyslená 2");
        CreateByParams("Fehervári", "Kovács", "Ružomberok", "Vimislená 2");

        findByParams("Fehervári", "kOVÁč","Ružomberok","Vymyslená 2","");
        var match =  Integer.parseInt(driver.findElement(By.cssSelector("tbody tr:first-child td.match")).getText());
        var match2 = Integer.parseInt(driver.findElement(By.cssSelector("tbody tr:nth-child(2) td.match")).getText());
        assert match == 100;
        assert match2 == 73;
        DeleteByParams(wait, "Fehervári", "Kovács", "Ružomberok", "Vimislená 2");
        DeleteByParams(wait, "Fehervári", "Kovács", "Ružomberok", "Vimislená 2");

        CreateByParams("Tomasz", "Gyula", "Košice", "Moskovská 2");
        CreateByParams("Tomáš", "Ďula", "Kosice", "Moskovská 2/4");
        CreateByParams("Tomáš", "Ďula", "Stará Ľubovňa", "Moskovská 2/4");

        findByParams("Tomasz", "Gyula","Košice","Moskovská 2","");
        match = Integer.parseInt(driver.findElement(By.cssSelector("tbody tr:first-child td.match")).getText());
        match2 = Integer.parseInt(driver.findElement(By.cssSelector("tbody tr:nth-child(2) td.match")).getText());
        assert match == 100;
        assert match2 == 55;
        DeleteByParams(wait,"Tomáš", "Ďula", "Levice", "Moskovská 2/4");
        DeleteByParams(wait, "Tomasz", "Gyula", "Košice", "Moskovská 2");
        DeleteByParams(wait, "Tomáš", "Ďula", "Kosice", "Moskovská 2/4");

        CreateByParams("Skuška", "Gyula", "Košice", "Moskovská 2");
        CreateByParams("Skuška", "Ďula", "Bratislava", "Moskovská 2");

        findByParams("Skuška", "Gyula","Košice","Moskovská 2","");
        match = Integer.parseInt(driver.findElement(By.cssSelector("tbody tr:first-child td.match")).getText());
        var text = driver.findElements(By.cssSelector("tbody tr:nth-child(2) td.match")).isEmpty();
        assert match == 100;
        assert text;
        DeleteByParams(wait, "Skuška", "Gyula", "Košice", "Moskovská 2");
        DeleteByParams(wait, "Skuška", "Ďula", "Bratislava", "Moskovská 2");


        CreateByParams("SkuskaJózsef", "Gyula", "Bratislava", "Moskovská 2");
        CreateByParams("SkuskaJóžef", "Ďula", "Košice", "nahodnaulica 4");
        CreateByParams("SkuskaJózsef", "Gyula", "Ružemberok", "Moskovenská 2");
        CreateByParams("SkuskaJóžef", "Ďula", "Košice - Západ", "Letná 9");
        CreateByParams("SkuskaJózsef", "Gyula", "Košice", "Kotbuská 8");
        CreateByParams("SkuskaJóžef", "Ďula", "Košice", "Tomášikova 4");

        findByParams("SkuskaJózsef", "Gyula","","","");
        text = driver.findElements(By.cssSelector("tbody tr:nth-child(6) td.match")).isEmpty();
        assert !text;
        text = driver.findElements(By.cssSelector("tbody tr:nth-child(7) td.match")).isEmpty();
        assert text;
        DeleteByParams(wait,"SkuskaJózsef", "Gyula", "Bratislava", "Moskovská 2");
        DeleteByParams(wait,"SkuskaJóžef", "Ďula", "Košice", "nahodnaulica 4");
        DeleteByParams(wait,"SkuskaJózsef", "Gyula", "Ružemberok", "Moskovenská 2");
        DeleteByParams(wait,"SkuskaJóžef", "Ďula", "Košice - Západ", "Letná 9");
        DeleteByParams(wait,"SkuskaJózsef", "Gyula", "Košice", "Kotbuská 8");
        DeleteByParams(wait,"SkuskaJóžef", "Ďula", "Košice", "Tomášikova 4");


        CreateByParams("SkuskaEszter", "Piroska", "Košice", "Moskovská 2");
        CreateByParams("SkuskaEster", "Piroška", "Košice", "Moskovská 2/32");

        findByParams("SkuskaEster", "Piroška","Košice","Moskovská 2","");
        match = Integer.parseInt(driver.findElement(By.cssSelector("tbody tr:first-child td.match")).getText());
        match2 = Integer.parseInt(driver.findElement(By.cssSelector("tbody tr:nth-child(2) td.match")).getText());
        assert match >= 80;
        assert match2 >= 80;
        DeleteByParams(wait,"SkuskaEszter", "Piroska", "Košice", "Moskovská 2");
        DeleteByParams(wait,"SkuskaEster", "Piroška", "Košice", "Moskovská 2/32");


        CreateByParams("Szilvia", "Nyúlová", "Košice", "Moskovská 2");
        var id = driver.findElement(By.cssSelector("tbody tr:first-child td.nanoId")).getText();
        findByParams("","","","",id);
        match = Integer.parseInt(driver.findElement(By.cssSelector("tbody tr:first-child td.match")).getText());
        var meno = driver.findElement(By.cssSelector("tbody tr:first-child td.meno")).getText();
        var priezvisko =driver.findElement(By.cssSelector("tbody tr:first-child td.priezvisko")).getText();
        assert match==100;
        assert meno.equals("Szilvia");
        assert priezvisko.equals("Nyúlová");
        DeleteByParams(wait,"Szilvia", "Nyúlová", "Košice", "Moskovská 2");
    }

    private void DeleteByParams(WebDriverWait wait, String name, String surname, String city, String street) {
        WebElement edit;
        WebElement delete;
        findByParams(name, surname,city,street,"");
        edit = driver.findElement(By.cssSelector("tbody tr:first-child td.rowBorderEnd button.changeBtn"));
        edit.click();
        wait.until(ExpectedConditions.titleIs("Selected Record"));
        delete = driver.findElement(By.className("item-button"));
        delete.click();
        driver.switchTo().alert().accept();
        wait.until(ExpectedConditions.titleIs("Submit Data to JPA Server"));

    }

    private void findByParams(String name, String surname, String city, String street, String id) {
        driver.navigate().to("http://localhost:8080/index.html");
        WebElement nameInput = driver.findElement(By.id("meno"));
        WebElement priezviskoInput = driver.findElement(By.id("priezvisko"));
        WebElement obecInput = driver.findElement(By.id("obec"));
        WebElement ulicaInput = driver.findElement(By.id("ulica"));
        WebElement idInput = driver.findElement(By.id("nanoId"));
        WebElement find = driver.findElement(By.className("item-button"));
        nameInput.sendKeys(name);
        idInput.sendKeys(id);
        priezviskoInput.sendKeys(surname);
        obecInput.sendKeys(city);
        ulicaInput.sendKeys(street);
        find.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(5000));
        wait.until(ExpectedConditions.titleIs("Results"));
    }

    private void CreateByParams(String name, String surname, String city, String street) {
        driver.navigate().to("http://localhost:8080/createnew.html");
        WebElement nameInput = driver.findElement(By.id("meno"));
        WebElement priezviskoInput = driver.findElement(By.id("priezvisko"));
        WebElement obecInput = driver.findElement(By.id("obec"));
        WebElement ulicaInput = driver.findElement(By.id("ulica"));
        WebElement create = driver.findElement(By.className("item-button"));
        nameInput.sendKeys(name);
        priezviskoInput.sendKeys(surname);
        obecInput.sendKeys(city);
        ulicaInput.sendKeys(street);
        create.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(5000));
        wait.until(ExpectedConditions.titleIs("Results"));
    }

    @AfterEach
    public void tearDown(){
        driver.quit();
    }
}
