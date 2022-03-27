package java1.homework7;

import com.sun.org.apache.bcel.internal.generic.RETURN;

public class Cat {
    private final String name;
    private final int appetite;
    private boolean full;

    public Cat(String name, int appetite) {
        this.name = name;
        this.appetite = appetite;
    }
    public void eat(Plate p) {
        if (!full) {
            full = p.decreaseFood(appetite);
        }
    }
    public boolean getFull() {
        return full;
    }
    public String isFull() {
        String isFule;
        if(full){
            isFule= name +  " сыт.";
        }
        else{
            isFule= name +" голоден";
        }
        return isFule;
    }


}
