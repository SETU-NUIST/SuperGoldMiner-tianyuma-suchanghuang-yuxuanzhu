import Repository.PlayerRepository;

public class Run {
    public static void main(String[] args) {
        //here's the console edition,remove "//" to play this
        //GoldMinerGame game = new GoldMinerGame();
        //game.gameMenu();
        javax.swing.SwingUtilities.invokeLater(() -> {
            new MainWindow(new PlayerRepository()).setVisible(true);
        });
    }
    }

