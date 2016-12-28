package wl.action;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import wl.entity.CustomerInfo;
import wl.entity.LogInfo;
import wl.service.CustomerInfoService;
import wl.service.OrderInfoService;
import wl.service.SaleOrderService;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Controller
public class OrderInfoInputController {
    private static final String QRY_BY_NAME = "0";
    private static final String QRY_BY_COMPLANY = "1";
    private static final String QRY_BY_CONTACT = "2";

    @Resource
    CustomerInfoService customerInfoService;

    @Autowired
    @Qualifier("orderInfoSrv")
    private OrderInfoService orderInfoService;

    @Autowired
    @Qualifier("saleOrderService")
    private SaleOrderService saleOrderService;
    
    private Log log = LogFactory.getLog(this.getClass());

    @RequestMapping(value = "subMainCustOrder", method = { RequestMethod.POST,
            RequestMethod.GET })
    @ResponseBody
    public String subOrdInfoAftIptOdrInfo(HttpServletRequest req, Model model) {
        Map paraMap = req.getParameterMap();
        HttpSession session = req.getSession();
        Object orders = ((String[]) paraMap.get("orders"))[0];
        Object custInfo = ((String[]) paraMap.get("custinfo"))[0];
        Object subscribleInfo = ((String[]) paraMap.get("subscrible"))[0];
        List<Map> ordersList = (List<Map>) JSONArray.parse(orders.toString());
        Map custMap = (Map) JSON.parse(custInfo.toString());
        Map subscribleMap = (Map) JSON.parse(subscribleInfo.toString());

        
        Map returnMap = Maps.newHashMap();
        returnMap.put("tag", "0");
        Map inMap = Maps.newHashMap();
        ordersList = seqAddOne(ordersList);
        try {
            //校验库存
            orderInfoService.verifyStockHasEnfProd(ordersList,req);
            // 查询大客户信息
            custMap = orderInfoService.queryCertainCust(custMap);
            custMap.putAll(subscribleMap);
            
            String subscribleId = orderInfoService.insertOrderInfo(custMap,ordersList, session);
            
            req.getSession().setAttribute("subscribeId", subscribleId);
        } catch (Exception e) {
            e.printStackTrace();
            returnMap.put("msg", e.getMessage());
            returnMap.put("tag", "1");
            return JSON.toJSONString(returnMap);
        }
        returnMap.put("msg", "添加成功");
        return  JSON.toJSONString(returnMap);
        
    }

    private List<Map> seqAddOne(List<Map> ordersList) {
        List<Map> returnList = Lists.newArrayList();
        for(Map tempMap : ordersList){
           Map tempOrdermap = Maps.newHashMap();
           tempOrdermap.putAll(tempMap);
           
           List<Map> newSendList = Lists.newArrayList();
           List<Map>sendList =  (List<Map>) tempMap.get("sendList");
           for(Map eachSend:sendList){
               Map tempSendMap = Maps.newHashMap();
               tempSendMap.putAll(eachSend);
               String proFrom =Integer.parseInt(eachSend.get("profrom").toString())+1+"";
               tempSendMap.put("profrom", proFrom);
               
               String sendWay =Integer.parseInt(eachSend.get("sendway").toString())+1+"";
               tempSendMap.put("sendway", sendWay);
               
               String bookmark =Integer.parseInt(eachSend.get("bookmark").toString())+1+"";
               tempSendMap.put("bookmark", bookmark);
               newSendList.add(tempSendMap);
           }
           tempOrdermap.put("sendList", newSendList);
           returnList.add(tempOrdermap);
        }
        return returnList;
    }

