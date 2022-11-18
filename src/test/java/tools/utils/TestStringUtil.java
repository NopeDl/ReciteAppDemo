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
        String json = "{\"许\":\"123213\",\"会认为，\":\"213213\",\"组数据\":\"21312hahha\",\"受启\":\"\",\"据科学\":\"hha爱睡觉的\",\"”不停\":\"哗啦啦\",\"可以\":\"\",\"点一滴水的浪费都是不应该的。\":\" \",\"结果。所以不\":\"\",\"些水资源\":\"\"}";
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

    @Test
    public void testParseQuote(){
        String content = "{你好，我{}是你abadssadaba呢，啊实{打实{大asdsadads大}{速度}{快吗临}时队列看见了可拉伸{的}sadsadasds{a}dsadasd}";
        System.out.println(StringUtil.parseQuote(content));
    }
}
