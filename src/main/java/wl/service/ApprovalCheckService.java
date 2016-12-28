package wl.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.n3r.eql.EqlPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import wl.dao.ApprovalCheckDao;
import wl.entity.LogInfo;
/**
 * 领导审核
 *
 */
@Service("approvalCheckService")
public class ApprovalCheckService {
    @Autowired
    @Qualifier("approvalCheckDao")
    private ApprovalCheckDao approvalCheckDao;
    public List<Map> appCheck(String appState,String appType,HttpSession session,EqlPage eqlPage){
    	List<Map> result=new ArrayList<Map>();
    	Map map=new HashMap();
    	LogInfo log=(LogInfo) session.getAttribute("logUser");
    	String right=log.getRoleName();//position
    	int role=right.indexOf("领导");
    	if(role>=0){
			map.put("staffRight", 1+"");
		}else{
			map.put("staffRight", 0+"");
			map.put("staff_id",log.getStaff_id());
		}
    	if(appType.contains("0")){
    		if("3".equals(appState)){
    			appState=""+(Integer.parseInt(appState)+1);
    		}
    		map.put("appState", appState+"");
    		List<Map> inMap=approvalCheckDao.SendFeeInfo(map, eqlPage);
    		for(int i=0;i<inMap.size();i++){
    			Map outMap=new HashMap();
    			outMap.put("stateName", "末付款先发货");
    			outMap.put("detail", (inMap.get(i).get("COMPANY")==null?"":inMap.get(i).get("COMPANY"))+""+inMap.get(i).get("CUSTOMER_NAME")+"申请先发货");
    			outMap.put("count", "");
    			outMap.put("staffId", inMap.get(i).get("UPDATE_STAFF"));
    			outMap.put("updateTime", (""+inMap.get(i).get("UPDATE_TIME")).substring(0,10));
    			if("1".equals(inMap.get(i).get("SUBSCRIBE_STATE"))){
    				outMap.put("appState", "未审批");
    			}else if("2".equals(inMap.get(i).get("SUBSCRIBE_STATE"))){
    				outMap.put("appState", "审批通过");
    			}else if("4".equals(inMap.get(i).get("SUBSCRIBE_STATE"))){
    				outMap.put("appState", "审批未通过");
    			}
    			if(inMap.get(i).get("APPROVAL_TIME")!=null){
    				outMap.put("appTime", (""+inMap.get(i).get("APPROVAL_TIME")).substring(0,10));
    			}else{
    				outMap.put("appTime","");
    			}
    			outMap.put("SUBSCRIBE_ID",inMap.get(i).get("SUBSCRIBE_ID"));
    			result.add(outMap);
    		}
    	}
    	if(appType.contains("1")){
    		List<Map> inMap=new ArrayList<Map>();
    		if("0".equals(appState)){
    			inMap=approvalCheckDao.printAllCheckInfo(map, eqlPage);
    		}else{
    			map.put("appState", appState);
    			inMap=approvalCheckDao.printCheckInfo(map, eqlPage);
    		}
    		for(int i=0;i<inMap.size();i++){
    			Map outMap=new HashMap();
    			outMap.put("stateName", "印刷量审批");
    			outMap.put("detail", ((String) inMap.get(i).get("PRINT_CODE")).substring(0,4)+"年"+((String) inMap.get(i).get("PRINT_CODE")).substring(4,6)+"期"+("1".equals(((String) inMap.get(i).get("PRINT_CODE")).substring(6,7))?"专业版":"普及版"));
    			outMap.put("count", "印刷数量："+inMap.get(i).get("TOTAL_COUNT"));
    			outMap.put("staffId", inMap.get(i).get("UPDATE_STAFF"));
    			outMap.put("updateTime", (""+inMap.get(i).get("ACCEPT_TIME")).substring(0,10));
    			if("1".equals(inMap.get(i).get("APPROVE_STATUS"))){
    				outMap.put("appState", "未审批");
    			}else if("2".equals(inMap.get(i).get("APPROVE_STATUS"))){
    				outMap.put("appState", "审批通过");
    			}else if("3".equals(inMap.get(i).get("APPROVE_STATUS"))){
    				outMap.put("appState", "审批未通过");
    			}
    			if(inMap.get(i).get("APPROVE_TIME")!=null){
    				outMap.put("appTime", (""+inMap.get(i).get("APPROVE_TIME")).substring(0,10));
    			}else{
    				outMap.put("appTime","");
    			}
    			outMap.put("SUBSCRIBE_ID",inMap.get(i).get("PRINT_CODE"));
    			result.add(outMap);
    		}
    	}
    	if(appType.contains("2")){
    		map.put("appState", appState+"");
    		List<Map> inMap=approvalCheckDao.supplementInfo(map, eqlPage);
    		if(inMap.size()==1 && inMap.get(0).get("total")==null){
    			inMap.remove(0);
    		}
    		for(int i=0;i<inMap.size();i++){
    			Map outMap=new HashMap();
    			outMap.put("stateName", "赠刊");
    			outMap.put("detail", inMap.get(i).get("COMPANY")+""+inMap.get(i).get("CUSTOMER_NAME")+"申请赠刊");
    			outMap.put("count", "领用数量："+inMap.get(i).get("total"));
    			outMap.put("staffId", inMap.get(i).get("UPDATE_STAFF"));
    			outMap.put("updateTime", (""+inMap.get(i).get("UPDATE_TIME")).substring(0,10));
    			if("1".equals(inMap.get(i).get("SUBSCRIBE_STATE"))){
    				outMap.put("appState", "未审批");
    			}else if("2".equals(inMap.get(i).get("SUBSCRIBE_STATE"))){
    				outMap.put("appState", "审批通过");
    			}else if("4".equals(inMap.get(i).get("SUBSCRIBE_STATE"))){
    				outMap.put("appState", "审批未通过");
    			}
    			if(inMap.get(i).get("APPROVAL_TIME")!=null){
    				outMap.put("appTime", (""+inMap.get(i).get("APPROVAL_TIME")).substring(0,10));
    			}else{
    				outMap.put("appTime", "");
    			}
    			outMap.put("SUBSCRIBE_ID",inMap.get(i).get("SUBSCRIBE_ID"));
    			result.add(outMap);
    		}
    	}
    	if(appType.contains("3")){
    		map.put("appState", appState+"");
    		List<Map> inMap=approvalCheckDao.bookInfo(map, eqlPage);
    		if(inMap.size()==1 && inMap.get(0).get("total")==null){
    			inMap.remove(0);
    		}
    		for(int i=0;i<inMap.size();i++){
    			Map outMap=new HashMap();
    			outMap.put("stateName", "样刊");
    			outMap.put("detail", inMap.get(i).get("COMPANY")+""+inMap.get(i).get("CUSTOMER_NAME")+"申请样刊");
    			outMap.put("count", "领用数量："+inMap.get(i).get("total"));
    			outMap.put("staffId", inMap.get(i).get("UPDATE_STAFF"));
    			outMap.put("updateTime", (""+inMap.get(i).get("UPDATE_TIME")).substring(0,10));
    			if("1".equals(inMap.get(i).get("SUBSCRIBE_STATE"))){
    				outMap.put("appState", "未审批");
    			}else if("2".equals(inMap.get(i).get("SUBSCRIBE_STATE"))){
    				outMap.put("appState", "审批通过");
    			}else if("4".equals(inMap.get(i).get("SUBSCRIBE_STATE"))){
    				outMap.put("appState", "审批未通过");
    			}
    			if(inMap.get(i).get("APPROVAL_TIME")!=null){
    				outMap.put("appTime", (""+inMap.get(i).get("APPROVAL_TIME")).substring(0,10));
    			}else{
    				outMap.put("appTime","");
    			}
    			outMap.put("SUBSCRIBE_ID",inMap.get(i).get("SUBSCRIBE_ID"));
    			result.add(outMap);
    		}
    	}
    	if(appType.contains("4")){
    		map.put("appState", appState+"");
    		List<Map> inMap=approvalCheckDao.sendInvoiceInfo(map, eqlPage);
    		for(int i=0;i<inMap.size();i++){
    			Map outMap=new HashMap();
    			outMap.put("stateName", "末付款先打印发票");
    			outMap.put("detail", (inMap.get(i).get("COMPANY")==null?"":inMap.get(i).get("COMPANY"))+""+inMap.get(i).get("CUSTOMER_NAME")+"申请先打印发票");
    			outMap.put("count", "");
    			outMap.put("staffId", inMap.get(i).get("UPDATE_STAFF"));
    			outMap.put("updateTime", (""+inMap.get(i).get("UPDATE_TIME")).substring(0,10));
    			if("1".equals(inMap.get(i).get("BEFORE_INVOICE_TAG"))){
    				outMap.put("appState", "未审批");
    			}else if("2".equals(inMap.get(i).get("BEFORE_INVOICE_TAG"))){
    				outMap.put("appState", "审批通过");
    			}else if("3".equals(inMap.get(i).get("BEFORE_INVOICE_TAG"))){
    				outMap.put("appState", "审批未通过");
    			}
    			if(inMap.get(i).get("APPROVAL_TIME")!=null){
    				outMap.put("appTime", (""+inMap.get(i).get("APPROVAL_TIME")).substring(0,10));
    			}else{
    				outMap.put("appTime","");
    			}
    			outMap.put("SUBSCRIBE_ID",inMap.get(i).get("SUBSCRIBE_ID"));
    			result.add(outMap);
    		}
    	}
    	return result;
    }
} 