package java2.homework2;

import javax.imageio.IIOException;
import java.io.IOException;

public class Main {
    private static final int SIZE_ARR = 4;
    public static void main(String[] args) {
        String [][] arr = new String [4][4];
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                arr[i][j] = String.valueOf(i + 1 + j);

            }
        }
        arr[3][2]= "a";
        try {
            System.out.println("Сумма значений ячеик массива: " + sumArr(arr));
        }catch (MyArrayDataException | MyArraySizeException e){
            System.out.println("Не удалось вычислить сумму массива: " + e.getMessage());
        }
    }

    private static int sumArr (String [][] array) {
        int sum= 0;
        int i;
        int j;
        if (array.length != SIZE_ARR){
            throw new MyArraySizeException ("Длинна Массива должна быть " + SIZE_ARR + "*" + SIZE_ARR
                    + ". Длинна вашего массива " + array.length + "*... " );
        }
        for (i = 0; i < array.length; i++) {
            if (array[i].length != SIZE_ARR){
                throw new MyArraySizeException ("Длинна Массива должна быть " + SIZE_ARR + "*" + SIZE_ARR
                        + ". Длинна вашего массива " + SIZE_ARR + "*" + array[i].length + " в строке " + i );
            }
            for (j = 0; j < array[i].length; j++) {
                try {
                    sum += Integer.parseInt(array[i][j]);
                }
                catch (NumberFormatException e) {
                    throw new MyArrayDataException ("Не удалось преобразовать элемент [" + i +"]["+ j + "] в число. " + "" +
                            "Значение элемента '" + array[i][j] + "'" );
                }
            }

        }
        return sum;

    }
    private static class MyArraySizeException extends ArrayIndexOutOfBoundsException
    {
        public MyArraySizeException(String s) {
            super(s);
        }
    }
    private static class MyArrayDataException extends NumberFormatException
    {
        public MyArrayDataException(String s) {
            super(s);
        }

    }

}
