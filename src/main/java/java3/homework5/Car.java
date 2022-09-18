package java3.homework5;

import java.util.concurrent.atomic.AtomicBoolean;

public class Car implements Runnable {
    private static int CARS_COUNT;
    static {
        CARS_COUNT = 0;
    }
    private Race race;
    private int speed;
    private String name;
    public String getName() {
        return name;
    }
    private static AtomicBoolean win = new AtomicBoolean();
    public int getSpeed() {
        return speed;
    }
    public Car(Race race, int speed) {
        this.race = race;
        this.speed = speed;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
    }
    @Override
    public void run() {
        try {
            try {
                System.out.println(this.name + " готовится");
                Thread.sleep(500 + (int) (Math.random() * 800));
                System.out.println(this.name + " готов");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                MainClass.preparationIsComplete.countDown();

            }

            try {
                MainClass.raceStart.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            for (int i = 0; i < race.getStages().size(); i++) {
                race.getStages().get(i).go(this);

                if (i + 1 >= race.getStages().size() && !win.get()) {
                    win.set(true);
                    System.out.println(this.name + " WIN");
                }

                MainClass.lockPrint.writeLock().unlock();
            }


        }
        finally {
            MainClass.raceIsComplete.countDown();
        }
    }
}