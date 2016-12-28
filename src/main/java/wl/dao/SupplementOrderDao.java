package wl.dao;

import java.util.Map;

import org.n3r.eql.Eql;
import org.springframework.stereotype.Repository;

import wl.entity.OrderInfo;
import freemarker.template.utility.StringUtil;

@Repository("supplementOrderDao")
public class SupplementOrderDao {
	public String CreateOrderId(String str){
		Object s = new Eql().selectFirst("seq").params(str).execute();
		String num=StringUtil.leftPad(""+s, 6, '0');
		return num;
	}
	//存储订单信息
	public void SaveOrderInfo(Map map){
		new Eql().insert("insertOrderInfo").params(map).execute();
	}
	//存储总订单信息
	public void saveSubscribeInfo(Map map){
		new Eql().insert("insertSubscribeInfo").params(map).execute();
	}
	//存储配送信息
	public void saveSendInfo(Map map){
		new Eql().insert("insertDistributInfo").params(map).execute();
	}
	//更新订单信息
	public void updateOrderInfo(String str){
		new Eql().update("updateOrderInfo").params(str).execute();
	}
	//更新总订单信息
	public void updateSubscribeInfo(String str){
		new Eql().update("updateSubscribeInfo").params(str).execute();
	}
	//查询配送单的库存数量
	public int selectSumStock(Map map,Eql eql){
		int num=0;
		Map outMap=eql.selectFirst("selectStock").params(map).execute();
		if(outMap!=null){
			num=Integer.parseInt(""+outMap.get("REMAIN_COUNT"))-Integer.parseInt(""+outMap.get("OCCUPY_AMOUNT"));
		}
		return num;
	}
	public void updateOccupyStock(Map map,Eql eql){
		eql.update("updateOccupyStock").params(map).execute();
	}
}
