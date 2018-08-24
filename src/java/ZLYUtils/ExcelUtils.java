package ZLYUtils;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 获取或创建Excle
 */
public class ExcelUtils {
    private static InputStream is;
    private static XSSFWorkbook xssfWorkbook;
    private static XSSFSheet xssfSheet;
    private static XSSFRow xssfRow;
    private static XSSFCell xssfCell;
    private static CellStyle style;
    private static Font headerFont;

    public static void main(String[] args) throws Exception {
        Map<Integer, Map<String, String>> dataMap = new LinkedHashMap<>();
        Map<String, String> m = new LinkedHashMap<>();
        m.put("测试1", "zhi1");
        m.put("测试2", "zhi2");
        m.put("测试3", "zhi3");
        m.put("测试4", "zhi4");
        m.put("测试5", "zhi5");
        m.put("测试6", "zhi6");
        dataMap.put(0, m);
        dataMap.put(1, m);
        if (createExcelFile(new File("x.xlsx"), "测试", dataMap

        )) {
            System.out.println("data.xlsx is created successfully.");
        }
        Map<Integer, Map<String, String>> S;
        S = getExcelXlsx(new File("x.xlsx"));
        Map<String, String> map;
        for (int i = 0; i < S.size(); i++) {
            map = S.get(i);
            for (Map.Entry<String, String> values : map.entrySet()) {
                System.out.println("Key:" + values.getKey() + " 值:" + values.getValue());
            }
        }


    }

    /**
     * 创建并写入xlsx文件
     *
     * @param excelPath 文件路径
     * @return
     */
    public synchronized static boolean createExcelFile(File excelPath, String sheetName, Map<Integer, Map<String, String>> dataMap) {
        boolean isCreateSuccess = false;
        Workbook workbook = null;
        try {
            // XSSFWork used for .xslx (>= 2007), HSSWorkbook for 03 .xsl
            workbook = new XSSFWorkbook();//HSSFWorkbook();//WorkbookFactory.create(inputStream);
            setStyle_Font(workbook);
        } catch (Exception e) {
            System.out.println("It cause Error on CREATING excel workbook: ");
            e.printStackTrace();
        }
        if (workbook != null) {
            Sheet sheet = workbook.createSheet(sheetName);
            Row row0 = sheet.createRow(0);
            Map<String, String> values = null;//保存dataMap中的map
            int num = 0;
            Map<String, String> titleMap = new LinkedHashMap<>();
            //读取出所有map的title
            Map.Entry<Integer, Map<String, String>> title;
            for (Iterator<Map.Entry<Integer, Map<String, String>>> it = dataMap.entrySet().iterator(); ((Iterator) it).hasNext(); ) {
                title = it.next();
                values = title.getValue();
                for (Map.Entry<String, String> m : values.entrySet()) {
                    if (!titleMap.containsKey(m.getKey())) titleMap.put(m.getKey(), "");
                }
            }
            //记录map中的key，用于在插入数据中，将指定key中的数据插入到相同的表格中
            String[] key = new String[titleMap.size()];
            Cell cell;
            //写入标题
            for (Map.Entry<String, String> m : titleMap.entrySet()) {
                cell = row0.createCell(num, Cell.CELL_TYPE_STRING);
                cell.setCellStyle(style);
                cell.setCellValue(m.getKey());
                sheet.autoSizeColumn(num);
                key[num] = m.getKey();
                num++;
            }
            int index = 1;
            Row row;
            //文本中如果存在false开头，使用false样式风格
            CellStyle falsestyle = workbook.createCellStyle();
            falsestyle.setFont(headerFont);
            falsestyle.setWrapText(false);
            //文本中如果存在true开头，true样式风格
            CellStyle trueStyle = workbook.createCellStyle();
            Font trueFont = workbook.createFont();
            trueStyle.setWrapText(false);
            trueFont.setColor(HSSFColor.GREEN.index);//设置为绿色
            trueStyle.setFont(trueFont);
            for (Iterator<Map.Entry<Integer, Map<String, String>>> it = dataMap.entrySet().iterator(); ((Iterator) it).hasNext(); ) {
                title = it.next();//获取每一个data中的map
                values = title.getValue();
                //插入数据
                row = sheet.createRow(index);
                for (int i = 0; i < key.length; i++) {
                    cell = row.createCell(i, Cell.CELL_TYPE_STRING);
                    //单独对松鼠接口测试工具中的自动化用例结果进行颜色设置
                    if (values.get(key[i]) != null &&
                            (values.get(key[i]).startsWith("false"))) {
                        cell.setCellStyle(falsestyle);
                    }
                    if (values.get(key[i]) != null &&
                            (values.get(key[i]).startsWith("true"))) {
                        cell.setCellStyle(trueStyle);
                    }
                    if (values.get(key[i]) == null) {
                        cell.setCellValue("");
                    } else {
                        cell.setCellValue(values.get(key[i]));
                    }

                }
                index++;
            }

            try {
                FileOutputStream outputStream = new FileOutputStream(excelPath);
                workbook.write(outputStream);
                outputStream.flush();
                outputStream.close();
                isCreateSuccess = true;
                TooltipUtil.generalTooltip("保存成功:" + excelPath.getAbsolutePath());
                workbook.close();
            } catch (Exception e) {
                System.out.println("It cause Error on WRITTING excel workbook: ");
                e.printStackTrace();
                TooltipUtil.errTooltip(e.toString());
                return isCreateSuccess;
            }
        }

        return isCreateSuccess;
    }

