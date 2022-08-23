package java3.homework1;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        //1. Написать метод, который меняет два элемента массива местами.(массив может быть любого ссылочного типа);
        Integer [] arr = {1,2,3,4,5};
        changeElement(arr,1,3);
        System.out.println(Arrays.toString(arr) );

        //2. Написать метод, который преобразует массив в ArrayList;
        System.out.println(arrayToArrayList(arr));

        //3. Большая задача:
        //a. Есть классы Fruit -> Apple, Orange;(больше фруктов не надо)
        //b. Класс Box в который можно складывать фрукты, коробки условно сортируются по типу фрукта, поэтому в одну
        // коробку нельзя сложить и яблоки, и апельсины;
        //c. Для хранения фруктов внутри коробки можете использовать ArrayList;
        Box<Apple> appleBox = new Box<>();
        appleBox.add(new Apple());
        appleBox.add(new Apple());
        appleBox.add(new Apple());
        appleBox.add(new Apple());
        Box<Orange> orangeBox = new Box<>();
        orangeBox.add(new Orange());
        orangeBox.add(new Orange());
        orangeBox.add(new Orange());
        orangeBox.add(new Orange());

        //d. Сделать метод getWeight() который высчитывает вес коробки, зная количество фруктов и вес одного фрукта
        // (вес яблока - 1.0f, апельсина - 1.5f, не важно в каких это единицах);
        System.out.println("Вес яблок " + appleBox.getWeight());
        System.out.println("Вес апельсинов " + orangeBox.getWeight());

        //e. Внутри класса коробка сделать метод compare, который позволяет сравнить текущую коробку с той, которую
        // подадут в compare в качестве параметра, true - если их веса равны, false в противном случае(коробки с
        // яблоками мы можем сравнивать с коробками с апельсинами);
        System.out.println("Вес коробки с апельсинами равен весу коробки с яблоками? " + orangeBox.compare(appleBox));

        //f. Написать метод, который позволяет пересыпать фрукты из текущей коробки в другую коробку(помним про
        // сортировку фруктов, нельзя яблоки высыпать в коробку с апельсинами), соответственно в текущей коробке фруктов
        // не остается, а в другую перекидываются объекты, которые были в этой коробке;
        Box<Apple> appleBox2 =new Box<>();
        appleBox2.add(new Apple());
        appleBox2.add(new Apple());
        System.out.println("Вес коробки 1 с яблоками = " + appleBox.getWeight() + " Вес коробки 2 с яблоками = " + appleBox2.getWeight());
        System.out.println("Пересыпаем содержимое коробки 2 с яблоками в коробку 1 с яблоками");
        appleBox.sprinkle(appleBox2);
        System.out.println("Вес коробки 1 с яблоками = " + appleBox.getWeight() + " Вес коробки 2 с яблоками = " + appleBox2.getWeight());

        System.out.println("Вес коробки с апельсинами равен весу коробки с яблоками? " + orangeBox.compare(appleBox));

        //g. Не забываем про метод добавления фрукта в коробку.
    }
    public static void changeElement (Object[] arr,int oneElement,int twoElement ){
        Object element;
        if (arr.length < oneElement || 0 >= oneElement){
            System.out.println("Элемент 1 за пределами списка");
        }
        else if (arr.length < twoElement || 0 >= twoElement){
            System.out.println("Элемент 2 за пределами списка");
        } else {
            element = arr[oneElement - 1 ];
            arr[oneElement - 1] = arr[twoElement - 1];
            arr[twoElement - 1]= element;

        }
    }
    public static ArrayList<Object> arrayToArrayList (Object[] arr ){
        ArrayList<Object> arrOut = new ArrayList<>();
        for (Object o : arr) {
            arrOut.add(o);
        }
        return arrOut;
    }
}
