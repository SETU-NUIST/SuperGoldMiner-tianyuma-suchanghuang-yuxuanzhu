import Repository.PlayerRepository;

public class Run {
    public static void main(String[] args) {
        //控制台版
        //GoldMinerGame game = new GoldMinerGame();
        //game.gameMenu();
        //Swing版
        javax.swing.SwingUtilities.invokeLater(() -> {
            new MainWindow(new PlayerRepository()).setVisible(true);
        });
    }
    }

