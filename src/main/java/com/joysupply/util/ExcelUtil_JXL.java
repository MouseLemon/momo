package com.joysupply.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.*;
import jxl.biff.DisplayFormat;
import jxl.biff.FontRecord;
import jxl.biff.FormatRecord;
import jxl.biff.XFRecord;
import jxl.format.*;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.CellFormat;
import jxl.format.Colour;
import jxl.format.Font;
import jxl.format.VerticalAlignment;
import jxl.read.biff.Record;
import jxl.write.*;
import jxl.write.biff.StyleXFRecord;

public class ExcelUtil_JXL {

	/**
	 * 导出excel(输出到网络)
	 * 
	 * @param fileName
	 * @param exportMap
	 * @param list
	 * @param response
	 * @return
	 */
	public static String exportExcel(String fileName, String workbookName, Map<String, Object> exportMap,
									 List<Map<String, Object>> list, HttpServletResponse response, HttpServletRequest request) {
		String result = "SUCCESS";
		// 以下开始输出到EXCEL
		try {
			// 定义输出流，以便打开保存对话框______________________begin
			// HttpServletResponse response=ServletActionContext.getResponse();
			OutputStream os = response.getOutputStream();// 取得输出流
			response.reset();// 清空输出流
			String userAgent = request.getHeader("User-Agent");
			// 针对IE或者以IE为内核的浏览器：
			if (userAgent.contains("MSIE") || userAgent.contains("Trident") || userAgent.contains("Edge")) {
				fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
			} else {
				// 非IE浏览器的处理：
				fileName = new String(fileName.getBytes("GB2312"), "ISO-8859-1");
			}
			response.setHeader("Content-disposition",
					"attachment; filename=" + fileName);
//			response.setHeader("Content-disposition",
//					"attachment; filename=" + new String(fileName.getBytes("GB2312"), "ISO8859-1"));
			// 设定输出文件头
			response.setContentType("application/msexcel");// 定义输出类型
			Cookie cookie = new Cookie("downSuccess", result);
			cookie.setMaxAge(600);
			response.addCookie(cookie);
			// 定义输出流，以便打开保存对话框_______________________end

			/** **********创建工作簿************ */
			WritableWorkbook workbook = Workbook.createWorkbook(os);

			/** **********创建工作表************ */

			WritableSheet sheet = workbook.createSheet(workbookName, 0);

			/** **********设置纵横打印（默认为纵打）、打印纸***************** */
			jxl.SheetSettings sheetset = sheet.getSettings();
			sheetset.setProtected(false);

			/** ************设置单元格字体************** */
			WritableFont NormalFont = new WritableFont(WritableFont.ARIAL, 10);
			WritableFont BoldFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);

			/** ************以下设置三种单元格样式，灵活备用************ */
			// 用于标题居中
			WritableCellFormat wcf_center = new WritableCellFormat(NumberFormats.TEXT);
			wcf_center.setFont(BoldFont);
			wcf_center.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条
			wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
			wcf_center.setAlignment(Alignment.CENTRE); // 文字水平对齐
			wcf_center.setWrap(false); // 文字是否换行
            // 红色字体样式
            WritableFont font1 = new WritableFont(WritableFont.ARIAL,10,WritableFont.BOLD,false,UnderlineStyle.NO_UNDERLINE,Colour.RED);
            WritableCellFormat cellFormat =  new WritableCellFormat(NumberFormats.TEXT);
            cellFormat.setFont(font1);
            cellFormat.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条
            cellFormat.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
            cellFormat.setAlignment(Alignment.CENTRE); // 文字水平对齐
            cellFormat.setWrap(false); // 文字是否换行

            // 用于正文居左
            WritableCellFormat wcf_left = new WritableCellFormat(NumberFormats.TEXT);
            wcf_left.setFont(NormalFont);
			wcf_left.setBorder(Border.NONE, BorderLineStyle.THIN); // 线条
			wcf_left.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
			wcf_left.setAlignment(Alignment.LEFT); // 文字水平对齐
			wcf_left.setWrap(false); // 文字是否换行


			/** ***************以下是EXCEL开头大标题，暂时省略********************* */
			// sheet.mergeCells(0, 0, colWidth, 0);
			// sheet.addCell(new Label(0, 0, "XX报表", wcf_center));
			/** ***************以下是EXCEL第一行列标题********************* */
			Object[] columArr = exportMap.keySet().toArray();
			Object[] filedNameArr = exportMap.values().toArray();
			// sheet.addCell(new Label(0, 0, "序号", wcf_center));
            CellView cv = new CellView();
            cv.setSize(256*20);
            cv.setFormat(wcf_left);
			for (int i = 0; i < columArr.length; i++) {
                Label cell = new Label(i, 0, columArr[i].toString(), wcf_center);
                if(exportMap.get(columArr[i].toString()).toString().startsWith("*")) {
                    cell.setCellFormat(cellFormat);
                }
                sheet.setColumnView(i, cv);
                sheet.addCell(cell);
			}
			/** ***************以下是EXCEL正文数据********************* */
			int i = 1;
			for (Map<String, Object> map : list) {
				int j = 0;
				for (Object fieldName : filedNameArr) {
                    String str = "";
					if (map.containsKey(fieldName)) {
						if(map.get(fieldName) != null)
							str = map.get(fieldName).toString();
					}
                    Label label = new Label(j, i, str);
                    sheet.addCell(label);
                    j++;
                }
                i++;
			}
			/** **********将以上缓存中的内容写到EXCEL文件中******** */
			workbook.write();
			/** *********关闭文件************* */
			workbook.close();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
			result = "ERROR:" + e.toString();
		}
		return result;
	}

	/**
	 * 导出excel(输出到本地)
	 * 
	 * @param fileName
	 *            Excel名称
	 * @param workbookName
	 *            sheet名称
	 * @param exportMap
	 *            标题行
	 * @param listContent
	 *            数据列表
	 * @param filePath
	 *            目标路径
	 * @return
	 */
	public static String exportExcelLocal(String fileName, String workbookName, Map<String, String> exportMap,
			List<Map<String, Object>> listContent, String filePath) {
		String result = "SUCCESS";
		// 以下开始输出到EXCEL
		try {
			// 定义输出流，以便打开保存对话框______________________begin
			// HttpServletResponse response=ServletActionContext.getResponse();
			File _file = new File(filePath);
			_file.mkdirs();
			OutputStream os = new FileOutputStream(filePath + "/" + fileName);
			// 定义输出流，以便打开保存对话框_______________________end

			/** **********创建工作簿************ */
			WritableWorkbook workbook = Workbook.createWorkbook(os);

			/** **********创建工作表************ */
			WritableSheet sheet = workbook.createSheet(workbookName, 0);

			/** **********设置纵横打印（默认为纵打）、打印纸***************** */
			SheetSettings sheetset = sheet.getSettings();
			sheetset.setProtected(false);

			/** ************设置单元格字体************** */
			WritableFont NormalFont = new WritableFont(WritableFont.ARIAL, 10);
			WritableFont BoldFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);

			/** ************以下设置三种单元格样式，灵活备用************ */
			// 用于标题
			WritableCellFormat wcf_center = new WritableCellFormat(BoldFont);
			wcf_center.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条
			wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
			wcf_center.setAlignment(Alignment.CENTRE); // 文字水平对齐
			wcf_center.setWrap(false); // 文字是否换行

			// 用于正文
			WritableCellFormat wcf_left = new WritableCellFormat(NormalFont);
			wcf_left.setBorder(Border.NONE, BorderLineStyle.THIN); // 线条
			wcf_left.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
			wcf_left.setAlignment(Alignment.LEFT); // 文字水平对齐
			wcf_left.setWrap(false); // 文字是否换行

			/** ***************以下是EXCEL开头大标题，暂时省略********************* */
			// sheet.mergeCells(0, 0, colWidth, 0);
			// sheet.addCell(new Label(0, 0, "XX报表", wcf_center));
			/** ***************以下是EXCEL第一行列标题********************* */
			Object[] columArr = exportMap.keySet().toArray();
			Object[] filedNameArr = exportMap.values().toArray();
			// sheet.addCell(new Label(0, 0, "序号", wcf_center));
            for (int i = 0; i < columArr.length; i++) {
                sheet.addCell(new Label(i, 0, columArr[i].toString(), wcf_center));
			}
			/** ***************以下是EXCEL正文数据********************* */
            int i = 1;
//            CellView cellView = new CellView(); 设置自动行哥，中文无效
//            cellView.setAutosize(true);
//            sheet.setColumnView(i,cellView);
            for (Map<String, Object> map : listContent) {
                int j = 0;
				for (Object fieldName : filedNameArr) {
					String str = "";
                    if (map.containsKey(fieldName)) {
                        str = map.get(fieldName).toString();
                    }
                    sheet.addCell(new Label(j, i, str, wcf_left));
                    j++;
				}
				i++;
			}
			/** **********将以上缓存中的内容写到EXCEL文件中******** */
			workbook.write();
			/** *********关闭文件************* */
			workbook.close();
			os.close();

		} catch (Exception e) {
			result = "ERROR:" + e.toString();
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 导出excel(输出到本地)-生成两个工作簿
	 * 
	 * @param fileName
	 * @param workbookName1
	 * @param workbookName2
	 * @param exportMap1
	 * @param exportMap2
	 * @param countList
	 * @param priceList
	 * @param filePath
	 * @return
	 */
	public static String exportExcelLocalTwoSheel(String fileName, String workbookName1, String workbookName2,
			Map<String, Object> exportMap1, Map<String, Object> exportMap2, List<Map<String, Object>> countList,
			List<Map<String, Object>> priceList, String filePath) {
		String result = "SUCCESS";
		System.out.println("fileName：" + fileName);
		System.out.println("workbookName1：" + workbookName1);
		System.out.println("workbookName2：" + workbookName2);
		System.out.println("exportMap1：" + exportMap1);
		System.out.println("exportMap2：" + exportMap2);
		System.out.println("firstList：" + countList);
		System.out.println("secondList：" + priceList);
		System.out.println("filePath：" + filePath);
		// 以下开始输出到EXCEL
		try {
			// 定义输出流，以便打开保存对话框______________________begin
			// HttpServletResponse response=ServletActionContext.getResponse();
			File _file = new File(filePath);
			_file.mkdirs();
			OutputStream os = new FileOutputStream(filePath + "/" + fileName);
			// 定义输出流，以便打开保存对话框_______________________end

			/** **********创建工作簿************ */
			WritableWorkbook workbook = Workbook.createWorkbook(os);

			/** **********创建工作表************ */

			WritableSheet sheet = workbook.createSheet(workbookName1, 0);
			WritableSheet sheet2 = workbook.createSheet(workbookName2, 0);

			/** **********设置纵横打印（默认为纵打）、打印纸***************** */
			jxl.SheetSettings sheetset = sheet.getSettings();
			sheetset.setProtected(false);
			jxl.SheetSettings sheetsets = sheet2.getSettings();
			sheetsets.setProtected(false);

			/** ************设置单元格字体************** */
			WritableFont NormalFont = new WritableFont(WritableFont.ARIAL, 10);
			WritableFont BoldFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);

			/** ************以下设置三种单元格样式，灵活备用************ */
			// 用于标题居中
			WritableCellFormat wcf_center = new WritableCellFormat(BoldFont);
			wcf_center.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条
			wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
			wcf_center.setAlignment(Alignment.CENTRE); // 文字水平对齐
			wcf_center.setWrap(false); // 文字是否换行

			// 用于正文居左
			WritableCellFormat wcf_left = new WritableCellFormat(NormalFont);
			wcf_left.setBorder(Border.NONE, BorderLineStyle.THIN); // 线条
			wcf_left.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
			wcf_left.setAlignment(Alignment.LEFT); // 文字水平对齐
			wcf_left.setWrap(false); // 文字是否换行

			/** ***************以下是EXCEL开头大标题，暂时省略********************* */
			// sheet.mergeCells(0, 0, colWidth, 0);
			// sheet.addCell(new Label(0, 0, "XX报表", wcf_center));
			/** ***************以下是EXCEL第一行列标题********************* */
			Object[] columArr = exportMap1.keySet().toArray();
			Object[] filedNameArr = exportMap1.values().toArray();
			// sheet.addCell(new Label(0, 0, "序号", wcf_center));
			for (int i = 0; i < columArr.length; i++) {
				sheet.addCell(new Label(i, 0, columArr[i].toString(), wcf_center));
			}
			// 针对第一行列标题不一致，添加两个exportMaps
			Object[] columArr2 = exportMap2.keySet().toArray();
			Object[] filedNameArr2 = exportMap2.values().toArray();
			// sheet.addCell(new Label(0, 0, "序号", wcf_center));
			for (int i = 0; i < columArr2.length; i++) {
				sheet2.addCell(new Label(i, 0, columArr2[i].toString(), wcf_center));
			}
			/** ***************以下是EXCEL正文数据********************* */
			int i = 1;
			for (Map<String, Object> map : countList) {
				int j = 0;
				for (Object fieldName : filedNameArr) {
					String str = "";
					if (map.containsKey(fieldName)) {
						str = map.get(fieldName).toString();
					}
					sheet.addCell(new Label(j, i, str, wcf_left));
					j++;
				}
				i++;
			}
			int n = 1;
			for (Map<String, Object> map : priceList) {
				int j = 0;
				for (Object fieldName : filedNameArr2) {
					String str = "";
					if (map.containsKey(fieldName)) {
						str = map.get(fieldName).toString();
					}
					sheet2.addCell(new Label(j, n, str, wcf_left));
					j++;
				}
				n++;
			}
			/** **********将以上缓存中的内容写到EXCEL文件中******** */
			workbook.write();
			/** *********关闭文件************* */
			workbook.close();
			os.close();

		} catch (Exception e) {
			result = "ERROR:" + e.toString();
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 导出excel(输出到网络)-生成两个工作簿
	 * 
	 * @param fileName
	 * @param workbookName1
	 * @param workbookName2
	 * @param exportMap1
	 * @param exportMap2
	 * @param countList
	 * @param priceList
	 * @return
	 */
	public static String exportExcelTwoSheel(String fileName, String workbookName1, String workbookName2,
			Map<String, Object> exportMap1, Map<String, Object> exportMap2, List<Map<String, Object>> countList,
			List<Map<String, Object>> priceList, HttpServletResponse response) {
		String result = "SUCCESS";
		System.out.println("fileName：" + fileName);
		System.out.println("workbookName1：" + workbookName1);
		System.out.println("workbookName2：" + workbookName2);
		System.out.println("exportMap1：" + exportMap1);
		System.out.println("exportMap2：" + exportMap2);
		System.out.println("firstList：" + countList);
		System.out.println("secondList：" + priceList);
		// 以下开始输出到EXCEL
		try {
			// 定义输出流，以便打开保存对话框______________________begin
			// HttpServletResponse response=ServletActionContext.getResponse();
			OutputStream os = response.getOutputStream();// 取得输出流
			response.reset();// 清空输出流
			response.setHeader("Content-disposition",
					"attachment; filename=" + new String(fileName.getBytes("GB2312"), "ISO8859-1"));
			// 设定输出文件头
			response.setContentType("application/msexcel");// 定义输出类型
			Cookie cookie = new Cookie("downSuccess", result);
			cookie.setMaxAge(600);
			response.addCookie(cookie);
			// 定义输出流，以便打开保存对话框_______________________end

			/** **********创建工作簿************ */
			WritableWorkbook workbook = Workbook.createWorkbook(os);

			/** **********创建工作表************ */

			WritableSheet sheet = workbook.createSheet(workbookName1, 0);
			WritableSheet sheet2 = workbook.createSheet(workbookName2, 0);

			/** **********设置纵横打印（默认为纵打）、打印纸***************** */
			jxl.SheetSettings sheetset = sheet.getSettings();
			sheetset.setProtected(false);
			jxl.SheetSettings sheetsets = sheet2.getSettings();
			sheetsets.setProtected(false);

			/** ************设置单元格字体************** */
			WritableFont NormalFont = new WritableFont(WritableFont.ARIAL, 10);
			WritableFont BoldFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);

			/** ************以下设置三种单元格样式，灵活备用************ */
			// 用于标题居中
			WritableCellFormat wcf_center = new WritableCellFormat(BoldFont);
			wcf_center.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条
			wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
			wcf_center.setAlignment(Alignment.CENTRE); // 文字水平对齐
			wcf_center.setWrap(false); // 文字是否换行

			// 用于正文居左
			WritableCellFormat wcf_left = new WritableCellFormat(NormalFont);
			wcf_left.setBorder(Border.NONE, BorderLineStyle.THIN); // 线条
			wcf_left.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
			wcf_left.setAlignment(Alignment.LEFT); // 文字水平对齐
			wcf_left.setWrap(false); // 文字是否换行

			/** ***************以下是EXCEL开头大标题，暂时省略********************* */
			// sheet.mergeCells(0, 0, colWidth, 0);
			// sheet.addCell(new Label(0, 0, "XX报表", wcf_center));
			/** ***************以下是EXCEL第一行列标题********************* */
			Object[] columArr = exportMap1.keySet().toArray();
			Object[] filedNameArr = exportMap1.values().toArray();
			// sheet.addCell(new Label(0, 0, "序号", wcf_center));
			for (int i = 0; i < columArr.length; i++) {
				sheet.addCell(new Label(i, 0, columArr[i].toString(), wcf_center));
			}
			// 针对第一行列标题不一致，添加两个exportMaps
			Object[] columArr2 = exportMap2.keySet().toArray();
			Object[] filedNameArr2 = exportMap2.values().toArray();
			// sheet.addCell(new Label(0, 0, "序号", wcf_center));
			for (int i = 0; i < columArr2.length; i++) {
				sheet2.addCell(new Label(i, 0, columArr2[i].toString(), wcf_center));
			}
			/** ***************以下是EXCEL正文数据********************* */
			int i = 1;
			for (Map<String, Object> map : countList) {
				int j = 0;
				for (Object fieldName : filedNameArr) {
					String str = "";
					if (map.containsKey(fieldName)) {
						str = map.get(fieldName).toString();
					}
					sheet.addCell(new Label(j, i, str, wcf_left));
					j++;
				}
				i++;
			}
			int n = 1;
			for (Map<String, Object> map : priceList) {
				int j = 0;
				for (Object fieldName : filedNameArr2) {
					String str = "";
					if (map.containsKey(fieldName)) {
						str = map.get(fieldName).toString();
					}
					sheet2.addCell(new Label(j, n, str, wcf_left));
					j++;
				}
				n++;
			}
			/** **********将以上缓存中的内容写到EXCEL文件中******** */
			workbook.write();
			/** *********关闭文件************* */
			workbook.close();
			os.close();
		} catch (Exception e) {
			result = "ERROR:" + e.toString();
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 读取excel 公共方法
	 * 
	 * @param is
	 *            文件流
	 * @param fileType
	 *            文件后缀
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> readExcel(InputStream is, String fileType, String[] titles, String[] titles_en) {
		Map<String, Object> ret = new HashMap<String, Object>();
		Workbook wb = null;
		try {
			if (fileType.equals("xls") || fileType.equals("xlsx")) {
				wb = Workbook.getWorkbook(is);
			} else {
				ret.put("result", false);
				ret.put("message", "请选择Excel文件");
				return ret;
			}
			int getNum = wb.getNumberOfSheets();
			if (getNum > 0) {
				// 循环每一页 并处理当前循环页
				for (int numSheet = 0; numSheet < getNum; numSheet++) {
					Sheet sheet = wb.getSheet(numSheet);// 这里表示当前页的数据
					if (sheet == null)
						continue; // 如果当前页面没有数据则跳出循环 继续下一页
					// 获取当前页的行数
					int rows = sheet.getRows();
					int columns = sheet.getColumns();
					Map<String, String> title = new LinkedHashMap<String, String>();
					if (titles.length != titles_en.length) {
						ret.put("result", false);
						ret.put("message", "内部原因【模板标题错误】");
						return ret;
					}
					for (int i = 0; i < titles.length; i++) {
						title.put(titles[i], titles_en[i]);
					}

					if (rows > 1) {
						// 获取第一行的标题行,并判断顺序是否正确
						if (checkTitleRule(sheet, title)) {
							columns = titles.length;
							List<Map<String, Object>> resList = new ArrayList<>();
							int index = 0;
							for (int i = 1; i < rows; i++) {
								Map<String, Object> map = new LinkedHashMap<String, Object>();
								Cell[] cells = sheet.getRow(i);
								// 判断当前行是否有数据
								boolean isNull = true;
								for (Cell cell : cells) {
									if (cell.getContents() != null && !"".equals(cell.getContents())) {
										isNull = false;
										break;
									}
								}
								if (isNull) {
									break;
								}
								// 判断是否存在 @重拍至
								boolean isJump = false;
								// 当Excel表中的列比设定的titles列多，并且其余全部匹配，此时会出现下标越界异常
								for (int j = 0; j < columns; j++) {
									Cell cell = sheet.getCell(j, i);
									String title_en = title.get(sheet.getCell(j, 0).getContents()).toString();
									if (!"".equals(cell.getContents())) {
										if (cell.getContents().contains("@") && title_en.equals("name")) {
											isJump = true;
										}
                                            if(cell.getType() == CellType.DATE){
                                                DateCell dc = (DateCell)cell;
                                                Date date = dc.getDate();
                                                SimpleDateFormat ds = new SimpleDateFormat("yyyy-MM-dd");
                                                map.put(title_en, ds.format(date));
                                            }else {

                                                map.put(title_en, cell.getContents().trim());
                                            }
									} else {
										map.put(title_en, "");
									}
								}
								if (!isJump) {
									resList.add(map);
								}
								index++;
							}
							ret.put("result", true);
							ret.put("message", "读取到" + index + "条信息");
							ret.put("list", resList);
						} else {
							ret.put("result", false);
							ret.put("message", "模板标题不符合要求");
						}
					} else {
						ret.put("result", false);
						ret.put("message", "Excel中数据为空");
					}
				}
			} else {
				ret.put("result", false);
				ret.put("message", "Excel中没有工作簿");
			}

		} catch (Exception e) {// 会出现下标越界异常
			// e.printStackTrace();
			System.out.println("系统异常：{" + e.toString() + "}");// 打印异常供开发人员查看
			ret.put("result", false);
			ret.put("message", "模板标题不符合要求");// 页面显示Excel表不符合要求
			return ret;
		}
		return ret;
	}

	/*
	 * 获取第一行的标题行,并判断顺序是否正确
	 * 
	 * @param sheet
	 */
	private static boolean checkTitleRule(Sheet sheet, Map<String, String> titles) {
		Cell[] cells = sheet.getRow(0);
		boolean flag = true;
		if (cells.length != titles.size()) {
			flag = false;
			return flag;
		}
		int index = 0;
		for (Cell cell : cells) {
            if (cell.getContents() != null && !"".equals(cell.getContents())) {
				index++;
				if (!titles.containsKey(cell.getContents())) {
					System.out.println("index:" + index);
					flag = false;
					return flag;
				}
			}
		}
		return flag;
	}
}
