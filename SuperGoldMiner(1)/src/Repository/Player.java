package Repository;

import Minerals.Mineral;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private Long id;
    private String nickname;
    private int totalScore;
    private int goldCoin;
    private List<Mineral> collectedMinerals = new ArrayList<>();

    public Player() {}

    public Player(Long id, String nickname, int totalScore,int goldCoin,List<Mineral> collectedMinerals) {
        this.id = id;
        this.nickname = nickname;
        this.totalScore = totalScore;
        this.collectedMinerals = collectedMinerals;
    }
    public void collectMineral(Mineral mineral) {
        this.collectedMinerals.add(mineral);
        this.totalScore += mineral.calculateValue();
        this.goldCoin += mineral.getValue();
    }

    public int getGoldCoin() {
        return goldCoin;
    }

    public void setGoldCoin(int goldCoin) {
        this.goldCoin = goldCoin;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public List<Mineral> getCollectedMinerals() {
        return collectedMinerals;
    }

    public void setCollectedMinerals(List<Mineral> collectedMinerals) {
        this.collectedMinerals = collectedMinerals;
    }
}
