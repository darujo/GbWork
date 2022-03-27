package java1.homework7;

public class Plate {
    private int food;
    private static final int MAX_FOOD = 97;
    public Plate(int food) {
        addFood( food);
    }
    public boolean decreaseFood(int n) {
        if (food >= n) {
            food -= n;
            return true;
        }
        return false;
    }
    public void info() {
        System.out.println("В тарелке " + food + " еды");
    }
    public void addFood(int food) {
        this.food +=food;
        int returnFood = 0;
        if(this.food> MAX_FOOD){
            returnFood = this.food - MAX_FOOD;
            this.food = MAX_FOOD;
        }
        if (returnFood !=0){
            System.out.println("В тарелку не влезло " + returnFood + " еды" );
        }

    }
}

