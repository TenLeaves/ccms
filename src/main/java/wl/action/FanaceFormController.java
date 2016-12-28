package wl.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.n3r.eql.EqlPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import wl.service.FananceFromService;

@Controller
public class FanaceFormController {
    @Autowired
    @Qualifier("fananceFromSrv")
    private FananceFromService fananceFormSrv;

    @RequestMapping(value = "/fananceMoneySum", method = RequestMethod.POST)
    @ResponseBody
    public String fananceMoneySum(HttpServletRequest request) {
        Map map = request.getParameterMap();
        String condition = ((String[]) map.get("condition"))[0];
        Map conditionMap = (Map) JSON.parse(condition);
        Map paraMap = prepareParam(conditionMap);

        List<Map> result = fananceFormSrv.qryFananceSumInfo(paraMap);
        Map returnMap = Maps.newHashMap();
        for (Map tempMap : result) {
            String subState = "";
            if (tempMap.get("BIND_FLAG") != null) {
                subState = tempMap.get("BIND_FLAG").toString();
            } else {
                subState = tempMap.get("SUBSCRIBE_STATE").toString();
            }

            if ("1".equals(subState)) {// 未绑定
                returnMap.put("MONEY_NOT_PAY", tempMap.get("TOTALMONEY"));
            } else if("2".equals(subState)) {
                returnMap.put("MONEY_HAS_PAY", tempMap.get("TOTALMONEY"));
            }
        }
        return JSON.toJSONString(returnMap);
    }

    private Map prepareParam(Map conditionMap) {
        Map returnMap = Maps.newHashMap();
        String isSale = conditionMap.get("isSale").toString();
        if ("1".equals(isSale)||"2".equals(isSale)) {//邮局和银行
            String bindFlag = conditionMap.get("bindflag").toString();
            if(!"0".equals(bindFlag)){
                returnMap.put("BIND_FLAG", bindFlag);   
            }
            returnMap.put("PAYMENT_ID", conditionMap.get("payNo"));
            returnMap.put("PAYMENT_NAME", conditionMap.get("payName"));
            
            returnMap.put("START_TIME", conditionMap.get("start"));
            returnMap.put("END_TIME", conditionMap.get("end"));
            returnMap.put("START_TIME1", conditionMap.get("start1"));
            returnMap.put("END_TIME1", conditionMap.get("end1"));
            returnMap.put("PAY_TYPE",isSale);  
        } else{
            returnMap.put("PAY_TYPE","3");
            returnMap.put("ORDER_START", conditionMap.get("orderStart"));
            returnMap.put("ORDER_END", conditionMap.get("orderEnd"));
        }
        
        return returnMap;
    }

    // 非现金
    @RequestMapping(value = "/qryOrderPaySituationNoneCash", method = RequestMethod.POST)
    public String qryOrderPaySituation(HttpServletRequest request,
            EqlPage eqlPage, Model model) {
        HttpSession session = request.getSession();
        Map conditionMap = Maps.newHashMap();
        Map map = request.getParameterMap();
        String condition = ((String[]) map.get("condition"))[0];
        conditionMap = (Map) JSON.parse(condition);

        Map paraMap = prepareParam(conditionMap);
        paraMap.put("eqlPage", eqlPage);
        List<Map> resultList=Lists.newArrayList();
        try {
            resultList = fananceFormSrv.qryOrderPaySituation(paraMap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute("results", resultList);

        session.setAttribute("conditionNoneSale", conditionMap);
        return "pages/fananceform/fanance_money";
    }

    // 非现金分页
    @RequestMapping(value = "/qryOrderPaySituationNoneCashPage", method = RequestMethod.POST)
    public String qryOrderPaySituationPage(HttpServletRequest request,
            EqlPage eqlPage, Model model) {
        HttpSession session = request.getSession();
        Map conditionMap = Maps.newHashMap();
        if (session.getAttribute("conditionNoneSale") != null) {
            conditionMap = (Map) session.getAttribute("conditionNoneSale");
        }

        Map paraMap = prepareParam(conditionMap);
        paraMap.put("eqlPage", eqlPage);
        List<Map> resultList = Lists.newArrayList();
        try {
            resultList = fananceFormSrv.qryOrderPaySituation(paraMap);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        model.addAttribute("results", resultList);

        return "pages/fananceform/fanance_money";
    }

    // 现金
    @RequestMapping(value = "/qryOrderPaySituationCash", method = RequestMethod.POST)
    public String qryOrderPaySituationSale(HttpServletRequest request,
            EqlPage eqlPage, Model model) {
        HttpSession session = request.getSession();
        Map conditionMap = Maps.newHashMap();
        Map map = request.getParameterMap();
        String condition = ((String[]) map.get("condition"))[0];
        conditionMap = (Map) JSON.parse(condition);

        Map paraMap = prepareParam(conditionMap);
        paraMap.put("eqlPage", eqlPage);
        List<Map> resultList = fananceFormSrv.qryOrderPaySituationCash(paraMap);
        model.addAttribute("results", resultList);

        session.setAttribute("conditionSale", conditionMap);
        return "pages/fananceform/fanance_money_sale";
    }

    // 零售分页
    @RequestMapping(value = "/qryOrderPaySituationCashPage", method = RequestMethod.POST)
    public String qryOrderPaySituationSalePage(HttpServletRequest request,
            EqlPage eqlPage, Model model) {
        HttpSession session = request.getSession();
        Map conditionMap = Maps.newHashMap();
        if (session.getAttribute("conditionSale") != null) {
            conditionMap = (Map) session.getAttribute("conditionSale");
        }

        Map paraMap = prepareParam(conditionMap);
        paraMap.put("eqlPage", eqlPage);
        List<Map> resultList = fananceFormSrv.qryOrderPaySituationCash(paraMap);
        model.addAttribute("results", resultList);

        return "pages/fananceform/fanance_money_sale";
    }
    // 导出所有财务报表
    @RequestMapping(value = "/exportAllFananceFormsNew", method = RequestMethod.POST)
    public void qryOrderPaySituationSale(HttpServletRequest request,HttpServletResponse response,Model model) {
    	HttpSession session = request.getSession();
        Map conditionMap = Maps.newHashMap();
        if (session.getAttribute("conditionSale") != null) {
            conditionMap = (Map) session.getAttribute("conditionSale");
        }

        Map paraMap = prepareParam(conditionMap);
        List<Map> resultList = fananceFormSrv.qryOrderPaySituationCash(paraMap);
        ExportFananceFormsCashController export=new ExportFananceFormsCashController();
		export.exportSendList(response, request, resultList);
    }
 // 非现金
    @RequestMapping(value = "/exportAllFananceForms", method = RequestMethod.POST)
    public void qryOrderPaySituationExport(HttpServletRequest request,HttpServletResponse response,Model model) {
    	 HttpSession session = request.getSession();
         Map conditionMap = Maps.newHashMap();
         if (session.getAttribute("conditionNoneSale") != null) {
             conditionMap = (Map) session.getAttribute("conditionNoneSale");
         }

         Map paraMap = prepareParam(conditionMap);
         List<Map> resultList=Lists.newArrayList();
        try {
            resultList = fananceFormSrv.qryOrderPaySituation(paraMap);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
         ExportFananceFormsNonCashController export=new ExportFananceFormsNonCashController();
 		 export.exportSendList(response, request, resultList);
    }
    @ModelAttribute("eqlPage")
    public EqlPage getEqlPage(EqlPage eqlPage) {
        if (eqlPage.getPageRows() == 0)
            eqlPage.setPageRows(5);
        return eqlPage;
    }

}
