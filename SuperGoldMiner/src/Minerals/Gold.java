package Minerals;

public class Gold extends Mineral {
    public Gold(Long id) {
        this.id = id;
        this.name = "gold";
        this.value = 100;
        this.weight = 5;
    }
    public Gold(){}
    @Override
    public int calculateValue() {
        return value * weight;
    }

}
