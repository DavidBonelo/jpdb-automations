package com.davidbonelo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class VocabCard {
    private final By globalFreqLocator = By.cssSelector(".tag.tooltip");
    private final By deckFreqLocator =
            By.cssSelector(".entry > div:nth-child(2) > div:nth-child" + "(2)");
    private final By optionsButtonLocator = By.cssSelector(".dropdown");
    private final By addToDeckBtnLocator = By.cssSelector("input[type=submit]");
    private final By wordLinkLocator = By.cssSelector(".vocabulary-spelling a");
    WebElement root;

    VocabCard(WebElement root) {
        this.root = root;
    }

    private String getWord() {
        // Decided to get the word using the link because the textContent contains the
        // pronunciation, and it was a pain to try to remove it with selenium apis
        String wordLink = root.findElement(wordLinkLocator).getAttribute("href");
        String word = wordLink.substring(wordLink.lastIndexOf("/") + 1, wordLink.lastIndexOf("#"));
        return URLDecoder.decode(word, StandardCharsets.UTF_8);
    }

    public int getGlobalFreq() {
        String freq = root.findElement(globalFreqLocator).getText().replace("Top ", "");
        return Integer.parseInt(freq);
    }

    public int getDeckFrequency() {
        String freq = root.findElement(deckFreqLocator).getText();
        return Integer.parseInt(freq);
    }

    /**
     * @return `true` if the card was successfully added, `false` if the card was already in the
     * first deck
     */
    public boolean addToFirstDeck() {
        // Open card options menu
        WebElement optionsBtn = root.findElement(optionsButtonLocator);
        optionsBtn.click();

        WebElement addToDeckBtn = optionsBtn.findElement(addToDeckBtnLocator);
        // Verify card hasn't been already added
        if (!addToDeckBtn.getAttribute("value").contains("Add to:")) {
            System.out.println("Already in deck, skipping this card");
            // click outside the popup to close it
            root.click();
            return false;
        }

        addToDeckBtn.click();
        return true;
    }

    public boolean isUniqueInDeck() {
        return getDeckFrequency() < 2;
    }

    public void printDetails() {
        System.out.println("Card: " + getWord() + ", globalFreq: " + getGlobalFreq() + ", " +
                "deckFreq: " + getDeckFrequency());
    }

}
