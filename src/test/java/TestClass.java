import org.junit.Test;

import java.lang.reflect.Field;

public class TestClass {
    @Test
    public void test() throws Exception{
        Class<?> aClass = Class.forName("java.lang.Integer");
        Field[] fields = aClass.getFields();
        for (Field field : fields) {
            System.out.println(field.getName());
        }
        Object o = aClass.newInstance();

    }
}
