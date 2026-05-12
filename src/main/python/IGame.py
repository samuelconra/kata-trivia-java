from abc import ABC, abstractmethod

class IGame(ABC):
    @abstractmethod
    def add(self, playerName: str) -> bool:
        pass

    @abstractmethod
    def roll(self, roll: int) -> None:
        pass

    @abstractmethod
    def handleCorrectAnswer(self) -> bool:
        pass

    @abstractmethod
    def wrongAnswer(self) -> bool:
        pass
