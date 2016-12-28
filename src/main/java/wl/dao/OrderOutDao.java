package wl.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.n3r.eql.Eql;
import org.n3r.eql.EqlPage;
import org.n3r.eql.map.EqlRun;
import org.springframework.stereotype.Repository;

@Repository("orderOutDao")
public class OrderOutDao {
    private Eql eql = new Eql();
    
	public List<Map> selectOrderAllInfo(Map map,EqlPage eqlPage){
		List<Map> bookInfo = eql.select("selectAllInfo")
                .limit(eqlPage)
                .params(map)
                .execute();
		 List<EqlRun> o = eql.getEqlRuns();
//	        System.out.println(o.get(0).getParamBean().toString());
//	        System.out.println(o.get(0).getRunSql());
        return bookInfo;
	}
	public List<Map> exportOrderAllInfo(Map map){
	    Eql eql1 = new Eql();
		List<Map> bookInfo = eql1.select("selectAllInfoNew")
                .params(map)
                .execute();
		List<EqlRun> o = eql1.getEqlRuns();
//        System.out.println(o.get(0).getParamBean().toString());
//        System.out.println(o.get(0).getRunSql());
    return bookInfo;
	}
	public List<Map> selStatusDetail(Map map){
		List<Map> bookInfo = new Eql().select("selStatusDetail")
                .params(map)
                .execute();
        
        return bookInfo;
	}
	public void updateExport(Map map){
		eql.update("updateExport").params(map).execute();
	}
	public int selectFinish(String str){
		String index = ""+eql.selectFirst("selectFinish")
                .params(str)
                .execute();
        index= "null".equals(index)?"0":index;
        return Integer.parseInt(index);
	}
	public Map selectSub(String str){
		Map index = new Eql().selectFirst("selectSub")
                .params(str)
                .execute();
        
        return index;
	}
	public void updateSub(String map,String staffId){
		new Eql().update("updateSub").params(staffId,map).execute();
	}
	public void updateStock(Map map){
		new Eql().update("updateStock").params(map).execute();
	}
}
