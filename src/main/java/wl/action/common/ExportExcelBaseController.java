package wl.action.common;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

/**
 * 该类主要是用来导出Excel文件
 * 集成该类实现 createExcel 即可
 * @author 
 *
 */
public abstract class ExportExcelBaseController {

    public void exoprt(HttpServletRequest request, HttpServletResponse response,String fileName) {
        HttpSession session = request.getSession();
        session.setAttribute("state", null);
        // 生成提示信息，
        response.setContentType("application/vnd.ms-excel");
        String codedFileName = fileName;
        OutputStream fOut = null;
        
        try {
            // 进行转码，使其支持中文文件名
            codedFileName= new String(codedFileName.getBytes("utf-8"),"ISO8859-1");
            response.setHeader("content-disposition", "attachment;filename="
                    + codedFileName + ".xls");
            Map inMap = new HashMap();
            inMap.put("req", request);
            Map dataMap = createData(inMap);
            
            String[] title= (String[]) dataMap.get("title");
            List<List> dataList = (List) dataMap.get("data");
            
            // 产生工作簿对象
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("sheet1");
            //添加表头
            HSSFRow titleRow = sheet.createRow(0);
          
            // 创建第一行样式
            HSSFCellStyle style = workbook.createCellStyle();
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
            style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            
            
            HSSFFont font = workbook.createFont();
            font.setFontName("微软雅黑");
            font.setBoldweight((short) 100);
            font.setFontHeight((short) 200);
            font.setColor(HSSFColor.BLACK.index);
            
            style.setFont(font);
            
            for(int i=0;i<title.length;i++){
                sheet.setColumnWidth(i, 8000);
                HSSFCell cell = titleRow.createCell(i);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(title[i]);
                cell.setCellStyle(style);
                
            }
            //添加数据
            for(int i=1;i<=dataList.size();i++){//创建
                HSSFRow row = sheet.createRow(i);
                List rowList = dataList.get(i-1);
                for(int j=0;j<title.length;j++){//创建列
                    
                    HSSFCell cell = row.createCell(j);
                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                    cell.setCellValue(rowList.get(j).toString());
                }
            }
            
            fOut = response.getOutputStream();
            workbook.write(fOut);
            
        }  catch (Exception e) {
            e.printStackTrace();
        } finally{
            try {
                fOut.flush();
                fOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    /**
     * 该函数由调用方实现 
     * 需要返回的数据map  格式[title=['姓名','送货地址'..]],data={ {name,address..},{第二行数据}...}]
     * 格式说明  title：是一个数组，是生成excel表格的第一行每列的说明
     *        data:是需要生成的数据的一个list集合
     *        
     *注：key 在此处已经指定为 title data
     * @return
     */
    public abstract Map createData(Map inMap);
    
   
}
