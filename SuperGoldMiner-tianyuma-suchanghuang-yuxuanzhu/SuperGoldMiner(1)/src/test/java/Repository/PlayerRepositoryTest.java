package Repository;

import org.junit.jupiter.api.*;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PlayerRepositoryTest {

    private PlayerRepository playerRepository;
    private static final String TEST_DATA_FILE = "data/players.json";

    @BeforeAll
    void setup() {
        File file = new File(TEST_DATA_FILE);
        if (file.exists()) {
            file.renameTo(new File(TEST_DATA_FILE + ".backup"));
        }
    }

    @BeforeEach
    void init() {
        playerRepository = new PlayerRepository();
    }

    @AfterAll
    void cleanup() {
        File backupFile = new File(TEST_DATA_FILE + ".backup");
        if (backupFile.exists()) {
            new File(TEST_DATA_FILE).delete();
            backupFile.renameTo(new File(TEST_DATA_FILE));
        } else {
            new File(TEST_DATA_FILE).delete();
        }
    }

    @Test
    @DisplayName("Test saving a new player")
    void testSaveNewPlayer() {
        Player player = new Player(null, "TestPlayer", 0, 0, List.of());
        Player saved = playerRepository.save(player);

        assertNotNull(saved.getId());
        assertEquals("TestPlayer", saved.getNickname());
        assertEquals(1, playerRepository.findAll().size());
    }

    @Test
    @DisplayName("Test saving an existing player (update)")
    void testSaveExistingPlayer() {
        Player player = new Player(null, "Player1", 100, 50, List.of());
        Player saved = playerRepository.save(player);
        
        saved.setNickname("UpdatedPlayer");
        saved.setTotalScore(200);
        Player updated = playerRepository.save(saved);

        assertEquals("UpdatedPlayer", updated.getNickname());
        assertEquals(200, updated.getTotalScore());
        assertEquals(1, playerRepository.findAll().size());
    }

    @Test
    @DisplayName("Test finding player by ID")
    void testFindById() {
        Player player1 = new Player(null, "Player1", 100, 50, List.of());
        Player player2 = new Player(null, "Player2", 200, 100, List.of());
        playerRepository.save(player1);
        playerRepository.save(player2);

        Player found = playerRepository.findById(player1.getId());
        assertNotNull(found);
        assertEquals("Player1", found.getNickname());

        Player notFound = playerRepository.findById(999L);
        assertNull(notFound);
    }

    @Test
    @DisplayName("Test finding all players")
    void testFindAll() {
        assertEquals(0, playerRepository.findAll().size());

        playerRepository.save(new Player(null, "Player1", 100, 50, List.of()));
        playerRepository.save(new Player(null, "Player2", 200, 100, List.of()));

        List<Player> players = playerRepository.findAll();
        assertEquals(2, players.size());
    }

    @Test
    @DisplayName("Test deleting player by ID")
    void testDeleteById() {
        Player player = new Player(null, "ToDelete", 100, 50, List.of());
        Player saved = playerRepository.save(player);

        assertEquals(1, playerRepository.findAll().size());

        playerRepository.deleteById(saved.getId());
        assertEquals(0, playerRepository.findAll().size());
        assertNull(playerRepository.findById(saved.getId()));
    }

    @Test
    @DisplayName("Test searching player by name")
    void testSearchByName() {
        playerRepository.save(new Player(null, "Alice", 100, 50, List.of()));
        playerRepository.save(new Player(null, "Bob", 200, 100, List.of()));
        playerRepository.save(new Player(null, "AliceSmith", 300, 150, List.of()));

        List<Player> results = playerRepository.searchByName("Alice");
        assertEquals(2, results.size());

        List<Player> exactMatch = playerRepository.searchByName("Bob");
        assertEquals(1, exactMatch.size());
        assertEquals("Bob", exactMatch.get(0).getNickname());

        List<Player> noResults = playerRepository.searchByName("Unknown");
        assertEquals(0, noResults.size());
    }

    @Test
    @DisplayName("Test search with case insensitivity")
    void testSearchCaseInsensitive() {
        playerRepository.save(new Player(null, "JohnDoe", 100, 50, List.of()));

        List<Player> results = playerRepository.searchByName("johndoe");
        assertEquals(1, results.size());
        assertEquals("JohnDoe", results.get(0).getNickname());

        results = playerRepository.searchByName("JOHNDOE");
        assertEquals(1, results.size());
    }
}
