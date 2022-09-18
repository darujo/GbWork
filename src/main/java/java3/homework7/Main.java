package java3.homework7;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        Class testClass = Class.forName("java3.homework7.Test1");
        start(testClass);
        start(Class.forName("java3.homework7.Test2"));
    }
    public static void start(Class testClass) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        Method [] methods = testClass.getDeclaredMethods();
        int beforeSuite = 0;
        int afterSuite = 0;
        Method before = null;
        Method after = null;
        String mesError;
        boolean badPriority = false;
        for (Method method:methods){
            if(method.getAnnotation(BeforeSuite.class)!=null){
                beforeSuite++;
                before= method;
            }
            if(method.getAnnotation(AfterSuite.class)!=null){
                afterSuite++;
                after=method;
            }
            Test test = method.getAnnotation(Test.class);
            if (test != null &&
                    (test.priority() > 10 || test.priority() < 1) ){
               badPriority =true;
            }
        }
        mesError = (beforeSuite > 1 ? "Методов BeforeSuite должно быть не больше 1 " : "")
                 + (afterSuite  > 1 ? "Методов AfterSuite должно быть не больше 1 "  : "")
                 + (badPriority     ? "Приоритет Test должен быть от 1 до 10 "       : "");

        if(!mesError.equals( "")){
            throw new RuntimeException(mesError);
        }
        Object testObject = testClass.getDeclaredConstructor().newInstance();
        if(before!=null){
            before.invoke(testObject);
        }
        for (int i = 1; i <= 10 ; i++) {
            for (Method method:methods){
                Test test = method.getAnnotation(Test.class);
                if (test != null &&
                        test.priority() == i){
                    method.invoke(testObject);

                }
            }
        }
        if(after!= null){
            after.invoke(testObject);
        }
    }
}
