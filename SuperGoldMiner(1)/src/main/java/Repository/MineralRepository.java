package Repository;

 import Minerals.Diamond;
import Minerals.Gold;
import Minerals.Mineral;
import Minerals.Stone;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
 public class MineralRepository {

        private final List<Mineral> minerals = new ArrayList<>();

        private final AtomicLong idGenerator = new AtomicLong(1);

        public Mineral save(Mineral mineral) {
            if (mineral.getId() == null) {
                mineral.setId(idGenerator.getAndIncrement());
            }
            minerals.removeIf(m -> m.getId().equals(mineral.getId()));
            minerals.add(mineral);
            return mineral;
        }

        public Mineral findById(Long id) {
            return minerals.stream()
                    .filter(m -> m.getId().equals(id))
                    .findFirst()
                    .orElse(null);
        }

        public List<Mineral> findAll() {
            return new ArrayList<>(minerals);
        }

        public void deleteById(Long id) {
            minerals.removeIf(m -> m.getId().equals(id));
        }

        public Mineral generateRandomMineral() {
            int randomType = (int) (Math.random() * 3);
            Mineral mineral;
            Long newId = idGenerator.getAndIncrement();

            switch (randomType) {
                case 0:
                    mineral = new Gold(newId);
                    break;
                case 1:
                    mineral = new Stone(newId);
                    break;
                default:
                    mineral = new Gold(newId);
                    break;
            }

            // save it to the repo
            save(mineral);
            return mineral;
        }

        public List<Mineral> findByTypeName(String typeName) {
            return minerals.stream()
                    .filter(m -> m.getName().equals(typeName))
                    .toList();
        }
}