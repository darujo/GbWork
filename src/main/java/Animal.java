public abstract class  Animal {
    private static int count = 0;
    private final String name;
    private final int maxLengthRun;
    private final int maxLengthSwim;

    public Animal (String name,int maxLengthRun,int maxLengthSwim) {
        count++;
        this.name = name;
        this.maxLengthRun = maxLengthRun;
        this.maxLengthSwim = maxLengthSwim;

    }
    public void despos(){
        count--;
    }
    public abstract int getCount();

    public int getCountAnimal(){
        return count;
    }

    public String getName() {
        return name;
    }
    public void run (int length){
        if (maxLengthRun <=0) {
            System.out.println(name + " не умею бегать");
        }
        else if(length > maxLengthRun){
            System.out.println(name         + " не могу пробежать "
                             + length       + " метров. Могу пробежать не более "
                             + maxLengthRun + " метров.");
        }
        else {
            System.out.println(name + " пробежал " + length + " метров"  );
        }
    }

    public void swim(int length){
        if (maxLengthSwim <=0) {
            System.out.println(name + " не умею плавать");
        }
        else if(length > maxLengthSwim){
            System.out.println(name         + " не могу проплыть "
                             + length       + " метров. Могу проплыть не более "
                             + maxLengthRun + " метров.");
        }
        else {
            System.out.println(name + " пробежал " + length + " метров"  );
        }
    }

}
