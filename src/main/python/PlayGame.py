import random
import re
import sys
from IGame import IGame
from Game import Game

class PlayGame:

    @staticmethod
    def readYesNo() -> bool:
        yn = input().strip().upper()
        if not re.match(r"^[YN]$", yn):
            print("y or n please", file=sys.stderr)
            return PlayGame.readYesNo()
        return yn == "Y"

    @staticmethod
    def readRoll() -> int:
        rollStr = input("\n>> Throw a die and input roll, or [ENTER] to generate a random roll: ").strip()
        if not rollStr:
            roll = random.randint(1, 6)
            print(f">> Random roll: {roll}")
            return roll
        if not re.match(r"^\d+$", rollStr):
            print(f"Not a number: '{rollStr}'", file=sys.stderr)
            return PlayGame.readRoll()
        roll = int(rollStr)
        if roll < 1 or roll > 6:
            print("Invalid roll", file=sys.stderr)
            return PlayGame.readRoll()
        return roll

    @staticmethod
    def main():
        print("*** Welcome to Trivia Game ***\n")
        print("Enter number of players: 1-4")
        
        try:
            playerCount = int(input())
        except ValueError:
            # Forzar excepción en la validación inferior si la entrada no es un entero
            playerCount = -1 
            
        if playerCount < 1 or playerCount > 4:
            raise ValueError("No player 1..4")
            
        print(f"Reading names for {playerCount} players:")

        aGame: IGame = Game()

        for i in range(1, playerCount + 1):
            playerName = input(f"Player {i} name: ")
            aGame.add(playerName)

        print("\n\n--Starting game--")

        notAWinner = True
        
        while notAWinner:
            roll = PlayGame.readRoll()
            aGame.roll(roll)

            print(">> Was the answer correct? [y/n] ", end="", flush=True)
            correct = PlayGame.readYesNo()
            
            if correct:
                notAWinner = aGame.handleCorrectAnswer()
            else:
                notAWinner = aGame.wrongAnswer()

        print(">> Game won!")


if __name__ == "__main__":
    PlayGame.main()