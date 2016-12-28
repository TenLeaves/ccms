package wl.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.n3r.eql.Eql;
import org.n3r.eql.EqlPage;
import org.n3r.eql.EqlTran;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import wl.dao.EditDao;
import wl.entity.LogInfo;
import wl.service.EditService;
import wl.service.InvoiceEntryService;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Controller
public class InvoiceInController {
    @Autowired
    @Qualifier("invoiceEntrySrv")
    private InvoiceEntryService invoiceEntryService;
    
    @Autowired 
    @Qualifier("editService")
    private EditService editService;

    private Log log = LogFactory.getLog(this.getClass());
    
    @RequestMapping(value = "searchOdersInvoice", method = {
            RequestMethod.POST, RequestMethod.GET })
    public String searchOrdersByCondition(HttpServletRequest req,
            EqlPage eqlPage, Model model) {
        Map paraMap = req.getParameterMap();
        Map inMap = Maps.newHashMap();
        Object conditionObj = paraMap.get("condition");
        List<Map> orders = Lists.newArrayList();
        inMap.put("eqlPage", eqlPage);
        if (conditionObj != null) {
            Object condition = ((String[]) conditionObj)[0];
            Map conditionMap = (Map) JSON.parse(condition.toString());

            inMap.put("SUBSCRIBE_ID", conditionMap.get("orderId"));
            inMap.put("OrderStartTime", conditionMap.get("orderStart"));
            inMap.put("OrderEndTime", conditionMap.get("orderEnd"));
            inMap.put("CUSTOMER_NAME", conditionMap.get("contactName"));
            inMap.put("COMPANY", conditionMap.get("contactAddr"));

            if (conditionMap.get("orderState") != null&&!"".equals(conditionMap.get("orderState").toString())) {
                if (conditionMap.get("orderState").toString().equals("0")) {// 所有
                    inMap.put("SUBSCRIBE_STATE", null);
                    inMap.put("INVOICE_RECORD", "1");
                    orders = invoiceEntryService.queryOrders(inMap);

                } else if (conditionMap.get("orderState").toString()
                        .equals("5")) {// 需要审批
                    inMap.put("SUBSCRIBE_STATE", "1");
                    inMap.put("FIRST_SEND", "1");
                    orders = invoiceEntryService.queryOrders(inMap);

                } else if (conditionMap.get("orderState").toString()
                        .equals("1")) {
                    inMap.put("FIRST_SEND", "2");
                    inMap.put("SUBSCRIBE_STATE", "1");// 代付款
                    orders = invoiceEntryService.queryOrders(inMap);

                    inMap.put("SUBSCRIBE_STATE", "4");// 审批未通过
                    orders.addAll(invoiceEntryService.queryOrders(inMap));
                }else{
                    inMap.put("SUBSCRIBE_STATE", conditionMap.get("orderState"));// 代付款
                    orders = invoiceEntryService.queryOrders(inMap);
                }
            }else{
                inMap.put("SUBSCRIBE_STATE", null);
                inMap.put("INVOICE_RECORD", "1");
                orders = invoiceEntryService.queryOrders(inMap);
            }
        } else {
            inMap.put("SUBSCRIBE_STATE", null);
            orders = invoiceEntryService.queryOrders(inMap);
        }
        orders = transStateDesc(orders);

        model.addAttribute("orderList", orders);
        return "pages/invoice/invoiceFrag";
    }
    
