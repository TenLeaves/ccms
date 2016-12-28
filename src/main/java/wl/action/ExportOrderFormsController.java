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
public class ExportOrderFormsController extends ExportExcelBaseController {
	private List<Map> resultInfo;

    public void exportSendList(HttpServletResponse rsp, HttpServletRequest req,List<Map> result) {
    	resultInfo=result;
        exoprt(req, rsp, "export");
    }

    @Override
    public Map createData(Map paraMap) {
        //模拟数据
        Map returnMap = Maps.newHashMap();

        String[] title = { "订单编号","订购日期", "订购单位", "联系人", "订购数量","订单金额","订单状态","订购产品","备注" };
        returnMap.put("title", title);
        
        List dataList = Lists.newArrayList();
        for (int i = 0;i<resultInfo.size(); i++) {
            List tempList = ImmutableList
                    .of(resultInfo.get(i).get("SUBSCRIBE_ID"),resultInfo.get(i).get("ACCEPT_TIME"), resultInfo.get(i).get("COMPANY"),
                    		resultInfo.get(i).get("CUSTOMER_NAME"), resultInfo.get(i).get("TOTAL_COUNT"),
                    		resultInfo.get(i).get("TOTAL_MONEY"), resultInfo.get(i).get("SUBSCRIBE_STATE"),resultInfo.get(i).get("product"),
                    		resultInfo.get(i).get("REMARK"));
            dataList.add(tempList);
        }
        returnMap.put("data", dataList);
        return returnMap;
    }

}
