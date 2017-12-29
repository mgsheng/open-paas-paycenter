package cn.com.open.openpaas.payservice.web.api.redirect;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.openpaas.payservice.app.common.BaseControllerUtil;
import cn.com.open.openpaas.payservice.dev.PayserviceDev;


/**
 * BANK_CARD-B2C(易慧金)
 */
@Controller
@RequestMapping("/yeepayPos")
public class YeepayPosController extends BaseControllerUtil{
	 @Autowired
	 private PayserviceDev payserviceDev;
	  @RequestMapping("/download/yeepay/plug")
	    public void downLoad(HttpServletRequest request,HttpServletResponse response) throws IOException {
	        String filePath =payserviceDev.getYeepay_plug_url();		//文件路径
	        ServletOutputStream servletOutputStream = response.getOutputStream();
	        response.setContentType("application/OCTET-STREAM;charset=gb2312");
	        response.setHeader("Content-disposition", "attachment;filename=yibaoSetup.msi");
	        BufferedInputStream bis = null;
	        BufferedOutputStream bos = null;
	        FileInputStream fis = null;
	        try {
	            fis = new FileInputStream(filePath);
	            bis = new BufferedInputStream(fis);
	            bos = new BufferedOutputStream(servletOutputStream);
	            byte[] buff = new byte[2048];
	            int bytesRead;
	            while ((bytesRead = bis.read(buff, 0, buff.length)) != -1) {
	                bos.write(buff, 0, bytesRead);
	            }
	        } catch (Exception e) {
	            throw e;
	        } finally {
	            if (fis != null)
	                fis.close();
	            if (bis != null)
	                bis.close();
	            if (bos != null)
	                bos.close();
	        }
	        
	    }
   
}