package Repository;

import Minerals.Diamond;
import Minerals.Gold;
import Minerals.Mineral;
import Minerals.Stone;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
public class MineralRepository {
// 存储所有生成过的矿物（用于查询/统计）
private final List<Mineral> minerals = new ArrayList<>();
        // 矿物ID自增生成器（确保每个矿物有唯一ID）
        private final AtomicLong idGenerator = new AtomicLong(1);

        /**
         * Create/保存矿物（CRUD-Create）
         * @param mineral 要保存的矿物对象
         * @return 保存后的矿物（自动补全ID）
         */
        public Mineral save(Mineral mineral) {
            if (mineral.getId() == null) {
                mineral.setId(idGenerator.getAndIncrement());
            }
            // 先移除旧的，再添加新的
            minerals.removeIf(m -> m.getId().equals(mineral.getId()));
            minerals.add(mineral);
            return mineral;
        }

        /**
         * Read/根据ID查询矿物（CRUD-Read）
         * @param id 矿物ID
         * @return 对应的矿物对象，不存在则返回null
         */
        public Mineral findById(Long id) {
            return minerals.stream()
                    .filter(m -> m.getId().equals(id))
                    .findFirst()
                    .orElse(null);
        }

        /**
         * Read/查询所有矿物（CRUD-Read）
         * @return 所有已生成的矿物列表
         */
        public List<Mineral> findAll() {
            return new ArrayList<>(minerals); // 返回副本，避免外部修改原列表
        }

        /**
         * Delete/根据ID删除矿物（CRUD-Delete）
         * @param id 矿物ID
         */
        public void deleteById(Long id) {
            minerals.removeIf(m -> m.getId().equals(id));
        }

        /**
         * 游戏核心功能：随机生成矿物（模拟游戏中刷新矿物）
         * @return 随机类型的矿物（黄金/钻石/石头）
         */
        public Mineral generateRandomMineral() {
            // 随机生成0-2的数字，对应不同矿物类型
            int randomType = (int) (Math.random() * 3);
            Mineral mineral;
            Long newId = idGenerator.getAndIncrement();

            switch (randomType) {
                case 0:
                    mineral = new Gold(newId); // 黄金
                    break;
                case 1:
                    mineral = new Diamond(newId); // 钻石
                    break;
                case 2:
                    mineral = new Stone(newId); // 石头
                    break;
                default:
                    mineral = new Gold(newId); // 默认生成黄金
                    break;
            }

            // 生成后自动保存到仓库
            save(mineral);
            return mineral;
        }

        /**
         * 高级查询：根据矿物类型筛选（满足Excellent等级的“过滤”需求）
         * @param typeName 矿物类型名（如“黄金”、“钻石”）
         * @return 对应类型的所有矿物
         */
        public List<Mineral> findByTypeName(String typeName) {
            return minerals.stream()
                    .filter(m -> m.getName().equals(typeName))
                    .toList();
        }
}