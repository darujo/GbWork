import java3.homework6.Main;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestHomeWork6 {

    @Test
    public void testArrayAfter4GoodArray(){
        Assertions.assertArrayEquals(Main.arrayAfter4(new int[]{1, 2, 3, 4, 5, 6}), new int[]{5, 6});
        Assertions.assertArrayEquals(Main.arrayAfter4(new int[]{1, 2, 4, 4, 2, 3, 4, 1, 7}), new int[]{1, 7});
    }
    @Test
    public void testArrayAfter4GoodArrayEmptyArray(){
        Assertions.assertArrayEquals(Main.arrayAfter4(new int[]{1, 2, 3, 4, 5, 6, 4}), null);
    }
    @Test
    public void testArrayAfter4GoodArrayException(){
        Exception exception = Assertions.assertThrowsExactly(RuntimeException.class,()-> Main.arrayAfter4(new int[]{1, 2, 3,5, 6}));
        Assertions.assertEquals(exception.getMessage(),Main.NOT_FOUND_4);
    }


}
