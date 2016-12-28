package wl.service;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.n3r.eql.Eql;
import org.n3r.eql.EqlTran;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import wl.dao.SaleOrderDao;
import wl.entity.LogInfo;

import com.google.common.collect.Maps;

@Service("saleOrderService")
public class SaleOrderService {
    @Autowired
    @Qualifier("saleOrderDao")
    private SaleOrderDao saleOrderDao;
    
    private Log log = LogFactory.getLog(this.getClass());

    public String CreateOrderSubId(String str) {
        String num = saleOrderDao.CreateOrderId(str);
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String date = sdf.format(now);
        String date_new = date.substring(2, date.length());
        return date_new + num;
    }

    public String insertStockInfo(List<Map> orderList, HttpSession session)
            throws SQLException {
        Eql eql = new Eql();
        EqlTran eqlTran = eql.newTran();
        String[] list = { "杂志社", "邮局", "印刷厂" };
        Date now = new Date();
        LogInfo logUser = (LogInfo) session.getAttribute("logUser");
        int year = now.getYear() + 1900;
        int month = now.getMonth() + 1;
        for (int i = 0; i < orderList.size(); i++) {
            List<Map> sendList = (List<Map>) orderList.get(i).get("sendList");
            int type = Integer.parseInt((String) orderList.get(i).get(
                    "productType")) + 1;
            List<String> code = new ArrayList<String>();
            if ("0".equals("" + orderList.get(i).get("monthChoice"))) {
                String[] startT = ((String) orderList.get(i).get("startTime"))
                        .split("-");
                String[] endT = ((String) orderList.get(i).get("endTime"))
                        .split("-");
                if (Integer.parseInt(startT[0]) == Integer.parseInt(endT[0])) {
                    for (int k = Integer.parseInt(startT[1]); k <= Integer
                            .parseInt(endT[1]); k++) {
                        String str = startT[0];
                        if (Integer.parseInt(str) < year
                                || (Integer.parseInt(str) == year && Integer
                                        .parseInt(k + "") <= month)) {
                            str += "" + (k < 10 ? "0" + k : k) + type;
                            code.add(str);
                        }
                    }
                } else {
                    for (int k = Integer.parseInt(startT[0]); k <= Integer
                            .parseInt(endT[0]); k++) {
                        if (k == Integer.parseInt(startT[0])) {
                            for (int l = Integer.parseInt(startT[1]); l <= 12; l++) {
                                String str = "" + k;
                                if (Integer.parseInt(str) < year
                                        || (Integer.parseInt(str) == year && Integer
                                                .parseInt(l + "") <= month)) {
                                    str += "" + (l < 10 ? "0" + l : l) + type;
                                    code.add(str);
                                }
                            }
                        } else if (k != Integer.parseInt(endT[0])) {
                            for (int l = 1; l <= 12; l++) {
                                String str = "" + k;
                                if (Integer.parseInt(str) < year
                                        || (Integer.parseInt(str) == year && Integer
                                                .parseInt(l + "") <= month)) {
                                    str += "" + (l < 10 ? "0" + l : l) + type;
                                    code.add(str);
                                }
                            }
                        } else {
                            for (int l = 1; l <= Integer.parseInt(endT[1]); l++) {
                                String str = "" + k;
                                if (Integer.parseInt(str) < year
                                        || (Integer.parseInt(str) == year && Integer
                                                .parseInt(l + "") <= month)) {
                                    str += "" + (l < 10 ? "0" + l : l) + type;
                                    code.add(str);
                                }
                            }
                        }
                    }
                }
            } else {
                String[] str = ((String) orderList.get(i).get("time2"))
                        .split(",");
                for (int k = 0; k < str.length; k++) {
                    String[] dateInfo = str[k].split("-");
                    String str1 = dateInfo[0];
                    if (Integer.parseInt(str1) < year
                            || (Integer.parseInt(str1) == year && Integer
                                    .parseInt(dateInfo[1]) <= month)) {
                        str1 += "" + dateInfo[1] + type;
                        code.add(str1);
                    }
                }
            }
            for (int j = 0; j < sendList.size(); j++) {
                int count = Integer.parseInt(""
                        + sendList.get(j).get("sendNumber"));
                Map map = new HashMap();
                map.put("stockType", Integer.parseInt((String) sendList.get(j)
                        .get("sendAdd")) + 1);
                map.put("count", count);
                map.put("staffId", logUser.getStaff_id());
                for (int k = 0; k < code.size(); k++) {
                    map.put("printCode", code.get(k));
                    int num = saleOrderDao.selectSumStock(map, eql);
                    if (count <= num) {
                        saleOrderDao.updateOccupyStock(map, eql);
                    } else {
                        eqlTran.rollback();
                        String prodDesc = "1".equals(code.get(k).substring(6, 7)) ? "专业版": "普及版";
                        return code.get(k).substring(0, 4)
                                + "年"
                                + code.get(k).substring(4, 6)
                                + "月"
                                + prodDesc
                                + list[Integer.parseInt(""
                                        + map.get("stockType")) - 1] + "缺货";
                    }
                }
            }
        }
        eqlTran.commit();
        return null;
    }

