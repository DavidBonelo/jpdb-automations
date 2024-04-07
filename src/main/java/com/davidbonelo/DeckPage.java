package com.davidbonelo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class DeckPage {
    protected WebDriver driver;

    private final By cardElementBy = By.cssSelector(".entry.new");
    private final By nextPageBy = By.linkText("Next page");

    public DeckPage(WebDriver driver) {
        this.driver = driver;
        if (!driver.getTitle().equals("Deck contents – jpdb")) {
            throw new IllegalStateException("Couldn't find deck at url: " + driver.getCurrentUrl());
        }
    }

    public List<VocabCard> getVocabList() {
        var cards = driver.findElements(cardElementBy);
        return cards.stream().map(VocabCard::new).toList();
    }

    public DeckPage goToNextPage() {
        driver.findElement(nextPageBy).click();
        return new DeckPage(driver);
    }
}