    @RequestMapping(value = "searchInvoiceToType", method = {
            RequestMethod.POST, RequestMethod.GET })
    public String searchInvoiceToType(HttpServletRequest req,
            EqlPage eqlPage, Model model) {
        Map paraMap = req.getParameterMap();
        Map inMap = Maps.newHashMap();
        Object conditionObj = paraMap.get("condition");
        if(conditionObj!=null){
            
            inMap.put("eqlPage", eqlPage);
            Object condition = ((String[]) conditionObj)[0];
            Map conditionMap = (Map) JSON.parse(condition.toString());
            
            if(notEmptyInMap(conditionMap,"state")){
                inMap.put("INVOICE_STATE", conditionMap.get("state"));
            }
            if(notEmptyInMap(conditionMap, "startTime")){
                inMap.put("invoiceTypeTimeStart", conditionMap.get("startTime"));
            }
            if(notEmptyInMap(conditionMap, "endTime")){
                inMap.put("invoiceTypeTimeEnd", conditionMap.get("endTime"));
            }
            
            
        }
        List<Map> result =invoiceEntryService.qureyTypedInvoice(inMap) ;   
        for(Map map:result){
        	if(map.get("INVOICE_NO")==null || "".equals(map.get("INVOICE_NO").toString())){
        		map.put("INVOICE_NO", "");
        	}
        	if(map.get("REMARK")==null || "".equals(map.get("REMARK").toString())){
        		map.put("REMARK", "");
        	}
        	if(map.get("INVOICE_TITLE")==null || "".equals(map.get("INVOICE_TITLE").toString())){
        		map.put("INVOICE_TITLE", "");
        	}
        	if(map.get("INVOICE_TYPE_TIME")==null || "".equals(map.get("INVOICE_TYPE_TIME").toString())){
        		map.put("INVOICE_TYPE_TIME", "");
        	}
        }
        
        model.addAttribute("invoiceList", result); 
        return "pages/invoice/invoiceTypedFrag";
    }

    private boolean notEmptyInMap(Map conditionMap,String key) {
        return conditionMap.get(key)!=null&&!"".equals(conditionMap.get(key).toString());
    }

    // 把状态展示成中文
    private List<Map> transStateDesc(List<Map> orders) {
        List<Map> returnList = Lists.newArrayList();
        for (Map orderMap : orders) {
            String state = (String) orderMap.get("SUBSCRIBE_STATE");
            String sendFirst = (String) orderMap.get("FIRST_SEND");
            if ("1".equals(state)) {// 代付款
                if ("2".equals(sendFirst)) {// 不先发货
                    orderMap.put("SUBSCRIBE_STATE_DESC", "待收款");
                } else {
                    orderMap.put("SUBSCRIBE_STATE_DESC", "待审批");
                }

            } else if ("2".equals(state)) {
                orderMap.put("SUBSCRIBE_STATE_DESC", "待配送");
            } else if ("3".equals(state)) {
                orderMap.put("SUBSCRIBE_STATE_DESC", "已完成");
            } else if ("4".equals(state)) {// 不通过
                orderMap.put("SUBSCRIBE_STATE_DESC", "待收款");
            }
            returnList.add(orderMap);
        }
        return returnList;
    }

