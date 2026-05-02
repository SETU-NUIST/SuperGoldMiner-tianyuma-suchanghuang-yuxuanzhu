package Repository;

import Minerals.Diamond;
import Minerals.Gold;
import Minerals.Mineral;
import Minerals.Stone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private Player player;

    @BeforeEach
    void init() {
        player = new Player(1L, "TestPlayer", 0, 0, new ArrayList<>());
    }

    @Test
    @DisplayName("Test collect gold mineral")
    void testCollectGold() {
        Mineral gold = new Gold(1L);

        player.collectMineral(gold);

        assertEquals(gold.calculateValue(), player.getTotalScore());
        assertEquals(gold.getValue(), player.getGoldCoin());
        assertEquals(1, player.getCollectedMinerals().size());
        assertTrue(player.getCollectedMinerals().contains(gold));
    }

    @Test
    @DisplayName("Test collect diamond mineral")
    void testCollectDiamond() {
        Mineral diamond = new Diamond(1L);

        player.collectMineral(diamond);

        assertEquals(diamond.calculateValue(), player.getTotalScore());
        assertEquals(diamond.getValue(), player.getGoldCoin());
        assertEquals(1, player.getCollectedMinerals().size());
    }

    @Test
    @DisplayName("Test collect stone mineral")
    void testCollectStone() {
        Mineral stone = new Stone(1L);

        player.collectMineral(stone);

        assertEquals(stone.calculateValue(), player.getTotalScore());
        assertEquals(stone.getValue(), player.getGoldCoin());
        assertEquals(1, player.getCollectedMinerals().size());
    }

    @Test
    @DisplayName("Test collect multiple minerals")
    void testCollectMultipleMinerals() {
        Mineral gold1 = new Gold(1L);
        Mineral diamond = new Diamond(2L);
        Mineral stone = new Stone(3L);
        Mineral gold2 = new Gold(4L);

        player.collectMineral(gold1);
        player.collectMineral(diamond);
        player.collectMineral(stone);
        player.collectMineral(gold2);

        int expectedScore = gold1.calculateValue() + diamond.calculateValue() 
                          + stone.calculateValue() + gold2.calculateValue();
        int expectedCoins = gold1.getValue() + diamond.getValue() 
                          + stone.getValue() + gold2.getValue();

        assertEquals(expectedScore, player.getTotalScore());
        assertEquals(expectedCoins, player.getGoldCoin());
        assertEquals(4, player.getCollectedMinerals().size());
    }

    @Test
    @DisplayName("Test player initialization")
    void testPlayerInitialization() {
        Player emptyPlayer = new Player();
        
        assertNull(emptyPlayer.getId());
        assertNull(emptyPlayer.getNickname());
        assertEquals(0, emptyPlayer.getTotalScore());
        assertEquals(0, emptyPlayer.getGoldCoin());
        assertNotNull(emptyPlayer.getCollectedMinerals());
        assertTrue(emptyPlayer.getCollectedMinerals().isEmpty());
    }

    @Test
    @DisplayName("Test player setters and getters")
    void testPlayerSettersAndGetters() {
        player.setId(2L);
        player.setNickname("NewName");
        player.setTotalScore(1000);
        player.setGoldCoin(500);

        assertEquals(2L, player.getId());
        assertEquals("NewName", player.getNickname());
        assertEquals(1000, player.getTotalScore());
        assertEquals(500, player.getGoldCoin());
    }
}
