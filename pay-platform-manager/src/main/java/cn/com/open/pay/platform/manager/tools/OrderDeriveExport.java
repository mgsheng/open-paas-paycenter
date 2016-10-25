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
import cn.com.open.pay.platform.manager.order.model.MerchantOrderRefund;


public class OrderDeriveExport {
	
	
	public static void exportChuBei(HttpServletResponse response,List<MerchantOrderInfo> infoList) {
		/*SecuritySubject user = getUser(request);
		String userId = user.getCode();*/

		// 声明一个工作薄
		HSSFWorkbook wb = new HSSFWorkbook();
		// 声明一个单子并命名
		HSSFSheet sheet = wb.createSheet("StoringTalentExcel");  
		// 给单子名称一个长度
		sheet.setDefaultColumnWidth((short) 20);
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
			String filedisplay = "downloadOrder.xls";
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
	
	public static void exportOrderOffline(HttpServletResponse response,List<MerchantOrderOffline> offlines) {
		
		// 声明一个工作薄
		HSSFWorkbook wb = new HSSFWorkbook();
		// 声明一个单子并命名
		HSSFSheet sheet = wb.createSheet("StoringTalentExcel");  
		// 给单子名称一个长度
		sheet.setDefaultColumnWidth((short) 20);
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
		cell.setCellValue("线下订单号");
		cell.setCellStyle(style);
		cell = row.createCell((short) 2);
		cell.setCellValue("收费商户");
		cell.setCellStyle(style);
		cell = row.createCell((short) 3);
		cell.setCellValue("收费金额");
		cell.setCellStyle(style);
		cell = row.createCell((short) 4);
		cell.setCellValue("缴费日期");
		cell.setCellStyle(style);
		cell = row.createCell((short) 5);
		cell.setCellValue("业务来源");
		cell.setCellStyle(style);
		cell = row.createCell((short) 6);
		cell.setCellValue("用户ID");
		cell.setCellStyle(style);
		cell = row.createCell((short) 7);
		cell.setCellValue("用户名");
		cell.setCellStyle(style);
		cell = row.createCell((short) 8);
		cell.setCellValue("真实姓名");
		cell.setCellStyle(style);
		cell = row.createCell((short) 9);
		cell.setCellValue("手机号");
		cell.setCellStyle(style);
		cell = row.createCell((short) 10);
		cell.setCellValue("支付方式");
		cell.setCellStyle(style);
		cell = row.createCell((short) 11);
		cell.setCellValue("发卡行");
		cell.setCellStyle(style);
		cell = row.createCell((short) 12);
		cell.setCellValue("操作人");
		cell.setCellStyle(style);
		cell = row.createCell((short) 13);
		cell.setCellValue("录入时间");
		cell.setCellStyle(style);
		cell = row.createCell((short) 14);
		cell.setCellValue("备注");
		cell.setCellStyle(style);
		cell = row.createCell((short) 15);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 向单元格里填充数据
		for (short i = 0; i < offlines.size(); i++) {
			row = sheet.createRow(i + 1);
			row.createCell(0).setCellValue(offlines.get(i).getId());//用户中心订单号
			row.createCell(1).setCellValue(offlines.get(i).getMerchantOrderId());//线下订单号
			row.createCell(2).setCellValue(offlines.get(i).getMerchantName());//收费商户
			row.createCell(3).setCellValue(offlines.get(i).getMoney());//收费金额
			if(offlines.get(i).getPayTime()!=null){
				row.createCell(4).setCellValue(sdf.format(offlines.get(i).getPayTime()));//缴费日期
			}
			row.createCell(5).setCellValue(offlines.get(i).getAppName());//业务来源
			row.createCell(6).setCellValue(offlines.get(i).getSourceUid());//用户ID
			row.createCell(7).setCellValue(offlines.get(i).getSourceUserName());//用户名
			row.createCell(8).setCellValue(offlines.get(i).getRealName());//真实姓名
			row.createCell(9).setCellValue(offlines.get(i).getPhone());//手机号
			row.createCell(10).setCellValue(offlines.get(i).getChannelName());//支付方式
			row.createCell(11).setCellValue(offlines.get(i).getBankName());//发卡行
			row.createCell(12).setCellValue(offlines.get(i).getOperator());//操作人
			if(offlines.get(i).getCreateTime()!=null){
				row.createCell(13).setCellValue(sdf1.format(offlines.get(i).getCreateTime()));//录入时间
			}
			row.createCell(14).setCellValue(offlines.get(i).getRemark());//备注
		}
		try {
			response.setContentType("application/vnd.ms-excel");
			String filedisplay = "线下收费单据维护.xls";
			filedisplay = URLEncoder.encode(filedisplay, "UTF-8");
			response.addHeader("Content-Disposition", "attachment;filename="+ filedisplay);
			ServletOutputStream out = response.getOutputStream();
			wb.write(out);                                                      
			out.close();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "导出失败!");
			e.printStackTrace();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "导出失败!");
			e.printStackTrace();
		}
	}

	public static void exportOrderRefund(HttpServletResponse response,
			List<MerchantOrderRefund> refunds) {
		// 声明一个工作薄
		HSSFWorkbook wb = new HSSFWorkbook();
		// 声明一个单子并命名
		HSSFSheet sheet = wb.createSheet("StoringTalentExcel");  
		// 给单子名称一个长度
		sheet.setDefaultColumnWidth((short) 20);
		// 生成一个样式
		HSSFCellStyle style = wb.createCellStyle();
		// 创建第一行（也可以称为表头）
		HSSFRow row = sheet.createRow(0);
		// 样式字体居中
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 给表头第一行一次创建单元格
		HSSFCell cell = row.createCell((short) 0);
		cell.setCellValue("商户订单号");
		cell.setCellStyle(style);
		cell = row.createCell((short) 1);
		cell.setCellValue("退费商户");
		cell.setCellStyle(style);
		cell = row.createCell((short) 2);
		cell.setCellValue("退费金额");
		cell.setCellStyle(style);
		cell = row.createCell((short) 3);
		cell.setCellValue("业务来源");
		cell.setCellStyle(style);
		cell = row.createCell((short) 4);
		cell.setCellValue("用户ID");
		cell.setCellStyle(style);
		cell = row.createCell((short) 5);
		cell.setCellValue("用户名");
		cell.setCellStyle(style);
		cell = row.createCell((short) 6);
		cell.setCellValue("真实姓名");
		cell.setCellStyle(style);
		cell = row.createCell((short) 7);
		cell.setCellValue("手机号");
		cell.setCellStyle(style);
		cell = row.createCell((short) 8);
		cell.setCellValue("处理时间");
		cell.setCellStyle(style);
		cell = row.createCell((short) 9);
		cell.setCellValue("备注");
		cell.setCellStyle(style);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 向单元格里填充数据
		for (short i = 0; i < refunds.size(); i++) {
			row = sheet.createRow(i + 1);
			row.createCell(0).setCellValue(refunds.get(i).getMerchantOrderId());//需退费商户订单号
			row.createCell(1).setCellValue(refunds.get(i).getMerchantName());//退费商户
			row.createCell(2).setCellValue(refunds.get(i).getRefundMoney());//退费金额
			row.createCell(3).setCellValue(refunds.get(i).getAppName());//业务来源
			row.createCell(4).setCellValue(refunds.get(i).getSourceUid());//用户ID
			row.createCell(5).setCellValue(refunds.get(i).getSourceUserName());//用户名
			row.createCell(6).setCellValue(refunds.get(i).getRealName());//真实姓名
			row.createCell(7).setCellValue(refunds.get(i).getPhone());//手机号
			if(refunds.get(i).getCreateTime()!=null){
				row.createCell(8).setCellValue(sdf.format(refunds.get(i).getCreateTime()));//处理时间
			}
			row.createCell(9).setCellValue(refunds.get(i).getRemark());//备注
		}
		try {
			response.setContentType("application/vnd.ms-excel");
			String filedisplay = "退费单据维护.xls";
			String iso_filename = parseGBK(filedisplay);
			response.addHeader("Content-Disposition", "attachment;filename="+ iso_filename);
			ServletOutputStream out = response.getOutputStream();
			wb.write(out);                                                      
			out.close();
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