    public void InsertOrderInfo(Map userMap, List<Map> orderList,
            HttpSession session) {
        Map map = new HashMap();
        String subscribeId = CreateOrderSubId("subscribeId");
        map.put("subscribeId", subscribeId);
        map.put("customerName", userMap.get("username"));
        map.put("customerPhone", userMap.get("telephone"));
        map.put("fixedTel", userMap.get("fixedTel"));
        map.put("discount", userMap.get("discount"));
        map.put("remark", userMap.get("textInfo"));
        LogInfo logUser = (LogInfo) session.getAttribute("logUser");
        session.setAttribute("subscribeId", subscribeId);
        map.put("staffId", logUser.getStaff_id());
        map.put("needInvoice",
                Integer.parseInt((String) userMap.get("invoiceDeal")) + 1);
        map.put("beforeInvoice",
                Integer.parseInt((String) userMap.get("beforeInvoice")) + 1);
        int sum = 0;
        for (int i = 0; i < orderList.size(); i++) {
            int count = Integer.parseInt((String) orderList.get(i).get(
                    "orderCount"));
            int date = Integer.parseInt("" + orderList.get(i).get("sum"));
            sum += count * date;
        }
        map.put("sum", sum);
       // Map outMap = countNum(orderList, userMap.get("discount").toString(), 2);
        Map outMap = calculateMoney(orderList, userMap.get("discount").toString(), 2);
        map.put("totalMoney", outMap.get("totalMoney"));
        map.put("postMoney", outMap.get("postMoney"));
        
        try {
            log.info("--------------添加订单---------------");
            log.info("\n生成总订单:"+map.toString());
            
            saleOrderDao.saveSubscribeInfo(map);
            for (int i = 0; i < orderList.size(); i++) {
                Map inMap = new HashMap();
                inMap.put("subscribeId", map.get("subscribeId"));
                inMap.put("orderId", orderList.get(i).get("OrderSeq"));
                inMap.put(
                        "subscribeType",
                        Integer.parseInt((String) orderList.get(i).get(
                                "orderWay")) + 1);
                inMap.put(
                        "productType",
                        Integer.parseInt((String) orderList.get(i).get(
                                "productType")) + 1);
                inMap.put("productPerCount", orderList.get(i).get("orderCount"));
                inMap.put(
                        "continMonth",
                        Integer.parseInt(""
                                + orderList.get(i).get("monthChoice")) + 1);
                if ("0".equals("" + orderList.get(i).get("monthChoice"))) {
                    inMap.put("productStart", orderList.get(i).get("startTime"));
                    inMap.put("productEnd", orderList.get(i).get("endTime"));
                } else {
                    inMap.put("productStart", orderList.get(i).get("time2"));
                }
                inMap.put("staffId", logUser.getStaff_id());
                log.info("\n生成子订单:"+inMap.toString());
                saleOrderDao.SaveOrderInfo(inMap);
                
                List<Map> sendList = (List<Map>) orderList.get(i).get(
                        "sendList");
                log.info("\n生成配送单:"+sendList.toString());
                for (int j = 0; j < sendList.size(); j++) {
                    HashMap resultMap = new HashMap();
                    resultMap.put("orderId", inMap.get("orderId"));
                    resultMap
                            .put("distributId", sendList.get(j).get("sendSeq"));
                    resultMap.put(
                            "address",
                            Integer.parseInt((String) sendList.get(j).get(
                                    "sendAdd")) + 1);
                    resultMap.put("count", sendList.get(j).get("sendNumber"));
                    resultMap.put(
                            "distributType",
                            Integer.parseInt((String) sendList.get(j).get(
                                    "getProWay")) + 1);
                    resultMap.put("distributAddress",
                            sendList.get(j).get("sendAddress"));
                    resultMap.put("addressName",
                            sendList.get(j).get("getCustomer"));
                    resultMap
                            .put("addressPhone", sendList.get(j).get("getTel"));
                    resultMap.put("zipCode", sendList.get(j).get("postcode"));
                    resultMap
                            .put("remark", sendList.get(j).get("textInfoSend"));
                    resultMap.put("labelContent",
                            sendList.get(j).get("labelContent"));
                    resultMap.put(
                            "labelFlag",
                            Integer.parseInt((String) sendList.get(j).get(
                                    "labelInfo")) + 1);
                    resultMap.put(
                            "productType",
                            Integer.parseInt((String) orderList.get(i).get(
                                    "productType")) + 1);
                    resultMap.put("staffId", logUser.getStaff_id());
                    if ("0".equals("" + orderList.get(i).get("monthChoice"))) {
                        String[] startT = ((String) orderList.get(i).get(
                                "startTime")).split("-");
                        String[] endT = ((String) orderList.get(i).get(
                                "endTime")).split("-");
                        if (Integer.parseInt(startT[0]) == Integer
                                .parseInt(endT[0])) {
                            resultMap.put("year", startT[0]);
                            for (int k = Integer.parseInt(startT[1]); k <= Integer
                                    .parseInt(endT[1]); k++) {
                                resultMap.put("month", k < 10 ? "0" + k : k);
                                saleOrderDao.saveSendInfo(resultMap);
                            }
                        } else {
                            for (int k = Integer.parseInt(startT[0]); k <= Integer
                                    .parseInt(endT[0]); k++) {
                                resultMap.put("year", k);
                                if (k == Integer.parseInt(startT[0])) {
                                    for (int l = Integer.parseInt(startT[1]); l <= 12; l++) {
                                        resultMap.put("month", l < 10 ? "0" + l
                                                : l);
                                        saleOrderDao.saveSendInfo(resultMap);
                                    }
                                } else if (k != Integer.parseInt(endT[0])) {
                                    for (int l = 1; l <= 12; l++) {
                                        resultMap.put("month", l < 10 ? "0" + l
                                                : l);
                                        saleOrderDao.saveSendInfo(resultMap);
                                    }
                                } else {
                                    for (int l = 1; l <= Integer
                                            .parseInt(endT[1]); l++) {
                                        resultMap.put("month", l < 10 ? "0" + l
                                                : l);
                                        saleOrderDao.saveSendInfo(resultMap);
                                    }
                                }
                            }
                        }
                    } else {
                        String[] str = ((String) orderList.get(i).get("time2"))
                                .split(",");
                        for (int k = 0; k < str.length; k++) {
                            String[] dateInfo = str[k].split("-");
                            resultMap.put("month", dateInfo[1]);
                            resultMap.put("year", dateInfo[0]);
                            saleOrderDao.saveSendInfo(resultMap);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("-------------------订单填写完毕--------------------");
    }

    // 计算总价钱
    public Map calculateMoney(List<Map> orderList, String discount, int custType) {
        Map returnMap = Maps.newHashMap();
        double totalMoney = 0.0;
        double totalSendMoneyAmt=0.0;
        double totalPostFee = 0.0;
        double discountRate = 0.0;
        int totalSendAmt = 0;
        int totalOrderAmt=0;//订单总共多少本
        if (!"".equals(discount)) {// 把折扣率转化成百分制
            discountRate = Double.parseDouble(discount) < 10 ? Double
                    .parseDouble(discount) * 10 : Double.parseDouble(discount);
        }
        // 计算总共订了多少本
        for (int i = 0; i < orderList.size(); i++) {
            double eachOrderMoney = 0.0;
            double eachOrderSendFee = 0.0;
            // 每月订阅的数量
            int count = Integer.parseInt((String) orderList.get(i).get(
                    "orderCount"));
            // 订阅的月数
            int date = Integer.parseInt("" + orderList.get(i).get("sum"));
            totalOrderAmt += count*date;
            // 不连续月存在合刊，判断是否为不连续月
            if ("1".equals("" + orderList.get(i).get("monthChoice"))) {
                String[] str = ((String) orderList.get(i).get("time2"))
                        .split(",");
                for (int k = 0; k < str.length; k++) {
                    String[] dateInfo = str[k].split("-");
                    // 如果是合刊，就把订阅月数加11
                    if ("13".equals(dateInfo[1])) {
                        date += 11;
                    }
                }
            }
            // 产品相应的钱数
            int productType = Integer.parseInt((String) orderList.get(i).get(
                    "productType")) + 1;
            int bookPrice = (productType == 1) ? 50 : 38;
            eachOrderMoney = bookPrice * date * count;
            totalMoney += eachOrderMoney;

            // 计算邮费
            List<Map> sendList = (List<Map>) orderList.get(i).get("sendList");
            int eachOrderSendAmt = 0;//每个订单配送的数量
            double eachOrderSendTotalMoney=0.0; //每个订单配送的总价钱
            for (int j = 0; j < sendList.size(); j++) {
                // 月配送数量
                int sendNumber = Integer.parseInt(sendList.get(j).get(
                        "sendNumber")
                        + "");
                // 配送方式
                int distributType = Integer.parseInt((String) sendList.get(j)
                        .get("getProWay")) + 1;
                // 需要邮递的数量（除去自提）
                if (distributType != 1) {
                    eachOrderSendAmt += sendNumber;
                }
            }
            
            totalSendAmt += eachOrderSendAmt * date;//需要配送的总数量
            eachOrderSendTotalMoney=eachOrderSendAmt * date*bookPrice;//每个订单配送的价钱
            totalSendMoneyAmt += eachOrderSendTotalMoney;//需要配送的总价钱
        }

        if (custType == 2) {
            if (totalSendAmt >= 12) {
                totalPostFee = 0;
            } else if (totalSendAmt >= 2) {
                totalPostFee = totalSendMoneyAmt * 0.15;
            } else if (totalSendAmt > 0) {
                totalPostFee = 8;
            } else {
                totalPostFee = 0;
            }
        }
        DecimalFormat df = new DecimalFormat("#.00");
        totalPostFee=Double.parseDouble(df.format(totalPostFee));
        // 邮费计算后才给订单打折
        if (discountRate > 0) {
            totalMoney = totalMoney * discountRate / 100;
        }
        totalMoney=Math.round(totalMoney+totalPostFee);
        returnMap.put("totalMoney", totalMoney);
        returnMap.put("postMoney", totalPostFee);
        return returnMap;
    }

    // 计算总钱数
    public Map countNum(List<Map> orderList, String discount, int custType) {
        int sum = 0;
        // 总钱数
        double total = 0;
        Map map = new HashMap();
        for (int i = 0; i < orderList.size(); i++) {
            sum = 0;
            // 每月订阅的数量
            int count = Integer.parseInt((String) orderList.get(i).get(
                    "orderCount"));
            // 订阅的月数
            int date = Integer.parseInt("" + orderList.get(i).get("sum"));
            // 不连续月存在合刊，判断是否为不连续月
            if ("1".equals("" + orderList.get(i).get("monthChoice"))) {
                String[] str = ((String) orderList.get(i).get("time2"))
                        .split(",");
                for (int k = 0; k < str.length; k++) {
                    String[] dateInfo = str[k].split("-");
                    // 如果是合刊，就把订阅月数加11
                    if ("13".equals(dateInfo[1])) {
                        date += 11;
                    }
                }
            }
            List<Map> sendList = (List<Map>) orderList.get(i).get("sendList");

            // 订购的产品类型
            int productType = Integer.parseInt((String) orderList.get(i).get(
                    "productType")) + 1;
            for (int j = 0; j < sendList.size(); j++) {
                // 月配送数量
                int sendNumber = Integer.parseInt(sendList.get(j).get(
                        "sendNumber")
                        + "");
                // 配送方式
                int distributType = Integer.parseInt((String) sendList.get(j)
                        .get("getProWay")) + 1;
                // 需要邮递的数量（除去自提）
                if (distributType != 1) {
                    sum += sendNumber;
                }
            }
            // 产品相应的钱数
            int money = (productType == 1) ? 50 : 38;
            // 不需要邮费的钱数
            total += (count - sum) * date * money;
            if (!"".equals(discount)) {
                int discountInfo = Integer.parseInt(discount) < 10 ? Integer
                        .parseInt(discount) * 10 : Integer.parseInt(discount);
                total = total * discountInfo / 100;
            }
            double postNum = 0;
            // 需要邮递的总钱数
            postNum = sum * money * date;
            if (!"".equals(discount)) {
                int discountInfo = Integer.parseInt(discount) < 10 ? Integer
                        .parseInt(discount) * 10 : Integer.parseInt(discount);
                postNum = postNum * discountInfo / 100;
            }
            if (custType == 2) {
                // 计算邮费
                if (sum * date >= 12) {
                    postNum += 0;
                    map.put("postMoney", 0);
                } else if (sum * date >= 2) {
                    map.put("postMoney", postNum * 0.15);
                    postNum += postNum * 0.15;
                } else if (sum * date == 1) {
                    postNum += 8;
                    map.put("postMoney", 8);
                } else {
                    postNum += 0;
                    map.put("postMoney", 0);
                }
            } else {
                map.put("postMoney", 0);
            }
            total += postNum;
        }
        map.put("totalMoney", total);
        return map;
    }
}