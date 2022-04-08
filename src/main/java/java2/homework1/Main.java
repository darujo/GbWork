package java2.homework1;

import java.util.Random;

public class Main {

    private   static final Random rand = new Random();
    private   static final int QUANTITY_PARTICIPANT = 10;
    private   static final int QUANTITY_OBSTACLE = 10;

    public static void main(String[] args) {
        Participant [] participant = new Participant [QUANTITY_PARTICIPANT] ;
        Obstacle [] obstacle = new Obstacle [QUANTITY_OBSTACLE];
        for (int i = 0; i < participant.length;) {
           int nomObj = rand.nextInt(3);
           if (nomObj == 1){
             participant[i] =  new People( ++i,rand.nextInt(1100),rand.nextInt(70));
           }
           else if (nomObj==2){
             participant[i] = new Cat( ++i,rand.nextInt(50),rand.nextInt(90));
           }
           else {
               participant[i] = new Robot(  ++i,rand.nextInt(3000),rand.nextInt(50));

           }

        }
        for (int i = 0; i < obstacle.length; i++) {
            int nomObj = rand.nextInt(2);
            if (nomObj == 1){
                obstacle[i] =  new Treadmill(rand.nextInt(1000));
            }
            else {
                obstacle[i] = new Wall(rand.nextInt(60));

            }

        }
        for (Participant part : participant) {
            System.out.println(part.toString());

            for (int i = 0; i < obstacle.length; i++) {
                if (!obstacle[i].pass(part)) {
                    System.out.println("Не смог преодолеть препятствие " + (i + 1));
                    break;
                }
            }
            System.out.println();

        }

    }

}
