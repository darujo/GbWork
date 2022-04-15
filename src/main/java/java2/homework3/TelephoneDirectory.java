package java2.homework3;

import java.util.*;

public class TelephoneDirectory {

    private static final HashMap<String,Set<String>> listPeople = new HashMap<>();

    public static void add(String firstName, String Phone){
        Set<String> listPhone = listPeople.get(firstName);
        if (listPhone == null){
            listPhone = new HashSet<>();
            listPeople.put(firstName,listPhone);
        }
        listPhone.add(Phone);
    }

    public static String get(String firstName){
        Set<String> listPhone = listPeople.get(firstName);
        if (listPhone != null){
            return firstName + ": " + listPhone;
        }
        else {
            return "Нет записей для " + firstName;
        }
    }
}
