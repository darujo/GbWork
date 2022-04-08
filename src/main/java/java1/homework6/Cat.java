package java1.homework6;
public class Cat extends Animal {
    private static int count = 0;
    private final int MAX_LENGTH_RUN  = 100;
    private final int MAX_LENGTH_SWIM =   0;

    public Cat(String name) {
        super(name,100,0);
        count++;
    }

    public int getCount() {
        return count;
    }

    @Override
    public void despos() {
        count--;
        super.despos();
    }




}
