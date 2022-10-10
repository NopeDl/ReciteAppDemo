import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.Test;

import java.io.*;
import java.util.Base64;

public class TestClass {
    @Test
    public void test() throws Exception {
        File src = new File("C:\\Users\\h2012\\Desktop\\test.pdf");
        File dest = new File("C:\\Users\\h2012\\Desktop\\test1.pdf");
        if (!dest.exists()) {
            dest.createNewFile();
        }
        InputStream input = new FileInputStream(src);
        byte[] bytes = new byte[1024];
        int len;
        StringBuilder sb = new StringBuilder();
        while ((len=input.read(bytes))!=-1){
            sb.append(new String(bytes,0,len));
        }
        input.close();
        byte[] encode = Base64.getEncoder().encode(sb.toString().getBytes());
        byte[] decode = Base64.getDecoder().decode(encode);

        OutputStream out = new FileOutputStream(dest);
        out.write(decode);
        out.close();
    }
}
