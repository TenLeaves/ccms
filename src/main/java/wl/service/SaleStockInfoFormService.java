package wl.service;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import wl.dao.SaleStockInfoDao;

@Service("saleStkFormsSrv")
public class SaleStockInfoFormService {
    @Autowired
    @Qualifier("saleStockInfoDao")
    private SaleStockInfoDao saleStockInfoDao;

    public final String BIG_CUST = "1";
    public final String WHOLE_SALER = "2";
    public final String SALER = "3";
    public final String SEND_PROD = "4";
    public final String SHOW_PROD = "5";
    public final String FAXINGJU = "6";
    public final String FAXINGZHAN = "7";
    public final String ZIBANFAXING = "123457";

    /**
     * 专业版和非专业版暂时数据不一样所以分开处理 普及版中自办发行只展示总的订阅信息，不以客户类型分开展示
     * 
     * @param paraMap
     * @return
     */
    public List<Map> qryFromInfo(Map paraMap) {
        // 专业版处理逻辑
        List resultList = Lists.newArrayList();
        if ("1".equals(paraMap.get("prodType").toString())) {
            resultList = dealWhenProfessional(paraMap);
        } else {
            resultList = dealWhenOrdinary(paraMap);
        }
        return resultList;

    }

    private List dealWhenOrdinary(Map paraMap) {
        List <Map>  orderList = saleStockInfoDao.qrySaleInfoForm(paraMap);
        List <Map > stockList = saleStockInfoDao.qryStockInfoForm(paraMap);
        
        List<Map> resultList = Lists.newArrayList();
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH)+1;
        for(int i=1;i<=currentMonth;i++){
            Map eachRowMap = Maps.newHashMap();
            eachRowMap.put("SEND_PROD", "0");
            eachRowMap.put("FAXINGJU", "0");
            eachRowMap.put("ZIBANFAXING", "0");
            //拼每一行订单
            for(Map orderMap :orderList){
                String month = orderMap.get("MONTH").toString();
                String custType = orderMap.get("custType").toString();
                Object amount = orderMap.get("PRODUCT_PER_COUNT");
                if (Integer.parseInt(month) != i) {
                    continue;
                }
                if(this.ZIBANFAXING.contains(custType)){
                    int preAmt = Integer.parseInt(eachRowMap.get(
                            "ZIBANFAXING").toString());
                    int newAmt = preAmt
                            + Integer.parseInt(amount.toString());
                    eachRowMap.put("ZIBANFAXING", newAmt);
                }else if(this.FAXINGJU.equals(custType)){
                    eachRowMap.put("FAXINGJU", amount);
                }else{
                    eachRowMap.put("SEND_PROD", amount);
                }
            }
            //拼每一个月库存
            for (Map stockMap : stockList) {
                String month = stockMap.get("PRINT_CODE").toString()
                        .substring(4, 6);// 2014071
                if (Integer.parseInt(month) == i) {
                    eachRowMap.put("STOCK", stockMap.get("stockAmt"));
                    break;
                }
            }
            if (!eachRowMap.containsKey("STOCK")) {
                eachRowMap.put("STOCK", "0");
            }
            eachRowMap.put("MONTH", i + "月");
            resultList.add(eachRowMap);
        }
        return resultList;
    }

    /**
     * 查询所有的客户类型的订阅信息
     * 
     * @param paraMap
     * @return
     */
    private List<Map> dealWhenProfessional(Map paraMap) {
        List<Map> orderList = saleStockInfoDao.qrySaleInfoForm(paraMap);
        List<Map> stockList = saleStockInfoDao.qryStockInfoForm(paraMap);

        List<Map> resultList = Lists.newArrayList();
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        for (int i = 1; i <= currentMonth; i++) {
            Map eachRowMap = Maps.newHashMap();
            eachRowMap.put("BIG_CUST", "0");
            eachRowMap.put("WHOLE_SALER", "0");
            eachRowMap.put("SALER", "0");
            eachRowMap.put("SEND_PROD", "0");
            eachRowMap.put("SHOW_PROD", "0");
            eachRowMap.put("FAXINGJU", "0");
            eachRowMap.put("FAXINGZHAN", "0");
            for (Map orderMap : orderList) {
                String month = orderMap.get("MONTH").toString();
                String custType = orderMap.get("custType").toString();
                Object amount = orderMap.get("PRODUCT_PER_COUNT");
                if (Integer.parseInt(month) != i) {
                    continue;
                }

                if (this.BIG_CUST.equals(custType)) {
                    eachRowMap.put("BIG_CUST", amount);
                } else if (this.WHOLE_SALER.equals(custType)) {
                    eachRowMap.put("WHOLE_SALER", amount);
                } else if (this.SALER.equals(custType)) {
                    eachRowMap.put("SALER", amount);
                } else if (this.SEND_PROD.equals(custType)) {// 增刊样刊合并
                    if (eachRowMap.containsKey("SEND_PROD")) {
                        int preAmt = Integer.parseInt(eachRowMap.get(
                                "SEND_PROD").toString());
                        int newAmt = preAmt
                                + Integer.parseInt(amount.toString());
                        eachRowMap.put("SEND_PROD", newAmt);
                    } else {
                        eachRowMap.put("SEND_PROD", amount);
                    }
                } else if (this.SHOW_PROD.equals(custType)) {
                    if (eachRowMap.containsKey("SEND_PROD")) {
                        int preAmt = Integer.parseInt(eachRowMap.get(
                                "SEND_PROD").toString());
                        int newAmt = preAmt
                                + Integer.parseInt(amount.toString());
                        eachRowMap.put("SEND_PROD", newAmt);
                    } else {
                        eachRowMap.put("SEND_PROD", amount);
                    }
                } else if (this.FAXINGJU.equals(custType)) {
                    eachRowMap.put("FAXINGJU", amount);
                } else {
                    eachRowMap.put("FAXINGZHAN", amount);
                }
            }
            for (Map stockMap : stockList) {
                String month = stockMap.get("PRINT_CODE").toString()
                        .substring(4, 6);// 2014071
                if (Integer.parseInt(month) == i) {
                    eachRowMap.put("STOCK", stockMap.get("stockAmt"));
                    break;
                }
            }
            if (!eachRowMap.containsKey("STOCK")) {
                eachRowMap.put("STOCK", "0");
            }
            eachRowMap.put("MONTH", i + "月");
            resultList.add(eachRowMap);
        }
        return resultList;
    }

}
