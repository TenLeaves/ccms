package wl.action.edit;

import java.util.Calendar;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import wl.common.utils.OrderUtils;
import wl.common.utils.ProDuctUtils;
import wl.entity.LogInfo;
import wl.service.EditService;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Controller
public class EditController {
    @Autowired 
    @Qualifier("editService")
    private EditService editService;
    
    private Log log = LogFactory.getLog(this.getClass());
    
    
    private final String FAXING="2";
    private final String KUGUAN="3";
    private final String CAIWU="4";
    private final String LINGDAO="1";
    
	//查询子订单的第一条 以及该子订单的第一条配送信息用于页面初始化
    @RequestMapping(value="queryFirstOrderAndFirstSend",method=RequestMethod.POST)
    @ResponseBody
    public String queryOrderAndFirstSendInfo(HttpServletRequest req){
        String subscribleId = req.getParameter("SUBSCRIBE_ID");
        String invoiceState = req.getParameter("INVOICE_STATE");
        String returnStr="";
        Map paraMap = Maps.newHashMap();
        paraMap.put("SUBSCRIBE_ID", subscribleId);
        paraMap.put("INVOICE_STATE", invoiceState);
        
        List<Map> orderList = OrderUtils.qryChildOrderOnly(paraMap);
        Map firstOrder = orderList.get(0);
        Map subscribOrder = OrderUtils.queryOrders(paraMap).get(0);
        firstOrder.put("needInvoice", subscribOrder.get("NEED_INVOICE"));
        firstOrder.put("sendFirst", subscribOrder.get("FIRST_SEND"));
        
        paraMap.put("ORDER_ID", firstOrder.get("ORDER_ID"));
        paraMap.put("EXPORT_FLAG", "1");//没有导出过的配送信息
        paraMap.put("CURRENT_MONTH", getCurrentMonth());
        paraMap.put("CURRENT_YEAR", getCurrentYear());
        List<Map> sendList = OrderUtils.qrySendListOnly(paraMap);
        
        Map firstSend = sendList.size()==0?null:sendList.get(0);
        Map returnMap = Maps.newHashMap();
        
        //获取当前登录用户的信息
        LogInfo logUser = (LogInfo) req.getSession().getAttribute("logUser");
        Map userMap = Maps.newHashMap();
        userMap.put("staff_id", logUser.getStaff_id());
        //userMap.put("staff_id", "yt");
        
        List<Map> roleList = editService.qryUserRol(userMap);
        String rolVal="";
        for(Map roleMap:roleList){
           rolVal= rolVal+","+roleMap.get("ROLE_VALUE").toString();
        }
        Map logMap=Maps.newHashMap();
        logMap.put("roleVal", rolVal);
        
        //获取发票信息
        paraMap.clear();
        paraMap.put("SUBSCRIBE_ID", firstOrder.get("SUBSCRIBE_ID"));
        List<Map> invoiceList = editService.queryInvoice(paraMap);
        
        if(invoiceList.size()>0){
            Map invoicMap = invoiceList.get(0);
            returnMap.put("invoiceInfo", invoicMap);
        }
        
        returnMap.put("roleInfo", logMap);
        returnMap.put("orderInfo", firstOrder);
        returnMap.put("sendInfo", firstSend);
       
        return JSON.toJSONString(returnMap);
    }
    
    //查询所有的子订单信息用于切换订单
    @RequestMapping(value="queryAllChildOrders",method=RequestMethod.POST)
    @ResponseBody
    public String selectAllChildOrder(HttpServletRequest req ){
    	String parentOrderId = req.getParameter("parentOrderId");
    	Map paraMap = Maps.newHashMap();
    	paraMap.put("SUBSCRIBE_ID", parentOrderId==null?"000112":parentOrderId);
    	List<Map> orderList =OrderUtils.qryChildOrderOnly(paraMap);
    	paraMap.clear();
    	paraMap.put("orderList",orderList);
    	return JSON.toJSONString(paraMap);
    }
    //查询第一条发票信息用于切换发票
    @RequestMapping(value="queryFirstInvoice",method=RequestMethod.POST)
    @ResponseBody
    public String selectFirstInvoice(HttpServletRequest req ){
        String parentOrderId = req.getParameter("SUBSCRIBE_ID");
        Map paraMap = Maps.newHashMap();
        paraMap.put("SUBSCRIBE_ID", parentOrderId);
        List<Map> orderList =editService.queryInvoice(paraMap);
        paraMap.clear();
        paraMap.put("invoiceInfo",orderList.get(0));
        return JSON.toJSONString(paraMap);
    }
    @RequestMapping(value="queryInvoice",method=RequestMethod.POST)
    @ResponseBody
    public String selectAllInvoice(HttpServletRequest req ){
        String parentOrderId = req.getParameter("SUBSCRIBE_ID");
        Map paraMap = Maps.newHashMap();
        paraMap.put("SUBSCRIBE_ID", parentOrderId);
        List<Map> orderList =editService.queryInvoice(paraMap);
        paraMap.clear();
        paraMap.put("invoiceList",orderList);
        return JSON.toJSONString(paraMap);
    }
    
