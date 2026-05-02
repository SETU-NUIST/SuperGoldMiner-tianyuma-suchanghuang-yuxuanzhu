package Minerals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MineralTest {

    @Test
    @DisplayName("Test Gold calculateValue")
    void testGoldCalculateValue() {
        Gold gold = new Gold(1L);
        
        assertEquals("gold", gold.getName());
        assertEquals(100, gold.getValue());
        assertEquals(5, gold.getWeight());
        assertEquals(100, gold.calculateValue());
    }

    @Test
    @DisplayName("Test Diamond calculateValue")
    void testDiamondCalculateValue() {
        Diamond diamond = new Diamond(1L);
        
        assertEquals("diamond", diamond.getName());
        assertEquals(500, diamond.getValue());
        assertEquals(2, diamond.getWeight());
        assertEquals(500, diamond.calculateValue());
    }

    @Test
    @DisplayName("Test Stone calculateValue")
    void testStoneCalculateValue() {
        Stone stone = new Stone(1L);
        
        assertEquals("stone", stone.getName());
        assertEquals(1, stone.getValue());
        assertEquals(10, stone.getWeight());
        assertEquals(1, stone.calculateValue());
    }

    @Test
    @DisplayName("Test Gold setters and getters")
    void testGoldSettersAndGetters() {
        Gold gold = new Gold(null);
        gold.setId(2L);
        gold.setName("gold");
        gold.setValue(200);
        gold.setWeight(10);

        assertEquals(2L, gold.getId());
        assertEquals("gold", gold.getName());
        assertEquals(200, gold.getValue());
        assertEquals(10, gold.getWeight());
    }

    @Test
    @DisplayName("Test Diamond setters and getters")
    void testDiamondSettersAndGetters() {
        Diamond diamond = new Diamond(null);
        diamond.setId(3L);
        diamond.setName("diamond");
        diamond.setValue(600);
        diamond.setWeight(3);

        assertEquals(3L, diamond.getId());
        assertEquals("diamond", diamond.getName());
        assertEquals(600, diamond.getValue());
        assertEquals(3, diamond.getWeight());
    }

    @Test
    @DisplayName("Test Stone setters and getters")
    void testStoneSettersAndGetters() {
        Stone stone = new Stone(null);
        stone.setId(4L);
        stone.setName("stone");
        stone.setValue(5);
        stone.setWeight(15);

        assertEquals(4L, stone.getId());
        assertEquals("stone", stone.getName());
        assertEquals(5, stone.getValue());
        assertEquals(15, stone.getWeight());
    }

    @Test
    @DisplayName("Test mineral comparison by value")
    void testMineralValueComparison() {
        Gold gold = new Gold(1L);
        Diamond diamond = new Diamond(2L);
        Stone stone = new Stone(3L);

        assertTrue(diamond.getValue() > gold.getValue());
        assertTrue(gold.getValue() > stone.getValue());
        assertTrue(diamond.getValue() > stone.getValue());
    }

    @Test
    @DisplayName("Test mineral comparison by weight")
    void testMineralWeightComparison() {
        Gold gold = new Gold(1L);
        Diamond diamond = new Diamond(2L);
        Stone stone = new Stone(3L);

        assertTrue(stone.getWeight() > gold.getWeight());
        assertTrue(gold.getWeight() > diamond.getWeight());
        assertTrue(stone.getWeight() > diamond.getWeight());
    }
}
