package wl.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.n3r.eql.EqlPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import wl.dao.LeaderFormsDao;

@Service("leaderFormsService")
public class LeaderFormsService {
    @Autowired
    @Qualifier("leaderFormsDao")
    private LeaderFormsDao leaderFormsDao;
    public List<Map> appCheck(String year) {
		List<Map> resultMap=Lists.newArrayList();
		List<Map> listPrint=leaderFormsDao.selectPrint(year);
		List<Map> listOrder=leaderFormsDao.selectOrder(year);
		List<Map> listStock=leaderFormsDao.selectStock(year);
		List<Map> listDistribut=leaderFormsDao.selectDistribut(year);
		for(int i=1;i<=12;i++){
			int j=0;
			Map map=new HashMap();
			map.put("month",i);
			for(j=0;j<listPrint.size();j++){
				int month=Integer.parseInt(listPrint.get(j).get("MONTH").toString());
				if(month==i){
					if("1".equals(listPrint.get(j).get("PRODUCT_TYPE").toString())){
						map.put("totalCount1",listPrint.get(j).get("TOTAL_COUNT").toString());
					}else{
						map.put("totalCount2",listPrint.get(j).get("TOTAL_COUNT").toString());
					}
				}
				if(month>i){
					if(map.get("totalCount1")==null ||"".equals(map.get("totalCount1").toString())){
						map.put("totalCount1", 0);
					}
					if(map.get("totalCount2")==null ||"".equals(map.get("totalCount2").toString())){
						map.put("totalCount2", 0);
					}
					break;
				}
			}
			if(j==listPrint.size()){
				int month=0;
				if(map.get("totalCount2")==null||"".equals(map.get("totalCount2"))){
					map.put("totalCount2", 0);
				}
				if(j!=0){
					month=Integer.parseInt(listPrint.get(--j).get("MONTH").toString());
				}
				if(month<i){
					map.put("totalCount1", 0);
					map.put("totalCount2", 0);
				}
			}
			for(j=0;j<listOrder.size();j++){
				int month=Integer.parseInt(listOrder.get(j).get("MONTH").toString());
				if(month==i){
					if("1".equals(listOrder.get(j).get("PRODUCT_TYPE").toString())){
						map.put("orderCount1",listOrder.get(j).get("PRODUCT_PER_COUNT").toString());
					}else{
						map.put("orderCount2",listOrder.get(j).get("PRODUCT_PER_COUNT").toString());
					}
				}
				if(month>i){
					if(map.get("orderCount1")==null ||"".equals(map.get("orderCount1").toString())){
						map.put("orderCount1", 0);
					}
					if(map.get("orderCount2")==null ||"".equals(map.get("orderCount2").toString())){
						map.put("orderCount2", 0);
					}
					break;
				}
			}
			if(j==listOrder.size()){
				int month=0;
				if(map.get("orderCount2")==null||"".equals(map.get("orderCount2"))){
					map.put("orderCount2", 0);
				}
				if(j!=0){
					month=Integer.parseInt(listOrder.get(--j).get("MONTH").toString());
				}
				if(month<i){
					map.put("orderCount1", 0);
					map.put("orderCount2", 0);
				}
			}
			for(j=0;j<listDistribut.size();j++){
				int month=Integer.parseInt(listDistribut.get(j).get("MONTH").toString());
				if(month==i){
					if("1".equals(listDistribut.get(j).get("PRODUCT_TYPE").toString())){
						map.put("distributCount1",listDistribut.get(j).get("DISTRIBUT_COUNT").toString());
					}else{
						map.put("distributCount2",listDistribut.get(j).get("DISTRIBUT_COUNT").toString());
					}
				}
				if(month>i){
					if(map.get("distributCount1")==null ||"".equals(map.get("distributCount1").toString())){
						map.put("distributCount1", 0);
					}
					if(map.get("distributCount2")==null ||"".equals(map.get("distributCount2").toString())){
						map.put("distributCount2", 0);
					}
					break;
				}
			}
			if(j==listDistribut.size()){
				int month=0;
				if(map.get("distributCount2")==null||"".equals(map.get("distributCount2"))){
					map.put("distributCount2", 0);
				}
				if(j!=0){
					month=Integer.parseInt(listDistribut.get(--j).get("MONTH").toString());
				}
				if(month<i){
					map.put("distributCount1", 0);
					map.put("distributCount2", 0);
				}
			}
			for(j=0;j<listStock.size();j++){
				int month=Integer.parseInt(listStock.get(j).get("month").toString());
				if(month==i){
					if("1".equals(listStock.get(j).get("productType").toString())){
						map.put("remainCount1",listStock.get(j).get("REMAIN_COUNT").toString());
					}else{
						map.put("remainCount2",listStock.get(j).get("REMAIN_COUNT").toString());
					}
				}
				if(month>i){
					if(map.get("remainCount1")==null ||"".equals(map.get("remainCount1").toString())){
						map.put("remainCount1", 0);
					}
					if(map.get("remainCount2")==null ||"".equals(map.get("remainCount2").toString())){
						map.put("remainCount2", 0);
					}
					break;
				}
			}
			if(j==listStock.size()){
				int month=0;
				if(map.get("remainCount2")==null||"".equals(map.get("remainCount2"))){
					map.put("remainCount2", 0);
				}
				if(j!=0){
					month=Integer.parseInt(listStock.get(--j).get("month").toString());
				}
				if(month<i){
					map.put("remainCount1", 0);
					map.put("remainCount2", 0);
				}
			}
			resultMap.add(map);
		}
		return resultMap;
	}
} 