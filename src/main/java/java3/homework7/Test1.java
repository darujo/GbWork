package java3.homework7;

public class Test1 {

    @AfterSuite
    public void after(){
        System.out.println("AfterSuite");
    }
    @BeforeSuite
    public void before(){
        System.out.println("BeforeSuite");
    }

    @Test(priority = 1)
    public void test6(){
        System.out.println("test6 priority 1");
    }

    @Test(priority = 3)
    public void test3(){
        System.out.println("test3 priority 3");
    }
    @Test(priority = 4)
    public void test4(){
        System.out.println("test4 priority 4");
    }
    @Test
    public void test5(){
        System.out.println("test5 not pri");
    }

}
