package java3.homework1;

import java.util.ArrayList;

public class Box <T extends Fruit>{
    ArrayList<T> arrFruit;

    public Box() {
        this.arrFruit = new ArrayList<>();
    }

    public void add (T fruit){
        arrFruit.add(fruit);
    }
    public float getWeight(){
        float boxWeight = 0;
        for (T t : arrFruit) {
            boxWeight = boxWeight + t.getWeight();
        }
        return   boxWeight;
    }
    public void sprinkle (Box<T> arrFruitSource){
        for (T t : arrFruitSource.arrFruit) {
            arrFruit.add(t);
        }
        arrFruitSource.arrFruit.clear();
    }
    public boolean compare (Box<?> arrFruitDiff){
        return arrFruitDiff.getWeight() == this.getWeight();
    }
}
