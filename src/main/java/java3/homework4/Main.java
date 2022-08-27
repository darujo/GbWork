package java3.homework4;

public class Main {
    //Создать три потока, каждый из которых выводит определенную букву (A, B и C) 5 раз (порядок – ABСABСABС). Используйте wait/notify/notifyAll.
    static final String [] chars  = new String[]{"A", "B","C"};
    private static final int SIZE_PRINT = 5;
    static String currentChar;
    private static final Object mon = new Object();

    public static void main(String[] args) {
        currentChar = chars[0];
        for (int i = 0; i < chars.length; i++) {
            int finalI = i;
            Thread t = new Thread(()->  printChar(finalI));
            t.start();
        }
    }

    private static void printChar(int finalI)  {
        synchronized (mon) {
            for (int j = 0; j < SIZE_PRINT; j++) {
                while (!currentChar.equals(chars[finalI])) {
                    try {
                        mon.wait();
                    } catch (InterruptedException e) {
                        System.out.println(e.getMessage());
                    }
                }
                System.out.println(currentChar);
                currentChar = chars[finalI + 1 == chars.length ? 0 : finalI + 1];
                mon.notifyAll();
            }
        }
    }
}
