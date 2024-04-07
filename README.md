# JPDB Manager

My JPDB workflows but automated. I used java and selenium (don't judge me, its what I'm forced to learn rn at my uni)

## Requirements:

- Java v17+
- Mozilla Firefox Web Brorwser
- Knowledge of how to run java code (I might learn how to make an executable later)

## How to use:

1. Go to the [jpdb website](https://jpdb.io/) and log in
2. Get your session id from the cookies
3. Create a new empty deck and leave it in the first position
4. Identify the id of the deck you want to mine cards from (it's in the link)
5. Open the [Main.java](./src/main/java/com/davidbonelo/Main.java) file, there you can change the variables at the top of the main method to include your session id, deck id, increase the limit of new cards to mine, and set a max global frequency limit.
6. Run the main method.
7. Enjoy, and study.

## Workflows explanation

After switching strategies for a while, at my current level (N4~N3) I'm mostly using one so for now it it's the default when you run the main method.

### Mine not unique words in a deck following the global frequency order

As the title says, the idea is to study words from a deck that are useful for a specific deck but at the same time follow the global frequency order so the words are also common in other decks.

