package wl.service;

import java.util.List;
import java.util.Map;

import org.n3r.eql.EqlPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import wl.dao.SureInvoiceDao;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Service("sureInvoiceSrv")
public class SureInvoiceService {
    @Autowired
    @Qualifier("sureInvoiceDao")
    private SureInvoiceDao sureInvoiceDao;

    public List<Map> queryOrders(Map inMap,EqlPage eqlPage) {
        List<Map> invoiceList=sureInvoiceDao.selectTypedInvoice(inMap, eqlPage);
        for(Map inputMap:invoiceList){
        	if(inputMap.get("INVOICE_TYPE_TIME")==null){
        		inputMap.put("INVOICE_TYPE_TIME", "");
        	}
        	if(inputMap.get("INVOICE_NO")==null){
        		inputMap.put("INVOICE_NO", "");
        	}
        }
        invoiceList = combinatePayInfo(invoiceList);
        return invoiceList;
    }

    private List<Map> combinatePayInfo(List<Map> invoiceList) {
        List<Map> returnList = Lists.newArrayList();
        for(Map invoiceMap:invoiceList){
            String subId = invoiceMap.get("SUBSCRIBE_ID").toString();
            List<Map> orderPayInfoList = sureInvoiceDao.qryPayInfoAsSubId(subId);
            if(orderPayInfoList.size()==0){//没有汇款单，包括没有付款的，现金付款的情况
                invoiceMap.put("PAYMENT_NAME", "现金交易或者未付款无汇款信息");
                invoiceMap.put("PAYMENT_TIME", "无");
                invoiceMap.put("PAYMENT_VALUE", "0");
            }else{//一条订单可能绑定了多条汇款单
                StringBuffer payName= new StringBuffer();
                StringBuffer paytime= new StringBuffer();
                StringBuffer payvalue=new StringBuffer();
                for(Map payMap:orderPayInfoList){
                    payName.append(payMap.get("PAYMENT_NAME").toString()+";");
                    paytime.append(payMap.get("PAYMENT_TIME").toString()+";");
                    payvalue.append(payMap.get("PAYMENT_VALUE").toString()+"元;");
                }
                invoiceMap.put("PAYMENT_NAME", payName);
                invoiceMap.put("PAYMENT_TIME", paytime);
                invoiceMap.put("PAYMENT_VALUE", payvalue);
            }
            returnList.add(invoiceMap);
        }
        return returnList;
    }

    public void updateInfo(Map inMap){
    	sureInvoiceDao.updateInvoice(inMap);
    	Map map=sureInvoiceDao.selectSub(inMap);
    	if(map!=null){
    		int total=Integer.parseInt(""+map.get("TOTAL_MONEY"));
    		int num=Integer.parseInt(""+map.get("num"));
    		if(total==num){
    			sureInvoiceDao.updateSub(inMap);
    		}
    	}
    }
    
    
}
