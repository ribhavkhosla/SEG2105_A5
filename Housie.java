/**
 * This is a game called Housie.
 * @author Ribhav Khosla
 * @version 1.3
 *
 * Copyright 2020, all rights reserved
 */

import java.util.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * startGame initializes the player and its opponents.
 */

class startGame {

    // Initialising the variables

    int playerNum;
    int numRange;
    String playerName;

    // variable to check if a player picked a number or not
    static int numChosenFlag=0;
    Moderator modObject;
    private ArrayList<Integer> arr = new ArrayList();
    Random rad = new Random();
    private player [] playerObjects;

    /**
     * the constructor startGame initializes the player
     * and the number of opponents he/she wants to play with.
     * @param arrayLen
     * the length of the array
     * @param playerNum
     * the number of players with an assigned number
     * @param numRange
     * the range of the numbers to play with (i.e the numbers
     * found on the list for each player and
     * the numbers the moderator chooses the random number from)
     * @param playerName
     * the name of then player
     */
    public startGame(int arrayLen,int playerNum, int numRange, String playerName){

        this.playerNum = playerNum;
        this.numRange = numRange;
        this.playerName = playerName;
        // initializing moderator
        modObject = Moderator.getInstance();
        // initializing the playerFactory
        playerFactoryClass p1 = new playerFactoryClass(playerNum,numRange,playerName);
        playerObjects = p1.getPlayerList();


    }
    /**
     * the method startGameMethod starts the game
     */

    void startGameMethod(){

        for(int j=0;j<100;j++) {

            int tempNum = rad.nextInt(numRange);
            this.getOrAddNumber(tempNum);

            modObject.display(tempNum);
            this.numChosenFlag=0;

            for(int i=0;i<playerNum;i++) {

                Thread object = new Thread(new playerThread(playerObjects[i],this,i+1));
                object.start();

            }

            try {
                Thread.sleep(1000);
                if(this.numChosenFlag==0){
                    System.out.println("No player has the number Chosen by moderator");
                }
            } catch (InterruptedException e) {

                e.printStackTrace();
            }

        }
    }
    /**
     * this method takes an integer input as parameter and
     * access the last element of the array if the parameter is -1
     * else, it adds the parameter to the array
     */
    synchronized int getOrAddNumber(int flag) {

        if(flag == -1)
            return (int)arr.get(arr.size()-1);
        else {
            arr.add(flag);
            return -1;
        }

    }

    /**
     * this method gives out a prompt
     * indicating the winner and the amount won
     */
    synchronized void playerWonPrompt(int idx) {

        System.out.println("");
        // prompt if the user(player 1 has won)
        if(idx==1){
            System.out.println("You have won the game");
            System.out.println("");
            System.out.println("You won "+playerNum*15+" CAD");
            System.out.println("");
            System.exit(0);
        }
        // prompt if some other player has won
        else{

            System.out.println("Sorry you lost! Player "+idx+" has won the game ");
            System.out.println("");
            System.out.println("Player "+idx+" has won "+playerNum*15+" CAD");
            System.out.println("");
            System.exit(0);
        }
    }

}
/**
 * the class playerFactoryClass initializes the
 * players indirectly using the factory design pattern in java.
 */

class playerFactoryClass{

    // initializing variables
    int playerNum;
    int numRange;
    String playerName;
    private player [] playerObjects;

    /**
     * the constructor to initialize the factory design pattern for players
     * @param playerNum
     * the number of players with an assigned number
     * @param numRange
     * the range of the numbers to play with (i.e the numbers
     * found on the list for each player and
     * the numbers the moderator chooses the random number from)
     * @param playerName
     * the name of the player
     * @return
     * a list of players with an assigned number
     */
    playerFactoryClass(int playerNum,int numRange,String playerName){

        this.playerNum = playerNum;
        this.numRange = numRange;
        this.playerName = playerName;
    }

    // creating a list of players
    player[] getPlayerList() {

        playerObjects = new player[playerNum];
        for(int i=0;i<playerNum;i++) {

            playerObjects[i] = new player(numRange,i+1,playerName);
        }

        return playerObjects;
    }
}
/**
 * The class playerThread implements Runnable.
 * It runs the thread of each player to access the
 * number produced by the moderator for a particular instance.
 */
class playerThread implements Runnable
{
    // initializing variables
    int num;
    player playerObjects;
    startGame selfObject;
    int idx;

    /**
     * The constructor playerThread initializes the player thread
     * @param playerObjects
     * list of player objects
     * @param selfObject
     * static instance of startGame
     * @param idx
     * identity index of the player
     */
    public playerThread(player playerObjects,startGame selfObject,int idx) {

        this.num = selfObject.getOrAddNumber(-1);
        this.selfObject = selfObject;
        this.playerObjects = playerObjects;
        this.idx = idx;
    }

