package cn.com.open.pay.platform.manager.tools;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
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


public class OrderDeriveExport {
	
	
	public static void exportChuBei(HttpServletResponse response,List<MerchantOrderInfo> infoList) {
		/*SecuritySubject user = getUser(request);
		String userId = user.getCode();*/

		// 声明一个工作薄
		HSSFWorkbook wb = new HSSFWorkbook();
		// 声明一个单子并命名
		HSSFSheet sheet = wb.createSheet("StoringTalentExcel");  
		// 给单子名称一个长度
		sheet.setDefaultColumnWidth((short) 50);
		// 生成一个样式
		HSSFCellStyle style = wb.createCellStyle();
		// 创建第一行（也可以称为表头）
		HSSFRow row = sheet.createRow(0);
		// 样式字体居中
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 给表头第一行一次创建单元格
		HSSFCell cell = row.createCell((short) 0);
		cell.setCellValue("订单号");
		cell.setCellStyle(style);
		cell = row.createCell((short) 1);
		cell.setCellValue("商户订单号");
		cell.setCellStyle(style);
		cell = row.createCell((short) 2);
		cell.setCellValue("第三方订单号");
		cell.setCellStyle(style);
		cell.setCellStyle(style);
		cell = row.createCell((short) 3);
		cell.setCellValue("下单时间");
		cell.setCellStyle(style);
		cell.setCellStyle

		(style);
		cell = row.createCell((short) 4);
		cell.setCellValue("交易时间");
		cell.setCellStyle(style);
		cell.setCellStyle

		(style);
		cell = row.createCell((short) 5);
		cell.setCellValue("支付方式");
		cell.setCellStyle(style);
		cell.setCellStyle

		(style);
		cell = row.createCell((short) 6);
		cell.setCellValue("业务类型");
		cell.setCellStyle(style);
		cell.setCellStyle

		(style);
		cell = row.createCell((short) 7);
		cell.setCellValue("发卡行");
		cell.setCellStyle(style);
		cell.setCellStyle

		(style);
		cell = row.createCell((short) 8);
		cell.setCellValue("卡类型");
		cell.setCellStyle(style);
		cell.setCellStyle

		(style);
		cell = row.createCell((short) 9);
		cell.setCellValue("缴费来源");
		cell.setCellStyle(style);
		cell.setCellStyle

		(style);
		cell = row.createCell((short) 10);
		cell.setCellValue("交易状态");
		cell.setCellStyle(style);
		cell.setCellStyle

		(style);
		cell = row.createCell((short) 11);
		cell.setCellValue("缴费金额");
		cell.setCellStyle(style);
		cell.setCellStyle

		(style);
		cell = row.createCell((short) 12);
		cell.setCellValue("实收金额");
		cell.setCellStyle
		
		(style);
		cell = row.createCell((short) 13);
		cell.setCellValue("手续费");
		cell.setCellStyle(style);
		/*cell = row.createCell((short) 11);
		cell.setCellValue("人才使用");
		cell.setCellStyle(style);*/
		// 向单元格里填充数据
		for (short i = 0; i < infoList.size(); i++) {
			row = sheet.createRow(i + 1);
			row.createCell(0).setCellValue(infoList.get(i).getId());//下单时间
			row.createCell(1).setCellValue(infoList.get(i).getMerchantOrderId());//商户订单号
			row.createCell(2).setCellValue(infoList.get(i).getPayOrderId());//第三方订单号
			row.createCell(3).setCellValue(infoList.get(i).getFoundDate());//下单时间 
			row.createCell(4).setCellValue(infoList.get(i).getBusinessDate());//交易时间
			row.createCell(5).setCellValue(infoList.get(i).getChannelName());//支付方式
			row.createCell(6).setCellValue(infoList.get(i).getAppId());//业务类型
			row.createCell(7).setCellValue(infoList.get(i).getPaymentName());//发卡行
			row.createCell(8).setCellValue(infoList.get(i).getPaymentName());//卡类型000
			row.createCell(9).setCellValue(infoList.get(i).getPaymentName());//缴费来源000
			row.createCell(10).setCellValue(infoList.get(i).getPayStatusName());//交易状态
			row.createCell(11).setCellValue(infoList.get(i).getAmount());//缴费金额
			row.createCell(12).setCellValue(infoList.get(i).getPayAmount());//实收金额
			row.createCell(13).setCellValue(infoList.get(i).getPayCharge());//手续费
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
			String filedisplay = "StoringTalentExcel.xls";
			filedisplay = URLEncoder.encode(filedisplay, "UTF-8");
			response.addHeader("Content-Disposition", "attachment;filename="+ filedisplay);
			ServletOutputStream out = response.getOutputStream();
			wb.write(out);                                                      
			out.close();
			JOptionPane.showMessageDialog(null, "导出成功!");
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "导出失败!");
			e.printStackTrace();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "导出失败!");
			e.printStackTrace();
		}
	}
	

}