    @ModelAttribute("eqlPage")
    public EqlPage getEqlPage(EqlPage eqlPage) {
        if (eqlPage.getPageRows() == 0)
            eqlPage.setPageRows(5);
        return eqlPage;
    }
    /**
     * 导入回款单解析
     */
    @RequestMapping(value="/importInvoice", method = RequestMethod.POST)
    @ResponseBody
    public String handleUploadData(String subscribeId,@RequestParam("file") CommonsMultipartFile file,HttpServletRequest request){
        String str1="";
    	if (!file.isEmpty()) {
               String fileName = file.getOriginalFilename();
               String fileType = fileName.substring(fileName.lastIndexOf("."));
               Map<String, String[]> titleMap = new HashMap<String, String[]>();
               String[] title = { "INVOICE_TYPE", "INVOICE_TITLE", "INVOICE_COUNT","INVOICE_AMOUNT","INVOICE_ITEM","REMARK"};
               titleMap.put("title", title);
               Map map=new HashMap();
               List infos = new ArrayList();
               List errorInfos= new ArrayList();
               LogInfo logUser = (LogInfo) request.getSession().getAttribute("logUser");
        	   EditDao editDao=new EditDao();
        	   Eql eql=editDao.getEql();
        	   EqlTran tran=eql.newTran();
        	   editDao.setEql(eql);
               try{
                   log.info("------------------导入汇款单--------------------");
                  
                   infos = (List)ImportFileHelper.resolveFile(titleMap, file).get("infos");
                   log.info("参数:"+infos+"   操作员工:"+logUser.getStaff_id());
                   errorInfos = importValidata(infos);
                   LogInfo logInfo = (LogInfo)(request.getSession().getAttribute("logUser"));
                   String staffCode = logInfo.getStaff_id();
                   for(int i=0;i<infos.size();i++){
                	   Map inMap=new HashMap();
                	   inMap.put("SUBSCRIBE_ID", subscribeId);
                	   inMap.put("INVOICE_ID", invoiceEntryService.CreateInvoiceId("invoiceId"));
                	   inMap.put("INVOICE_TITLE", ((String) ((Map) infos.get(i)).get("INVOICE_TITLE")).trim());
                	   inMap.put("INVOICE_COUNT", ((String) ((Map) infos.get(i)).get("INVOICE_COUNT")).trim());
                	   inMap.put("INVOICE_AMOUNT", ((String) ((Map) infos.get(i)).get("INVOICE_AMOUNT")).trim());
                	   inMap.put("REMARK", ((String) ((Map) infos.get(i)).get("REMARK")).trim());
                	   inMap.put("ACCEPT_STAFF", logUser.getStaff_id());
                	   String invoiceType= ((String) ((Map) infos.get(i)).get("INVOICE_TYPE")).trim();
                	   if("普通发票".equals(invoiceType)){
                		   inMap.put("INVOICE_TYPE", 1);
                	   }else if("增值发票".equals(invoiceType)){
                		   inMap.put("INVOICE_TYPE", 2);
                	   }
                	   String invoiceItem= ((String) ((Map) infos.get(i)).get("INVOICE_ITEM")).trim();
                	   if("中国书法".equals(invoiceItem)){
                		   inMap.put("INVOICE_ITEM", 1);
                	   }else if("书本".equals(invoiceItem)){
                		   inMap.put("INVOICE_ITEM", 2);
                	   }else if("广告费".equals(invoiceItem)){
                		   inMap.put("INVOICE_ITEM", 3);
                	   }
                	   else if("宣传费".equals(invoiceItem)){
                		   inMap.put("INVOICE_ITEM", 4);
                	   }
                	   editService.addNewInvoice(inMap,i);
                   }
                   map.put("status", "success");
                   tran.commit();
               }catch( Exception e){
            	   tran.rollback();
                   e.printStackTrace();
                   map.put("status", e.getMessage());
               }
               
               int infoLength=infos.size();
               int errorLength=errorInfos.size();
               int sum=errorLength+infoLength;
               map.put("infoLength", infoLength);
               map.put("errorLength", errorLength);
               map.put("sum", sum);
               map.put("infos", infos);
               map.put("errorInfos", errorInfos);
               
               str1=JSON.toJSONString(map);
        }
		return str1;
    }
    /**
     * 校验批量上传客户信息
     * @throws Exception 
     */
    public List importValidata(List impList) throws Exception{
        List errorList = new ArrayList();
        
        if(impList.size()>500){
            throw new Exception("文件条数不能超过500条");
        }
        
        for(int i=0; i<impList.size(); i++){
            Map impMap = (Map)impList.get(i);
           
            
            // 发票类型
            if(impMap.get("INVOICE_TYPE") == null || "".equals(impMap.get("INVOICE_TYPE"))){
                impMap.put("error", "发票类型为空");
                impMap.put("right", "填写发票类型");
                errorList.add(impMap);
                impList.remove(i);
                i--;
                continue;
            }else{
            	String invoiceType = ((String)impMap.get("INVOICE_TYPE")).trim();
            	if(!"普通发票".equals(invoiceType) && !"增值发票".equals(invoiceType)){
            		impMap.put("error", "发票类型有误");
            		impMap.put("right", "正确填写发票类型");
            		errorList.add(impMap);
            		impList.remove(i);
            		i--;
            		continue;
            	}
            }
            
         // 发票抬头
            if(impMap.get("INVOICE_TITLE") == null || "".equals(impMap.get("INVOICE_TITLE"))){
                impMap.put("error", "发票抬头为空");
                impMap.put("right", "填写发票抬头");
                errorList.add(impMap);
                impList.remove(i);
                i--;
                continue;
            }
         // 发票数量
            if(impMap.get("INVOICE_COUNT") == null || "".equals(impMap.get("INVOICE_COUNT"))){
                impMap.put("error", "发票数量为空");
                impMap.put("right", "填写发票数量");
                errorList.add(impMap);
                impList.remove(i);
                i--;
                continue;
            }else{
            	String invoiceCount = ((String)impMap.get("INVOICE_COUNT")).trim();
                if(!invoiceCount.matches("[1-9][0-9]*")){
            		impMap.put("error", "发票数量不是数字");
            		impMap.put("right", "正确填写发票数量");
            		errorList.add(impMap);
            		impList.remove(i);
            		i--;
            		continue;
            	}
            }
         // 发票金额
            if(impMap.get("INVOICE_AMOUNT") == null || "".equals(impMap.get("INVOICE_AMOUNT"))){
                impMap.put("error", "发票金额为空");
                impMap.put("right", "填写发票金额");
                errorList.add(impMap);
                impList.remove(i);
                i--;
                continue;
            }else{
            	String invoiceAmount = ((String)impMap.get("INVOICE_AMOUNT")).trim();
                if(!invoiceAmount.matches("[1-9][0-9]*.?[0-9]*")){
            		impMap.put("error", "发票金额必须为正数");
            		impMap.put("right", "正确填写发票金额");
            		errorList.add(impMap);
            		impList.remove(i);
            		i--;
            		continue;
            	}
            }
            
         // 发票项目
            if(impMap.get("INVOICE_ITEM") == null || "".equals(impMap.get("INVOICE_ITEM"))){
                impMap.put("error", "发票项目为空");
                impMap.put("right", "填写发票项目");
                errorList.add(impMap);
                impList.remove(i);
                i--;
                continue;
            }else{
            	String invoiceItem = ((String)impMap.get("INVOICE_ITEM")).trim();
            	if(!"中国书法".equals(invoiceItem) && !"书本".equals(invoiceItem) 
            			&& !"广告费".equals(invoiceItem) && !"宣传费".equals(invoiceItem)){
            		impMap.put("error", "发票项目有误");
            		impMap.put("right", "正确填写发票项目");
            		errorList.add(impMap);
            		impList.remove(i);
            		i--;
            		continue;
            	}
            }
        } 
        return errorList;
    }
    