    /**
     * The method run checks if a number in the list of any
     * player has matched with the key generated by the moderator.
     */
    public void run()
    {
        try
        {
            if(playerObjects.map.containsKey(num)==true && playerObjects.map.get(num)!=0) {
                int cutCount = playerObjects.count+1;
                selfObject.numChosenFlag=1;

                // compares values for the user (player1)
                if(idx == 1){
                    System.out.println("You have " + num+" in your list and you have till now found " + cutCount +" numbers");
                }

                // compares values for other players
                else {
                    System.out.println("Player " + idx + " found " + num + " and has till now found " + cutCount + " numbers");
                }

                if(playerObjects.map.get(num)==1) {
                    // If the key is matched with
                    // one number the key is removed from the list.
                    playerObjects.map.remove(num);
                }
                else {
                    // if key is matched with multiple numbers the key is decremented by 1.
                    playerObjects.map.put(num, playerObjects.map.get(num)-1);
                }
                playerObjects.count++;
            }

            // the winning condition
            if(playerObjects.count == 3) {
                selfObject.playerWonPrompt(idx);
            }
        }
        // catches exception if any
        catch (Exception e)
        {
            System.out.println ("Exception is caught");
        }
    }
}
/**
 * the class player initializes the Hashmap which stores the count of numbers.
 * it creates an array to store the numbers present in the ticket of a player
 */
class player{

    // initialization of variables
    int[] cardArray = new int[10];
    int count;
    // initializing Hash map
    Map<Integer, Integer> map=new HashMap<Integer,Integer>();
    Random rad = new Random();

    /**
     * This method creates separate tickets for
     * each player consisting of a certain amount of numbers.
     * @param numRange
     * the range of the numbers to play with (i.e the numbers
     * found on the list for each player and
     * the numbers the moderator chooses the random number from)
     * @param idx
     * identity index of the player
     * @param playerName
     * the name of the player
     */
    player(int numRange,int idx, String playerName) {

        count = 0;
        for(int i=0;i<10;i++) {
            int temp = rad.nextInt(numRange);
            cardArray[i]=temp;
            if(map.containsKey(temp)==true) {
                int tempCount = map.get(temp)+1;
                map.put(temp,tempCount);
            }
            else
                map.put(temp,1);
        }
        // ticket for the user (player 1)
        if(idx == 1){
            System.out.println("Random Number list for you is " + Arrays.toString(cardArray) );
        }
        // tickets for other players
        else{
            System.out.println("Random Number list for player " + idx + " is " + Arrays.toString(cardArray) );
        }
    }
}
/**
 * The class moderator puts numbers in an array list
 */
class Moderator{

    /**
     * instantiates the moderator
     */
    private static Moderator instance = new Moderator();

    /**
     * moderator class
     */
    private Moderator() {}

    /**
     * getter for the instance of the moderator
     * @return
     * instance of moderator
     */
    public static Moderator getInstance() {
        return instance;
    }
    /**
     * method to display the number chosen by the moderator
     * @param num
     * number chosen by the moderator
     */
    void display(int num) {
        System.out.println("Number chosen by moderator is : " + num);
    }

}

public class Housie {

    /**
     * the main method of the application creates
     * an instance of the method startGame and
     * takes parameters for length of the array,
     * player numbers, the range of possible numbers
     * to play with, and the name of the first player (i.e the user).
     * Before instantiating the startGame method it gives a welcome message,
     * gets player name and the number of opponents the
     * user want to play with as input from the user.
     * @param args
     */

    public static void main(String[] args) throws IOException {

        // initializing variables
        int playerNum = 9; // if no number entered, it takes 9 as by default so that total players is 10
        int numRange = 50;
        int arrayLen = 100;
        String playerName ="Player 1";
        System.out.println("");
        System.out.println("*****************************************");
        System.out.println("**                                     **");
        System.out.println("**          Welcome to HOUSIE          **");
        System.out.println("**                                     **");
        System.out.println("**  Entry fees for the game is 15 CAD  **");
        System.out.println("**                                     **");
        System.out.println("*****************************************");
        System.out.println("");
        Scanner inp1 = new Scanner(System.in);
        System.out.println("Enter your name: ");
        //BufferedReader inp1 = new BufferedReader (new InputStreamReader(System.in));
        playerName=inp1.nextLine();

        if ( playerName == null || playerName == ""){
            playerName="Player 1";
        }

        System.out.println("");
        System.out.println("Welcome to the game " + playerName);
        System.out.println("");

        System.out.println("Enter the number of players that you want to play with: ");
        BufferedReader inp = new BufferedReader (new InputStreamReader(System.in));

        try {
            playerNum = Integer.parseInt(inp.readLine());
        } catch (NumberFormatException | IOException e) {
            System.out.println("Invalid input. Using default..");
            playerNum = 9;
        }

        System.out.println("");
        System.out.println("The total number of players is: "+ (playerNum+1));
        System.out.println("");
        System.out.println("The total pool of money is: " + (playerNum+1)*15 + " CAD");
        System.out.println("");

        startGame s1 = new startGame(arrayLen,playerNum+1, numRange, playerName);
        s1.startGameMethod();
        System.out.println("No Player Won");
    }

}

