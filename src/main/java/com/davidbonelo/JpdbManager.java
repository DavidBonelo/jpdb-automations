package com.davidbonelo;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

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

    DeckPage navigateToDeck(String deckId) {
        driver.get("https://jpdb.io/deck?id=" + deckId + "&sort_by=by-frequency-global&show_only" + "=new");
        return new DeckPage(driver);
    }

    public void mineNotUniquesInDeck(DeckPage deckPage) throws InterruptedException {
        List<VocabCard> cards = deckPage.getVocabList();
        System.out.println("cards found: " + cards.size());

        try {
            while (addedCount < newCardsLimit) {
                addNotUniqueCards(cards);
                if (addedCount >= newCardsLimit) break;
                deckPage = deckPage.goToNextPage();
                cards = deckPage.getVocabList();
            }
        } catch (Exception e) {
            Thread.sleep(Duration.ofSeconds(5));
            System.out.println(e.getMessage());
        } finally {
            System.out.println("Cards added: " + addedCount);
        }
    }

    public void addNotUniqueCards(List<VocabCard> cards) throws Exception {
        for (VocabCard card : cards) {
            if (addedCount >= newCardsLimit) break;
            if (card.getGlobalFreq() >= maxGlobalFreq) {
                throw new Exception("No more cards under maxGlobalFreq: " + maxGlobalFreq);
            }
            card.printDetails();

            if (card.isUniqueInDeck()) continue;
            boolean wasAdded = card.addToFirstDeck();
            if (!wasAdded) continue;

            addedCount++;
            System.out.println("Card added, addedCount: " + addedCount);
            Thread.sleep(Duration.ofSeconds(3)); // pause to avoid ban
        }
    }
}