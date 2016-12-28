package wl.common.utils;

import java.util.List;
import java.util.Map;

import org.n3r.eql.Eql;
import org.n3r.eql.EqlPage;
import org.n3r.eql.map.EqlRun;

import com.google.common.collect.Lists;

public class OrderUtils {
    private static Eql eql = new Eql();
    /**
     * 订单查询的通用函数
     * 
     * 该函数的入参为Map类型 ，根据所给条件查询订单全信息  
     * Map中的key值和数据库字段名一致
     * @param inMap
     * @return
     */
    public static List<Map> queryOrders(Map inMap) {
        
        EqlPage page = (EqlPage)inMap.get("eqlPage");
        List<Map> resultList = Lists.newArrayList();
        
        resultList = eql.select("selectOrderCommon").params(inMap).limit(page).execute();
        List<EqlRun> o = eql.getEqlRuns();
        return resultList;
    }
    /**
     * 根据子订单号查询子订单和该子订单下所有的配送信息
     * 使用者自行传入参数作为查询条件  KEY和数据库字段名一致
     * @param ids
     * @return
     */
    public static List<Map> qryChildOrdersByParentId(Map ids) {
        List<Map> resultList = eql.select("selectOrdersByParentIds").params(ids).execute();
        
        return resultList;
    }
    /**
     * 单纯的查询子订单信息   不包含配送信息
     */
    public static List<Map> qryChildOrderOnly(Map paraMap){
        
        return eql.select("selectChildOrderByChildId").params(paraMap).execute();
    }
    
    /**
     * 查询配送信息 该表中所有字段均可以作为条件查询
     * @param paraMap
     * @return
     */
    public static List<Map> qrySendListOnly(Map paraMap) {
        
        return eql.select("selectSendInfo").params(paraMap).execute();
    }
    
	public static void deleteSendInfo(Map paraMap) {
		eql.delete("deleteSendInfo").params(paraMap).execute();
		
	}
	
	//
    public static List<Map> qryChildOrdersWithOutSendByParentId( Map paraMap) {
        // TODO Auto-generated method stub
        return eql.select("selectChildOrderWithOutSendBySubscribleID").params(paraMap).execute();
    }
    
}
