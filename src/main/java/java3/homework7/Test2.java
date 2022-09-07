package java3.homework7;

public class Test2 {
    @AfterSuite
    public void after1(){
        System.out.println("AfterSuite");
    }
    @AfterSuite
    public void after2(){
        System.out.println("AfterSuite");
    }

    @BeforeSuite
    public void before1(){
        System.out.println("BeforeSuite");
    }
    @BeforeSuite
    public void before2(){
        System.out.println("BeforeSuite");
    }

    @Test(priority = 15)
    public void test(){
        System.out.println("priority 15");
    }
}
