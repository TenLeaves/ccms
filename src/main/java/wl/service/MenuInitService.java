package wl.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import wl.dao.MenuDao;
import wl.entity.LogInfo;

@Service("menuInitService")
public class MenuInitService {
    @Autowired
    @Qualifier("menuRelatedDao")
    private MenuDao menuDao;

    public Map initMenu(LogInfo logUsr) throws Exception {
        
        Map returnMenuMap = Maps.newHashMap();
        
        List<Map> resultList1 = menuDao.qryMenuInfo(logUsr,"1");
        List<Map> resultList2 = menuDao.qryMenuInfo(logUsr,"2");
        List<Map> resultList3 = menuDao.qryMenuInfo(logUsr,"3");
        List<Map> resultList4 = menuDao.qryMenuInfo(logUsr,"4");
        List<Map> resultList5 = menuDao.qryMenuInfo(logUsr,"5");
        
        List<Map> menuList1 = Lists.newArrayList(),
                menuList2 = Lists.newArrayList(),
                menuList3 = Lists.newArrayList(),
                menuList4 = Lists.newArrayList(),
                menuList5 = Lists.newArrayList();
        
        getMenuInfo(resultList1, menuList1);
        returnMenuMap.put("menuList1", menuList1);
        
        getMenuInfo(resultList2, menuList2);
        returnMenuMap.put("menuList2", menuList2);
        
        getMenuInfo(resultList3, menuList3);
        returnMenuMap.put("menuList3", menuList3);
        
        getMenuInfo(resultList4, menuList4);
        returnMenuMap.put("menuList4", menuList4);
        
        getMenuInfo(resultList5, menuList5);
        returnMenuMap.put("menuList5", menuList5);
        
        return returnMenuMap;
    }

    private List<Map> getMenuInfo(List<Map> resultList1, List<Map> menuList1) {
        for(Map tempMenu:resultList1){
            Map tempMap= Maps.newHashMap();
            tempMap.put("name", tempMenu.get("MENU_NAME"));
            tempMap.put("location", tempMenu.get("MENU_URL"));
            menuList1.add(tempMap);
        }
        return menuList1;
    }

}
