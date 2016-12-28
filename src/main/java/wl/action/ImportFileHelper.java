package wl.action;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.taobao.gecko.core.util.StringUtils;

public class ImportFileHelper {

    // 信息列表
    private static List<Map> infos = null;

    /**
     * 解析文件
     * 
     * @return
     * @throws Exception
     */
    public static Map resolveFile(Map praMap, CommonsMultipartFile file)
            throws Exception {
        if (file == null) {
            throw new Exception("请选择要导入的文件！");
        }
        // 校验文件类型为Excel类型
        if (file.getOriginalFilename().endsWith(".xls")
                || file.getOriginalFilename().endsWith(".xlsx")) {
            // 解析文件
            parseExcelFile(praMap, file);
        } else {
            throw new Exception("请选择正确的文件类型(*.xls,*.xlsx)");
        }

        // 录入数据库
        Map in = new HashMap();
        in.put("infos", infos);
        return in;
    }

    /**
     * @function: 导入 *.xls,*.xlsx 文件
     */
    public static void parseExcelFile(Map praMap, CommonsMultipartFile file)
            throws Exception {
        Workbook book = null;
        Sheet sheet = null;
        Row row = null;

        // 获得第一个sheet页
        try {
            // 被注调的地方这么写有风险
            // book = file.getName().endsWith(".xls") ? new
            // HSSFWorkbook(file.getInputStream())
            // : new XSSFWorkbook(file.getInputStream());
            book = WorkbookFactory.create(file.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("文件解析错误");
        }

        sheet = book.getSheetAt(0);

        // sheet页内容为空
        if (sheet == null || sheet.getPhysicalNumberOfRows() == 0) {
            throw new Exception("文件没有记录行.");
        }

        int totleNumber = sheet.getPhysicalNumberOfRows();

        infos = new ArrayList();

        // 处理记录
        String[] title = (String[]) praMap.get("title");
        for (int i = 1; i < totleNumber; i++) {
            row = sheet.getRow(i);
            if (row != null) {
                Map info = new HashMap();

                info.put("line", i + 1); // 行号
                for (int j = 0; j < title.length; j++) {
                    String cell = parseCellString(row.getCell(j));
                    if (StringUtils.isEmpty(cell) && j == 0) {
                        return;
                    }
                    info.put(title[j], cell);
                }

                infos.add(info);
            }
        }
    }

    /**
     * 解析一个单元格为字符串
     * 
     * @param cell
     * @return
     */
    public static String parseCellString(Cell cell) {
        if (cell == null) {
            return null;
        }
        if (cell.getCellType() == 0) {
            DecimalFormat df = new DecimalFormat("#");
            double cellData = cell.getNumericCellValue();
            String cellString = df.format(cellData);
            return cellString;
        } else if (cell.getCellType() == 1 || cell.getCellType() == 2
                || cell.getCellType() == 3) {
            return cell.getStringCellValue();
        } else {
            return null;
        }
    }

    public List<Map> getInfos() {
        return infos;
    }

    public void setInfos(List<Map> infos) {
        this.infos = infos;
    }

}
