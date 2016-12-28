package wl.service;

import java.util.List;
import java.util.Map;

import javassist.compiler.ast.Variable;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jcraft.jsch.Buffer;

import wl.common.utils.OrderUtils;
import wl.common.utils.ProDuctUtils;
import wl.dao.FananceFromDao;

@Service("fananceFromSrv")
public class FananceFromService {
	@Autowired
	@Qualifier("fananceFormDao")
	private FananceFromDao fananceFormDao;

	//总金额统计
	public List<Map> qryFananceSumInfo(Map paraMap) {
	    String payType=paraMap.get("PAY_TYPE").toString();
	    
		if("2".equals(payType)){//银行
			return fananceFormDao.selectFananceSumBank(paraMap);
		}else if("1".equals(payType)){//邮局
		    return fananceFormDao.selectFananceSumPostOffice(paraMap);
		}else{//现金
		    return fananceFormDao.selectFananceSumCash(paraMap);
		}
	}
	
	//查询汇款单详细信息 非现金
	public List<Map> qryOrderPaySituation(Map paraMap) throws Exception{
			List<Map> resultMap= fananceFormDao.qryNoneCashPayentInfo(paraMap);
			for(Map map:resultMap){//循环总订单
				if(map.get("REMARK")==null){
					map.put("REMARK", "");
				}
				//获取订单中的购买信息
				String subscirbeId = map.get("SUBSCRIBE_ID")==null?"":map.get("SUBSCRIBE_ID").toString();
				if(!"".equals(subscirbeId)){
				    Map inMap = Maps.newHashMap();
				    inMap.put("SUBSCRIBE_ID", subscirbeId);
				    List<Map> childOrder = OrderUtils.qryChildOrdersWithOutSendByParentId(inMap);
				    StringBuffer orderProDesc=new StringBuffer();
				    for(Map childMap:childOrder){//遍历每条总订单中的子订单
				        //List< Map> sendList = fananceFormDao.qrySendInfoByParentId(inMap);
				        List< Map> sendList = fananceFormDao.qrySendInfoByOrderId(childMap);
				        orderProDesc.append(getProdInfoDesc(sendList));
				    }
				   map.put("BUY_COUNT", orderProDesc.toString());
				   orderProDesc.delete(0,orderProDesc.length()-1);//每条订单结束后清空
				}
			}
			return resultMap;
	}
	private String getProdInfoDesc(List< Map> sendList) {
	    String proTypeDesc[] = { "", "专业版", "普及版" };
	    StringBuffer sbBuffer =  new StringBuffer();
	    Map firstSend =  sendList.get(0);
	    Map lastSend = sendList.get(sendList.size()-1);
	    String isConsistance = firstSend.get("CONTIN_MONTH").toString();
	    int startYear = paseInt(firstSend.get("YEAR").toString());
        int endYear = paseInt(lastSend.get("YEAR").toString());
        int amountEachMonth = paseInt(firstSend.get("PRODUCT_PER_COUNT").toString());
        int yearNum = endYear-startYear+1;
        int protype = paseInt(firstSend.get("PRODUCT_TYPE").toString());
        
	    if("1".equals(isConsistance)){//连续
	        if(sendList.size()%12==0){
	           
	            if(yearNum>1){
	                sbBuffer.append(proTypeDesc[protype]+startYear+"年--"+endYear+"年各"+amountEachMonth+"套;");   
	            }else{
	                sbBuffer.append(proTypeDesc[protype]+startYear+"年"+amountEachMonth+"套;");  
	            }
	        }else{
	            //System.out.println(firstSend.get("MONTH")+"---"+lastSend.get("MONTH"));
	            sbBuffer.append(proTypeDesc[protype]+startYear+"-"+firstSend.get("MONTH").toString()+"至"+
	                              endYear+"-"+lastSend.get("MONTH").toString()+"每期"+amountEachMonth+"本;");
	        }
	       
	    }else{
	        for(Map sendMap:sendList){
	            String printCode = sendMap.get("YEAR").toString()+sendMap.get("MONTH").toString()+sendMap.get("PRODUCT_TYPE").toString();
	            sbBuffer.append(ProDuctUtils.prodToDesc(printCode)+amountEachMonth+"本;");
	        }
	    }
        return sbBuffer.toString();
    }

    //查询汇款单详细信息并导出  非现金
	public List<Map> qryOrderPaySituationExport(Map paraMap) {
		List<Map> resultMap= fananceFormDao.qryNoneCashPayentInfo(paraMap);
		for(Map map:resultMap){
			if(map.get("REMARK")==null){
				map.put("REMARK", "");
			}
		}
		return resultMap;
	}
    //导出现金的
    public List<Map> qryOrderPaySituationCashExport(Map paraMap) {
		// TODO Auto-generated method stub
		 return fananceFormDao.qryCashPaymentInfoExport(paraMap);
	}
	

    public List<Map> qryOrderPaySituationCash(Map paraMap) {
		// TODO Auto-generated method stub
		 List<Map> resultList = fananceFormDao.qryCashPaymentInfo(paraMap);
		 //拼装产品信息
		 resultList=combinateProd1(resultList);
		 //拼装发票信息
		 resultList=combInvioce(resultList);
		 return resultList;
	}

    private List<Map> combInvioce(List<Map> resultList) {
        List<Map> returnList = Lists.newArrayList();
        for(Map orderMap:resultList){
            String subscribleId = orderMap.get("SUBSCRIBE_ID").toString();
            Map paraMap = Maps.newHashMap();
            paraMap.put("SUBSCRIBE_ID", subscribleId);
            Map resultMap = fananceFormDao.qryInvoiceMoneyAllById(paraMap);
            if(resultMap!=null){
            orderMap.put("TOTALMONEY", resultMap.get("TOTALMONEY")==null?"0":resultMap.get("TOTALMONEY"));
            }else{
                orderMap.put("TOTALMONEY", "0"); 
            }
            returnList.add(orderMap);
        }
        return returnList;
    }

    
    private List<Map> combinateProd1(List<Map> resultMap){
        for(Map map:resultMap){//循环总订单
            if(map.get("REMARK")==null){
                map.put("REMARK", "");
            }
            //获取订单中的购买信息
            String subscirbeId = map.get("SUBSCRIBE_ID")==null?"":map.get("SUBSCRIBE_ID").toString();
            if(!"".equals(subscirbeId)){
                Map inMap = Maps.newHashMap();
                inMap.put("SUBSCRIBE_ID", subscirbeId);
                List<Map> childOrder = OrderUtils.qryChildOrdersWithOutSendByParentId(inMap);
                StringBuffer orderProDesc=new StringBuffer();
                for(Map childMap:childOrder){//遍历每条总订单中的子订单
                    List< Map> sendList = fananceFormDao.qrySendInfoByParentId(inMap);
                    orderProDesc.append(getProdInfoDesc(sendList));
                }
               map.put("PRODUCT_INFO", orderProDesc.toString());
               orderProDesc.delete(0,orderProDesc.length()-1);//每条订单结束后清空
            }
        }
        return resultMap;
    }
    
    private int paseInt(String s) {
        return Integer.parseInt(s);
    }
    private int caculateMonth(String start, String end) {
        return paseInt(end.substring(5))-paseInt(start.substring(5))+1;
    }

}
