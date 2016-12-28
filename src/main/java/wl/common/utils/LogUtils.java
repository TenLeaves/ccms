package wl.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogUtils {
    public static String fillStringByArgs(String str,String[] arr){
        Matcher m=Pattern.compile("\\{(\\d)\\}").matcher(str);
        while(m.find()){
            str=str.replace(m.group(),arr[Integer.parseInt(m.group(1))]);
        }
        return str;
    }
}
