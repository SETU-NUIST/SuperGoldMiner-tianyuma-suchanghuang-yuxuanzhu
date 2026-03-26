import Minerals.Mineral;
import Repository.MineralRepository;
import Repository.Player;
import Repository.PlayerRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class GoldMinerGame{
    private final PlayerRepository playerRepo = new PlayerRepository();
    private final MineralRepository mineralRepo = new MineralRepository();
    private final Scanner scanner = new Scanner(System.in);
    public void delay(){
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {}
    }
    public void gameMenu() {
        //想要先执行一遍再判断choice
        int choice;
        do {
            System.out.println("SuperGoldMiner-----------Now Start!");
            System.out.println("1. create player");
            System.out.println("2. choose your player(through self-increment id like 1,2,3....)");
            System.out.println("3. check the player's score");
            System.out.println("4. modify the player's name");
            System.out.println("5. delete the player");
            System.out.println("6. search the player by name");
            System.out.println("Now,please enter your choice ： ");
            while (!scanner.hasNextInt()) {
                System.out.print("Something went wrong, please try again: ");
                scanner.next();
            }
            choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    createPlayer();
                    delay();
                    break;
                case 2:
                    choosePlayerAndStartGame();
                    delay();
                    break;
                case 3:
                    showPlayerScores();
                    delay();
                    break;
                case 4:
                    updatePlayer();
                    delay();
                    break;
                case 5:
                    deletePlayer();
                    delay();
                    break;
                case 6:
                    searchPlayerByName();
                    delay();
                    break;
                case 0:
                    System.out.println("Goodbye!");
                    delay();
                    break;
                default:
                    System.out.println("Wrong choice, please try again");
            }
        }while (choice != 0);
    }
    private void createPlayer() {
        System.out.print("Please enter player's name: ");
        String playerName = scanner.nextLine();
        while (playerName.length() < 2||playerName.length() >8 ) {
            System.out.println("The length of the name must be between 2 and 8 characters! Please try again:");
            playerName= scanner.nextLine();
        }
        Player player = new Player(null,playerName,0,0,new ArrayList<>());
        playerRepo.save(player);
        System.out.println("Player created successfully!");
    }
    private void choosePlayerAndStartGame() {
        System.out.print("Please enter player's id you want to choose: ");
        while (!scanner.hasNextLong()) {
            System.out.print("ID must be a long number!please try again: ");
            scanner.next();
        }
        Long playerId = scanner.nextLong();
        scanner.nextLine();
        Player currentPlayer = playerRepo.findById(playerId);
        if (currentPlayer == null) {
            System.out.println("Player not found!");
            return;
        }
        System.out.println("\n===== " + currentPlayer.getNickname() + "'s game =====");
        System.out.println("Game Rule:press 'enter' to grab mineral and three times for each turn");
        int grabCount = 5;
        while (grabCount > 0) {
            System.out.println("The remaining number of grabs :"+grabCount);
            System.out.println("Please press 'enter' to start grab mineral");
            scanner.nextLine();
            Mineral mineral = mineralRepo.generateRandomMineral();
            if(mineral.getName()=="stone"){
                System.out.println("Bad luck! You have grabbed"+mineral.getName());
            }
            else {
                System.out.println("you have grabbed"+mineral.getName()+",Congratulations!");
            }
            System.out.println("The value of it:"+mineral.calculateValue()+",and you have got the number of goldCoin:"+mineral.getValue());
            currentPlayer.collectMineral(mineral);
            playerRepo.save(currentPlayer);
            grabCount--;
        }
        System.out.println("\n======Settlement of this bureau======");
        System.out.println("Total score :"+currentPlayer.getTotalScore());
        System.out.println("Total goldCoins :"+currentPlayer.getGoldCoin());
        System.out.println("Collected minerals :"+currentPlayer.getCollectedMinerals().size());
    }
    private void showPlayerScores() {
       List<Player> players = playerRepo.findAll();
       if (players.isEmpty()) {
       System.out.println("No players found!");
           return;
       }
        System.out.println("\n=====Player score list=====");
       for (Player player : players) {
           System.out.println("ID : "+player.getId()+" | nickName : "+player.getNickname()+" | GoldCoin : "+player.getGoldCoin()+" | totalScore : "+player.getTotalScore());
       }
    }
    private void updatePlayer() {
        System.out.print("Please enter player's id you want to update: ");
        Long playerId = scanner.nextLong();
        scanner.nextLine();
        Player player = playerRepo.findById(playerId);
        if (player == null) {
            System.out.println("Player not found!");
            return;
        }
        System.out.print("Please enter player's name you want to update: ");
        String playerName = scanner.nextLine();
        while (playerName.length() < 2||playerName.length() >8 ) {
            System.out.println("The length of the name must be between 2 and 8 characters!Please enter again");
            playerName = scanner.nextLine();
        }
        player.setNickname(playerName);
        playerRepo.save(player);
        System.out.println("Player updated successfully!");
    }
    private void deletePlayer() {
        System.out.print("Please enter player's id you want to delete: ");
        Long playerId = scanner.nextLong();
        playerRepo.deleteById(playerId);
        System.out.println("Player deleted successfully!");
    }
    private void searchPlayerByName() {
        System.out.print("Please enter the keyword you want to search: ");
        String keyword = scanner.nextLine();

        // 调用你写的模糊搜索方法
        List<Player> resultList = playerRepo.searchByName(keyword);

        if (resultList.isEmpty()) {
            System.out.println("No matching players found!");
            return;
        }

        System.out.println("\n===== Search Result =====");
        for (Player player : resultList) {
            System.out.println("ID: " + player.getId()
                    + " | Name: " + player.getNickname()
                    + " | Score: " + player.getTotalScore());
        }
    }
}




