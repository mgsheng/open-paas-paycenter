package cn.com.open.pay.platform.manager.tools;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;


import cn.com.open.pay.platform.manager.order.model.MerchantOrderInfo;
import cn.com.open.pay.platform.manager.order.model.MerchantOrderOffline;


public class OrderCountExport {
	
	
	public static void exportChuBei(HttpServletResponse response,List<MerchantOrderInfo> infoList) {
		/*SecuritySubject user = getUser(request);
		String userId = user.getCode();*/

		// 声明一个工作薄
		HSSFWorkbook wb = new HSSFWorkbook();
		// 声明一个单子并命名
		HSSFSheet sheet = wb.createSheet("StoringTalentExcel");  
		// 给单子名称一个长度
		sheet.setDefaultColumnWidth((short) 15);
		// 生成一个样式
		HSSFCellStyle style = wb.createCellStyle();
		// 创建第一行（也可以称为表头）
		HSSFRow row = sheet.createRow(0);
		// 样式字体居中
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 给表头第一行一次创建单元格
		HSSFCell cell = row.createCell((short) 0);
		cell.setCellValue("成交金额");
		cell.setCellStyle(style);
		cell = row.createCell((short) 1);
		cell.setCellValue("成交笔数");
		cell.setCellStyle(style);
		cell = row.createCell((short) 2);
		cell.setCellValue("缴费人数");
		cell.setCellStyle(style);
		cell.setCellStyle(style);
		cell = row.createCell((short) 3);
		cell.setCellValue("手续费");
		cell.setCellStyle(style);
		cell.setCellStyle
		(style);
		cell = row.createCell((short) 4);
		cell.setCellValue("日期");
		cell.setCellStyle(style);
		cell.setCellStyle

		(style);
		
		/*cell = row.createCell((short) 11);
		cell.setCellValue("人才使用");
		cell.setCellStyle(style);*/
		// 向单元格里填充数据
		for (short i = 0; i < infoList.size(); i++) {
			row = sheet.createRow(i + 1);
			row.createCell(0).setCellValue(infoList.get(i).getOrderAmount());//成交金额
			row.createCell(1).setCellValue(infoList.get(i).getMerchantOrderId());//成交笔数
			row.createCell(2).setCellValue(infoList.get(i).getSourceUid());//缴费人数
			if(infoList.get(i).getPayCharge()!=null){
				row.createCell(3).setCellValue(infoList.get(i).getPayCharge());//手续费
			}else{
				row.createCell(3).setCellValue("0");//手续费
			}
			
			row.createCell(4).setCellValue(infoList.get(i).getFoundDate());//日期
			
			//人才使用总和
			//String thingName="";
			/*String orderState="4";
			List<WfHistOrder> listWfHistOrderSy=wfOrderService.getMessageHistOrder(userId, orderState,thingName);
			int syNumber = listWfHistOrderSy.size();
			request.setAttribute("syNumber", syNumber);*/

			//row.createCell(11).setCellValue(infoList.get(i).getStrUiPclassFication());
		}
		try {
			response.setContentType("application/vnd.ms-excel");
			String filedisplay = "交易数据.xls";
			String iso_filename = parseGBK(filedisplay);
			response.addHeader("Content-Disposition", "attachment;filename="+ iso_filename);
			ServletOutputStream out = response.getOutputStream();
			wb.write(out);                                                      
			out.close();
//			JOptionPane.showMessageDialog(null, "导出成功!");
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "导出失败!");
			e.printStackTrace();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "导出失败!");
			e.printStackTrace();
		}
	}
	
	public static String parseGBK(String sIn) {
		if (sIn == null || sIn.equals(""))
			return sIn;
		try {
			return new String(sIn.getBytes("GBK"), "ISO-8859-1");
		} catch (UnsupportedEncodingException usex) {
			return sIn;
		}
	}

}
