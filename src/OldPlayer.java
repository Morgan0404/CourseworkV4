

import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class OldPlayer implements Runnable {
    public int playerNumber; //not same as num of players,is index of thread
    public Map<Integer, List<Integer>> hands = new HashMap<>();



    //constructor to initialise the player with a player number
    public Player(int playerNumber, Map<Integer, List<Integer>> hands ){
        this.playerNumber = playerNumber;
        this.hands = hands;
    }
    
    
    public void drawCard(int playerNumber, CardDeck gameDecks){
        int drawnCard = gameDecks.getLastCardFromDeck(playerNumber, gameDecks);
        List<Integer> drawnCardDestination = hands.get(playerNumber);
        drawnCardDestination.add(drawnCard);

    }




    public void discardCard(int playerNumber, CardDeck gameDecks, int numberOfPlayers){
        int preferredNumber = playerNumber;


        for(int i = 0; i < 4; i++){
            if(hands.get(playerNumber).get(i) != preferredNumber){
                int removedCard = hands.get(playerNumber).get(i);

                if(playerNumber == numberOfPlayers){
                    gameDecks.addCardToDeck(1, removedCard);
                    hands.get(playerNumber).remove(i);
                    break;

                }else{
                gameDecks.addCardToDeck(playerNumber + 1, removedCard);
                hands.get(playerNumber).remove(i);
                break;}


                }
            
        }

    } 

    public boolean checkWin(int playerNumber) {
        List<Integer> tempHand = hands.get(playerNumber);
        for (Integer s : tempHand) {
            if (!s.equals(tempHand.get(0)))
                return false;
        }
        System.out.println("Winner");
        return true;
    }
 
    
    
    
    public void run(){
        //code to be executed by the players thread
        System.out.println("Player "+ playerNumber + " is running. ");

        drawCard(playerNumber, null);
        




    }




}
   
    
