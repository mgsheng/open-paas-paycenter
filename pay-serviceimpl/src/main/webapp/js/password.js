/**
 * 密码强中弱判断算法
 * 1. 如果输入的密码位数少于5位，那么就判定为弱。
 * 2. 如果输入的密码只由数字、小写字母、大写字母或其它特殊符号当中的一种组成，则判定为弱。
 * 3. 如果密码由数字、小写字母、大写字母或其它特殊符号当中的两种组成，则判定为中。
 * 4. 如果密码由数字、小写字母、大写字母或其它特殊符号当中的三种以上组成，则判定为强。
 * @param {Object} pwd
 */
function checkIntensity(pwd){ 
	var Mcolor,Wcolor,Scolor,Color_Html; 
  	var m=0; 
  	var Modes=0; 
  	for(i=0; i<pwd.length; i++){ 
   		var charType=0; 
    	var t=pwd.charCodeAt(i); 
    	if(t>=48 && t <=57){
    		charType=1;
    	}else if(t>=65 && t <=90){
    		charType=2;
    	}else if(t>=97 && t <=122){
    		charType=4;
    	}else{
    		charType=4;
    	} 
    	Modes |= charType; 
  	} 
  	for(i=0;i<4;i++){ 
	  	if(Modes & 1){
	  		m++;
	  	} 
	    Modes>>>=1; 
	} 
  	if(pwd.length<=4){m=1;} 
  	if(pwd.length<=0){m=0;} 
  	switch(m){ 
	    case 1 : 
	      Wcolor="pwdck pwdck_Weak_c"; 
	      Mcolor="pwdck pwdck_c"; 
	      Scolor="pwdck pwdck_c pwdck_c_r"; 
	      Color_Html="弱"; 
	    break; 
	    case 2 : 
	      Wcolor="pwdck pwdck_Medium_c"; 
	      Mcolor="pwdck pwdck_Medium_c"; 
	      Scolor="pwdck pwdck_c pwdck_c_r"; 
	      Color_Html="中"; 
	    break; 
	    case 3 : 
	      Wcolor="pwdck pwdck_Strong_c"; 
	      Mcolor="pwdck pwdck_Strong_c"; 
	      Scolor="pwdck pwdck_Strong_c pwdck_Strong_c_r"; 
	      Color_Html="强"; 
	    break; 
	    default : 
	      Wcolor="pwdck pwdck_c"; 
	      Mcolor="pwdck pwdck_c pwdck_f"; 
	      Scolor="pwdck pwdck_c pwdck_c_r"; 
	      Color_Html="无"; 
	    break; 
	} 
  	jQuery('#pwdck_Weak').attr("class",Wcolor); 
 	jQuery('#pwdck_Medium').attr("class",Mcolor); 
  	jQuery('#pwdck_Strong').attr("class",Scolor); 
  	jQuery('#pwdck_Medium').html(Color_Html); 
} 