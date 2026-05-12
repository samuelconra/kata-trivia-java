from collections import deque
from IGame import IGame

class Player:
    def __init__(self, name: str):
        self.name = name
        self.position = 1
        self.coins = 0
        self.inPenaltyBox = False

    def addCoin(self) -> None:
        self.coins += 1

    def sendToPenaltyBox(self) -> None:
        self.inPenaltyBox = True

    def advanceTo(self, newPosition: int) -> None:
        self.position = newPosition

    def hasWon(self, coinsToWin: int) -> bool:
        return self.coins == coinsToWin

    def exitPenaltyBox(self) -> None:
        self.inPenaltyBox = False


class QuestionDeck:
    def __init__(self, maxQuestions: int, categories: list):
        self.questionsByCategory = {}
        for category in categories:
            self.questionsByCategory[category] = deque()

        for i in range(maxQuestions):
            for category in categories:
                self.questionsByCategory[category].append(f"{category} Question {i}")

    def askQuestion(self, category: str) -> None:
        # Equivalente a removeFirst()
        print(self.questionsByCategory[category].popleft())


class Game(IGame):
    MAX_QUESTIONS_PER_CATEGORY = 50
    BOARD_SIZE = 12
    COINS_TO_WIN = 6
    CATEGORIES = ["Pop", "Science", "Sports", "Rock", "Geography"]

    def __init__(self):
        self.players = []
        self.deck = QuestionDeck(self.MAX_QUESTIONS_PER_CATEGORY, self.CATEGORIES)
        self.currentPlayer = 0
        self.isGettingOutOfPenaltyBox = False

    def hasEnoughPlayers(self) -> bool:
        return self.howManyPlayers() >= 2

    def add(self, playerName: str) -> bool:
        self.players.append(Player(playerName))
        print(f"{playerName} was added")
        print(f"They are player number {len(self.players)}")
        return True

    def howManyPlayers(self) -> int:
        return len(self.players)

    def _currentPlayerObj(self) -> Player:
        # Metodo auxiliar privado para obtener la instancia del jugador actual (sustituye a currentPlayer() en Java)
        return self.players[self.currentPlayer]

    def roll(self, roll: int) -> None:
        print(f"{self._currentPlayerObj().name} is the current player")
        print(f"They have rolled a {roll}")

        if self._currentPlayerObj().inPenaltyBox:
            if roll % 2 != 0:
                self.isGettingOutOfPenaltyBox = True
                print(f"{self._currentPlayerObj().name} is getting out of the penalty box")
                self._movePlayerAndAskQuestion(roll)
            else:
                print(f"{self._currentPlayerObj().name} is not getting out of the penalty box")
                self.isGettingOutOfPenaltyBox = False
        else:
            self._movePlayerAndAskQuestion(roll)

    def _currentCategory(self) -> str:
        return self.CATEGORIES[(self._currentPlayerObj().position - 1) % len(self.CATEGORIES)]

    def handleCorrectAnswer(self) -> bool:
        if self._currentPlayerObj().inPenaltyBox:
            if self.isGettingOutOfPenaltyBox:
                self._currentPlayerObj().exitPenaltyBox()
                return self._rewardPlayerAndCheckWinner()
            else:
                self._nextPlayer()
                return True
        else:
            return self._rewardPlayerAndCheckWinner()

    def wrongAnswer(self) -> bool:
        print("Question was incorrectly answered")
        print(f"{self._currentPlayerObj().name} was sent to the penalty box")
        self._currentPlayerObj().sendToPenaltyBox()

        self._nextPlayer()
        return True

    def _didPlayerWin(self) -> bool:
        return not self._currentPlayerObj().hasWon(self.COINS_TO_WIN)

    def _nextPlayer(self) -> None:
        self.currentPlayer += 1
        if self.currentPlayer == len(self.players):
            self.currentPlayer = 0

    def _movePlayerAndAskQuestion(self, roll: int) -> None:
        newPosition = self._currentPlayerObj().position + roll
        if newPosition > self.BOARD_SIZE:
            newPosition = newPosition - self.BOARD_SIZE
        self._currentPlayerObj().advanceTo(newPosition)

        print(f"{self._currentPlayerObj().name}'s new location is {self._currentPlayerObj().position}")
        print(f"The category is {self._currentCategory()}")
        self.deck.askQuestion(self._currentCategory())

    def _rewardPlayerAndCheckWinner(self) -> bool:
        print("Answer was correct!!!!")
        self._currentPlayerObj().addCoin()
        print(f"{self._currentPlayerObj().name} now has {self._currentPlayerObj().coins} Gold Coins.")

        winner = self._didPlayerWin()
        self._nextPlayer()

        return winner
