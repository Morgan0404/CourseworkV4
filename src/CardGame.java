

import java.io.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.FileHandler;
import javax.print.DocFlavor.STRING;
import javax.swing.plaf.TreeUI;

public class CardGame{
    private static Scanner scanner = new Scanner(System.in);
    //public static volatile List <Player> players = new CopyOnWriteArrayList<>();
    public static List <Player> players = new CopyOnWriteArrayList<>();

    public static List<Card> cardPack = new ArrayList<>();
    public static CardDeck allDecks = new CardDeck();

    //private static volatile boolean shouldExit = false;

    //players list of 
    
   
    //inpiut or generate file 
public static void packGeneration(int numberOfPlayers,String fileName){
        
           try{
               //Create a FileWriter and buffered to write to the new file
               FileWriter fileWriter = new FileWriter(fileName);
               BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
   
               //create a random object to generate random numbers
               Random random = new Random();
   
               //calculate the total number of random numbers the particular pack will need
               int totalNumbers = 8 * numberOfPlayers;
              
   
               //Generate and write rnadom numbers to the file
               for (int i = 0; i < totalNumbers; i++){
                   int cardValue = random.nextInt(15); //generates random numbers betweem 0 and 15
                   bufferedWriter.write(Integer.toString(cardValue));//writting random number as string
                  
                   bufferedWriter.newLine();
               }
   
               //closing the buffredWriter and FileWriter to save changes and release resources
               bufferedWriter.close();
               fileWriter.close();

            } catch(IOException e ){
               e.printStackTrace();}//print any exception that may occur when trying to run this method
           }
    
public static String getPackLocation(){

        String locationOfPack = null;
        boolean validPackLocation = false;

        while (!validPackLocation) {
            try {
                System.out.println("Please enter the location of pack to load: ");
                locationOfPack = scanner.next();//reads user as input
                System.out.println("Chosen Pack: " + locationOfPack);
                validPackLocation = true; // Input is valid; exit the loop
            } catch (Exception e) {
                //handle exceptions if the user enters an invalid input
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.next(); // Consume the invalid input
            }
        }
        return locationOfPack;
    }

public static int getPlayerNum() {
        //Initializing variables to store the number of players and track input validity
        int numberOfPlayers = 0;
        boolean validInput = false;

        //Keep prompting the user until valid number entered
        while (!validInput) {
            try {
                System.out.print("Please enter the number of players: ");
                numberOfPlayers = scanner.nextInt();//reads user as input
                System.out.println("You entered: " + numberOfPlayers);
                validInput = true; // Input is valid; exit the loop
            } catch (Exception e) {
                //handle exceptions if the user enters an invalid input
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.next(); // Consume the invalid input
            }
        }
        return numberOfPlayers;
    }

//This is how we create new player instances and add to list of player objects 
 public static void createPlayers(int numberOfPlayers) {
        //Grabs number of players

        //Creates a loop that iterates through each player
        for (int i = 1; i<= numberOfPlayers; i++){
            List<Card>emptyPlayerHand = new ArrayList<>()  ;
            //Create a player instance for the current player
            Player player = new Player(i, emptyPlayerHand);
            players.add(player);

        }
         
    }

public static void packDistribution(List<Card> cardPack, int numberOfPlayers, String LocationOfPack) {

        //need to acc create players so call createplayers method
        createPlayers(numberOfPlayers);

        System.err.println("players size inside pack distribution: "+ players.size());
        

        try (BufferedReader reader = new BufferedReader(new FileReader(LocationOfPack))){
            String line;
            while ((line = reader.readLine()) != null) {
                int cardValue = Integer.parseInt(line.trim()); //trim() removes whitespaces and end of line symbols. parseInt() parses a string to an Int
                Card card = new Card(cardValue);
                cardPack.add(card);

        }}catch(IOException e) {
            System.out.println("Invalid pack file. Please provide a valid pack file.");
        }

        for (int i = 0; i < 4; i++) {
            for (int currentPlayer = 0; currentPlayer < numberOfPlayers; currentPlayer++) {
                if (currentPlayer < players.size() && !cardPack.isEmpty()) {
                    List<Card>CurrentPlayerHand = players.get(currentPlayer).hand;
                    if (!cardPack.isEmpty()) {
                        CurrentPlayerHand.add(cardPack.remove(0));
                    }
                }
            }
        }
    }

public static void showPlayersHands(int numberOfPlayers){
    for (int i = 0 ; i <= numberOfPlayers-1; i++){
        System.err.println(players.get(i).getHand());
    }
}  


public List<Player>getPlayers(){
    return players;
}

/*public static boolean checkWin(int playerNumber) {
    List<Integer> tempHand = players.get(playerNumber).getHand();
    for (Integer s : tempHand) {
        if (!s.equals(tempHand.get(0)))
            return false;
    }
    System.out.println("Winner");
    return true;
}*/

//checkwin 2
public static boolean checkWin(int playerIndex,List <Player> players) {
    playerIndex =playerIndex-1;
    //check if the player index is within bounds
    /*if (playerIndex < 0 || playerIndex >= players.size()) {
        System.out.println("Invalid player index");
        System.out.println("player index:"+ playerIndex);
        System.err.println("players size"+ CardGame.players.size());
    
        return false;
    }*/
    System.out.println("players size inside check win cardgame: "+ CardGame.players.size());
    Player currentPlayer = players.get(playerIndex);

     // Check if the player's hand is empty
    List<Card> tempHand = currentPlayer.getHand();
    if (tempHand == null || tempHand.isEmpty()) {
        System.out.println("Player " + (playerIndex) + " hand is empty");
        return false;
    }

    

   
    int firstCardValue = tempHand.get(0).getValue();

        for (Card card : tempHand) {
            if (card == null ||card.getValue() != firstCardValue) {
                return false;
            }
        }

        System.out.println("Player " + (playerIndex + 1) + " Wins");
        return true;
   
        // Handle appropriately if the player's hand is empty
        
    
}
public static void startPlayerThreads(List<Player> players) {
    // Create player threads
    List<Thread> playerThreads = new ArrayList<>();
    for (Player player : players) {
        Thread playerThread = new Thread(player);
        playerThreads.add(playerThread);
        playerThread.start();
    }

    // Wait for any player to win
    synchronized (CardGame.class) {
        try {
            CardGame.class.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Interrupt all player threads
    for (Thread thread : playerThreads) {
        thread.interrupt();
    }

    // Continue with any cleanup or final actions
}


public static void simulateBasicRound2(int numberOfPlayers, CardDeck gameDecks) {
        
    
    boolean gameWon = false;

    while (!gameWon) {
        for (int i = 0; i<numberOfPlayers; i++) {

            int currentPlayer = i+1;
            System.out.println(currentPlayer);
            List<Card> CurrentHand = players.get(i).getHand();
            
            //String playerFilePath = "player" + currentPlayer + "_output.txt";

            //initial hand output
            // Initialize the output file with the initial hand
            //writeToTextFile(playerFilePath, "initial hand", true);

            //List<Integer>tempHand = players.get(currentPlayer).getHand();

           
            
            
            //Discard card output
            System.out.println("Player "+ currentPlayer + " current hand : " + CurrentHand );

            System.out.println("Player " + currentPlayer + " discards a card.");
            
            //writeToTextFile(playerFilePath, "Player "+ currentPlayer+" discards a card",true);
            players.get(currentPlayer-1).discardCard(currentPlayer-1, gameDecks, numberOfPlayers,players);
            System.err.println(numberOfPlayers);
            System.out.println("Player "+ currentPlayer + " current hand : " + CurrentHand );


            //Draw card output
            System.out.println("Player " + currentPlayer + " draws a card.");
            //writeToTextFile(playerFilePath, "Player" + currentPlayer +" draws a card ",true);
            players.get(currentPlayer-1).drawCard(currentPlayer, gameDecks);

            System.err.println("player " + currentPlayer + allDecks.getPlayerDeck(currentPlayer) );


            //Showing current hand of the player :
       

            System.out.println("Player "+ currentPlayer + " current hand : " + CurrentHand );
            //writeToTextFile(playerFilePath,"Player" + currentPlayer + " current hand: " + CurrentHand, true);




            if (checkWin(i,players)) {
                gameWon = true;
                System.out.println("Player "+ currentPlayer + " Wins");
                //writeToTextFile(playerFilePath, "Player "+ currentPlayer + " Wins", gameWon);
                break; // Exit the for loop if the game is won
            }
        }
    }
}
    public static void main(String[] args) {
    int numberOfPlayers = getPlayerNum();
    String PackLocation = getPackLocation();
    packGeneration(numberOfPlayers, PackLocation);
    packDistribution(cardPack, numberOfPlayers, PackLocation);

    //createPlayers(playerNumber);
    
   // System.out.println("Printing all hands");
    //showPlayersHands(playerNumber);

    allDecks.populateDecks( numberOfPlayers, cardPack);

    System.out.println("Printing all decks");
    System.err.println(allDecks);

    startPlayerThreads(players);
    System.out.println("Final Player Hands:");
        showPlayersHands(numberOfPlayers);
        System.out.println("Final Decks:");
        System.out.println(allDecks);

   // System.err.println("Simulating:");
   // simulateBasicRound2(playerNumber, allDecks);



    



    }
}


