package java3.homework5;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MainClass {
    public static final int CARS_COUNT = 4;
    public static final CountDownLatch preparationIsComplete = new CountDownLatch(CARS_COUNT);
    public static final CountDownLatch raceIsComplete = new CountDownLatch(CARS_COUNT);

    public static ReadWriteLock lockPrint;

    public static final CountDownLatch raceStart = new CountDownLatch(1);
    public static void main(String[] args) throws InterruptedException {
        lockPrint = new ReentrantReadWriteLock();
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");
        Race race = new Race(new Road(60), new Tunnel(), new Road(40));
        Car[] cars = new Car[CARS_COUNT];
        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(race, 20 + (int) (Math.random() * 10));
        }
        for (int i = 0; i < cars.length; i++) {
            new Thread(cars[i]).start();
        }
        preparationIsComplete.await();
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");
        raceStart.countDown();
        raceIsComplete.await();
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");
    }
}




