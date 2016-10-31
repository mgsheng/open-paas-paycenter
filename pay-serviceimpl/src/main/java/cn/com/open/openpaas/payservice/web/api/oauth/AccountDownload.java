package cn.com.open.openpaas.payservice.web.api.oauth;

import java.io.BufferedOutputStream;
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

import cn.com.open.openpaas.payservice.app.order.model.MerchantOrderInfo;




public class AccountDownload {
	
	
	public static void AccountDownload(HttpServletResponse response,List<MerchantOrderInfo> infoList,String marking) {
		
		// 向单元格里填充数据
		if(marking.equals("1")){
			try {
				//导出txt文件 
			    response.setContentType("text/plain");  
			    String fileName="对账单下载.txt";
			    String iso_filename = parseGBK(fileName);
			    fileName = URLEncoder.encode(fileName, "UTF-8"); 
			    response.setHeader("Content-Disposition","attachment; filename=" + iso_filename); 
			    BufferedOutputStream buff = null;   
			    StringBuffer write = new StringBuffer(); 
			    String enter = "\r\n";
			    ServletOutputStream outSTr = null; 
			    outSTr = response.getOutputStream(); // 建立 
			    buff = new BufferedOutputStream(outSTr); 
			    write.append("平台订单号|");
			    write.append("商户订单号|");
			    write.append("订单金额|");
			    write.append("订单创建时间|");
			    write.append("交易完成时间|");
			    write.append("手续费|");
			  //把内容写入文件 
			    if(infoList.size()>0){ 
			     for (int i = 0; i < infoList.size(); i++) { 
			      write.append(infoList.get(i).getId()+"|"); 
			      write.append(infoList.get(i).getMerchantOrderId()+"|"); 
			      write.append(infoList.get(i).getOrderAmount()+"|"); 
			      write.append(infoList.get(i).getCreateDate1()+"|"); 
			      write.append(infoList.get(i).getDealDate1()+"|");
			      write.append(infoList.get(i).getPayCharge()+"|"); 
			      write.append(enter);   
			     } 
			    } 
			    buff.write(write.toString().getBytes("UTF-8"));
			    buff.flush();   
			    buff.close();
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(null, "导出失败!");
				e.printStackTrace();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "导出失败!");
				e.printStackTrace();
			}
			
		}else{
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
			cell.setCellValue("平台订单号");
			cell.setCellStyle(style);
			cell = row.createCell((short) 1);
			cell.setCellValue("商户订单号");
			cell.setCellStyle(style);
			cell = row.createCell((short) 2);
			cell.setCellValue("订单金额");
			cell.setCellStyle(style);
			cell.setCellStyle(style);
			cell = row.createCell((short) 3);
			cell.setCellValue("订单创建时间");
			cell.setCellStyle(style);
			cell.setCellStyle
			(style);
			cell = row.createCell((short) 4);
			cell.setCellValue("交易完成时间");
			cell.setCellStyle(style);
			cell.setCellStyle

			(style);
			cell = row.createCell((short) 5);
			cell.setCellValue("手续费");
			cell.setCellStyle(style);
			cell.setCellStyle

			(style);
			for (short i = 0; i < infoList.size(); i++) {
				row = sheet.createRow(i + 1);
				row.createCell(0).setCellValue(infoList.get(i).getId());//平台订单号
				row.createCell(1).setCellValue(infoList.get(i).getMerchantOrderId());//商户订单号
				row.createCell(2).setCellValue(infoList.get(i).getOrderAmount());//订单金额
				row.createCell(3).setCellValue(infoList.get(i).getCreateDate1());//创建日期
				row.createCell(4).setCellValue(infoList.get(i).getDealDate1());//交易完成时间
				row.createCell(5).setCellValue(infoList.get(i).getPayCharge());//交易完成时间
			}
			try {
				response.setContentType("application/vnd.ms-excel");
				String filedisplay = "对账单下载.xls";
				String iso_filename = parseGBK(filedisplay);
				response.addHeader("Content-Disposition", "attachment;filename="+ iso_filename);
				ServletOutputStream out = response.getOutputStream();
				wb.write(out);                                                      
				out.close();
//				JOptionPane.showMessageDialog(null, "导出成功!");
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(null, "导出失败!");
				e.printStackTrace();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "导出失败!");
				e.printStackTrace();
			}
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
