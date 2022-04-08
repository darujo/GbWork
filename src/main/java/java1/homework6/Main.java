package java1.homework6;
public class Main {
    public static void main(String[] args) {
        Cat cat = new Cat("Барсик");
        Cat cat2 = new Cat("Мурка");
        cat.swim(10);
        cat.run(25);
        cat2.run(101);
        Dog dog  = new Dog("Бобик");
        dog.swim(10);
        dog.swim(25);
        dog.run(505);

        System.out.println("Кошек    : " + cat.getCount());
        System.out.println("Собак    : " + dog.getCount());
        System.out.println("Животных : " + cat2.getCountAnimal());

    }
}
