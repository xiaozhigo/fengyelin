package com.example.fengcommon.utils;

import com.alibaba.fastjson.JSON;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sherry.Wang
 * @Title:StringUtils
 * @Description:String工具类
 * @date 2017/1/12
 */
public class StringUtils {
	private static final Logger logger = LogManager.getLogger(StringUtils.class);

	private StringUtils() {
		throw new IllegalAccessError("Utility class");
	}

	public static boolean equals(String str1, String str2) {
		return str1 == null ? str2 == null : str1.equals(str2);
	}

	/**
	 * @param pStr
	 *            需要判断的值
	 * @Title:isEmpty
	 * @Description:是否为空
	 * @author Sherry.Wang
	 * @date 2017/1/12
	 */
	public static boolean isEmpty(String pStr) {
		return pStr == null || pStr.trim().length() == 0 || "null".equals(pStr);
	}

	public static boolean isNotEmpty(String pStr) {
		return !isEmpty(pStr);
	}

	public static String defaultIfBlank(String str, String defaultStr) {
		return isEmpty(str) ? defaultStr : str;
	}

	/**
	 * @param ch
	 *            需要转换的char
	 * @Title:charToByteAscii
	 * @Description:char转换成byte
	 * @author Sherry.Wang
	 * @date 2017/1/12
	 */
	public static byte charToByteAscii(char ch) {
		byte byteAscii = (byte) ch;
		return byteAscii;
	}

	/**
	 * @Title:copyModel
	 * @Description:拷贝属性
	 * @author Sherry.Wang
	 * @date 2017/1/12
	 */
	public static Object copyModel(Object classType, String[] parameters, Object o) {
		Object invoker = classType;
		Map<String, String> map = new HashMap<String, String>();
		try {
			Method method;
			Class<?> clazz = classType.getClass();
			Field[] fields = clazz.getDeclaredFields();// 根据Class对象获得属性 私有的也可以获得
			Field[] ofields = o.getClass().getDeclaredFields();// 根据Class对象获得属性 私有的也可以获得
			for (Field of : ofields) {
				map.put(of.getName(), of.getName());
			}
			for (String param : parameters) {
				for (Field f : fields) {
					if (f.getName().equals(param) && map.containsKey(param)) {
						String strFieldName = f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1);
						if (f.getType().getName().equals("java.lang.String")) {
							method = classType.getClass().getMethod("set" + strFieldName, new Class[] { String.class });
							Method m = o.getClass().getMethod("get" + strFieldName);
							method.invoke(invoker, new Object[] { (String) m.invoke(o) });
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("copyModel is error:::", e);
		}
		return invoker;
	}

	/**
	 * @Title:defIfEmpty
	 * @Description:判断为空的默认值
	 * @author Sherry.Wang
	 * @date 2017/1/12
	 */
	public static String defIfEmpty(String pStr, String pDefStr) {
		return isEmpty(pStr) ? pDefStr : pStr;
	}

	/**
	 * 对象转成json
	 * 
	 * @param o
	 * @return
	 */
	public static String toJson(Object o) {
        return JSON.toJSONString(o);
	}

	public static String format2RatioString(String value) {
		DecimalFormat formatter = new DecimalFormat("###############0.00");
		return (formatter.format(parseDouble(value)));
	}

	public static String format4RatioString(String value) {
		DecimalFormat formatter = new DecimalFormat("###############0.0000");
		return (formatter.format(parseDouble(value)));
	}
	public static String format5RatioString(String value) {
		DecimalFormat formatter = new DecimalFormat("###############0.00000");
		return (formatter.format(parseDouble(value)));
	}

	private static double parseDouble(String value) {
		try {
			return Double.parseDouble(value);
		} catch (Exception e) {
			logger.error("parseDouble 错误", e);
			return 0;
		}
	}

	/**
	 * 转换sql查询条件IN需要的值域
	 * @param sourceStr
	 * @param separator
	 * @return
	 */
	public static String valueInSql(String sourceStr,String separator){
		StringBuilder targetStrBuilder =  new StringBuilder();
		if(isNotEmpty(sourceStr)){
			String []strs = sourceStr.split(separator);
			Arrays.stream(strs).forEach(str->{
				targetStrBuilder.append("'");
				targetStrBuilder.append(str);
				targetStrBuilder.append("',");
			});
		}else {
			return null;
		}
		return isNotEmpty(targetStrBuilder.toString()) ? targetStrBuilder.toString().substring(0,targetStrBuilder.length()-1) : null;
	}
}
