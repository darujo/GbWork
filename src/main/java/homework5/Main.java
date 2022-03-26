package homework5;

public class Main {
    public static void main(String[] args) {
        Employee [] person = new Employee[5];

        person [0] = new Employee("Иванов Иван Иванович"      ,"менеджер"        ,"iii@boxmail.ru", "+7(111)111-11-11",  30000.0f, 20);
        person [1] = new Employee("Петров Петор Петрович"     ,"менеджер"        ,"ppp@boxmail.ru", "+7(111)111-11-12",  35000.0f, 30);
        person [2] = new Employee("Кириллов Кирилл Кириллович","ген директор"    ,"kkk@boxmail.ru", "+7(111)111-11-13", 100000.0f, 50);
        person [3] = new Employee("Максимов Максим Максимович","начальник отдела","mmm@boxmail.ru", "+7(111)111-11-14",  70000.0f, 41);
        person [4] = new Employee("Денисов Денис Денисович"   ,"главный менеджер","ddd@boxmail.ru", "+7(111)111-11-15",  70000.0f, 37);
        for (int i = 0; i < person.length; i++) {
            if (person[i].getAge() > 40){
                person[i].print();
            }
        }
    }
}
