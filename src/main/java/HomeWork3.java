import java.util.Arrays;
import java.util.Random;

public class HomeWork3 {
    public static void main(String[] args) {
        System.out.println("1. Задать целочисленный массив, состоящий из элементов 0 и 1. Например: [ 1, 1, 0, 0, 1, 0, " +
                "1, 1, 0, 0 ]. С помощью цикла и условия заменить 0 на 1, 1 на 0;");
        int [] arr ={1, 1, 0, 0, 1, 0, 1, 1, 0, 0 };
        System.out.println("Массив до    " + Arrays.toString(arr));
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == 1){
                arr[i] = 0;
            }
            else{
                arr[i] = 1;
            }
        }
        System.out.println("Массив после " + Arrays.toString(arr));
        System.out.println();
        System.out.println("2. Задать пустой целочисленный массив длиной 100. С помощью цикла заполнить его значениями 1 2 3 4 5 6 7 8 … 100");

        int [] arr100 = new int[100];
        for (int i = 0; i < arr100.length;) {
            arr100[i] = ++i;
        }
        System.out.println("Результат: " + Arrays.toString(arr100));
        System.out.println();
        System.out.println("3. Задать массив [ 1, 5, 3, 2, 11, 4, 5, 2, 4, 8, 9, 1 ] пройти по нему циклом, и числа меньшие 6 умножить на 2");
        int [] arr3 ={1, 5, 3, 2, 11, 4, 5, 2, 4, 8, 9, 1};
        System.out.println("Массив до    " + Arrays.toString(arr3));
        for (int i = 0; i < arr3.length; i++) {
            if (arr3[i] < 6){
                arr3[i] = arr3[i]*2;
            }
        }
        System.out.println("Массив после " + Arrays.toString(arr3));

        System.out.println("4. Создать квадратный двумерный целочисленный массив (количество строк и столбцов одинаковое), и с помощью " +
        "цикла(-ов) заполнить его диагональные элементы единицами (можно только одну из диагоналей, если обе сложно). " +
        "Определить элементы одной из диагоналей можно по следующему принципу: индексы таких элементов равны, " +
        "то есть [0][0], [1][1], [2][2], …, [n][n]");
        int [] [] arr4 = new int [9][9];
        System.out.println("Результат: ");

        for (int i = 0; i < arr4.length; i++) {
            for (int j = 0; j < arr4[i].length; j++) {
                if(i==j || i == arr4.length - j - 1 ){
                   arr4[i][j] = 1;
                }
                System.out.print(arr4[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();

        System.out.println("5. Написать метод, принимающий на вход два аргумента: len и initialValue, и возвращающий одномерный массив " +
        "типа int длиной len, каждая ячейка которого равна initialValue");
        int [] arr5 = initArr(3,10);
        System.out.println("Результат: " + Arrays.toString(arr5));
        System.out.println();

        System.out.println("6. * Задать одномерный массив и найти в нем минимальный и максимальный элементы");
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
        System.out.println("Массив " + Arrays.toString(arr6));
        System.out.println("MaxElem № " + maxArr6Elem + " MaxValue " + maxValue  );
        System.out.println("MinElem № " + minArr6Elem + " MinValue " + minValue  );
        System.out.println();

        System.out.println("7. ** Написать метод, в который передается не пустой одномерный целочисленный массив, метод должен вернуть " +
           "true, если в массиве есть место, в котором сумма левой и правой части массива равны.");
        int [] arr7 = {2, 1,1,1,1};
        System.out.println("Массив " + Arrays.toString(arr7));
        System.out.println("Результат: " + checkSum(arr7));
        System.out.println();

        System.out.println("8. *** Написать метод, которому на вход подается одномерный массив и число n (может быть положительным, " +
        "или отрицательным), при этом метод должен сместить все элементы массива на n позиций. " +
        "Элементы смещаются циклично. Для усложнения задачи нельзя пользоваться вспомогательными массивами. " +
        "Примеры: [ 1, 2, 3 ] при n = 1 (на один вправо) -> [ 3, 1, 2 ]; [ 3, 5, 6, 1] при n = -2 (на два влево) -> " +
        "[ 6, 1, 3, 5 ]. При каком n в какую сторону сдвиг можете выбирать сами.");
        int [] arr8 = {1,2,3,4,5};
        shiftArr(2,arr8);
        System.out.println("Результат: " + Arrays.toString(arr8));
    }
//        5. Написать метод, принимающий на вход два аргумента: len и initialValue, и возвращающий одномерный массив
//        типа int длиной len, каждая ячейка которого равна initialValue;

    private static int [] initArr    (int len,int initialValue){
        System.out.println("Элементов " + len + " Значения " + initialValue);
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
//        7. ** Написать метод, в который передается не пустой одномерный целочисленный массив, метод должен вернуть
//        true, если в массиве есть место, в котором сумма левой и правой части массива равны.
//**Примеры:
//        checkBalance([2, 2, 2, 1, 2, 2, ||| 10, 1]) → true, т.е. 2 + 2 + 2 + 1 + 2 + 2 = 10 + 1
//        checkBalance([1, 1, 1, ||| 2, 1]) → true, т.е. 1 + 1 + 1 = 2 + 1
//
//        граница показана символами |||, эти символы в массив не входят и не имеют никакого отношения к ИЛИ.
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

//        8. *** Написать метод, которому на вход подается одномерный массив и число n (может быть положительным,
//        или отрицательным), при этом метод должен сместить все элементы массива на n позиций.
//        Элементы смещаются циклично. Для усложнения задачи нельзя пользоваться вспомогательными массивами.
//        Примеры: [ 1, 2, 3 ] при n = 1 (на один вправо) -> [ 3, 1, 2 ]; [ 3, 5, 6, 1] при n = -2 (на два влево) ->
//        [ 6, 1, 3, 5 ]. При каком n в какую сторону сдвиг можете выбирать сами.
     private static int [] shiftArr (int n, int[] arr){
         System.out.println("Сдвиг " + n);
         System.out.println("Массив " + Arrays.toString(arr));
//       Сократим количество сдвигов
         n = n % arr.length;
//       Чтобы не изменять направление сдвига в циклк инвертируем сдвиг
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
             System.out.println(i + Arrays.toString(arr));
         }
         return arr;
     }
}
