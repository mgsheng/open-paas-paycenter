package cn.com.open.pay.platform.manager.login.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import cn.com.open.pay.platform.manager.tools.AbstractDomain;
import cn.com.open.pay.platform.manager.tools.Help_Encrypt;
import cn.com.open.pay.platform.manager.tools.MD5;
import cn.com.open.pay.platform.manager.tools.StringTool;

/**
 * 
 */
public class User extends AbstractDomain {


	private static final long serialVersionUID = 1L;
	
	public static String STATUS_INACTIVE = "0";	//未激活
	public static String STATUS_ENABLE = "1";	//正常
	public static String STATUS_DISABLED = "2";	//封停
	
	//证件类型
	public static int IDENTIFYTYPE_ID = 1;			//身份证
	public static int IDENTIFYTYPE_MILITARYID = 2;	//军官证
	public static int IDENTIFYTYPE_HKID = 3;		//港澳台证
	public static int IDENTIFYTYPE_PASSPORT = 4;	//护照
	public static int IDENTIFYTYPE_OTHER = 5;		//其他
	
	public static int ACTIVATION_NO = 0;	//激活状态：未激活【用于邮箱、手机等验证】
	public static int ACTIVATION_YES = 1;	//激活状态：激活【用于邮箱、手机等验证】
	
	private String username;
    private String password;
    private String passwordSalt;
    
    private String phone;
    private String email;
    private String cardNo;
    //Default user is initial when create database, do not delete
    private boolean defaultUser = false;
    
    private String nickName;
    private String realName;
    private String headPicture;
    
    private String identifyNo;

    private Date lastLoginTime;
    
    private String userState;

    private Integer id;
    private String pid;
    
    private Integer identifyType;
    private Integer userType;
    private Integer userSercure;
    private Integer emailActivation;
    private Date updatePwdTime;
    
    private String desPassword;
    private long createTime; //注册时间  
    private String create_Time;// 作用是将long型转成String型
    
    private Integer deptID;//所属部门ID
    private String deptName;//所属部门名称
    private String role;//所属角色
    private Integer pageSize;//每页的显示条数
    private Integer startRow;//每页的开始记录
    private Integer countQuantity;//保存记录条数
   /* private List<Privilege> privileges = new ArrayList<Privilege>();*/

   

	public User() {
    }
    
    public User(String username, String password, String phone, String email) {
        this.username = username;
        this.buildPasswordSalt();
        this.setPlanPassword(password);
        this.phone = phone;
        this.email = email;
    }
    
    public User(String username, String password, String phone, String email, String nickName, String realName, String headPicture) {
        this.username = username;
        this.buildPasswordSalt();
        this.setPlanPassword(password);
        this.phone = phone;
        this.email = email;
        this.nickName = nickName;
        this.realName = realName;
        this.headPicture = headPicture;
    }
    public User(String phone, String email, String realName, String identifyNo, Boolean defaultUser) {
        this.phone = phone;
        this.email = email;
        this.realName = realName;
        this.identifyNo = identifyNo;
    }
    
    /**
     * 自定义密码加密排列
     * @param password
     * @return password+passwordSalt
     */
    public String customPassword(String password){
    	if(passwordSalt!=null){
    		return password+passwordSalt;
    	}
    	return password;
    }
    
    /**
     * 随机生成MD5盐值
     */
    public void buildPasswordSalt() {
    	this.passwordSalt = StringTool.getRandomString(16);
    }
    
