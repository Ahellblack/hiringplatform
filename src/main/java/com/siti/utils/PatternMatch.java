package com.siti.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternMatch {

    /**
     * 手机号正则表达式验证
     *
     * @param phoneNum 　手机号
     */
    public static boolean matchPhone(String phoneNum) {
        String regExp = "^((13[0-9])|(14[0-9])|(15[^4,\\D])|(16[0-9])|(17[0-9])|(18[0-9])|(19[0-9]))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(phoneNum);
        return m.find();
    }

    /**
     * 正则表达式判断包含中文/英文
     */
    public static boolean matchCNorEn(String content) {
        String regExp = "[\\u4e00-\\u9fa5]+";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(content);
        return m.find();
    }

    /**
     * 判断路径格式是否正确
     */
    public static boolean mathLocalPath(String path) {
        String regExp = "^[A-z]:\\\\(.+?\\\\)*$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(path);
        return m.find();
    }

    public static boolean matchEmailAddr(String emailAddr) {
        String regExp = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(emailAddr);
        return m.find();
    }

	/*@Test
	public void matchPhone(*//*String phoneNum*//*){
		String regExp = "^((13[0-9])|(15[^4,\\D])|(18[0-9])|(17[0-9]))\\d{8}$";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher("15900848683");
		System.out.println(m.find());
		//return m.find();
	}*/
}
