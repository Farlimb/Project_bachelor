package com.example.bakalarka_jpa;
import org.junit.Assert;
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

    @Test
    public void verifyCreateAndDelete(){
        var expectedURL = "http://localhost:8080/createnew.html";
        driver.navigate().to("http://localhost:8080/createnew.html");
        //assert driver.getTitle().equals("Submit Data to JPA Server");
        WebElement nameInput = driver.findElement(By.id("meno"));
        WebElement priezviskoInput = driver.findElement(By.id("priezvisko"));
        WebElement obecInput = driver.findElement(By.id("obec"));
        WebElement ulicaInput = driver.findElement(By.id("ulica"));
        WebElement create = driver.findElement(By.className("item-button"));
        nameInput.sendKeys("");
        priezviskoInput.sendKeys("");
        obecInput.sendKeys("");
        ulicaInput.sendKeys("");
        create.click();
        assert expectedURL.equals(driver.getCurrentUrl()); //kontrola prázdneho vstupu

        driver.navigate().to("http://localhost:8080/createnew.html");
        //assert driver.getTitle().equals("Submit Data to JPA Server");
        nameInput = driver.findElement(By.id("meno"));
        priezviskoInput = driver.findElement(By.id("priezvisko"));
        obecInput = driver.findElement(By.id("obec"));
        ulicaInput = driver.findElement(By.id("ulica"));
        create = driver.findElement(By.className("item-button"));
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
    public void verifySearch(){
        driver.navigate().to("http://localhost:8080/createnew.html");
        WebElement nameInput = driver.findElement(By.id("meno"));
        WebElement priezviskoInput = driver.findElement(By.id("priezvisko"));
        WebElement obecInput = driver.findElement(By.id("obec"));
        WebElement ulicaInput = driver.findElement(By.id("ulica"));
        WebElement create = driver.findElement(By.className("item-button"));
        nameInput.sendKeys("Tomáš");
        priezviskoInput.sendKeys("Pietrík");
        obecInput.sendKeys("Ružomberok");
        ulicaInput.sendKeys("Moskovská 2");
        create.click();


    }
    @AfterEach
    public void tearDown(){
        driver.quit();
    }
}
