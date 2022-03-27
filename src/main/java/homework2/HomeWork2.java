package homework2;

public class HomeWork2 {
    public static void main(String[] args) {
       System.out.println(sumInRange(8 ,1));
       printSign(1);
       printSign(0);
       printSign(-1);
       printStringNTimes("Печать текста N раз",3);
       System.out.println( isLeapYear(2000));
       System.out.println( isLeapYear(1900));
       System.out.println( isLeapYear(2004));
       System.out.println( isLeapYear(2022));
    }

    private static boolean sumInRange (int iNumber1,int iNumber2 ){
        return iNumber1 + iNumber2 <= 20 && 10 <= iNumber1 + iNumber2 ;

    }
    private static void printSign (int iNumber){
        if (negativeNumber(iNumber) ) {
            System.out.println("Отрицательное");
        }
        else {
            System.out.println("Положительное");
        };
    }
    private static boolean negativeNumber (int iNumber) {
        return iNumber < 0;
    }
    private static void printStringNTimes (String iText,int iNTimes){
        for (int i = 0; i < iNTimes; i++) {
            System.out.print(i + 1);
            System.out.println(" " +iText);

        }
    }

    private static boolean isLeapYear (int iYear){
        return (iYear % 400 == 0) | (iYear % 4 == 0 & iYear % 100 != 0) ;
    }

}
