package cn.com.open.openpaas.payservice.web.api.order;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.openpaas.payservice.app.log.service.LogMonitorService;
import cn.com.open.openpaas.payservice.app.tools.MonitorInfo;
import cn.com.open.openpaas.payservice.app.tools.MonitorTools;
import cn.com.open.openpaas.payservice.app.tools.NetworkUtil;
import cn.com.open.openpaas.payservice.app.tools.WebUtils;
import cn.com.open.openpaas.payservice.dev.PayserviceDev;



@Controller
public class ApiCommonController   {
	
    @Autowired
    private LogMonitorService logMonitorService;
    @Autowired
	 private PayserviceDev payserviceDev;
    
    /**
	 * 获取服务器状态
	 * @param request
	 * @param response
	 */
    @RequestMapping("common/status")
    public void status(HttpServletRequest request,HttpServletResponse response) {
    	String ip = NetworkUtil.getIpAddress(request);
    	System.out.println(ip);
    	Map<String,Object> map = new LinkedHashMap<String, Object>();
    	//运行状态
    	map.put("running", true);
    	//连接数据库状态
    	int val = 0;
    	try {
			val = logMonitorService.connectionTest();
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
    	if(val==1){
    		map.put("database", true);
    	}
    	else{
    		map.put("database", false);
    	}
    	//系统状态
    	try {
    		MonitorInfo monitorInfo = MonitorTools.getMonitorInfo();
			map.put("cpuratio", monitorInfo.getCpuRatio());
			map.put("freememory", monitorInfo.getFreePhysicalMemorySize());
			map.put("totalmemory", monitorInfo.getTotalMemorySize());
			map.put("freespace", monitorInfo.getUsableSpace());
			map.put("totalspace", monitorInfo.getTotalSpace());
		} catch (Exception e) {
	//		e.printStackTrace();
		}
    	WebUtils.writeJson(response, JSONObject.fromObject(map));
    }
    
    @RequestMapping("/dnotdelet/mom.html")
    public String home() {
        return "pay/mom";
    }
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