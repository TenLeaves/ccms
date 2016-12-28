package wl.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.n3r.eql.EqlPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import wl.entity.LogInfo;
import wl.service.PrintTableService;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Controller
public class PrintTableController {
    @Autowired
    @Qualifier("printTableService")
    private PrintTableService printTableService;

    private Log log = LogFactory.getLog(this.getClass());
    
    @RequestMapping(value = "/selectPrint", method = RequestMethod.GET)
    @ResponseBody
    public Map selectPrint(HttpServletRequest request, Model model) {

        Map map = printTableService.selectTableInfo();

        return map;
    }
  //跳转到修改页面
    @RequestMapping(value="/printTableEdit",method=RequestMethod.GET) 
    public String toPrintTableEdit(HttpServletRequest request, Model model){
        String printCode = request.getParameter("printCode");
        Map printMap = printTableService.qryPrintByCode(printCode);
        List list =  Lists.newArrayList();
        list.add(printMap);
        model.addAttribute("printPageList", list);
        return "pages/printTable/printTableEdit";
    }
  //制表申请修改
    @RequestMapping(value = "updatePrintTbl", method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public String updatePrintTbl(HttpServletRequest req,Model model) {
        Map paraMap = req.getParameterMap();
        HttpSession session = req.getSession();
        Object printTbl = ((String[])paraMap.get("printTbl"))[0];
        Map printTblMap = (Map) JSON.parse(printTbl.toString());
        
        log.info("-----------------修改制表申请------------------");
        log.info("参数:"+printTblMap.toString()+"   操作人："+((LogInfo)session.getAttribute("logUser")).getStaff_id());
        
        printTableService.updatePrintTbl(printTblMap);
        
        log.info("-----------------修改制表申请结束------------------");
        
        return "修改成功";
    }

    @RequestMapping(value = "/qryPrintApply", method = {RequestMethod.GET,RequestMethod.POST})
    public String qryPrintApply(HttpServletRequest req, Model model,EqlPage eqlPage) {
        String prodDate = req.getParameter("prodDate") == null ? "" : req
                .getParameter("prodDate");
        Map paraMap = Maps.newHashMap();
        paraMap.put("prodType", req.getParameter("prodType"));
        paraMap.put("prodDate", prodDate);
        paraMap.put("eqlPage", eqlPage);
        
        if(StringUtils.isNotEmpty(prodDate)){
            paraMap.put("YEAR", prodDate.substring(0, 4));
            paraMap.put("MONTH", prodDate.substring(5));
        }
        List<Map> printTblList = printTableService
                .selectApplyPrintTable(paraMap);
        
        HttpSession session = req.getSession();
        session.setAttribute("applyPrintCondition", paraMap);
        model.addAttribute("applyTbList", printTblList);
        
        return "pages/printTable/applyTableInfo";
    }
    
    @RequestMapping(value = "/qryPrintApplyPage", method = RequestMethod.POST)
    public String qryPrintApplyPage(HttpServletRequest req, Model model,EqlPage eqlPage) {
        
        Map paraMap = Maps.newHashMap();
        paraMap = (Map) req.getSession().getAttribute("applyPrintCondition");
        paraMap.put("eqlPage", eqlPage);
        List<Map> printTblList = printTableService
                .selectApplyPrintTable(paraMap);
        
        model.addAttribute("applyTbList", printTblList);
        
        return "pages/printTable/applyTableInfo";
    }

    @RequestMapping(value = "/updatePrintDetail", method = RequestMethod.POST)
    @ResponseBody
    public String toUpdateOrderInfo(HttpServletRequest request, Model model) {
        Map map = request.getParameterMap();
        String selInfoList = ((String[]) map.get("user"))[0];
        Map userMap = (Map) JSON.parse(selInfoList);
        HttpSession session = request.getSession();
        try {
            printTableService.updateDetailInfo(userMap, session);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }

    /**
     * 该方法会在该controler所有的的方法执行器执行这个函数在model中添加了 eqlPage对象
     * 
     * @param eqlPage
     * @return
     */
    @ModelAttribute("eqlPage")
    public EqlPage getEqlPage(EqlPage eqlPage) {
        if (eqlPage.getPageRows() == 0)
            eqlPage.setPageRows(5);
        return eqlPage;
    }
}
