package java2.homework1;

public class Treadmill implements Obstacle{
    private final int length;

    public Treadmill(int length) {
        this.length = length;
    }

    public boolean pass (Participant Object){
        return Object.run(length);
    }
}
