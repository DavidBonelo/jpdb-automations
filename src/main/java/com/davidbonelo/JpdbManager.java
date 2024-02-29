package com.davidbonelo;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.time.Duration;
import java.util.List;

public class JpdbManager {
    WebDriver driver;
    int newCardsLimit;
    int maxGlobalFreq;
    int addedCount = 0;

    public JpdbManager(WebDriver driver, int newCardsLimit, int maxGlobalFreq) {
        this.driver = driver;
        this.newCardsLimit = newCardsLimit;
        this.maxGlobalFreq = maxGlobalFreq;
    }

    public void login(String jpdbSessionId) throws Exception {
        driver.get("https://jpdb.io");
        Cookie sid = new Cookie("sid", jpdbSessionId);
        driver.manage().addCookie(sid);
        driver.get("https://jpdb.io/learn");
        if (!driver.getTitle().equals("Learn – jpdb")) {
            throw new Exception("Couldn't log in, verify the jpdbSessionId");
        }
    }

    void navigateToDeck(String deckId) throws Exception {
        driver.get("https://jpdb.io/deck?id=" + deckId + "&sort_by=by-frequency-global&show_only" + "=new");
        if (!driver.getTitle().equals("Deck contents – jpdb")) {
            throw new Exception("Couldn't find deck with id: " + deckId);
        }
    }

    private void navigateToNextPage() {
        System.out.println("Next page");
        driver.findElement(By.linkText("Next page")).click();
    }

    private List<WebElement> getCards() {
        return driver.findElements(By.cssSelector(".entry.new"));
    }

    public void start() throws InterruptedException {
        List<WebElement> cards = getCards();
        System.out.println("cards found: " + cards.size());

        try {
            while (addedCount < newCardsLimit) {
                addCards(cards);
                if (addedCount >= newCardsLimit) break;
                navigateToNextPage();
                cards = getCards();
            }
        } catch (Exception e) {
            Thread.sleep(10);
            System.out.println(e.getMessage());
        } finally {
            System.out.println("Cards added: " + addedCount);
        }

    }

    public void addCards(List<WebElement> cards) throws Exception {
        Actions actions = new Actions(driver);
        for (WebElement card : cards) {
            if (addedCount >= newCardsLimit) break;
            actions.moveToElement(card);
//            By.xpath("/div[2]/div[2]")
            String globalFreq =
                    card.findElement(By.cssSelector(".tag.tooltip")).getText().replace("Top ", "");
            if (Integer.parseInt(globalFreq) >= maxGlobalFreq) {
                throw new Exception("No more cards under maxGlobalFreq: " + maxGlobalFreq);
            }

            String deckFreq = card.findElement(By.cssSelector(".entry > div:nth-child(2) > " +
                    "div:nth-child(2)")).getText();

            System.out.println("card with globalFreq: " + globalFreq + " and deckFreq: " + deckFreq);

            if (Integer.parseInt(deckFreq) < 2) continue;
//            By.xpath("/div[2]/div[1]")
            WebElement optionsBtn = card.findElement(By.cssSelector(".dropdown"));
            optionsBtn.click();

            WebElement addToDeckBtn = optionsBtn.findElement(By.cssSelector("input[type=submit]"));
            // Verify card hasn't been already added
            if (!addToDeckBtn.getAttribute("value").contains("Add to:")) {
                System.out.println("Already in deck, skipping this card");
                // click outside the popup to close it
                card.click();
                continue;
            }

            addToDeckBtn.click();
            addedCount++;
            System.out.println("Card added, addedCount: " + addedCount);
            Thread.sleep(Duration.ofSeconds(3));
        }
    }
}