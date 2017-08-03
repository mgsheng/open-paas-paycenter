package cn.com.open.user.platform.manager.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
public class ApiCommonController   {
	
  
    @RequestMapping("/dnotdelet/mom.html")
    public String home() {
        return "mom";
    }
}