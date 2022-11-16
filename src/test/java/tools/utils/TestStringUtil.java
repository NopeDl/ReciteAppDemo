package tools.utils;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.util.List;

/**
 * @author yeyeye
 * @Date 2022/11/13 22:30
 */
public class TestStringUtil {
    @Test
    public void testCalDistance(){
        String str1 = "horse";
        String str2 = "horseasdsadsadsads";

        System.out.println(StringUtil.calDistance(str1,str2));
    }

    @Test
    public void testStringMatch(){
        String json = "{'horse':'horse','horse1':'horse','horse2':'horse'}";
        System.out.println(StringUtil.stringMatch(JSONObject.parseObject(json)));
    }

    @Test
    public void testJson(){
        String json = "{\"arr\": [\"1\", \"2\", \"3\"]}";
        JSONObject jsonObject = JSONObject.parseObject(json);
        List<String> arr = (List<String>) jsonObject.get("arr");
//        String[] s = (String[]) arr.toArray();
        String[] s = new String[arr.size()];
        arr.toArray(s);
        System.out.println(s[0]);
    }
}
