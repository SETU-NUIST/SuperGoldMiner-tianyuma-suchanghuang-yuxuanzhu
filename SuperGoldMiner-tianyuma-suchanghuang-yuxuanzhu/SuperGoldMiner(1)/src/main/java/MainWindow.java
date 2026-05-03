import Minerals.Mineral;
import Repository.MineralRepository;
import Repository.Player;
import Repository.PlayerRepository;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainWindow extends JFrame {

    private final PlayerRepository playerRepo;
    private JTextArea displayArea;
    private BackgroundPanel mainPanel;
    private int grabCount;
    private Player currentPlayer;

    private class BackgroundPanel extends JPanel {
        private final Image bg;
        private final Image bg1;
        private final Image peo;
        private final Image hook;

        private final double length = 100;       // normal length
        private double angle = 0.5;
        private int dir = 1;
        private boolean rotating = true;  // rotating switch

        // about grabbing
        private boolean isGrabbing = false;
        private boolean isExtending = true;
        private double currentLength = length;
        private final double extendSpeed = 14;
        private final double maxExtend = 350;

        private final int lineX1, lineY1;
        private final Color lineColor = Color.RED;

        private Runnable grabCompleteCallback;

        public BackgroundPanel(Image image1, Image image2, Image image3,Image image4, int x1, int y1) {
            this.bg = image1;
            this.bg1 = image2;
            this.peo = image3;
            this.hook = image4;
            this.lineX1 = x1;
            this.lineY1 = y1;
            //setLayout(new BorderLayout());This part is never been used,so I add annotation temporarily here.
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int w = getWidth();
            int h = getHeight();
            if (bg1 != null) g.drawImage(bg1, 0, 0, w, 200, this);
            if (bg != null) g.drawImage(bg, 0, 200, w, h - 200, this);
            if (peo != null) g.drawImage(peo, 310, 50, 150, 150, this);
            List<Object> objects = new ArrayList<>();
            for (int i = 1; i <= 2; i++) {
                objects.add(new GoldPic());
            }
            for (int i = 1; i <= 2; i++) {
                objects.add(new Rock());
            }
                for (Object o : objects) {
                    o.paintSelf(g);
                }

            g.setColor(lineColor);
            int lineX2 = (int) (lineX1 + currentLength * Math.cos(angle * Math.PI));
            int lineY2 = (int) (lineY1 + currentLength * Math.sin(angle * Math.PI));
            g.drawLine(lineX1-1, lineY1, lineX2-1, lineY2);
            g.drawLine(lineX1, lineY1, lineX2, lineY2);
            g.drawLine(lineX1+1, lineY1, lineX2+1, lineY2);
            g.drawImage(hook, lineX2-36, lineY2-2, this);
        }

        public void update() {
            updateGrab();
            updateAngle();
        }

        private void updateAngle() {
            if (!rotating || isGrabbing) return;
            if (angle < 0.1) dir = 1;
            else if (angle > 0.9) dir = -1;
            angle += 0.02 * dir;
        }

        private void updateGrab() {
            if (!isGrabbing) return;

            if (isExtending) {
                if (currentLength < maxExtend) {
                    currentLength += extendSpeed;
                } else {
                    isExtending = false;
                }
            } else {
                currentLength -= extendSpeed;
                if (currentLength <= length) {
                    currentLength = length;
                    isGrabbing = false;
                    rotating = true;

                    // 抓取完成后执行回调（在 EDT 上）
                    if (grabCompleteCallback != null) {
                        Runnable cb = grabCompleteCallback;
                        grabCompleteCallback = null;
                        SwingUtilities.invokeLater(cb);
                    }
                }
            }
        }

        public void doGrab(Runnable onComplete) {
            if (isGrabbing) return;
            rotating = false;
            isGrabbing = true;
            isExtending = true;
            currentLength = length;
            this.grabCompleteCallback = onComplete;
        }
    }

    // ============== Constructor ==============
    public MainWindow(PlayerRepository playerRepo) {
        this.playerRepo = playerRepo;
        initUI();
        startAnimationLoop();
    }

    private void initUI() {
        setTitle("Super Gold Miner - Swing Edition");
        setSize(768, 1000);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Image bg = new ImageIcon(getClass().getResource("/imgs/bg.jpg")).getImage();
        Image bg1 = new ImageIcon(getClass().getResource("/imgs/bg1.jpg")).getImage();
        Image peo = new ImageIcon(getClass().getResource("/imgs/peo.png")).getImage();
        Image hook =  new ImageIcon(getClass().getResource("/imgs/hook.png")).getImage();
        mainPanel = new BackgroundPanel(bg, bg1, peo,hook,380, 180);
        setContentPane(mainPanel);

        mainPanel.setLayout(new BorderLayout());

        displayArea = new JTextArea();
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        displayArea.setOpaque(false);
        displayArea.setEditable(false);

        JScrollPane sp = new JScrollPane(displayArea);
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);
        mainPanel.add(sp, BorderLayout.CENTER);

        JPanel panel = new JPanel(new GridLayout(2, 4));
        panel.setOpaque(false);

        panel.add(createBtn("1.Create Player", e -> createPlayer()));
        panel.add(createBtn("2.Choose Player and play", e -> choosePlayerAndPlay()));
        panel.add(createBtn("3.Check the score", e -> showScores()));
        panel.add(createBtn("4.Update Player", e -> updateName()));
        panel.add(createBtn("5.Delete Player", e -> deletePlayer()));
        panel.add(createBtn("6.Search Player", e -> searchPlayer()));
        panel.add(createBtn("7.Clear", e -> displayArea.setText("")));
        panel.add(createBtn("0.Exit", e -> System.exit(0)));

        mainPanel.add(panel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
    }

    private void startAnimationLoop() {
        Timer timer = new Timer(20, e -> {
            mainPanel.update();
            mainPanel.repaint();
        });
        timer.start();
    }

    private JButton createBtn(String text, java.awt.event.ActionListener listener) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.addActionListener(listener);
        return btn;
    }

    //Here are some logics
    private void createPlayer() {
        String name = JOptionPane.showInputDialog(this, "Input your name (2-8):");
        if (name == null || name.length() < 2 || name.length() > 8) {
            showMsg("Invalid name!");
            return;
        }
        Player p = new Player(null, name, 0, 0, new ArrayList<>());
        playerRepo.save(p);
        showMsg("Create successful!");
    }

    private void choosePlayerAndPlay() {
        String idStr = JOptionPane.showInputDialog(this, "Input ID you want to choose:");
        if (idStr == null) return;
        try {
            long id = Long.parseLong(idStr);
            Player p = playerRepo.findById(id);
            if (p == null) { showMsg("Player not found!"); return;
            }
            currentPlayer = p;
            displayArea.setText("");
            displayArea.append("===== " + p.getNickname() + "'s game =====\n");
            displayArea.append("Press OK to start grabbing\n\n");

            grabCount = 5;
            startNextGrab();
        } catch (NumberFormatException ex) {
            showMsg("Invalid ID!");
        }
    }

    // (modeless JDialog) because before,when our dialog appears,the animation will stop,so I let AI do a special JDialog
    private void startNextGrab() {
        if (grabCount <= 0) {
            displayArea.append("\n====== Settlement ======\n");
            displayArea.append("Total score: " + currentPlayer.getTotalScore() + "\n");
            displayArea.append("Gold coin: " + currentPlayer.getGoldCoin() + "\n");
            return;
        }

        displayArea.append("\nRemaining times: " + grabCount + "\n");

        // create（modeless)
        JDialog dlg = new JDialog(this, "Press OK to grab", Dialog.ModalityType.MODELESS);
        JPanel content = new JPanel(new BorderLayout(10, 10));
        content.add(new JLabel("Press OK to grab!"), BorderLayout.CENTER);
        JButton ok = new JButton("OK");
        JButton cancel = new JButton("Cancel");
        JPanel btns = new JPanel();
        btns.add(ok);
        btns.add(cancel);
        content.add(btns, BorderLayout.SOUTH);
        dlg.setContentPane(content);
        dlg.pack();
        dlg.setResizable(false);
        dlg.setLocationRelativeTo(this);

        ok.addActionListener(e -> {
            dlg.dispose();
            // press OK to play
            mainPanel.doGrab(this::onGrabComplete);
        });

        cancel.addActionListener(e -> {
            dlg.dispose();
            // 如果用户取消，本次不消耗抓取次数，或者你可改为消耗一次 grabCount-- 后调用 startNextGrab()
        });

        // 显示非模态对话框（不会阻塞 EDT）
        dlg.setVisible(true);
    }

    // 抓取完成的回调（在 EDT 上）
    private void onGrabComplete() {
        Mineral mineral = new MineralRepository().generateRandomMineral();
        String msg;
        if ("stone".equals(mineral.getName())) {
            msg = "Bad luck! You got a stone.\n";
        } else {
            msg = "Good job! You got " + mineral.getName() + "!\n";
        }
        displayArea.append(msg);
        displayArea.append("Value: " + mineral.calculateValue() + "\n");

        currentPlayer.collectMineral(mineral);
        playerRepo.save(currentPlayer);

        grabCount--;
        // 继续下一次抓取
        startNextGrab();
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
            sb.append("ID: %-2d | Name: %-8s | Score: %d\n".formatted(p.getId(), p.getNickname(), p.getTotalScore()));
        }
        displayArea.setText(sb.toString());
    }

    private void updateName() {
        String idStr = JOptionPane.showInputDialog(this, "Please input ID you want to update:");
        if (idStr == null) return;
        try {
            long id = Long.parseLong(idStr);
            Player p = playerRepo.findById(id);
            if (p == null) { showMsg("Player not found!"); return; }
            String newName = JOptionPane.showInputDialog(this, "New Name:");
            if (newName != null) {
                p.setNickname(newName);
                playerRepo.save(p);
                showMsg("Update successful!");
            }
        } catch (NumberFormatException ex) {
            showMsg("wrong");
        }
    }

    private void deletePlayer() {
        String idStr = JOptionPane.showInputDialog(this, "Input ID you want to delete:");
        if (idStr == null) return;
        try {
            long id = Long.parseLong(idStr);
            playerRepo.deleteById(id);
            showMsg("delete successful!");
        } catch (NumberFormatException ex) {
            showMsg("wrong");
        }
    }

    private void searchPlayer() {
        String key = JOptionPane.showInputDialog(this, "search the keyword:");
        if (key == null) return;
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