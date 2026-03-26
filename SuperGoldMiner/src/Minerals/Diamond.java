package Minerals;

public class Diamond extends Mineral {
    public Diamond(Long id) {
        this.setId(id);
        this.name = "diamond";
        this.value = 500; //
        this.weight = 2; //
    }
    public Diamond() {}
    @Override
    public int calculateValue() {
        return this.value;
    }
}
