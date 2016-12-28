package wl.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import wl.action.common.ExportExcelBaseController;

@Controller
public class ExportSendListController extends ExportExcelBaseController {

    @RequestMapping(value = "/exportSendList", method = { RequestMethod.POST,
            RequestMethod.GET })
    public void exportSendList(HttpServletResponse rsp, HttpServletRequest req) {
        exoprt(req, rsp, "送货清单");
    }

    @Override
    public Map createData(Map inMap) {
        //模拟数据
        Map returnMap = Maps.newHashMap();

        String[] title = { "订单号", "送货地址", "收件人", "电话" };
        returnMap.put("title", title);
        
        List dataList = Lists.newArrayList();
        for (int i = 10001; i < 10006; i++) {
            List tempList = ImmutableList
                    .of(i, "送货地址" + i, "收货人" + i, "电话" + i);
            dataList.add(tempList);
        }
        returnMap.put("data", dataList);
        return returnMap;
    }

}
