package wl.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import wl.action.common.ExportExcelBaseController;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Controller
public class ExportFananceFormsNonCashController extends ExportExcelBaseController {
	private List<Map> resultInfo;

    public void exportSendList(HttpServletResponse rsp, HttpServletRequest req,List<Map> result) {
    	resultInfo=result;
        exoprt(req, rsp, "export");
    }

    @Override
    public Map createData(Map paraMap) {
        //模拟数据
        Map returnMap = Maps.newHashMap();

        String[] title = { "汇款单号","汇款人", "汇款人电话", "汇款地址","汇款人邮编",
        		"汇款金额","购买套/本","汇款时间","录入时间","汇款渠道","是否绑定","绑定订单号",
        		"订购人","订单金额","备注"};
        returnMap.put("title", title);
        
        List dataList = Lists.newArrayList();
        for (int i = 0;i<resultInfo.size(); i++) {
            List tempList = ImmutableList
                    .of(resultInfo.get(i).get("PAYMENT_ID"),resultInfo.get(i).get("PAYMENT_NAME"), resultInfo.get(i).get("PAYMENT_PHONE"),
                    		resultInfo.get(i).get("PAYMENT_ADDRESS"), resultInfo.get(i).get("POST_CODE"),
                    		resultInfo.get(i).get("PAYMENT_VALUE"), resultInfo.get(i).get("BUY_COUNT"),resultInfo.get(i).get("PAYMENT_TIME"),
                    		resultInfo.get(i).get("ACCEPT_TIME"),resultInfo.get(i).get("POST_COMPANY"),resultInfo.get(i).get("BIND_FLAG"),
                    		resultInfo.get(i).get("SUBSCRIBE_ID"),resultInfo.get(i).get("CUSTOMER_NAME"),resultInfo.get(i).get("TOTAL_MONEY"),
                    		resultInfo.get(i).get("REMARK"));
            dataList.add(tempList);
        }
        returnMap.put("data", dataList);
        return returnMap;
    }

}
