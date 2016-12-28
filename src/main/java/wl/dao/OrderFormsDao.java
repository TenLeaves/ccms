package wl.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.n3r.eql.Eql;
import org.n3r.eql.EqlPage;
import org.n3r.eql.map.EqlRun;
import org.springframework.stereotype.Repository;

@Repository("orderFormsDao")
public class OrderFormsDao {
    private Eql eql = new Eql();
    
	public Map selectOrderSumInfo(Map map){
		Map bookInfo = eql.selectFirst("selectOrderSumInfo")
                .params(map)
                .execute();
		 List<EqlRun> o = eql.getEqlRuns();
	        System.out.println(o.get(0).getParamBean().toString());
	        System.out.println(o.get(0).getRunSql());
        return bookInfo;
	}
	
	public Map selectOrderSumInfoNew(Map map){
		Object bookInfo = eql.selectFirst("selectOrderSumInfoNew")
                .params(map)
                .execute();
		 List<EqlRun> o = eql.getEqlRuns();
	        System.out.println(o.get(0).getParamBean().toString());
	        System.out.println(o.get(0).getRunSql());
	        Map inMap=new HashMap();
	        inMap.put("num", bookInfo);
        return inMap;
	}
	public List<Map> selectOrderInfo(Map map,EqlPage eqlPage){
		List<Map> bookInfo = eql.select("selectOrderInfo")
				.limit(eqlPage)
                .params(map)
                .execute();
		 List<EqlRun> o = eql.getEqlRuns();
//	        System.out.println(o.get(0).getParamBean().toString());
//	        System.out.println(o.get(0).getRunSql());
        return bookInfo;
	}
	public List<Map> selectOrderInfo(Map map){
		List<Map> bookInfo = new Eql().select("selectOrderInfo")
                .params(map)
                .execute();
        return bookInfo;
	}
	public int selectSendSumInfo(Map map){
		Object bookInfo = eql.selectFirst("selectSendSumInfo")
                .params(map)
                .execute();
		if(bookInfo == null){
			bookInfo="0";
		}else{
			bookInfo=""+bookInfo;
		}
		 List<EqlRun> o = eql.getEqlRuns();
//	        System.out.println(o.get(0).getParamBean().toString());
//	        System.out.println(o.get(0).getRunSql());
        return Integer.parseInt((String) bookInfo);
	}
	public List<Map> selectSendInfo(Map map,EqlPage eqlPage){
		List<Map> bookInfo = eql.select("selectSendInfo")
				.limit(eqlPage)
                .params(map)
                .execute();
		 List<EqlRun> o = eql.getEqlRuns();
//	        System.out.println(o.get(0).getParamBean().toString());
//	        System.out.println(o.get(0).getRunSql());
        return bookInfo;
	}
	public List<Map> selectSendInfo(Map map){
		List<Map> bookInfo = new Eql().select("selectSendInfo")
                .params(map)
                .execute();
        return bookInfo;
	}
	public int selectDetailOrderSumInfo(Map map){
		Object bookInfo = eql.selectFirst("selectDetailOrderSumInfo")
                .params(map)
                .execute();
		if(bookInfo == null){
			bookInfo="0";
		}else{
			bookInfo=""+bookInfo;
		}
		 List<EqlRun> o = eql.getEqlRuns();
//	        System.out.println(o.get(0).getParamBean().toString());
//	        System.out.println(o.get(0).getRunSql());
        return Integer.parseInt((String) bookInfo);
	}
	public List<Map> selectDetailOrderInfo(Map map,EqlPage eqlPage){
		List<Map> bookInfo = eql.select("selectDetailOrderInfo")
				.limit(eqlPage)
                .params(map)
                .execute();
		 List<EqlRun> o = eql.getEqlRuns();
//	        System.out.println(o.get(0).getParamBean().toString());
//	        System.out.println(o.get(0).getRunSql());
        return bookInfo;
	}
	public List<Map> selectDetailOrderInfo(Map map){
		List<Map> bookInfo = new Eql().select("selectDetailOrderInfo")
                .params(map)
                .execute();
        return bookInfo;
	}
}