  //查询子订单的第一条配送信息
    @RequestMapping(value="queryFirstSendInfoByOrderId",method=RequestMethod.POST)
    @ResponseBody
    public String selectFirstSendInfoby(HttpServletRequest req ){
    	String orderId = req.getParameter("ORDER_ID");
    	Map paraMap = Maps.newHashMap();
    	paraMap.put("ORDER_ID",orderId );
    	 paraMap.put("EXPORT_FLAG", "1");//没有导出过的配送信息
         paraMap.put("CURRENT_MONTH", getCurrentMonth());
         paraMap.put("CURRENT_YEAR", getCurrentYear());
    	List<Map> sendList =OrderUtils.qrySendListOnly(paraMap);
    	paraMap.clear();
    	paraMap.put("sendMap",sendList.size()==0?null:sendList.get(0));
    	return JSON.toJSONString(paraMap);
    }
    
    //查询子订单的所有配送信息 
    //当月的以及当月之前的切没有导出过的配送单  
    
    @RequestMapping(value="querySendInfoByOrderId",method=RequestMethod.POST)
    @ResponseBody
    public String selectAllSendInfoby(HttpServletRequest req ){
    	String orderId = req.getParameter("ORDER_ID");
    	Map paraMap = Maps.newHashMap();
    	paraMap.put("ORDER_ID",orderId );
    	paraMap.put("EXPORT_FLAG", "1");//没有导出过的配送信息
    	paraMap.put("CURRENT_MONTH", getCurrentMonth());
    	paraMap.put("CURRENT_YEAR", getCurrentYear());
    	List<Map> sendList =OrderUtils.qrySendListOnly(paraMap);
    	sendList = putProdDesc4EachProd(sendList);
    	paraMap.clear();
    	paraMap.put("sendList",sendList);
    	return JSON.toJSONString(paraMap);
    }
    private List<Map> putProdDesc4EachProd(List<Map> sendList) {
        List<Map> returnList = Lists.newArrayList();
        for(Map prodMap :sendList){
          String prodCode = prodMap.get("YEAR").toString()+prodMap.get("MONTH").toString()+prodMap.get("PRODUCT_TYPE").toString();
           prodMap.put("PROD_DESC", ProDuctUtils.prodToDesc(prodCode)) ;
           returnList.add(prodMap);
        }
        return returnList;
    }
    
    private Object getCurrentYear() {
        String year = Calendar.getInstance().get(Calendar.YEAR)+"";
        return year;
    }

    private Object getCurrentMonth() {
       int month=  Calendar.getInstance().get(Calendar.MONTH)+1;
        return StringUtils.leftPad(""+month, 2,"0");
    }

    //删除配送信息
    @RequestMapping(value="deleteSendInfo",method=RequestMethod.POST)
    @ResponseBody
    public String deleteOneSendInfo(HttpServletRequest req ){
    	String sendId = req.getParameter("DISTRIBUT_ID");
    	Map paraMap = Maps.newHashMap();
    	paraMap.put("DISTRIBUT_ID",sendId);
    	
    	try {
    	    log.info("-----------------删除配送单------------------");
            editService.deleteSendInfo(paraMap,req);
        } catch (Exception e) {
        	e.printStackTrace();
        	log.info("-----------------删除配送单失败------------------");
        	log.info("失败原因："+e.getMessage());
            return "删除失败";
        }
    	log.info("-----------------删除配送单结束------------------");
    	return "该条配送信息已删除";
    }
    
