
import java.io.*;
import java.util.*;
import java.util.logging.FileHandler;



import javax.print.DocFlavor.STRING;
import javax.swing.plaf.TreeUI;



public class OldCardGame extends Thread{

    // Create a single Scanner instance at the class level to be shared across methods
    private static Scanner scanner = new Scanner(System.in);

    public static List<Integer> cardPack = new ArrayList<>();

    public static List<Player> players = new ArrayList<>();


    public static int numberOfCardsPerPlayer = 4;
    public  static Map<Integer, List<Integer>> hands = new HashMap<>();






    public static void packGeneration(int numberOfPlayers,String fileName){
        //This functiomn will genereate a pack of cards based on amount of players
           //it should take in 8n cards n being the total amount of players
           //hence it should take in the amount of players and generate it based on that
   
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
                   int randomNumber = random.nextInt(15); //generates random numbers betweem 0 and 15
                   bufferedWriter.write(Integer.toString(randomNumber));//writting random number as string
                  
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



    public static void packDistribution(List<Integer> cardPack, int numberOfPlayers, String LocationOfPack) {

        BufferedReader reader = null; 

        
        try {
            reader = new BufferedReader(new FileReader(LocationOfPack));

        String line;
        while ((line = reader.readLine()) != null) {
            int cardValue = Integer.parseInt(line.trim()); //trim() removes whitespaces and end of line symbols. parseInt() parses a string to an Int
            cardPack.add(cardValue);

        }}catch(IOException e) {
            System.out.println("Invalid pack file. Please provide a valid pack file.");
        }



        for (int i = 1; i <= numberOfPlayers; i++) {
            hands.put(i, new ArrayList<>());
        }



        

        for (int i = 0; i < numberOfCardsPerPlayer; i++) {
            for (int currentPlayer = 1; currentPlayer <= numberOfPlayers; currentPlayer++) {
                hands.get(currentPlayer).add(cardPack.remove(0));
            }
        }
    }

    


    
    



    public static void createThread(int numberOfPlayers) {
        //Grabs number of players

        //Creates a loop that iterates through each player
        for (int i = 1; i<= numberOfPlayers; i++){

            //Create a player instance for the current player
            Player player = new Player(i, hands);
            players.add(player);

            //creates a new thread for each player using this class
            Thread playerThread = new Thread(player);
            //Set the name of the thread based on the players number
            playerThread.setName("player" + i + "Thread");
            playerThread.start();
        }
         
    }

    //outputting



    public static void writeToTextFile(String filePath, String content ,boolean append) {
        
        if(!append){
            deleteFile(filePath);
        }
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath,append))) {
            writer.write(content + "\n");
            System.out.println("Data has been written to the file.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteFile(String filePath){
        File file = new File(filePath);
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("File " + filePath + " deleted successfully.");
            } else {
                System.out.println("Failed to delete file " + filePath);
            }
        } else {
            System.out.println("File " + filePath + " does not exist.");
        }
    }
        

    


    public static void simulateBasicRound2(int numberOfPlayers, CardDeck gameDecks) {
        
        //Need to make sure to clear the text files before it runs a new game so that it dosnt overlap from past games 
        
        boolean gameWon = false;
    
        while (!gameWon) {
            for (Player player : players) {

                int currentPlayer = player.playerNumber;
                
                String playerFilePath = "player" + currentPlayer + "_output.txt";

                //initial hand output
                // Initialize the output file with the initial hand
                writeToTextFile(playerFilePath, "initial hand", true);

    

                gameWon = player.checkWin(numberOfPlayers);
                
                
                //Discard card output
                System.out.println("Player " + currentPlayer + " discards a card.");
                writeToTextFile(playerFilePath, "Player "+ currentPlayer+" discards a card",true);
                player.discardCard(currentPlayer, gameDecks, numberOfPlayers);
    
                //Draw card output
                System.out.println("Player " + currentPlayer + " draws a card.");
                writeToTextFile(playerFilePath, "Player" + currentPlayer +" draws a card ",true);
                player.drawCard(currentPlayer, gameDecks);


                //Showing current hand of the player :
                String CurrentHand = hands.get(currentPlayer).toString();

                System.out.println("Player "+ currentPlayer + " current hand : " + CurrentHand );
                writeToTextFile(playerFilePath,"Player" + currentPlayer + " current hand: " + CurrentHand, true);


                System.out.println("Hands after drawing/dicarding card: " + hands);
    
                // Print hands after draw/discard
                System.out.println("Decks after drawing/discarding card: " + gameDecks);
    
                if (gameWon) {
                    System.out.println("Player "+ currentPlayer + " Wins");
                    writeToTextFile(playerFilePath, "Player "+ currentPlayer + " Wins", gameWon);
                    break; // Exit the for loop if the game is won
                }
            }
        }
    }


        




    


public static void main(String[] args) {

    //range of the random number of cards in a pack
    

    
    //Requests number of players in game
    int numberOfPlayers = getPlayerNum();

        for (int i = 1; i <= numberOfPlayers; i++) {
            hands.put(i, new ArrayList<>());
        }

    //dynamically generating the filename based on the number of players
    String fileName = numberOfPlayers + ".txt";

    //generates the txt pack file with random numbers in
    System.out.println("Random numbers have been written to: " + fileName);


    packGeneration(numberOfPlayers, fileName);

    createThread(numberOfPlayers);

    //Request loaction of pack to load
    String PackLocation = getPackLocation();

    CardDeck gameDecks = new CardDeck(); //Create an instance of gameDecks

    packDistribution(cardPack, numberOfPlayers, PackLocation);

    gameDecks.populateDecks(numberOfPlayers, cardPack);


    // Print initial hands and decks
    System.out.println("Initial Hands: " + hands);
    System.out.println("Initial Decks: " + gameDecks.getDeck());

    simulateBasicRound2(numberOfPlayers, gameDecks);



}}


