package java1.homework7;

public class MainClass {
    public static void main(String[] args) {
        Cat [] cat= new Cat [10] ;
        for (int i = 0; i <cat.length ; i++) {
           cat[i]= new Cat("Мурка_" + i, 15);
        }
        Plate plate = new Plate(100);
        boolean isFull = feedCat(plate,cat);
        plate.info();
        while (!isFull){

            plate.addFood(10);
            isFull = feedCat(plate,cat);
            plate.info();
        }

    }
    private static boolean feedCat(Plate plate,Cat [] cat){
        boolean isFull = true;
        for (Cat value : cat) {
            if (isFull) {
                value.eat(plate);
                isFull = value.getFull();
            }
            System.out.println(value.isFull());

        }
        return isFull;
    }

}