    @RequestMapping(value = "/exportAllInvoiceInfo", method = RequestMethod.POST)
    public void exportAllInvoiceInfo(HttpServletRequest request,HttpServletResponse response,Model model) {
         HttpSession session = request.getSession();
         Map conditionMap = Maps.newHashMap();
         if (session.getAttribute("userMap") != null) {
             conditionMap = (Map) session.getAttribute("userMap");
         }

         
        List<Map> result =invoiceEntryService.qureyTypedInvoice1(conditionMap) ;   
            for(Map map:result){
                if(map.get("INVOICE_NO")==null || "".equals(map.get("INVOICE_NO").toString())){
                    map.put("INVOICE_NO", "");
                }
                if(map.get("REMARK")==null || "".equals(map.get("REMARK").toString())){
                    map.put("REMARK", "");
                }
                if(map.get("INVOICE_TITLE")==null || "".equals(map.get("INVOICE_TITLE").toString())){
                    map.put("INVOICE_TITLE", "");
                }
                if(map.get("INVOICE_TYPE_TIME")==null || "".equals(map.get("INVOICE_TYPE_TIME").toString())){
                    map.put("INVOICE_TYPE_TIME", "");
                }
            }
            
         ExportInvoiceController export=new ExportInvoiceController();
         export.exportSendList(response, request, result);
    }
    
}
