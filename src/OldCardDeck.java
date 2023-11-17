


import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OldCardDeck {

    // Declare a instance variable 'deck' as a HashMap
    public Map<Integer, List<Integer>> deck;

    // Static instance of CardDeck accessible throughout the class
    public static CardDeck decks = new CardDeck();

    // Constructor to initialize the deck as a HashMap
    public CardDeck() {
        this.deck = new HashMap<>();
    }

    // Method to add a player to the deck
    public void addPlayerDeck(int playerId) {
        deck.put(playerId, new ArrayList<>());
    }

    // Method to add a card to a specific player
    public void addCardToDeck(int playerId, int card) {
        if (deck.containsKey(playerId)) {
            deck.get(playerId).add(card);
        } else {
            System.out.println("Player ID not found in the deck.");
        }
    }

    // Method to get the entire deck
    public Map<Integer, List<Integer>> getDeck() {
        return deck;
    }

    // Static method to populate decks for the given number of players and card pack
    public void populateDecks(int numberOfPlayers, List<Integer> cardPack) {
        // Add players to the deck
        for (int i = 1; i <= numberOfPlayers; i++) {
            addPlayerDeck(i);
        }
    
        // Distribute cards to players in a round-robin fashion
        for (int i = 0; i < 4; i++) {
            for (int currentDeck = 1; currentDeck <= numberOfPlayers; currentDeck++) {
                addCardToDeck(currentDeck, cardPack.remove(0));
            }
        }
    }

    public List<Integer> get(int playerId) {
        return deck.get(playerId);
    }



    public int getLastCardFromDeck(int playerNumber, CardDeck deck) {
        List<Integer> currentDeck = deck.get(playerNumber);
            int lastIndex = currentDeck.size() - 1;
            int lastCard = currentDeck.get(lastIndex);
            currentDeck.remove(lastIndex); // Remove the last card
            return lastCard;

    }
           
    

    // Method (not implemented) to add a card to a specific player
    public void put(int playerId, int tempCard) {
        // Implementation to add a card to a specific player can be added here if needed
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, List<Integer>> entry : deck.entrySet()) {
            int playerId = entry.getKey();
            List<Integer> cards = entry.getValue();
            sb.append("Deck ").append(playerId).append(": ").append(cards).append("\n");
        }
        return sb.toString();
    }
}

 {
    
}
