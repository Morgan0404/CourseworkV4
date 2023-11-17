


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Collections;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CardDeck {
    
    private final Map<Integer, List<Card>> deck;


    public CardDeck() {
        this.deck = new ConcurrentHashMap<>();
    }

    public void addPlayerDeck(int playerNumber) {
        deck.put(playerNumber, Collections.synchronizedList(new ArrayList<>()));
    }

    public void addCardToDeck(int playerId, Card card) {
        List<Card>playerDeck = deck.get(playerId);
        if (playerDeck != null) {
            playerDeck.add(card);
        } else {
            System.out.println("Player ID not found in the deck.");
        }
    }

    /*public Map<Integer, List<Integer>> getDeck() {
        return deck;
    }*/
    //initially cardpack is a list of integers representing card values (0,12,3..)
    //populate iterates over them 4 times (so player has 4 cards in deck)
    //each iteration removes it from cardPack, creates a card object with that values and adds Card to player deck
    public void populateDecks(int numberOfPlayers, List<Card> cardPack) {
        for (int i = 1; i <= numberOfPlayers; i++) {
            addPlayerDeck(i);
        }
    
        for (int i = 0; i < 4; i++) {
            for (int currentDeck = 1; currentDeck <= numberOfPlayers; currentDeck++) {
                int cardValue = cardPack.remove(0).getValue();
                Card card = new Card(cardValue);
                addCardToDeck(currentDeck, card);
            }
        }
    }

    public List<Card> getPlayerDeck(int playerId) {
        List<Card> playerDeck = deck.get(playerId);
        return playerDeck != null ? Collections.unmodifiableList(playerDeck) : Collections.emptyList();
    }
    

    

    public Card getLastCardFromDeck(int playerNumber, CardDeck deck) {
        List<Card> currentDeck = deck.getPlayerDeck(playerNumber);
        if (!currentDeck.isEmpty()) {
            int lastIndex = currentDeck.size() - 1;
            Card lastCard = currentDeck.get(lastIndex);
            currentDeck.remove(lastIndex); // Remove the last card
            return lastCard;
        } else {
            System.out.println("Player deck is empty or not found.");
            return null;

        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, List<Card>> entry : deck.entrySet()) {
            int playerId = entry.getKey();
            List<Card> cards = entry.getValue();
            sb.append("Deck ").append(playerId).append(": ");
            for(Card card: cards){
                sb.append(card.getValue()).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

}