    /**
     * 新增配送信息
     * @param req
     * @return
     */
    @RequestMapping(value="addOneSendInfo",method=RequestMethod.POST)
    @ResponseBody
    public String addOneSendInfo(HttpServletRequest req ){
        Map reqMap = req.getParameterMap();
        Object sendInfo = ((String[])reqMap.get("sendInfo"))[0];
        Map sendInfoMap = JSON.parseObject(sendInfo.toString());
        LogInfo logUser = (LogInfo) req.getSession().getAttribute("logUser");
        Map paraMap = Maps.newHashMap();
        paraMap.put("ORDER_ID",sendInfoMap.get("orderId"));
        paraMap.put("DISTRIBUT_ID",sendInfoMap.get("sendId"));
        paraMap.put("STOCK_ADDRESS",sendInfoMap.get("selectStockType"));
        paraMap.put("DISTRIBUT_COUNT",sendInfoMap.get("amount"));
        paraMap.put("DISTRIBUT_TYPE",sendInfoMap.get("sendWay"));
        paraMap.put("DISTRIBUT_ADDRESS",sendInfoMap.get("sendAddr"));
        paraMap.put("ZIP_CODE",sendInfoMap.get("postCode"));
        paraMap.put("ADDRESSEE_NAME",sendInfoMap.get("name"));
        paraMap.put("ADDRESSEE_PHONE",sendInfoMap.get("phone"));
        paraMap.put("NEED_LABEL_FLAG",sendInfoMap.get("needMark"));
        paraMap.put("EXPORT_FLAG","1");
        paraMap.put("PRODUCT_TYPE",sendInfoMap.get("prodType"));
        paraMap.put("PRODUCT_BEGIN",sendInfoMap.get("prodBegin"));
        paraMap.put("PRODUCT_END",sendInfoMap.get("prodEnd"));
        paraMap.put("ISLIANXU",sendInfoMap.get("isLianxu"));
        paraMap.put("staffId", logUser.getStaff_id());
        try {
            log.info("-----------------新增配送单------------------");
            log.info("配送单信息:"+paraMap+"操作人:"+logUser.getStaff_id());
            editService.AddSendInfo(paraMap);
        } catch (Exception e) {
            log.info("-----------------新增配送单失败------------------");
            log.info("失败原因："+e.getMessage());
            return e.getMessage();
        }
        log.info("-----------------新增配送单结束------------------");
        return "添加成功";
    }
    /**
     * 修改某条配送信息
     */
    @RequestMapping(value="modifyOneSendInfo",method=RequestMethod.POST)
    @ResponseBody
    public String modifyOneSendInfo(HttpServletRequest req ){
        Map reqMap = req.getParameterMap();
        Object sendInfo = ((String[])reqMap.get("sendInfo"))[0];
        Map sendInfoMap = JSON.parseObject(sendInfo.toString());
        LogInfo logUser = (LogInfo) req.getSession().getAttribute("logUser");
        Map paraMap = Maps.newHashMap();
        paraMap.put("ORDER_ID",sendInfoMap.get("orderId"));
        paraMap.put("DISTRIBUT_ID",sendInfoMap.get("sendId"));
        paraMap.put("STOCK_ADDRESS",sendInfoMap.get("selectStockType"));
        paraMap.put("DISTRIBUT_COUNT",sendInfoMap.get("amount"));
        paraMap.put("DISTRIBUT_TYPE",sendInfoMap.get("sendWay"));
        paraMap.put("DISTRIBUT_ADDRESS",sendInfoMap.get("sendAddr"));
        paraMap.put("ZIP_CODE",sendInfoMap.get("postCode"));
        paraMap.put("ADDRESSEE_NAME",sendInfoMap.get("name"));
        paraMap.put("ADDRESSEE_PHONE",sendInfoMap.get("phone"));
        paraMap.put("NEED_LABEL_FLAG",sendInfoMap.get("needMark"));
        paraMap.put("staffId", logUser.getStaff_id());
        try {
            log.info("------------------------修改配送单----------------------");
            log.info("配送单参数:"+paraMap+"操作人："+logUser.getStaff_id());
            editService.modifySendInfo(paraMap);
        } catch (Exception e) {
        	log.info("------------------------修改配送单失败----------------------");
        	log.info("失败原因:"+e.getMessage());
            return e.getMessage();
        }
        return "修改成功";
    }
    /**
     * 新增发票信息
     * @param req
     * @return
     */
    @RequestMapping(value="addOneInvoice",method=RequestMethod.POST)
    @ResponseBody
    public String addOneInvoiceInfo(HttpServletRequest req ){
        Map reqMap = req.getParameterMap();
        Object invoiceInfo = ((String[])reqMap.get("invoiceInfo"))[0];
        Map invoiceInfoMap = JSON.parseObject(invoiceInfo.toString());
        HttpSession session = req.getSession();
        LogInfo loginfo = (LogInfo) session.getAttribute("logUser");
        Map paraMap = Maps.newHashMap();
        paraMap.put("SUBSCRIBE_ID", invoiceInfoMap.get("subscribeId"));
        paraMap.put("INVOICE_ID", invoiceInfoMap.get("id"));
        paraMap.put("INVOICE_TYPE", invoiceInfoMap.get("type"));
        paraMap.put("INVOICE_ITEM", invoiceInfoMap.get("item"));
        paraMap.put("INVOICE_TITLE", invoiceInfoMap.get("title"));
        paraMap.put("INVOICE_COUNT", invoiceInfoMap.get("amount"));
        paraMap.put("INVOICE_AMOUNT", invoiceInfoMap.get("money"));
        paraMap.put("REMARK", invoiceInfoMap.get("remark"));
        paraMap.put("ACCEPT_STAFF", loginfo.getStaff_id());

        try {
            log.info("------------------------添加发票----------------------");
            log.info("发票参数："+paraMap+"操作人:"+loginfo.getStaff_id());
            editService.addInvoice(paraMap);
            
        } catch (Exception e) {
            log.info("------------------------添加发票失败----------------------");
            log.info("失败原因:"+e.getMessage());
            return e.getMessage();
        }
        
        return "添加成功";
    }
    
