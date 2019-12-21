package com.joysupply.util;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.xerces.xs.datatypes.ObjectList;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class AllTimetable {
    //总课表导出
    public static HSSFWorkbook table(List<Map<String, Object>> headList, List<Map<String, Object>> bodyList) {
        List<String> headTable1 = new ArrayList<String>(Arrays.asList("所属位置", "教室号", "人数"));
        List<String> headTable2 = new ArrayList<String>(Arrays.asList("", "", ""));
        List<String> pamLis = new ArrayList<String>(Arrays.asList("0,1,0,0", "0,1,1,1", "0,1,2,2"));
        List<Integer> pam = new ArrayList<Integer>();
        for (int i = 0; i < headList.size(); i++) {
            headTable1.add(headList.get(i).get("step").toString());
            headTable2.add(headList.get(i).get("time") + "");
            if (i == 0) {
                pam.add(Integer.parseInt(headList.get(i).get("total") + ""));
            } else if (!headList.get(i - 1).get("step").toString().equals(headList.get(i).get("step").toString())) {
                pam.add(Integer.parseInt(headList.get(i).get("total") + ""));
            }
        }
        pam.forEach(e -> {
            String a = pamLis.get(pamLis.size() - 1);
            Integer num = Integer.parseInt(a.split(",")[a.split(",").length - 1]);
            pamLis.add("0,0," + (num + 1) + "," + (num + e));
        });
        String[] excelHeader02 = headTable1.toArray(new String[headTable1.size()]);
        String[] excelHeader12 = headTable2.toArray(new String[headTable2.size()]);
        //  “0,2,0,0”  ===>  “起始行，截止行，起始列，截止列”
        String[] headnum02 = pamLis.toArray(new String[pamLis.size()]);

        // 声明一个工作簿
        HSSFWorkbook wb = new HSSFWorkbook();
        for (int x = 0; x < bodyList.size(); x++) {
            // 生成一个表格
            HSSFSheet sheet = wb.createSheet(bodyList.get(x).get("weekDay") + "");
            // 第一行表头courseSearchView
            HSSFRow row = sheet.createRow(0);

            //添加样式
            CellStyle style = wb.createCellStyle();
            style.setAlignment(HorizontalAlignment.CENTER); // 水平居中格式
            style.setVerticalAlignment(VerticalAlignment.CENTER); //垂直居中

            for (int i = 0; i < excelHeader02.length; i++) {

                sheet.autoSizeColumn(i, true);// 根据字段长度自动调整列的宽度
                HSSFCell cell = row.createCell(i);
                cell.setCellValue(excelHeader02[i]);
                cell.setCellStyle(style);

                if (i >= 0 && i <= 18) {
                    for (int j = 0; j < excelHeader02.length; j++) {
                        // 从第j列开始填充
                        cell = row.createCell(j);
                        // 填充excelHeader1[j]第j个元素
                        cell.setCellValue(excelHeader02[j]);
                        cell.setCellStyle(style);
                    }
                }
            }
            // 动态合并单元格
            for (int i = 0; i < headnum02.length; i++) {
                sheet.autoSizeColumn(i, true);
                String[] temp = headnum02[i].split(",");
                Integer startrow = Integer.parseInt(temp[0]);
                Integer overrow = Integer.parseInt(temp[1]);
                Integer startcol = Integer.parseInt(temp[2]);
                Integer overcol = Integer.parseInt(temp[3]);
                sheet.addMergedRegion(new CellRangeAddress(startrow, overrow, startcol, overcol));
            }
            // 第二行表头
            row = sheet.createRow(1);
            for (int i = 0; i < excelHeader12.length; i++) {

                sheet.autoSizeColumn(i, true);// 自动调整宽度
                HSSFCell cell = row.createCell(i);
                cell.setCellValue(excelHeader12[i]);
                cell.setCellStyle(style);

                for (int j = 0; j < excelHeader12.length; j++) {
                    // 从第j+1列开始填充
                    cell = row.createCell(j);
                    // 填充excelHeader1[j]第j个元素
                    cell.setCellValue(excelHeader12[j]);
                    cell.setCellStyle(style);
                }
            }
            HSSFCellStyle cellStyle=wb.createCellStyle();
            cellStyle.setWrapText(true);
            cellStyle.setAlignment(HorizontalAlignment.CENTER );//水平居中 
            cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
            //本周数据
            List<Map<String, Object>> weekDayData = (List<Map<String, Object>>) bodyList.get(x).get("data");
            // 第三行数据
            for (int i = 0; i < weekDayData.size(); i++) {
                row = sheet.createRow(i + 2);
                sheet.autoSizeColumn(i, true);// 根据字段长度自动调整列的宽度
                // 导入对应列的数据
                HSSFCell cell = row.createCell(0);
                cell.setCellValue(weekDayData.get(i).get("floorName") + "");
                cell.setCellStyle(style);
                sheet.setColumnWidth(0,  15 * 256);
                // 导入对应列的数据
                HSSFCell cell1 = row.createCell(1);
                cell1.setCellValue(weekDayData.get(i).get("roomNum") + "");
                cell1.setCellStyle(style);
                sheet.setColumnWidth(1,  15 * 256);
                // 导入对应列的数据
                HSSFCell cell2 = row.createCell(2);
                cell2.setCellValue(weekDayData.get(i).get("numberOfStudent") + "");
                cell2.setCellStyle(style);
                sheet.setColumnWidth(2,  15 * 256);
                //对应列的数据
                List<Map<String, Object>> data = (List<Map<String, Object>>) weekDayData.get(i).get("data");

                for (int j = 0; j < headList.size(); j++) {
                    if (data.size() < 1 ) {
                        HSSFCell cellfor = row.createCell(j + 3);
                        cellfor.setCellValue("");
                        cellfor.setCellStyle(style);
                    } else {
                        Map<String, Object> head = headList.get(j);
                        for (Map<String, Object> map1 : data) {
                            List<Map<String,Object>> list2 = (List<Map<String, Object>>) map1.get("data");
                            if (map1 != null && map1.get("time") != null) {
                                boolean flg = comparisonTime(map1.get("time").toString(), head.get("time") + "");
                                if (map1.get("time").toString().equals(head.get("time") + "")) {
                                    String text = "";
                                    for (Map<String,Object> map2:list2) {
                                        text += ""+map2.get("projectName")+"\r\n" + map2.get("date")+"";
                                    }
                                    // 导入对应列的数据
                                    HSSFCell cellfor = row.createCell(j + 3);
                                    cellfor.setCellStyle(cellStyle);
                                    cellfor.setCellValue(new HSSFRichTextString(text));
//                                    cellfor.setCellStyle(style);
                                } else if (flg) {
                                    String text = "";
                                    // 导入对应列的数据
                                    for (Map<String,Object> map2:list2) {
                                        text += ""+map2.get("projectName")+"\r\n" + map2.get("date")+"\r\n" + map1.get("time").toString() + "";
                                    }
                                    HSSFCell cellfor = row.createCell(j + 3);
                                    cellfor.setCellStyle(cellStyle);
                                    cellfor.setCellValue(new HSSFRichTextString(text));
//                                    cellfor.setCellStyle(style);
                                }
                            }
                        }
                    }
                    sheet.setColumnWidth(j+3,  25 * 256);
                }

            }
        }
        return wb;
    }

    public static boolean comparisonTime(String starts, String ends) {
        List<String> headTime = Arrays.asList(starts.split("-"));
        List<String> dataTime = Arrays.asList(ends.split("-"));
        String headStartTime = headTime.get(0);
        String headendTime = headTime.get(1);
        String dataStartTime = dataTime.get(0);
        if (DateUtil.dataStrTime(headStartTime, dataStartTime)
                && DateUtil.dataStrTime(dataStartTime, headendTime)) {
            return true;
        }
        return false;
    }
    
    //教师类表导出
    public static HSSFWorkbook exportTeacher(Map<String,Object> map){
        List<Map<String,Object>> head = (List<Map<String, Object>>) map.get("head");
        List<Map<String,Object>> data = (List<Map<String, Object>>) map.get("data");

        List<String> header = new ArrayList<String>(Arrays.asList("年份","月份","教师类别"));
        head.forEach(e -> {
            header.add(e.get("pName").toString());
        });
        header.add("工作量扣除");
        //header.add("讲座");
        //header.add("补发课酬");
        header.add("课酬合计");
        String [] excelHeader = header.toArray(new String[header.size()]);
        HSSFWorkbook wb = new HSSFWorkbook();
        // 生成一个表格
        HSSFSheet sheet = wb.createSheet("教师类别课酬数据");
        
        HSSFRow row = sheet.createRow(0);
        // 第一行表头courseSearchView
        for (int i = 0; i < excelHeader.length; i++) {

            sheet.autoSizeColumn(i, true);// 自动调整宽度
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(excelHeader[i]);

            for (int j = 0; j < excelHeader.length; j++) {
                // 从第j+1列开始填充
                cell = row.createCell(j);
                // 填充excelHeader1[j]第j个元素
                cell.setCellValue(excelHeader[j]);
            }
        }
        //数据
        for ( int i = 0 ; i < data.size() ; i ++ ) {
            Map<String,Object> d = data.get(i);
            row = sheet.createRow(i + 1);
            // 导入对应列的数据
            HSSFCell cell = row.createCell(0);
            cell.setCellValue(d.get("year") + "");
            sheet.setColumnWidth(0,  15 * 256);
            // 导入对应列的数据
            HSSFCell cell1 = row.createCell(1);
            cell1.setCellValue(d.get("month") + "");
            sheet.setColumnWidth(1,  15 * 256);
            // 导入对应列的数据
            HSSFCell cell2 = row.createCell(2);
            cell2.setCellValue(d.get("teacherType") + "");
            sheet.setColumnWidth(2,  15 * 256);
            List<String> fd = (List<String>) d.get("valName");
            double sum = 0d;
            for( int j = 0 ; j < fd.size() ; j ++ ){
                // 导入对应列的数据
                HSSFCell cellfor = row.createCell(j + 3);
                String num = "0.00";
                if(!fd.get(j).equals("null")){
                    sum += Double.parseDouble(fd.get(j));
                    num = fd.get(j);
                }
                cellfor.setCellValue(num);
                sheet.setColumnWidth(j+3,  15 * 256);
            }
            
            DecimalFormat df = new DecimalFormat("0.00");
            // 导入对应列的数据
            HSSFCell cell3 = row.createCell(fd.size()+3);
            double deduction=Double.parseDouble(d.get("deduction").toString())*(-1);
            cell3.setCellValue(df.format(deduction));
            sheet.setColumnWidth(fd.size()+3,  15 * 256);
            // 导入对应列的数据
           /* HSSFCell cell4 = row.createCell(fd.size()+4);
            double lecture = Double.parseDouble(d.get("lecture").toString());
            cell4.setCellValue(df.format(lecture));
            sheet.setColumnWidth(fd.size()+4,  15 * 256);*/
            // 导入对应列的数据
            /*HSSFCell cell5 = row.createCell(fd.size()+5);
            double replacement = Double.parseDouble(d.get("replacement").toString());
            cell5.setCellValue(df.format(replacement));
            sheet.setColumnWidth(fd.size()+5,  15 * 256);*/
            // 导入对应列的数据
            /*sum += Double.parseDouble(d.get("deduction").toString())+
                    Double.parseDouble(d.get("lecture").toString())+
                    Double.parseDouble(d.get("replacement").toString());*/
            sum += Double.parseDouble(d.get("deduction").toString());
            HSSFCell cell6 = row.createCell(fd.size()+4);
            //cell6.setCellValue(df.format(sum) + "");
            cell6.setCellValue(d.get("total").toString());
            sheet.setColumnWidth(fd.size()+4,  15 * 256);
        }
        return wb;
    }

}
