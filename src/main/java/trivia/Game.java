package trivia;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.LinkedHashMap;

public class Game implements IGame {
   ArrayList<Player> players = new ArrayList<>();

   QuestionDeck deck = new QuestionDeck(MAX_QUESTIONS_PER_CATEGORY, CATEGORIES);

   int currentPlayer = 0;
   boolean isGettingOutOfPenaltyBox;

   private static final int MAX_QUESTIONS_PER_CATEGORY = 50;
   private static final int BOARD_SIZE = 12;
   private static final int COINS_TO_WIN = 6;
   private static final String[] CATEGORIES = {"Pop", "Science", "Sports", "Rock", "Geography"};

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
            movePlayerAndAskQuestion(roll);
         } else {
            System.out.println(currentPlayer().name + " is not getting out of the penalty box");
            isGettingOutOfPenaltyBox = false;
         }
      } else {
         movePlayerAndAskQuestion(roll);
      }
   }

   private String currentCategory() {
      return CATEGORIES[(currentPlayer().position - 1) % CATEGORIES.length];
   }

   public boolean handleCorrectAnswer() {
      if (currentPlayer().inPenaltyBox) {
         if (isGettingOutOfPenaltyBox) {
            currentPlayer().exitPenaltyBox();
            return rewardPlayerAndCheckWinner();
         } else {
            nextPlayer();
            return true;
         }
      } else {
         return rewardPlayerAndCheckWinner();
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
      return !currentPlayer().hasWon(COINS_TO_WIN);
   }

   private void nextPlayer() {
      currentPlayer++;
      if (currentPlayer == players.size()) currentPlayer = 0;
   }

   private void movePlayerAndAskQuestion(int roll) {
      int newPosition = currentPlayer().position + roll;
      if (newPosition > BOARD_SIZE) newPosition = newPosition - BOARD_SIZE;
      currentPlayer().advanceTo(newPosition);

      System.out.println(currentPlayer().name
                           + "'s new location is "
                           + currentPlayer().position);
      System.out.println("The category is " + currentCategory());
      deck.askQuestion(currentCategory());
   }

   private boolean rewardPlayerAndCheckWinner() {
      System.out.println("Answer was correct!!!!");
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

   void advanceTo(int newPosition) {
      position = newPosition;
   }

   boolean hasWon(int coinsToWin) {
      return coins == coinsToWin;
   }

   void exitPenaltyBox() {
      inPenaltyBox = false;
   }
}

class QuestionDeck {
   private final Map<String, LinkedList<String>> questionsByCategory = new LinkedHashMap<>();

   QuestionDeck(int maxQuestions, String[] categories) {
      for (String category : categories) {
         questionsByCategory.put(category, new LinkedList<>());
      }

      for (int i = 0; i < maxQuestions; i++) {
         for (String category : categories) {
            questionsByCategory.get(category).addLast(category + " Question " + i);
         }
      }
   }

   void askQuestion(String category) {
      System.out.println(questionsByCategory.get(category).removeFirst());
   }
}
