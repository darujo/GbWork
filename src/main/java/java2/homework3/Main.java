package java2.homework3;

import java.util.*;

public class Main {
    private static final String  [] arrayMonth = {"Январь","Февраль","Март","Апрель","Май","Июнь","Июль","Август","Сентябрь","Октябрь","Ноябрь","Декабрь"};
    public static final Random rand =new Random();
    public static void main(String[] args) {

        String [] arrayText = new String[20];
        for (int i = 0; i < arrayText.length; i++) {
            arrayText[i] = arrayMonth[rand.nextInt(arrayMonth.length )];
        }
        Map<String, Integer> listMonth = new HashMap<>();


        System.out.println(Arrays.toString(arrayText));
        for (String val : arrayText) {
            listMonth.put(val, listMonth.getOrDefault(val, 0) + 1 );
        }
        System.out.println(listMonth);

        TelephoneDirectory.add("Петров","+7-999-999-99-99");
        TelephoneDirectory.add("Иванов","+7-999-999-99-98");
        TelephoneDirectory.add("Петров","+7-999-999-99-99");
        TelephoneDirectory.add("Петров","+7-999-999-99-91");
        TelephoneDirectory.add("Петров","+7-999-999-99-93");
        System.out.println(TelephoneDirectory.get("Петров"));
        System.out.println(TelephoneDirectory.get("Иванов"));
        System.out.println(TelephoneDirectory.get("Сидоров"));


    }


}