    @RequestMapping(value = "searchCust", method = { RequestMethod.GET,
            RequestMethod.POST })
    public String searchExitsCustInfo(HttpServletRequest req, Model model) {

        Map paraMap = Maps.newHashMap();
        String manner = (String) req.getParameter("qryManner");
        String keywords = req.getParameter("condition") == null ? "" : req
                .getParameter("condition").toString();
        if (StringUtils.isNotEmpty(keywords)) {
            keywords = "%" + keywords + "%";
            if (QRY_BY_NAME.equals(manner))
                paraMap.put("CUST_NAME", keywords);
            if (QRY_BY_COMPLANY.equals(manner))
                paraMap.put("CUST_COMPLANY", keywords);
            if (QRY_BY_CONTACT.equals(manner))
                paraMap.put("CUST_PHONE", keywords);
        }
        List<CustomerInfo> customerList = orderInfoService
                .queryExistCust(paraMap);

        model.addAttribute("custInfoList", customerList);
        return "pages/custInfoList";
    }

    @RequestMapping(value = "qryCustomerByCompanyAndName", method = {
            RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public String qryCustomerByCompanyAndName(HttpServletRequest req) {

        Map paraMap = Maps.newHashMap();
        String company = (String) req.getParameter("company");
        String name = (String) req.getParameter("name");
        paraMap.put("COMPANY", company);
        paraMap.put("CUSTOMER_NAME", name);
        Map custMap = orderInfoService.queryCertainCust(paraMap);

        Map returnMap = Maps.newHashMap();

        if (custMap != null) {
            returnMap.put("custInfo", custMap);
        }
        return JSON.toJSONString(returnMap);
    }

    @RequestMapping(value = "addOneNewCustomer", method = { RequestMethod.GET,
            RequestMethod.POST })
    @ResponseBody
    public String addNewCustomerBeforOrder(HttpServletRequest req) {
        Map paraMap = req.getParameterMap();
        HttpSession session = req.getSession();
        Object custInfo = ((String[]) paraMap.get("newCustomer"))[0];
        Map custMap = (Map) JSON.parse(custInfo.toString());

        CustomerInfo cust = new CustomerInfo();
        cust.setUpdateStaff(((LogInfo) session.getAttribute("logUser"))
                .getStaff_id());
       
        
        Map inMap = Maps.newHashMap();
        inMap.put("CUSTOMER_ID", custMap.get("CUSTOMER_ID"));
        inMap.put("CUSTOMER_NAME", custMap.get("CUSTOMER_NAME"));
        inMap.put("CUSTOMER_PHONE", custMap.get("CUSTOMER_PHONE"));
        inMap.put("CUSTOMER_TYPE", custMap.get("CUSTOMER_TYPE"));
        inMap.put("FIXED_TELEPHONE", custMap.get("FIXED_TELEPHONE"));
        inMap.put("COMPANY", custMap.get("COMPANY"));
        inMap.put("DISCOUNT_RATE", custMap.get("DISCOUNT_RATE"));
        inMap.put("CONTACT_NAME", custMap.get("CONTACT_NAME"));
        inMap.put("CONTACT_PHONE", custMap.get("CONTACT_PHONE"));
        inMap.put("REMARK", custMap.get("REMARK"));
        inMap.put("UPDATE_STAFF", ((LogInfo) session.getAttribute("logUser"))
                .getStaff_id());
        try {
            orderInfoService.addCustomerInfo(inMap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 获取添加的用户的Id
        Map addCust = orderInfoService.queryCertainCust(custMap);
        Map returnMap = Maps.newHashMap();
        returnMap.put("custInfo", addCust);
        return JSON.toJSONString(returnMap);
    }
    
    /**
     * 添加订单时候如果有以往产品，需要先校验库存时候有货，货源充足才允许添加
     * 
     * @return
     */
    public String checkPastProdStockInfo(List<Map> ordersList,HttpServletRequest req) {
        Map returnMap = Maps.newHashMap();
        returnMap.put("tag", "0");
        
        Map inMap = Maps.newHashMap();
        try {
            orderInfoService.verifyStockHasEnfProd(ordersList,req);
        } catch (Exception e) {
            returnMap.put("errorMsg", e.getMessage());
            returnMap.put("tag", "1");
            return JSON.toJSONString(returnMap);
        }
        return JSON.toJSONString(returnMap);
    }
}