  //删除发票信息
    @RequestMapping(value="deleteInvoice",method=RequestMethod.POST)
    @ResponseBody
    public String deleteOneInvoiceInfo(HttpServletRequest req ){
    	String invoiceId = req.getParameter("INVOICE_ID");
    	Map paraMap = Maps.newHashMap();
    	paraMap.put("INVOICE_ID",invoiceId);
    	log.info("------------------删除发票-------------------------");
    	log.info("参数:"+paraMap);
    	try {
            editService.deleteInvoiceInfo(paraMap);
            List<Map> invoiceList = editService.queryInvoice(paraMap);
            if(invoiceList.size()==0){//该订单下发票删除干净了就改状态
                paraMap.put("INVOICE_FLAG", "2");
                editService.updateInvioceStateInSubScribe(paraMap);
            }
        } catch (Exception e) {
            log.info("------------------删除发票失败-------------------------");
            log.info("失败原因:"+e.getMessage());
            return "删除失败";
        }
    	log.info("------------------删除发票结束-------------------------");
    	return "该条配送信息已删除";
    }
    //修改发票信息
    @RequestMapping(value="updateInvoice",method=RequestMethod.POST)
    @ResponseBody
    public String updateOneInvoiceInfo(HttpServletRequest req ){
        Map reqMap = req.getParameterMap();
        Object invoiceInfo = ((String[])reqMap.get("invoiceInfo"))[0];
        Map invoiceInfoMap = JSON.parseObject(invoiceInfo.toString());
        
        Map paraMap = Maps.newHashMap();
        paraMap.put("SUBSCRIBE_ID", invoiceInfoMap.get("subscribeId"));
        paraMap.put("INVOICE_ID", invoiceInfoMap.get("id"));
        paraMap.put("INVOICE_TYPE", invoiceInfoMap.get("type"));
        paraMap.put("INVOICE_TITLE", invoiceInfoMap.get("title"));
        paraMap.put("INVOICE_COUNT", invoiceInfoMap.get("amount"));
        paraMap.put("INVOICE_AMOUNT", invoiceInfoMap.get("money"));
        
        try {
            editService.updateInvoiceInfo(paraMap);
        } catch (Exception e) {
            e.printStackTrace();
            return "修改失败"+e.getMessage();
        }
        
        return "修改成功";
    }
    
    //更新是否未付款先发货
    @RequestMapping(value="updateOrderSendFirst",method=RequestMethod.POST)
    @ResponseBody
    public String updateSendFirstOrNot(HttpServletRequest req ){
        String parentId = req.getParameter("SUBSCRIBE_ID");
        String sendFirst =req.getParameter("SEND_FIRST");
        LogInfo logUser = (LogInfo) req.getSession().getAttribute("logUser");
        Map paraMap = Maps.newHashMap();
        paraMap.put("SUBSCRIBE_ID",parentId);
        paraMap.put("SEND_FIRST",sendFirst);
        paraMap.put("staffId", logUser.getStaff_id());
        try {
            editService.upadateSendFirst(paraMap);
        } catch (Exception e) {
            return "保存失败";
        }
        
        return "保存成功";
    }
    
}
