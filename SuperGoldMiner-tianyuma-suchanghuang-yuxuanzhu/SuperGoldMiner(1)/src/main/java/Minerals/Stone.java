package Minerals;

public class Stone extends Mineral {
    public Stone(Long id) {
        this.setId(id);
        this.name = "stone";
        this.value = 1;
        this.weight = 10;
    }
    public Stone() {}
    @Override
    public int calculateValue() {
        return this.value;
    }
}
