# SEG2105_A5
Architecture: 

This is the game of Housie, also known as Tambola or The Indian Bingo. In the game there is a moderator which speaks out random numbers from 1-90 and there are players who have tickets and they simultaneously cross out the numbers that match with the one chosen randomly the moderator. the person who crosses out all the numbers from his/her ticket wins the prize money.

This implementation of the game housie that I have created is slightly different from the traditional game. In my implementation there the moderator chooses from 0-100 random numbers and the players gets a ticket with 10 random numbers. The player who crosses out three numbers at the earliest wins the game and the total pool of money, in this case which is 15 CAD X the number of players. Rest of the rules apply the same.

Coming on the implementation of the code there are two different design patterns. For instantiating a moderator, a singleton pattern is used that instantiates the moderator only one time. For instantiating players, a factory method is used which creates multiple players.

Each player is assigned a ticket consisting of 10 random numbers from 0-100. This ticket is created using Hash maps. The benefit of using hash map is that the number that matches the number produced by the moderator can be easily removed from the ticket with a time complexity of O(log n) to search and remove the number.

The moderator chooses random numbers from 0-100 and put them into an Array list. Each player picks the last element from the Array list and check the value in their tickets. Each player fetch the last element of the array list via the synchronized method(getOrAddNumber()). We used multi-threading to make sure that the players are playing in a concurrent fashion rather that a sequential fashion so that no player has to wait for its turn.


How to run the game:
To compile and run simply open terminal or command prompt and open the directory where the file is saved on your computer.

To Compile: 
javac Housie.java

To run: 
java Housie

You will see a welcome message and it will prompt you to enter your name. After you enter your name you will be prompted to enter the number of opponents you want to play with. After you enter the number the game will automatically run and will give out the name of the winner and total money won in the end.

Hints to update:
1.	Add a user interface in form of a mobile app or a web app.
2.	We can add a multiplayer feature.
3.	We can give voice to the moderator to give it a real feeling
4.	We can introduce game currency to make it realistic, as traditional version has real money involved.