    /**
     * 密码加密
     * @param planPassword
     */
    public void setPlanPassword(String planPassword) {
        this.password = MD5.Md5(customPassword(planPassword));
        try {
			this.desPassword = Help_Encrypt.encrypt(planPassword);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    /**
     * 验证密码是否正确
     * @param password
     * @return
     */
    public boolean checkPasswod(String password){
    	if(this.password==null||"".equals(this.password)){
    		return false;
    	}
    	if(this.password.equals(MD5.Md5(customPassword(password)))){
    		return true;
    	}
    	//如果des密码符合也算正确
    	if(StringUtils.isNotBlank(this.desPassword)){
    		try {
				if(password.equals(Help_Encrypt.decrypt(this.desPassword))){
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    	return false;
    }

    public boolean defaultUser() {
        return defaultUser;
    }

	public Date getUpdatePwdTime() {
		return updatePwdTime;
	}

	public void setUpdatePwdTime(Date updatePwdTime) {
		this.updatePwdTime = updatePwdTime;
	}
	
	public String username() {
		return username;
	}

	public void username(String username) {
		this.username = username;
	}

	public String password() {
		return password;
	}

	public void password(String password) {
        buildPasswordSalt();
        setPlanPassword(password);
//		this.password = password;
	}

	public String phone() {
		return phone;
	}

	public void phone(String phone) {
		this.phone = phone;
	}

	public String email() {
		return email;
	}

	public void email(String email) {
		this.email = email;
	}

	public String nickName() {
		return nickName;
	}

	public void nickName(String nickName) {
		this.nickName = nickName;
	}

	public String realName() {
		return realName;
	}

	public void realName(String realName) {
		this.realName = realName;
	}

	public String headPicture() {
		return headPicture;
	}

	public void headPicture(String headPicture) {
		this.headPicture = headPicture;
	}

	public String identifyNo() {
		return identifyNo;
	}

	public void identifyNo(String identifyNo) {
		this.identifyNo = identifyNo;
	}

	public Date lastLoginTime() {
		return lastLoginTime;
	}

	public void lastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String userState() {
		return userState;
	}

	public void userState(String userState) {
		this.userState = userState;
	}

	public String pid() {
		return pid;
	}

	public void pid(String pid) {
		this.pid = pid;
	}

	public Integer identifyType() {
		return identifyType;
	}

	public void identifyType(Integer identifyType) {
		this.identifyType = identifyType;
	}

	public Integer userType() {
		return userType;
	}

	public void userType(Integer userType) {
		this.userType = userType;
	}

	public Integer userSercure() {
		return userSercure;
	}

	public void userSercure(Integer userSercure) {
		this.userSercure = userSercure;
	}
	
	public String passwordSalt(){
		return passwordSalt;
	}
	
	public void passwordSalt(String passwordSalt){
		this.passwordSalt = passwordSalt;
	}
	
	public String cardNo(){
		return cardNo;
	}
	
	public void cardNo(String cardNo){
		this.cardNo = cardNo;
	}
	
	@Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{username='").append(username).append('\'');
        sb.append(", phone='").append(phone).append('\'');
        sb.append(", id='").append(id).append('\'');
        sb.append(", guid='").append(guid).append('\'');
        sb.append(", defaultUser='").append(defaultUser).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", identifyNo='").append(identifyNo).append('\'');
        sb.append(", realName='").append(realName).append('\'');
        sb.append(", pid='").append(pid).append('\'');
        sb.append(", identifyType='").append(identifyType).append('\'');
        sb.append(", userType='").append(userType).append('\'');
        sb.append(", userSercure='").append(userSercure).append('\'');
        sb.append('}');
        return sb.toString();
    }

	public Integer getEmailActivation() {
		return emailActivation;
	}

	public void setEmailActivation(Integer emailActivation) {
		this.emailActivation = emailActivation;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordSalt() {
		return passwordSalt;
	}

	public void setPasswordSalt(String passwordSalt) {
		this.passwordSalt = passwordSalt;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public boolean isDefaultUser() {
		return defaultUser;
	}

	public void setDefaultUser(boolean defaultUser) {
		this.defaultUser = defaultUser;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getHeadPicture() {
		return headPicture;
	}

	public void setHeadPicture(String headPicture) {
		this.headPicture = headPicture;
	}

	public String getIdentifyNo() {
		return identifyNo;
	}

	public void setIdentifyNo(String identifyNo) {
		this.identifyNo = identifyNo;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getUserState() {
		return userState;
	}

	public void setUserState(String userState) {
		this.userState = userState;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public Integer getIdentifyType() {
		return identifyType;
	}

	public void setIdentifyType(Integer identifyType) {
		this.identifyType = identifyType;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public Integer getUserSercure() {
		return userSercure;
	}

	public void setUserSercure(Integer userSercure) {
		this.userSercure = userSercure;
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDesPassword() {
		return desPassword;
	}

	public void setDesPassword(String desPassword) {
		this.desPassword = desPassword;
	}
	
	 public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public String getCreate_Time() {
		setCreate_Time();
		return create_Time;
	}
	//将注册时间类型转换成 String
	public void setCreate_Time() {
		if(createTime != 0){
			String createtime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(createTime));
			this.create_Time = createtime;
		}else{
			this.create_Time = "";
		}
	}

	public Integer getDeptID() {
		return deptID;
	}

	public void setDeptID(Integer deptID) {
		this.deptID = deptID;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getStartRow() {
		return startRow;
	}

	public void setStartRow(Integer startRow) {
		this.startRow = startRow;
	}

}