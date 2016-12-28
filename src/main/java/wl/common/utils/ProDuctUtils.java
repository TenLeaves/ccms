package wl.common.utils;

public class ProDuctUtils {
    public static String prodToDesc(String prodCode){
        String proTypeDesc[] = { "", "专业版", "普及版" };
        String prospishes[] = { "", "第一期", "第二期", "第三期", "第四期", "第五期", "第六期",
                "第七期", "第八期", "第九期", "第十期", "第十一期", "第十二期", "合刊期" };
        String year = prodCode.substring(0, 4);
        int month = Integer.parseInt(prodCode.substring(4, 6));
        int protype = Integer.parseInt(prodCode.substring(6));
        return   proTypeDesc[protype]+year +"年"+prospishes[month];
    }
}
