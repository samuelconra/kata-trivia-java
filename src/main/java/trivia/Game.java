package trivia;

import java.util.ArrayList;
import java.util.LinkedList;

public class Game implements IGame {
   ArrayList<Player> players = new ArrayList<>();

   LinkedList popQuestions = new LinkedList();
   LinkedList scienceQuestions = new LinkedList();
   LinkedList sportsQuestions = new LinkedList();
   LinkedList rockQuestions = new LinkedList();

   int currentPlayer = 0;
   boolean isGettingOutOfPenaltyBox;

   private static final int MAX_QUESTIONS_PER_CATEGORY = 50;
   private static final int BOARD_SIZE = 12;
   private static final int COINS_TO_WIN = 6;

   public Game() {
      for (int i = 0; i < MAX_QUESTIONS_PER_CATEGORY; i++) {
         popQuestions.addLast("Pop Question " + i);
         scienceQuestions.addLast(("Science Question " + i));
         sportsQuestions.addLast(("Sports Question " + i));
         rockQuestions.addLast(createRockQuestion(i));
      }
   }

   public String createRockQuestion(int index) {
      return "Rock Question " + index;
   }

   public boolean hasEnoughPlayers() {
      return (howManyPlayers() >= 2);
   }

   public boolean add(String playerName) {
      players.add(new Player(playerName));

      System.out.println(playerName + " was added");
      System.out.println("They are player number " + players.size());
      return true;
   }

   public int howManyPlayers() {
      return players.size();
   }

   private Player currentPlayer() {
      return players.get(currentPlayer);
   }

   public void roll(int roll) {
      System.out.println(currentPlayer().name + " is the current player");
      System.out.println("They have rolled a " + roll);

      if (currentPlayer().inPenaltyBox) {
         if (roll % 2 != 0) {
            isGettingOutOfPenaltyBox = true;

            System.out.println(currentPlayer().name + " is getting out of the penalty box");
            currentPlayer().position = currentPlayer().position + roll;
            if (currentPlayer().position > BOARD_SIZE) currentPlayer().position = currentPlayer().position - BOARD_SIZE;

            System.out.println(currentPlayer().name
                               + "'s new location is "
                               + currentPlayer().position);
            System.out.println("The category is " + currentCategory());
            askQuestion();
         } else {
            System.out.println(currentPlayer().name + " is not getting out of the penalty box");
            isGettingOutOfPenaltyBox = false;
         }

      } else {

         currentPlayer().position = currentPlayer().position + roll;
         if (currentPlayer().position > BOARD_SIZE) currentPlayer().position = currentPlayer().position - BOARD_SIZE;

         System.out.println(currentPlayer().name
                            + "'s new location is "
                            + currentPlayer().position);
         System.out.println("The category is " + currentCategory());
         askQuestion();
      }

   }

   private void askQuestion() {
      if (currentCategory() == "Pop")
         System.out.println(popQuestions.removeFirst());
      if (currentCategory() == "Science")
         System.out.println(scienceQuestions.removeFirst());
      if (currentCategory() == "Sports")
         System.out.println(sportsQuestions.removeFirst());
      if (currentCategory() == "Rock")
         System.out.println(rockQuestions.removeFirst());
   }


   private String currentCategory() {
      if (currentPlayer().position - 1 == 0) return "Pop";
      if (currentPlayer().position - 1 == 4) return "Pop";
      if (currentPlayer().position - 1 == 8) return "Pop";
      if (currentPlayer().position - 1 == 1) return "Science";
      if (currentPlayer().position - 1 == 5) return "Science";
      if (currentPlayer().position - 1 == 9) return "Science";
      if (currentPlayer().position - 1 == 2) return "Sports";
      if (currentPlayer().position - 1 == 6) return "Sports";
      if (currentPlayer().position - 1 == 10) return "Sports";
      return "Rock";
   }

   public boolean handleCorrectAnswer() {
      if (currentPlayer().inPenaltyBox) {
         if (isGettingOutOfPenaltyBox) {
            System.out.println("Answer was correct!!!!");
            currentPlayer().addCoin();
            System.out.println(currentPlayer().name
                               + " now has "
                               + currentPlayer().coins
                               + " Gold Coins.");

            boolean winner = didPlayerWin();
            nextPlayer();

            return winner;
         } else {
            nextPlayer();
            return true;
         }


      } else {

         System.out.println("Answer was corrent!!!!");
         currentPlayer().addCoin();
         System.out.println(currentPlayer().name
                            + " now has "
                            + currentPlayer().coins
                            + " Gold Coins.");

         boolean winner = didPlayerWin();
         nextPlayer();

         return winner;
      }
   }

   public boolean wrongAnswer() {
      System.out.println("Question was incorrectly answered");
      System.out.println(currentPlayer().name + " was sent to the penalty box");
      currentPlayer().sendToPenaltyBox();

      nextPlayer();
      return true;
   }


   private boolean didPlayerWin() {
      return !(currentPlayer().coins == COINS_TO_WIN);
   }

   private void nextPlayer() {
      currentPlayer++;
      if (currentPlayer == players.size()) currentPlayer = 0;
   }
}

class Player {
   final String name;
   int position = 1; 
   int coins = 0; 
   boolean inPenaltyBox = false;

   Player(String name) {
      this.name = name;
   }

   void addCoin() {
      coins++;
   }

   void sendToPenaltyBox() {
      inPenaltyBox = true;
   }
}
