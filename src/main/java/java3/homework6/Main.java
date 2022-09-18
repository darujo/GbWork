package java3.homework6;

import java.lang.reflect.Array;
import java.util.Arrays;

public class Main {
    public static final String NOT_FOUND_4 = "Нет 4 в массиве";

    public static int[] arrayAfter4 (int[] arr){
        int position4;
        for (position4 = arr.length; position4 > 0 ;) {
            if (arr[--position4]==4){
                break;
            }
        }
        if (position4==0){
            throw new RuntimeException(NOT_FOUND_4);
        }
        if(position4 == arr.length -1 ){
            return null;
        }
        int [] newArr = new int[arr.length - position4 - 1];
        for (int i = 0 ;i < arr.length - position4 - 1; i++ ){
            newArr [i] = arr[position4 + 1 +i];
        }
        return newArr;
    }
}
