package wl.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import wl.dao.InvoiceEntryDaoImpl;
import wl.dao.SureInvoiceDao;

@Service("invoiceEntrySrv")
public class InvoiceEntryService {
    @Autowired
    @Qualifier("invoiceEntryDao")
    private InvoiceEntryDaoImpl invoiceEntryDaoImpl;
    
    @Autowired
    @Qualifier("sureInvoiceDao")
    private SureInvoiceDao sureInvoiceDao;

    public List<Map> queryOrders(Map inMap) {
        //该处查询到的订单信息并不包括自定的一些信息，如果需要子订单的相关信息需要自己获取
        List<Map> orders=Lists.newArrayList();
        orders=invoiceEntryDaoImpl.selectOrders(inMap);
        if(orders.size()!=0){
          //根据总订单号获取子订单的信息 （订阅总数，已经配送总数）
            List ids = Lists.newArrayList();
            for(Map tempMap:orders){
                ids.add(tempMap.get("SUBSCRIBE_ID"));
            }
            Map paraMap = Maps.newHashMap();
            paraMap.put("parentIdList", ids);
            List<Map> childOrders = invoiceEntryDaoImpl.selectChildOrders(paraMap);
            
            for(int i=0;i<orders.size();i++){
                Map eachOrder =orders.get(i);
              String parentOrderId=(String) eachOrder.get("SUBSCRIBE_ID");
              int sendNum=0;
              for(Map childMap:childOrders){
                  if(parentOrderId.equals(childMap.get("SUBSCRIBE_ID"))){
                      sendNum = sendNum+Integer.parseInt(childMap.get("DISTRIBUT_COUNT").toString());
                  }
              }
              orders.get(i).put("TOTALSENDNUM", sendNum);
            }
        }
        
        return orders;
    }

    public List<Map> qureyTypedInvoice(Map inMap) {
        List<Map> result = invoiceEntryDaoImpl.selectTypedInvoice(inMap);
        return result;
    }
    public List<Map> qureyTypedInvoice1(Map inMap) {
        List<Map> result = invoiceEntryDaoImpl.selectTypedInvoice1(inMap);
        result = combinatePayInfo(result);
        return result;
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
    public String CreateInvoiceId(String str){
    	String num=invoiceEntryDaoImpl.CreateInvoiceId(str);
    	Date now=new Date();
    	SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
    	String date=sdf.format(now);
    	return date+num;
    }

    
}
