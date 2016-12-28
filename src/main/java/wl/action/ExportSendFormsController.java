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
public class ExportSendFormsController extends ExportExcelBaseController {
	private List<Map> resultInfo;

    public void exportSendList(HttpServletResponse rsp, HttpServletRequest req,List<Map> result) {
    	resultInfo=result;
        exoprt(req, rsp, "export");
    }

    @Override
    public Map createData(Map paraMap) {
        //模拟数据
        Map returnMap = Maps.newHashMap();

        String[] title = { "订单号","配送单号", "收件人", "收件地址", "联系电话","配送单状态","配送产品","配送时间","配送数量" };
        returnMap.put("title", title);
        
        List dataList = Lists.newArrayList();
        for (int i = 0;i<resultInfo.size(); i++) {
            List tempList = ImmutableList
                    .of(resultInfo.get(i).get("SUBSCRIBE_ID"),resultInfo.get(i).get("DISTRIBUT_ID"), resultInfo.get(i).get("ADDRESSEE_NAME"),
                    		resultInfo.get(i).get("DISTRIBUT_ADDRESS"), resultInfo.get(i).get("ADDRESSEE_PHONE"),
                    		resultInfo.get(i).get("EXPORT_FLAG"), resultInfo.get(i).get("product"),resultInfo.get(i).get("EXPORT_TIME"),
                    		resultInfo.get(i).get("DISTRIBUT_COUNT"));
            dataList.add(tempList);
        }
        returnMap.put("data", dataList);
        return returnMap;
    }

}
