package Repository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PlayerRepository {
    private final AtomicLong idGenerator = new AtomicLong(1);
    private List<Player> players = new ArrayList<>();
    private static final String DATA_FILE = "data/players.json";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public PlayerRepository() {
        loadDataFromJson();
    }

    public Player save(Player player) {
        if (player.getId() == null) {
            player.setId(idGenerator.getAndIncrement());
        }
        players.removeIf(p -> p.getId().equals(player.getId()));
        players.add(player);
        saveDataToJson();
        return player;
    }

    public Player findById(Long id) {
        return players.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Player> findAll() {
        return new ArrayList<>(players);
    }

    public void deleteById(Long id) {
        players.removeIf(p -> p.getId().equals(id));
        saveDataToJson();
    }
    public List<Player> searchByName(String keyword) {
        return players.stream()
                .filter(player -> player.getNickname().toLowerCase().contains(keyword.toLowerCase()))
                .toList();
    }
    private void loadDataFromJson() {
        try {
            File file = new File(DATA_FILE);
            if (file.exists()) {
                players = objectMapper.readValue(file, new TypeReference<List<Player>>() {});
                System.out.println("✅ Player data loaded successfully！Total：" + players.size());
                if (!players.isEmpty()) {
                    long maxId = players.stream().mapToLong(Player::getId).max().getAsLong();
                    idGenerator.set(maxId + 1);
                }
            } else {
                file.getParentFile().mkdirs();
                System.out.println("ℹ️ Players not found!，please create a new one");
            }
        } catch (Exception e) {
            e.printStackTrace();
            players = new ArrayList<>();
            System.out.println("❌ Player data failed to load");
        }
    }

    private void saveDataToJson() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(DATA_FILE), players);
        } catch (Exception e) {
            System.out.println("❌ Player data could not be saved!");
            e.printStackTrace();
        }
    }
}
