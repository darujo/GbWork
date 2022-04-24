package java2.homework5;

import java.util.Arrays;

public class Main {
    private static final int SIZE_ARR = 500_000_000;
    private static final float[] arr = new float[SIZE_ARR];
    private static final int QUANTITY_STREAM = 3;
    private static final int PRINT_MAX_ARR = 100;

    public static void main(String[] args) {
        CalcOrigArr test1 = new CalcOrigArr(arr);
        checkSpeed(test1);

        CalcArrToManyArr test2 = new CalcArrToManyArr(arr, QUANTITY_STREAM);
        checkSpeed(test2);

        CallArrStep test3 = new CallArrStep(arr, QUANTITY_STREAM);
        checkSpeed(test3);
    }

    private static void checkSpeed(CheckSpeed obj) {
        initArr();
        long timeBeg = System.currentTimeMillis();
        obj.run();
        long timeEnd = System.currentTimeMillis();
        printArr();
        System.out.printf("Время выполнения %d %s", timeEnd - timeBeg, obj.toString());
        System.out.println();
    }

    private static void initArr() {
        Arrays.fill(Main.arr, 1);
    }

    private static void printArr() {
        if (arr.length > PRINT_MAX_ARR) {
            return;
        }

        // System.out.println(Arrays.toString(arr)); не работает с большими массивами
        System.out.print("[" + arr[0]);
        for (int i = 1; i < arr.length; i++) {
            System.out.print("," + arr[i]);
        }
        System.out.println("]");
    }
}
