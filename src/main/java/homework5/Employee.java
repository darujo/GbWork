package homework5;

public class Employee {
    private final String fio,position,email,tel;
    private final float salary;
    private final int age;

    public Employee (String fio
                    ,String position
                    ,String email
                    ,String tel
                    ,float salary
                    ,int age){
        this.fio      = fio;
        this.position = position;
        this.email    = email;
        this.tel      = tel;
        this.salary   = salary;
        this.age      = age;
    }
    public void print(){
        System.out.println("ФИО       : " + fio);
        System.out.println("Должность : " + position);
        System.out.println("Email     : " + email);
        System.out.println("Телефон   : " + tel);
        System.out.println("Зарплата  : " + salary);
        System.out.println("Возраст   : " + age);
        System.out.println();
    }

    public int getAge() {
        return age;
    }

}

