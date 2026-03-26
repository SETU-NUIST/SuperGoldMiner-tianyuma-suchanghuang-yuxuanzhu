
import Repository.Player;
import Repository.PlayerRepository;

import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
public class MainWindow extends JFrame {
        private final PlayerRepository playerRepo;
        private JTextArea displayArea;

        // 构造：启动窗口
        public MainWindow(PlayerRepository playerRepo) {
            this.playerRepo = playerRepo;
            initUI();
        }

        // 初始化界面
        private void initUI() {
            setTitle("Super Gold Miner - Swing Edition");
            setSize(650, 500);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setLayout(new BorderLayout());

            // 显示区域
            displayArea = new JTextArea();
            displayArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
            add(new JScrollPane(displayArea), BorderLayout.CENTER);

            // 按钮面板
            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(2, 4));

            // 所有功能按钮
            panel.add(createBtn("1.Create the player", e -> createPlayer()));
            panel.add(createBtn("2.Choose the player", e -> choosePlayer()));
            panel.add(createBtn("3.Check the score", e -> showScores()));
            panel.add(createBtn("4.Update the player's name", e -> updateName()));
            panel.add(createBtn("5.Delete the player", e -> deletePlayer()));
            panel.add(createBtn("6.Search the player", e -> searchPlayer()));
            panel.add(createBtn("7.Clear", e -> displayArea.setText("")));
            panel.add(createBtn("0.exit", e -> System.exit(0)));

            add(panel, BorderLayout.SOUTH);
            setLocationRelativeTo(null);
        }

        // 创建按钮
        private JButton createBtn(String text, java.awt.event.ActionListener listener) {
            JButton btn = new JButton(text);
            btn.setFont(new Font("Arial", Font.BOLD, 12));
            btn.addActionListener(listener);
            return btn;
        }

        // --------------------- 功能实现 ---------------------
        private void createPlayer() {
            String name = JOptionPane.showInputDialog("Input your name (2-8):");
            if (name == null || name.length() < 2 || name.length() > 8) {
                showMsg("Invalid name!");
                return;
            }
            Player p = new Player(null, name, 0, 0, new java.util.ArrayList<>());
            playerRepo.save(p);
            showMsg("Create successful!");
        }

        private void choosePlayer() {
            String idStr = JOptionPane.showInputDialog(" Input ID:");
            try {
                long id = Long.parseLong(idStr);
                Player p = playerRepo.findById(id);
                if (p == null) {
                    showMsg("Player not found!");
                    return;
                }
                showMsg("Welcome：" + p.getNickname());
            } catch (Exception e) {
                showMsg("Invalid ID!");
            }
        }

        private void showScores() {
            List<Player> list = playerRepo.findAll();
            if (list.isEmpty()) {
                displayArea.setText("Player list is empty!");
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("======= Score =======\n");
            for (Player p : list) {
                sb.append("ID: %-2d | Name: %-8s | Score: %d\n"
                        .formatted(p.getId(), p.getNickname(), p.getTotalScore()));
            }
            displayArea.setText(sb.toString());
        }

        private void updateName() {
            String idStr = JOptionPane.showInputDialog("Please input ID you want to update:");
            try {
                long id = Long.parseLong(idStr);
                Player p = playerRepo.findById(id);
                if (p == null) {
                    showMsg("Player not found!");
                    return;
                }
                String newName = JOptionPane.showInputDialog("New Name:");
                p.setNickname(newName);
                playerRepo.save(p);
                showMsg("Update successful!");
            } catch (Exception e) {
                showMsg("wrong");
            }
        }

        private void deletePlayer() {
            String idStr = JOptionPane.showInputDialog("Input ID you want to delete:");
            try {
                long id = Long.parseLong(idStr);
                playerRepo.deleteById(id);
                showMsg("delete successful!");
            } catch (Exception e) {
                showMsg("wrong");
            }
        }

        private void searchPlayer() {
            String key = JOptionPane.showInputDialog("search the keyword:");
            List<Player> res = playerRepo.searchByName(key);
            if (res.isEmpty()) {
                displayArea.setText("no results found!");
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("======= Search Result =======\n");
            for (Player p : res) {
                sb.append("ID: %-2d | Name: %s\n".formatted(p.getId(), p.getNickname()));
            }
            displayArea.setText(sb.toString());
        }

        private void showMsg(String msg) {
            JOptionPane.showMessageDialog(this, msg);
        }
    }

