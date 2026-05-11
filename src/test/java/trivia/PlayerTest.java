package trivia;

import org.junit.Test;
import static org.junit.Assert.*;

public class PlayerTest {

    @Test
    public void playerStartsWithCorrectDefaultValues() {
        Player player = new Player("Samuel");
        
        assertEquals("Samuel", player.name);
        assertEquals(1, player.position);
        assertEquals(0, player.coins);
        assertFalse(player.inPenaltyBox);
    }

    @Test
    public void addCoinIncreasesCoinCount() {
        Player player = new Player("Samuel");
        
        player.addCoin();
        player.addCoin();
        
        assertEquals(2, player.coins);
    }

    @Test
    public void playerWinsWhenTargetCoinsAreReached() {
        Player player = new Player("Samuel");
        int coinsToWin = 6;
        
        for (int i = 0; i < 5; i++) {
            player.addCoin();
            assertFalse("Player should not have won yet", player.hasWon(coinsToWin));
        }
        
        player.addCoin();
        assertTrue("Player should win with 6 coins", player.hasWon(coinsToWin));
    }

    @Test
    public void penaltyBoxStatusCanBeToggled() {
        Player player = new Player("Samuel");
        
        player.sendToPenaltyBox();
        assertTrue(player.inPenaltyBox);
        
        player.exitPenaltyBox();
        assertFalse(player.inPenaltyBox);
    }

    @Test
    public void advanceToUpdatesPositionCorrectly() {
        Player player = new Player("Samuel");
        
        player.advanceTo(5);
        assertEquals(5, player.position);
        
        player.advanceTo(1);
        assertEquals(1, player.position);
    }
}