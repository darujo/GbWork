package java2.homework1;

public class Wall implements Obstacle{
    private final int height ;
    public Wall(int height) {
        this.height =height;
    }

    public boolean pass (Participant Object){
        return Object.jump(height);
    }
}