    private static void setStyle_Font(Workbook workbook) {
        style = workbook.createCellStyle();
        // 设置单元格字体
        headerFont = workbook.createFont(); // 字体
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(HSSFColor.RED.index);
        headerFont.setFontName("宋体");
        style.setFont(headerFont);
        style.setWrapText(true);
        // 设置单元格边框及颜色
        style.setBorderBottom(BorderStyle.valueOf((short) 1));
        style.setBorderLeft(BorderStyle.valueOf((short) 1));
        style.setBorderRight(BorderStyle.valueOf((short) 1));
        style.setBorderTop(BorderStyle.valueOf((short) 1));
        style.setWrapText(true);
    }


    /**
     * 获取xlsx文件
     *
     * @param file
     */
    public synchronized static Map<Integer, Map<String, String>> getExcelXlsx(File file) throws FileNotFoundException {
        if (!file.exists()) throw new FileNotFoundException("文件不存在:" + file.getPath());
        Map<Integer, Map<String, String>> rowMap = new LinkedHashMap<>();//所有行的总值
        Map<String, String> valuesMap;//每一行的总值
        try {
            is = new FileInputStream(file);
            xssfWorkbook = new XSSFWorkbook(is);
            // 获取每一个工作薄
            // for (int numSheet = 0; numSheet <
            // xssfWorkbook.getNumberOfSheets(); numSheet++) {
            // XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
            // if (xssfSheet == null) {
            // continue;
            // }
            //指定第一个工作表
            xssfSheet = xssfWorkbook.getSheetAt(0);
            int Column = xssfSheet.getRow(0).getPhysicalNumberOfCells();// 获取所有的列
            int row = xssfSheet.getLastRowNum(); // 获取所有的行
            for (int rowIndex = 1; rowIndex <= row; rowIndex++) {
                valuesMap = new LinkedHashMap<>();
                for (int columnIndex = 0; columnIndex < Column; columnIndex++) {
                    valuesMap.put(getSpecifyRowsAndColumns(0, columnIndex).toLowerCase(),
                            getSpecifyRowsAndColumns(rowIndex, columnIndex));
                }
                rowMap.put(rowIndex - 1, valuesMap);

            }
        } catch (Exception e) {
            e.printStackTrace();
            TooltipUtil.errTooltip(e.toString());
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return rowMap;
    }

    /**
     * 获取指定行和列的内容
     *
     * @param row
     * @param colum
     * @return
     */
    private static String getSpecifyRowsAndColumns(int row, int colum) {
        try {
            xssfRow = xssfSheet.getRow(row);
            xssfCell = xssfRow.getCell(colum);
            xssfCell.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);// 将小数转换成整数
            return xssfCell.toString() != null ? xssfCell.toString() : "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }


}
