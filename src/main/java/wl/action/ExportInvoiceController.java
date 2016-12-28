package wl.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import wl.action.common.ExportExcelBaseController;

public class ExportInvoiceController extends ExportExcelBaseController{
    private List<Map> resultInfo;

    public void exportSendList(HttpServletResponse rsp, HttpServletRequest req,List<Map> result) {
        resultInfo=result;
        exoprt(req, rsp, "export");
    }
    
    @Override
    public Map createData(Map inMap) {
        Map returnMap = Maps.newHashMap();

        String[] title = { "所属订单","发票号", "抬头", "项目内容","发票类别",
                "金额","数量（张）","打印状态","打印时间","汇款人","汇款时间","汇款金额","备注"};
        returnMap.put("title", title);
        
        List dataList = Lists.newArrayList();
        for (int i = 0;i<resultInfo.size(); i++) {
            List tempList = ImmutableList
                    .of(resultInfo.get(i).get("SUBSCRIBE_ID"),resultInfo.get(i).get("INVOICE_ID"), resultInfo.get(i).get("INVOICE_TITLE"),
                            resultInfo.get(i).get("INVOICE_ITEM"), resultInfo.get(i).get("INVOICE_TYPE"),
                            resultInfo.get(i).get("INVOICE_AMOUNT"), resultInfo.get(i).get("INVOICE_COUNT"),resultInfo.get(i).get("INVOICE_STATEDESC"),
                            resultInfo.get(i).get("INVOICE_TYPE_TIME"),resultInfo.get(i).get("PAYMENT_NAME"),resultInfo.get(i).get("PAYMENT_TIME"),resultInfo.get(i).get("PAYMENT_VALUE"),resultInfo.get(i).get("REMARK"));
            dataList.add(tempList);
        }
        returnMap.put("data", dataList);
        return returnMap;
    }

}
