package Repository;

import Minerals.Diamond;
import Minerals.Gold;
import Minerals.Mineral;
import Minerals.Stone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MineralRepositoryTest {

    private MineralRepository mineralRepository;

    @BeforeEach
    void init() {
        mineralRepository = new MineralRepository();
    }

    @Test
    @DisplayName("Test saving a new mineral")
    void testSaveNewMineral() {
        Gold gold = new Gold(null);
        Mineral saved = mineralRepository.save(gold);

        assertNotNull(saved.getId());
        assertEquals("gold", saved.getName());
        assertEquals(1, mineralRepository.findAll().size());
    }

    @Test
    @DisplayName("Test saving an existing mineral (update)")
    void testSaveExistingMineral() {
        Gold gold = new Gold(null);
        Mineral saved = mineralRepository.save(gold);
        
        saved.setValue(200);
        Mineral updated = mineralRepository.save(saved);

        assertEquals(200, updated.getValue());
        assertEquals(1, mineralRepository.findAll().size());
    }

    @Test
    @DisplayName("Test finding mineral by ID")
    void testFindById() {
        Gold gold = new Gold(null);
        Diamond diamond = new Diamond(null);
        mineralRepository.save(gold);
        mineralRepository.save(diamond);

        Mineral found = mineralRepository.findById(gold.getId());
        assertNotNull(found);
        assertEquals("gold", found.getName());

        Mineral notFound = mineralRepository.findById(999L);
        assertNull(notFound);
    }

    @Test
    @DisplayName("Test finding all minerals")
    void testFindAll() {
        assertEquals(0, mineralRepository.findAll().size());

        mineralRepository.save(new Gold(null));
        mineralRepository.save(new Diamond(null));
        mineralRepository.save(new Stone(null));

        List<Mineral> minerals = mineralRepository.findAll();
        assertEquals(3, minerals.size());
    }

    @Test
    @DisplayName("Test deleting mineral by ID")
    void testDeleteById() {
        Gold gold = new Gold(null);
        Mineral saved = mineralRepository.save(gold);

        assertEquals(1, mineralRepository.findAll().size());

        mineralRepository.deleteById(saved.getId());
        assertEquals(0, mineralRepository.findAll().size());
        assertNull(mineralRepository.findById(saved.getId()));
    }

    @Test
    @DisplayName("Test generating random mineral")
    void testGenerateRandomMineral() {
        // Run multiple times to test randomness
        for (int i = 0; i < 10; i++) {
            Mineral mineral = mineralRepository.generateRandomMineral();
            
            assertNotNull(mineral);
            assertNotNull(mineral.getId());
            assertNotNull(mineral.getName());
            
            assertTrue(List.of("gold", "stone").contains(mineral.getName()));
            assertTrue(mineral.getValue() > 0);
            assertTrue(mineral.getWeight() > 0);
        }
        
        assertEquals(10, mineralRepository.findAll().size());
    }

    @Test
    @DisplayName("Test finding minerals by type name")
    void testFindByTypeName() {
        mineralRepository.save(new Gold(null));
        mineralRepository.save(new Gold(null));
        mineralRepository.save(new Diamond(null));
        mineralRepository.save(new Stone(null));

        List<Mineral> golds = mineralRepository.findByTypeName("gold");
        assertEquals(2, golds.size());

        List<Mineral> stones = mineralRepository.findByTypeName("stone");
        assertEquals(1, stones.size());

        List<Mineral> diamonds = mineralRepository.findByTypeName("diamond");
        assertEquals(1, diamonds.size());

        List<Mineral> unknown = mineralRepository.findByTypeName("unknown");
        assertEquals(0, unknown.size());
    }

    @Test
    @DisplayName("Test mineral ID generation sequence")
    void testIdGeneration() {
        Mineral m1 = mineralRepository.save(new Gold(null));
        Mineral m2 = mineralRepository.save(new Diamond(null));
        Mineral m3 = mineralRepository.save(new Stone(null));

        assertEquals(1L, m1.getId());
        assertEquals(2L, m2.getId());
        assertEquals(3L, m3.getId());
    }
}
