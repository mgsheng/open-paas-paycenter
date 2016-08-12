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


public class orderMessageExport {
	
	
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
		cell.setCellValue("下单时间");
		cell.setCellStyle(style);
		cell = row.createCell((short) 1);
		cell.setCellValue("交易时间");
		cell.setCellStyle(style);
		cell = row.createCell((short) 2);
		cell.setCellValue("商户订单号");
		cell.setCellStyle(style);
		cell.setCellStyle(style);
		cell = row.createCell((short) 3);
		cell.setCellValue("第三方订单号");
		cell.setCellStyle(style);
		cell.setCellStyle

		(style);
		cell = row.createCell((short) 4);
		cell.setCellValue("支付方式");
		cell.setCellStyle(style);
		cell.setCellStyle

		(style);
		cell = row.createCell((short) 5);
		cell.setCellValue("发卡行");
		cell.setCellStyle(style);
		cell.setCellStyle

		(style);
		cell = row.createCell((short) 6);
		cell.setCellValue("卡类型");
		cell.setCellStyle(style);
		cell.setCellStyle

		(style);
		cell = row.createCell((short) 7);
		cell.setCellValue("缴费来源");
		cell.setCellStyle(style);
		cell.setCellStyle

		(style);
		cell = row.createCell((short) 8);
		cell.setCellValue("交易状态");
		cell.setCellStyle(style);
		cell.setCellStyle

		(style);
		cell = row.createCell((short) 9);
		cell.setCellValue("缴费金额");
		cell.setCellStyle(style);
		cell.setCellStyle

		(style);
		cell = row.createCell((short) 10);
		cell.setCellValue("实收金额");
		cell.setCellStyle(style);
		cell.setCellStyle

		(style);
		cell = row.createCell((short) 11);
		cell.setCellValue("手续费");
		cell.setCellStyle(style);
		
		/*cell = row.createCell((short) 11);
		cell.setCellValue("人才使用");
		cell.setCellStyle(style);*/
		// 向单元格里填充数据
		for (short i = 0; i < infoList.size(); i++) {
			row = sheet.createRow(i + 1);
			row.createCell(0).setCellValue(infoList.get(i).getMerchantOrderDate());//下单时间
			row.createCell(1).setCellValue(infoList.get(i).getMerchantOrderDate());//交易时间 0
			row.createCell(2).setCellValue(infoList.get(i).getMerchantOrderId());//商户订单号
			row.createCell(3).setCellValue(infoList.get(i).getPayOrderId());//第三方订单号
			row.createCell(4).setCellValue(infoList.get(i).getPayOrderId());//支付方式 0
			row.createCell(5).setCellValue(infoList.get(i).getChannelOrderId());//发卡行 0
			row.createCell(6).setCellValue(infoList.get(i).getChannelOrderId());//卡类型 0
			row.createCell(7).setCellValue(infoList.get(i).getChannelOrderId());//缴费来源 0
			row.createCell(8).setCellValue(infoList.get(i).getPayStatus());//缴费状态
			row.createCell(9).setCellValue(infoList.get(i).getAmount());//缴费金额
			row.createCell(10).setCellValue(infoList.get(i).getPayAmount());//实收金额
			row.createCell(11).setCellValue(infoList.get(i).getPayCharge());//手续费
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
	
	
//	LtExportExcelToLocal
//	
//	
//	//导出Excel
//	public boolean exportExcel(HttpServletResponse response,List<MerchantOrderInfo> list)
//	    {  
//			try
//			{
//			OutputStream os = response.getOutputStream();// 取得输出流  
//			        response.reset();// 清空输出流  
//			        response.setHeader("Content-disposition", "attachment; filename=fine.xls");// 设定输出文件头  
//			        response.setContentType("application/msexcel");// 定义输出类型
//			       
//			        WritableWorkbook wbook = Workbook.createWorkbook(os); // 建立excel文件  
//			        String tmptitle = "财务报表"; // 标题  
//			        WritableSheet wsheet = wbook.createSheet(tmptitle, 0); // sheet名称 
//			       
//			// 设置excel标题  
//			WritableFont wfont = new WritableFont(WritableFont.ARIAL, 16,WritableFont.BOLD,
//			                       false,UnderlineStyle.NO_UNDERLINE,Colour.BLACK);  
//			WritableCellFormat wcfFC = new WritableCellFormat(wfont);
//			wcfFC.setBackground(Colour.AQUA);
//			wsheet.addCell(new Label(1, 0, tmptitle, wcfFC));  
//			wfont = new jxl.write.WritableFont(WritableFont.ARIAL, 14,WritableFont.BOLD,
//			                   false, UnderlineStyle.NO_UNDERLINE,Colour.BLACK);  
//			wcfFC = new WritableCellFormat(wfont); 
//		
//			// 开始生成主体内容                  
//			wsheet.addCell(new Label(0, 2, "城市代码"));  
//			wsheet.addCell(new Label(1, 2, "城市名")); 
//		
//			for(int i=0;i<list.size();i++)   <br="">{  
//			    wsheet.addCell(new Label(0, i+3, list.get(i).getCityid()));   //数据库的城市代码字段
//			    wsheet.addCell(new Label(1, i+3, list.get(i).getName()));  //数据库的城市名字段
//			}          
//			// 主体内容生成结束          
//			wbook.write(); // 写入文件  
//			wbook.close();  
//			os.close(); // 关闭流
//			return true;
//			}
//			catch(Exception ex)
//			{
//			ex.printStackTrace();
//			return false;
//			}
//	    }  
//		


}
