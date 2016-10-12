package cn.com.open.pay.platform.manager.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.pay.platform.manager.tools.BaseControllerUtil;
/**
 * 线下收费管理
 * @author lvjq
 *
 */
@Controller
@RequestMapping("/manage/")
public class MarchantOrderOfflineController extends BaseControllerUtil{
	private static final Logger log = LoggerFactory.getLogger(UserLoginController.class);
	//@Autowired
	//private PayChannelRateService payChannelRateService;
	
	/**
	 * 跳转到线下收费维护页面
	 * @return
	 */
	@RequestMapping(value="offlineOrderPages")
	public String channelRate(){
		log.info("---------------offlineOrderPages----------------");
		return "usercenter/merchantOrderOffline";
	}	
}
