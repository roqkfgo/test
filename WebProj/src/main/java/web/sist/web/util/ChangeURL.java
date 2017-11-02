package web.sist.web.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ChangeURL {
	public static String getURLformat(String oriName){
		String newName = null;
		try {
			newName = URLEncoder.encode(oriName, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			System.out.println("url濡� �븳湲� 蹂�寃쎌떆 �삤瑜� 諛쒖깮");
			e.printStackTrace();
		}
		return newName;
	}
	
	public static String getISO88591(String oriName){
		String newName = null;
		try {
			newName = new String(oriName.getBytes(), "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			System.out.println("ISO-8859-1 蹂��솕以� �삤瑜� 諛쒖깮");
			e.printStackTrace();
		}
		return newName;
	}
}
