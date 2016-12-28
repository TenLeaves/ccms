package wl.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import wl.service.LeaderFormsService;
import wl.service.SaleStockInfoFormService;

@Controller
public class SaleStockInfoFormController {
    @Autowired
    @Qualifier("saleStkFormsSrv")
    private SaleStockInfoFormService saleStkFormsSrv;

    @RequestMapping(value = "/searchFromInfo")
    public String toSubmit(HttpServletRequest req, Model model) {
        Map paraMap = Maps.newHashMap();
        paraMap.put("prodType", req.getParameter("prodType"));
        paraMap.put("YEAR", req.getParameter("prodDate"));// 2014
        List result = Lists.newArrayList();
        try {
            result = saleStkFormsSrv.qryFromInfo(paraMap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute("results", result);
        if ("1".equals(paraMap.get("prodType").toString())) {// 专业版
            return "pages/leaderForm/saleStockInfo_profession";
        }
        return "pages/leaderForm/saleStockInfo_ordinary";
    }
}
