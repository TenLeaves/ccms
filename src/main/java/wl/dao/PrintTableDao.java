package wl.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.n3r.eql.Eql;
import org.n3r.eql.EqlPage;
import org.n3r.eql.map.EqlRun;
import org.springframework.stereotype.Repository;

import freemarker.template.utility.StringUtil;

@Repository("printTableDao")
public class PrintTableDao {

	public Map selectPage(Map map){
		Map bookInfo = new Eql().selectFirst("selectPage")
                .params(map)
                .execute();
        
        return bookInfo;
	}
	public List<Map> selectCustomer(){
		List<Map> bookInfo = new Eql().select("selectCustomer")
                .execute();
        
        return bookInfo;
	}
	public List<Map> selectTableInfo(Map map){
		List<Map> bookInfo = new Eql().select("selectTableInfo")
                .params(map)
                .execute();
        
        return bookInfo;
	}
	public void insertOrderAllInfo(Map map,Eql eql){
		eql.insert("insertPrintPage")
                .params(map)
                .execute();
	}
	public void insertCustPage(Map map,Eql eql){
		eql.insert("insertCustPage")
                .params(map)
                .execute();
	}
	public String seq(String map){
		Object s = new Eql().selectFirst("seq").params(map).execute();
		String num=StringUtil.leftPad(""+s, 6, '0');
        
        return num;
	}
    public List<Map> qryApplyPrintTableInfo(Map paraMap) {
        EqlPage eqlPage = (EqlPage) paraMap.get("eqlPage");
        Eql eql = new Eql();
        List<Map> resultList = eql.select("selectPrintApply").params(paraMap).limit(eqlPage).execute();
        List<EqlRun> o = eql.getEqlRuns();
        System.out.println(o.get(0).getParamBean().toString());
        System.out.println(o.get(0).getRunSql());
        
        return resultList;
    }
    public Map selectPrintByCode(String printCode) {
        
        return new Eql().selectFirst("selectPrintByPrintCode").params(printCode).execute();
    }
    public void updatePrintTbl(Map printTblMap) {
        new Eql().update("updatePrintTblByPrintCode").params(printTblMap).execute();
        
    }
}
