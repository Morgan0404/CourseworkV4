import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Player3 extends Thread {
    private final int playerIndex;
    private final List<Card> playerHand;
    private final int preferredDenomination;
    private final String outputFile;
    private final CardDeck leftDeck;
    private final CardDeck rightDeck;
    private volatile boolean running = true;
    private static volatile int winnerIndex = -1;
    private List<Player> allPlayers;

    public Player(int playerIndex, CardDeck leftDeck, CardDeck rightDeck) {
        this.playerIndex = playerIndex;
        this.preferredDenomination = playerIndex;
        this.outputFile = "player" + playerIndex + "_output.txt";
        this.leftDeck = leftDeck;
        this.rightDeck = rightDeck;
        this.playerHand = new ArrayList<>(4);
        this.allPlayers = new ArrayList<>();

    }

    @Override
    public void run() {
        List<Integer> handList = new ArrayList<>();
        for(Card card : playerHand){
            handList.add(card.getFaceValue());
        }
        writeToOutputFile("Player " + playerIndex + " initial hand: " + handList);
        try {
            while (running && !Thread.currentThread().isInterrupted()) {
                Card drawnCard = drawCard();
                if (drawnCard != null) {
                    playerHand.add(drawnCard);
                    writeToOutputFile("Player " + playerIndex + " draws a " + drawnCard.getFaceValue() + " from deck " + leftDeck.getDeckIndex());
                    Card discardedCard = discardCard();
                    if (discardedCard != null) {
                        writeToOutputFile("Player " + playerIndex + " discards a " + discardedCard.getFaceValue() + " to deck " + rightDeck.getDeckIndex());
                    }
                }
        
                if (hasWinningHand()) {
                    winnerIndex = this.playerIndex; // assigning to the winner's index.
                    writeToOutputFile("Player " + playerIndex + " wins");
                    CardGame.declareVictory(); // CardGame class method logic that should output the winner to terminal
                    break;
                }
             
                if (winnerIndex != -1) {
                    // for all the other players 
                    if (this.playerIndex != winnerIndex) {
                        handList = new ArrayList<>();
                        for(Card card : playerHand){
                            handList.add(card.getFaceValue());
                        }
                        writeToOutputFile("Player " + winnerIndex + " has informed player " + playerIndex + " that player " + winnerIndex + " has won");
                        writeToOutputFile("Player " + playerIndex + " exits");
                        writeToOutputFile("Player " + playerIndex + " hand: " + handList);
                    }
                    break;
                }
            }
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        } finally {
            if (this.playerIndex == winnerIndex) {
                List<Integer> winHand = new ArrayList<>();
                for(Card card : playerHand){
                    winHand.add(card.getFaceValue());
                }
                writeToOutputFile("Player " + playerIndex + " final hand: " + winHand);
            }
            writeToOutputFile("Player " + playerIndex + " exits");
        }
    }

    private Card drawCard() {
        synchronized (leftDeck) {
            if (!leftDeck.isEmpty()) {
                return leftDeck.removeCard();
            }
        }
        return null;
    }

    private Card discardCard() {
        List<Card> nonPreferredCards = new ArrayList<>();
        for (Card card : playerHand) {
            if (card.getFaceValue() != preferredDenomination) {
                nonPreferredCards.add(card);
            }
        }
        if (!nonPreferredCards.isEmpty()) {
            Card cardToDiscard = nonPreferredCards.get(ThreadLocalRandom.current().nextInt(nonPreferredCards.size()));
            playerHand.remove(cardToDiscard);
            synchronized (rightDeck) {
                rightDeck.addCard(cardToDiscard);
            }
            return cardToDiscard;
        }
        return null;
    }

    private boolean hasWinningHand() {
        if (playerHand.size() < 4) {
            return false;
        }
        int firstCardFaceValue = playerHand.get(0).getFaceValue();
        for (Card card : playerHand) {
            if (card.getFaceValue() != firstCardFaceValue) {
                return false;
            }
        }
        Player.setWinnerIndex(this.playerIndex);
        return true;
    }

    private void writeToOutputFile(String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile, true))) {
            writer.write(content);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // For CardGame class to check 
    public static boolean isGameWon() {
        return winnerIndex != -1;
    }

    // For CardGame class to retrieve the player that won and output to terminal.
    public static int getWinnerIndex() {
        return winnerIndex;
    }

    public static void setWinnerIndex(int num){winnerIndex = num;}

    public List<Player> getPlayers() {
        return allPlayers;
    }

    public void setPlayers(List<Player> players){
        this.allPlayers = players;
    }

    public void addCard(Card card){
        this.playerHand.add(card);
    }   
}
