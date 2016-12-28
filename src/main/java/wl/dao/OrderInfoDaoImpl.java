package wl.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jetty.util.log.Log;
import org.n3r.eql.Eql;
import org.n3r.eql.EqlPage;
import org.n3r.eql.map.EqlRun;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import wl.entity.OrderInfo;

@Repository("orderInfoDao")
public class OrderInfoDaoImpl {
    
    private Eql eql= new Eql();
    
    public void subOrder(OrderInfo orderInfo) {
        
        Map<String, String> paraMap = Maps.newHashMap();
        paraMap.put("orderId", orderInfo.getOrderId());
        paraMap.put("subscribeType", orderInfo.getCustType());
        paraMap.put("orderState", "");
        
    }

    public  List<Map> qryExisitCust(Map paraMap) {
        return eql.select("selectCustInfo").params(paraMap).execute();
    }

    public  Map qryCertainCust(Map custMap) {
        return eql.selectFirst("selectCertainCust").params(custMap).execute();
    }


    //存储总订单信息  
    public void saveSubscribeInfo(Map map){
        
        
        eql.insert("insertParentOrder").params(map).execute();
    }

    public List<Map> selectPayment(Map paraMap) {
        List<Map> resultList = Lists.newArrayList();
        resultList = eql.select("selectPayment").params(paraMap).execute();
//        List<EqlRun> o = eql.getEqlRuns();
//        System.out.println(o.get(0).getParamBean());
//        System.out.println(o.get(0).getRunSql());
        return resultList;
    }

    public List<Map> selectOrder(Map paraMap) {
        return eql.select("selectOrdertoBind").params(paraMap).execute();
    }

	public void bindpayMent(List<Map> paymentList, Map orderMap) {
		// TODO Auto-generated method stub
		Map inMap = new HashMap();
		List paymentIdList = Lists.newArrayList();
		for(Map tempMap:paymentList){
			paymentIdList.add(tempMap.get("paymentId"));
		}
        inMap.putAll(orderMap);
        inMap.put("paymentList", paymentIdList);
		 eql.update("bindpaymentToOrder").params(inMap).execute();
				
	}

    public Map queryStockAmountByPrintCode(Map inMap) {
        
        return eql.selectFirst("queryStockAmountByPrintId").params(inMap).execute();
    }

    public Eql getEql() {
        return eql;
    }

    public void setEql(Eql eql) {
        this.eql = eql;
    }

    public void occupyBookAftCheck(Map tempMap) {
        eql.update("occupyBookAfterCheck").params(tempMap).execute();
        
    }

    public Map searchTotalMoneyHasBind(Map orderMap) {
        
        return eql.selectFirst("selectTotalBindMoneyAsOrderId").params(orderMap).execute();
    }

    public void updateSubscribleState(Map orderMap) {
        
        eql.update("updateOrderStateToWaitToSend").params(orderMap).execute();
    }

    public void addNewCustomer(Map inMap) {
        eql.insert("insertANewCustomer").params(inMap).execute();
    }
    

}
