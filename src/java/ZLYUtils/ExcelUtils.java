package ZLYUtils;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.HashMap;
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
    public static boolean createExcelFile(File excelPath, String sheetName, Map<Integer, Map<String, String>> dataMap) {
        boolean isCreateSuccess = false;
        Workbook workbook = null;
        try {
            // XSSFWork used for .xslx (>= 2007), HSSWorkbook for 03 .xsl
            workbook = new XSSFWorkbook();//HSSFWorkbook();//WorkbookFactory.create(inputStream);
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
            for (int i = 0; i < dataMap.size(); i++) {
                values = dataMap.get(i);
                for (Map.Entry<String, String> m : values.entrySet()) {
                    if (!titleMap.containsKey(m.getKey())) titleMap.put(m.getKey(), "");
                }
            }
            //记录map中的key，用于在插入数据中，将指定key中的数据插入到相同的表格中
            String[] key = new String[titleMap.size()];
            //写入标题
            for (Map.Entry<String, String> m : titleMap.entrySet()) {
                Cell cell_1 = row0.createCell(num, Cell.CELL_TYPE_STRING);
                CellStyle style = getStyle(workbook);
                cell_1.setCellStyle(style);
                cell_1.setCellValue(m.getKey());
                sheet.autoSizeColumn(num);
                key[num] = m.getKey();
                num++;
            }
            for (int index = 0; index < dataMap.size(); index++) {
                values = dataMap.get(index);//获取每一个data中的map
                //插入数据
                Row row = sheet.createRow(index + 1);
                for (int i = 0; i < key.length; i++) {
                    Cell cell = row.createCell(i, Cell.CELL_TYPE_STRING);
                    //单独对松鼠接口测试工具中的自动化用例结果进行颜色设置
                    if (values.get(key[i]) != null &&
                            (values.get(key[i]).startsWith("false:") || "true".equals(values.get(key[i])))) {
                        CellStyle style = workbook.createCellStyle();
                        // 设置单元格字体
                        Font headerFont = workbook.createFont(); // 字体
                        headerFont.setFontHeightInPoints((short) 14);
                        if ("true".equals(values.get(key[i]))) {
                            headerFont.setColor(HSSFColor.GREEN.index);
                        } else {
                            headerFont.setColor(HSSFColor.RED.index);
                        }
                        headerFont.setFontName("宋体");
                        style.setFont(headerFont);
                        style.setWrapText(false);
                        cell.setCellStyle(style);
                    }
                    if (values.get(key[i]) == null) {
                        cell.setCellValue("");
                    } else {
                        cell.setCellValue(values.get(key[i]));
                    }

                }

            }

            try {
                FileOutputStream outputStream = new FileOutputStream(excelPath);
                workbook.write(outputStream);
                outputStream.flush();
                outputStream.close();
                isCreateSuccess = true;
            } catch (Exception e) {
                System.out.println("It cause Error on WRITTING excel workbook: ");
                e.printStackTrace();
                TooltipUtil.errTooltip(e.toString());
                return isCreateSuccess;
            }
        }
        TooltipUtil.generalTooltip("保存成功:" + excelPath.getAbsolutePath());
        return isCreateSuccess;
    }

    private static CellStyle getStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        // 设置单元格字体
        Font headerFont = workbook.createFont(); // 字体
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
        return style;

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
                    valuesMap.put(getSpecifyRowsAndColumns(0, columnIndex),
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
