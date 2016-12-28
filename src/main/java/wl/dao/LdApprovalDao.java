package wl.dao;

import java.util.List;
import java.util.Map;

import org.n3r.eql.Eql;
import org.n3r.eql.EqlPage;
import org.springframework.stereotype.Repository;

@Repository("ldapprovalDao")
public class LdApprovalDao {
	public List printCheckInfo(){
		List<Map> printCheckInfo = new Eql().select("printCheckInfo")
                .execute();
        
        return printCheckInfo;
	}
	public List supplementInfo(EqlPage eqlPage){
		List<Map> supplementInfo = new Eql().select("supplementInfo")
                .limit(eqlPage)
                .execute();
        
        return supplementInfo;
	}
	public void updateLdAppSuc(String str,String staffId){
		new Eql().update("updateLdAppSuc").params(staffId,str).execute();
	}
	public void updateLdAppSuc1(String str,String staffId){
		new Eql().update("updateLdAppSuc1").params(staffId,str).execute();
	}
	public void updatePageAppSuc(String key,String staffId){
		new Eql().update("updatePageAppSuc").params(staffId,key).execute();
	}
	public void updateLdAppErr(String str,String boxInfo,String staffId){
		new Eql().update("updateLdAppErr").params(boxInfo,staffId,str).execute();
	}
	public void updateLdAppErr1(String str,String boxInfo,String staffId){
		new Eql().update("updateLdAppErr1").params(boxInfo,staffId,str).execute();
	}
	public void updatePageAppErr(String key,String staffId,String boxInfo){
		new Eql().update("updatePageAppErr").params(staffId,boxInfo,key).execute();
	}
}
