package wl.dao;

import java.util.List;
import java.util.Map;

import org.n3r.eql.Eql;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Maps;

import wl.entity.LogInfo;

@Repository("loginUserDao")
public class LoginUserDao {
    private Eql eql = new Eql();
    
    public Map qryUser(LogInfo logUser) throws Exception{
        Map paraMap = Maps.newHashMap();
        paraMap.put("staff_id", logUser.getStaff_id());
        List<Map<String, String>> resultList = eql.select("selectStaffById").params(paraMap).execute();
        return (resultList==null||resultList.size()==0)?null:resultList.get(0);
    }

    public List qryRoleInfo(Map paraMap) {
        return eql.select("selectRoleById").params(paraMap).execute();
    }

    public void changePassWord(Map inputMap) {
        eql.update("chagePassword").params(inputMap).execute();
        
    }
    
}
