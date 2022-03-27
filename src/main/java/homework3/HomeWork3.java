package homework3;

import java.util.Arrays;
import java.util.Random;

public class HomeWork3 {
    public static void main(String[] args) {
//    1.
        int [] arr ={1, 1, 0, 0, 1, 0, 1, 1, 0, 0 };
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == 1){
                arr[i] = 0;
            }
            else{
                arr[i] = 1;
            }
        }

//     2.
        int [] arr100 = new int[100];
        for (int i = 0; i < arr100.length;) {
            arr100[i] = ++i;
        }

//      3.
        int [] arr3 ={1, 5, 3, 2, 11, 4, 5, 2, 4, 8, 9, 1};
        for (int i = 0; i < arr3.length; i++) {
            if (arr3[i] < 6){
                arr3[i] = arr3[i]*2;
            }
        }

//     4.
        int [] [] arr4 = new int [9][9];
        for (int i = 0; i < arr4.length; i++) {
            for (int j = 0; j < arr4[i].length; j++) {
                if(i==j || i == arr4.length - j - 1 ){
                   arr4[i][j] = 1;
                }
            }
        }

//     5.
        int [] arr5 = initArr(3,10);

//     6.
        Random random = new Random();
        int [] arr6 =  new int[10];
        int maxArr6Elem =0,minArr6Elem =0, maxValue =0, minValue = 0;
        for (int i = 0; i < arr6.length; i++) {
            arr6[i] = random.nextInt(50);
            if (i == 0){
               maxValue =arr6[i];
               minValue =arr6[i];
            }
            else {
                if (maxValue < arr6[i]) {
                    maxValue = arr6[i];
                    maxArr6Elem = i;
                }
                if (minValue > arr6[i]) {
                    minValue = arr6[i];
                    minArr6Elem = i;
                }
            }
        }
//        System.out.println("Массив " + Arrays.toString(arr6));
//        System.out.println("MaxElem № " + maxArr6Elem + " MaxValue " + maxValue  );
//        System.out.println("MinElem № " + minArr6Elem + " MinValue " + minValue  );

//     7.
        int [] arr7 = {2, 1,1,1,1};
        checkSum(arr7);

//     8.
        int [] arr8 = {1,2,3,4,5};
        shiftArr(2,arr8);
    }

//  5.
    private static int [] initArr    (int len,int initialValue){
        int [] arr;
        if (len < 0){
            len = 0;
        }
        arr= new int [len];
        for (int i = 0; i < arr.length; i++) {
           arr[i] = initialValue;
        }
        return arr;
    }

//   7.
     private static boolean checkSum (int [] arr){
         for (int i = 0; i < arr.length; i++) {
             int sumLeft = 0, sumRight = 0;
             for (int j = 0; j < i; j++) {
                 sumLeft = sumLeft + arr[j];
             }
             for (int j = i; j < arr.length; j++) {
                 sumRight = sumRight + arr[j];
             }
             if (sumLeft ==sumRight){
                 return true;
             }
             else if (sumLeft > sumRight){
                 return false;
             }
         }
         return false;
     }

//   8.
     private static int [] shiftArr (int n, int[] arr){
//       Сократим количество сдвигов
         n = n % arr.length;
//       Чтобы не изменять направление сдвига в цикле инвертируем сдвиг
         if (n < 0) {
             n = arr.length + n;
         }
         for (int i = 0; i < n; i++) {
             int elem;
             elem = arr [0];
             arr[0] = arr[arr.length -1];
             arr[arr.length -1] = elem;
             for (int j = 0; j < arr.length -2 ; j++) {
                 elem = arr [j];
                 arr[j] = arr[j + 1];
                 arr[j+ 1] = elem;
             }
         }
         return arr;
     }
}
