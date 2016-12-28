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
public class ExportCustOutController extends ExportExcelBaseController {
	private List<Map> resultInfo;

    public void exportSendList(HttpServletResponse rsp, HttpServletRequest req,List<Map> result) {
    	resultInfo=result;
        exoprt(req, rsp, "export");
    }

    @Override
    public Map createData(Map paraMap) {
        //模拟数据
        Map returnMap = Maps.newHashMap();

        String[] title = { "配送序列","收件人", "联系电话", "邮寄地址", "邮寄刊物","是否已导出","导出时间","标签内容" };
        returnMap.put("title", title);
        
        List dataList = Lists.newArrayList();
        for (int i = 0;i<resultInfo.size(); i++) {
            List tempList = ImmutableList
                    .of(resultInfo.get(i).get("DISTRIBUT_ID"),resultInfo.get(i).get("ADDRESSEE_NAME"), resultInfo.get(i).get("ADDRESSEE_PHONE"),
                    		resultInfo.get(i).get("DISTRIBUT_ADDRESS"), resultInfo.get(i).get("detail")+"("+resultInfo.get(i).get("date")+")",
                    		resultInfo.get(i).get("EXPORT_FLAG"), resultInfo.get(i).get("EXPORT_TIME"),resultInfo.get(i).get("LABEL_CONTENT"));
            dataList.add(tempList);
        }
        returnMap.put("data", dataList);
        return returnMap;
    }

}
