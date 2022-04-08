package java1.homework1;

public class HomeWorkApp {
    public static void main(String[] args) {
        printThreeWords();
        checkSumSign();
        for (int number1 = -3; number1 <= 3 ;number1++){
            for (int number2 = number1 - 1; number2 <= 0;number2++){
                System.out.print(number1 + " + " + number2 + " = ");
                checkSumSign(number1,number2);
            }
        }
        printColor();
        for (int number1 = -1; number1 <= 101 ;number1++){
            System.out.print(number1 + " ");
            printColor(number1);
        }
    }

    public static void printThreeWords() {
        System.out.println("Orange");
        System.out.println("Banana");
        System.out.println("Apple");

    }
    public static void checkSumSign(){
        int a = 10,b=-15;
        checkSumSign(a,b);
    }
    public static void checkSumSign(int iNumber1 ,int iNumber2){
        if (iNumber1 + iNumber2 >= 0) {
            System.out.println("Сумма положительная");
        } else {
            System.out.println("Сумма отрицательная");
        }
    }
    public static void printColor(){
        int value = 10;
        printColor(value);
    }
    public static void printColor(int iValue){
        if        (iValue <= 0  ) {
            System.out.println("Красный");
        } else if (iValue <= 100) {
            System.out.println("Желтый");
        } else
            System.out.println("Зеленый");


    }
}

