public class Dog extends Animal{
    private static int count;
    public Dog(String name) {
        super(name,500,100);
        count++;
    }

    public int getCount() {
        return count;
    }

}
