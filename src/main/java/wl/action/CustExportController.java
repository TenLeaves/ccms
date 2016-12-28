package wl.action;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import wl.action.common.ExportExcelBaseController;
import wl.service.CustomerInfoService;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Controller
public class CustExportController extends ExportExcelBaseController {
    
    @Resource
    CustomerInfoService customerInfoService;
    
    @RequestMapping(value = "/exportError", method = { RequestMethod.POST,
            RequestMethod.GET })
    public void exportSendList(HttpServletResponse rsp, HttpServletRequest req) {
        exoprt(req, rsp, "客户导入文件错误信息");
    }

    @Override
    public Map createData(Map inMap) {
        
        Map returnMap = Maps.newHashMap();

        String[] title = { "行数", "错误原因", "操作建议" };
        returnMap.put("title", title);
        
        List<Map> errorList = customerInfoService.selAllCustError();
        
        List dataList = Lists.newArrayList();
        
        for (int i = 0; i < errorList.size(); i++) {
            List tempList = ImmutableList
                    .of(errorList.get(i).get("line"), errorList.get(i).get("error"), errorList.get(i).get("right_operate"));
            dataList.add(tempList);
        }
        
        returnMap.put("data", dataList);
        return returnMap;
    }
    
    
}
