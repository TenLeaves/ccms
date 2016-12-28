package wl.dao;

import java.util.List;
import java.util.Map;

import org.n3r.eql.Eql;
import org.n3r.eql.EqlPage;
import org.n3r.eql.map.EqlRun;
import org.springframework.stereotype.Repository;

@Repository("leaderFormsDao")
public class LeaderFormsDao {
    private Eql eql = new Eql();
    
	public List<Map> selectPrint(String map){
		List<Map> bookInfo = eql.select("selectPrint")
                .params(map)
                .execute();
		 List<EqlRun> o = eql.getEqlRuns();
//	        System.out.println(o.get(0).getParamBean().toString());
//	        System.out.println(o.get(0).getRunSql());
        return bookInfo;
	}
	public List<Map> selectOrder(String map){
		List<Map> bookInfo = eql.select("selectOrder")
                .params(map)
                .execute();
		 List<EqlRun> o = eql.getEqlRuns();
//	        System.out.println(o.get(0).getParamBean().toString());
//	        System.out.println(o.get(0).getRunSql());
        return bookInfo;
	}
	public List<Map> selectDistribut(String map){
		List<Map> bookInfo = eql.select("selectDistribut")
                .params(map)
                .execute();
		 List<EqlRun> o = eql.getEqlRuns();
//	        System.out.println(o.get(0).getParamBean().toString());
//	        System.out.println(o.get(0).getRunSql());
        return bookInfo;
	}
	public List<Map> selectStock(String map){
		List<Map> bookInfo = eql.select("selectStock")
                .params(map)
                .execute();
        return bookInfo;
	}
}
