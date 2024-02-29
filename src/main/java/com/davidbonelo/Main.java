package com.davidbonelo;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.time.Duration;

public class Main {
    public static void main(String[] args) {
        String jpdbSessionId = "YOUR_SESSION_TOKEN_GOES_HERE";
        String deckId = "78";
        int newCardsLimit = 2;
        int maxGlobalFreq = 10000;

        WebDriver driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        JpdbManager jpdb = new JpdbManager(driver, newCardsLimit, maxGlobalFreq);

        try {
            jpdb.login(jpdbSessionId);
            jpdb.navigateToDeck(deckId);
            jpdb.start();
            Thread.sleep(Duration.ofSeconds(5));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }

    }
}