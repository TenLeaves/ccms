package wl.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import wl.common.utils.OrderUtils;
import wl.entity.LogInfo;
import wl.service.OrderInfoService;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Maps;

@Controller
public class OrderBindPaymentController {
    @Autowired
    @Qualifier("orderInfoSrv")
    private OrderInfoService orderInfoService;
    
    private Log log = LogFactory.getLog(this.getClass());
     //查询回款单
    @RequestMapping(value = "searchPayment")
    public String searhPayment(HttpServletRequest req,Model model) {
        HttpSession session = req.getSession();
        
        String qryType = req.getParameter("qryType");
        Object keywords = req.getParameter("condition");
        
        Map paraMap = Maps.newHashMap();
        if(StringUtils.isNotEmpty(qryType)){
          paraMap.put("keyWords", keywords); 
          paraMap.put("qryType", qryType);
        }
        
        List<Map> resultList = orderInfoService.queryPayMent(paraMap);
        
        session.setAttribute("paymentCondition", paraMap);
        model.addAttribute("pamentList", resultList);
        return "pages/orderbinding/paymentInfoFragment";
    }
    //查询汇款单分页
    @RequestMapping(value = "searchPaymentPage")
    public String searchPaymentPage(HttpServletRequest req,Model model) {
        HttpSession session = req.getSession();
        Map paraMap = (Map) session.getAttribute("paymentCondition");
        List<Map> resultList = orderInfoService.queryPayMent(paraMap);
        
        model.addAttribute("pamentList", resultList);
        return "pages/orderbinding/paymentInfoFragment";
    }
    
    //查询订单
    @RequestMapping(value = "searchOrderTobind", method = {
            RequestMethod.POST})
    public String searchOrder(HttpServletRequest req,Model model) {
        HttpSession session = req.getSession();
        LogInfo user = (LogInfo) session.getAttribute("logUser");
        
        String qryType = req.getParameter("qryType");
        Object keywords = req.getParameter("condition");
        
        
        Map paraMap = Maps.newHashMap();
        if(StringUtils.isNotEmpty(qryType)){
            paraMap.put("keyWords", keywords); 
            paraMap.put("qryType", qryType);
        }
        
        paraMap.put("staff_id", user.getStaff_id());
        
        List<Map> resultList = orderInfoService.queryOrder(paraMap);
        session.setAttribute("orderCondition", paraMap);
        model.addAttribute("orderList", resultList);
        return "pages/orderbinding/orderInfoFragment";
    }
    
    //查询订单分页
    @RequestMapping(value = "searchOrderTobindPage", method = {
            RequestMethod.POST})
    public String searchOrderTobindPage(HttpServletRequest req,Model model) {
        HttpSession session = req.getSession();
        Map paraMap = (Map) session.getAttribute("orderCondition");
        List<Map> resultList = orderInfoService.queryOrder(paraMap);
        model.addAttribute("orderList", resultList);
        return "pages/orderbinding/orderInfoFragment";
    }
    //订单汇款单绑定
    @RequestMapping(value = "bindPaymentOperation", method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public String bindPaymentOperation(HttpServletRequest req,Model model) {
        Map paraMap = req.getParameterMap();
        HttpSession session = req.getSession();
        
        Object payments = ((String[]) paraMap.get("payments"))[0];
        Object orderInfo = ((String[])paraMap.get("order"))[0];
        List<Map> paymentList = (List<Map>) JSONArray.parse(payments.toString());
//        List paymentIdList = Lists.newArrayList();
//        for(int i=0;i<paymentList.size();i++){
//        	paymentIdList.add(paymentList.get(i).get("paymentId"));
//        }
        Map orderMap = (Map) JSON.parse(orderInfo.toString());
        orderMap.put("staffId", ((LogInfo)session.getAttribute("logUser")).getStaff_id());
       
       log.info("-----------------订单汇款单绑定---------------------");
       log.info("参数:"+paymentList.toString()+";"+orderMap.toString()+"  操作员工:"+orderMap.get("staffId"));
       
       orderInfoService.bindPaymentToOrder(paymentList,orderMap);
       orderMap.put("SUBSCRIBE_ID", orderMap.get("orderId"));
       int totalMoney = parseInt(OrderUtils.queryOrders(orderMap).get(0).get("TOTAL_MONEY").toString());
       int moneyHasBind = parseInt(orderInfoService.searchTotalMoneyHasBind(orderMap).get("TOTALMONEY_HAS_BIND").toString());
       if(moneyHasBind>=totalMoney){//绑定的金额已经达到订单的金额
          orderInfoService.updateSubscribleState(orderMap); 
       }
       log.info("-----------------订单汇款单绑定结束---------------------");
        return "绑定成功";
    }
    
    private int parseInt(String string) {
        try {
            return Integer.parseInt(string);
        } catch (Exception e) {
            return (int)Double.parseDouble(string);
        }
        
    }
}
