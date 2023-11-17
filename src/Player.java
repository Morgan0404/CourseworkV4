


import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

//Implements Runnable interface, indicating it can be executed by a thread. 
public class Player implements Runnable{
    
    //Marked as volatile to ensure visibility across threads
    private volatile int playerNumber;
    
    //List of Card objects, marked as volatile for thread safety
    public volatile List<Card> hand;


    public Player(int playerNumber, List<Card> hand ){
        this.playerNumber = playerNumber;
        this.hand = new CopyOnWriteArrayList<>(hand);//used for thread safety
    }



    public List<Card> getHand(){
        return hand;
    }




    public synchronized void discardCard(int playerNumber, CardDeck gameDecks, int numberOfPlayers,List<Player> players){
        
        if (playerNumber < 0 || playerNumber >= numberOfPlayers) {
            System.out.println("Invalid player index. Please check your code logic.");
            return;
        }
        
        int preferredNumber = playerNumber;
        
        synchronized(hand){
            int handSize = hand.size(); //size of players hand

        //iterate through players hand
        for(int i = 0; i <handSize;i++){

            //Check if the current card's value is not equal to preffered number
            if(hand.get(i).getValue() != preferredNumber){


                Card removedCard = hand.get(i);//Retrieve the card to be removed from hand 
                
                //checks if they are last player in the game, -1 because player number starts at 1
                if(playerNumber == numberOfPlayers-1){ 
                    gameDecks.addCardToDeck(1, removedCard);
                    hand.remove(i);
                    i--;
                    break;

                }else{
                 //This is where the palyer adds there card to the player deck in front of them   
                gameDecks.addCardToDeck(playerNumber+1, removedCard);
                hand.remove(i);
                i--;
                break;}


                }
            
        }

    } 
        }

        


    public synchronized void drawCard(int playerNumber, CardDeck gameDecks){
        //Get the last card from the specified deck using the gameDecks object
        Card drawnCard = gameDecks.getLastCardFromDeck(playerNumber, gameDecks);
        
        //Print the drawn card for debugging purposes
        System.err.println(drawnCard);

        //Assign the player's hand to a local variable for convenience
        List<Card> drawnCardDestination = hand;

        // Add the drawn card to the players hand 
        drawnCardDestination.add(drawnCard);

    }








    @Override
    public void run(){
        while (!Thread.interrupted()) {

            //discard and draw

            //checkwin and notify 
            discardCard(playerNumber, CardGame.allDecks, playerNumber, CardGame.players);

            drawCard(playerNumber, CardGame.allDecks);

            // Check if the player wins
            if (CardGame.checkWin(playerNumber,CardGame.players)){
                // Notify other players of the win
                synchronized (CardGame.class) {
                    CardGame.class.notifyAll();
                }
                break; // Exit the loop if the player wins
            }
        }
    }    
    


    //get player from players list in cardgame
    //get player.hand 


}


