package com.winning.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.junit.Test;

public class ParseHtmToFile {

	@Test
	public void test() throws Exception {
		File folders = new File("H:/work/120ask");
		List<Map<String, String>> list = null;

		for (File htmlFolder : folders.listFiles()) {
			File[] files = htmlFolder.listFiles();
			list = new ArrayList<Map<String, String>>();

			list = generateListData120Ask(files);
			writeObject("H:/work/120ask_data/" + htmlFolder.getName() + ".data",
					list);
		}

	}

	public static List<Map<String, String>> generateListData120Ask(File[] files)
			throws ParserException {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String, String> map = null;
		String key = null;
		String value = null;
		String []temp = null;
		String []temp1 = null;
		for (File file : files) {
			map = new HashMap<String, String>();
			String szContent = openFile(file);
			Parser parser = new Parser(szContent);
			NodeFilter tagNameFilter = new TagNameFilter("dl");
			NodeList nodes = parser.extractAllNodesThatMatch(tagNameFilter);
			Node dlNode = (Node) nodes.elementAt(0);
			NodeList dNodes = dlNode.getChildren();
			for (int i = 0; i < dNodes.size(); i++) {
				if (StringUtils.isNotBlank(dNodes.elementAt(i).toHtml())) {
					if (key == null) {
						key = StringUtils.trim(dNodes.elementAt(i)
								.toPlainTextString());
					} else {
						
						value = StringUtils.trim(dNodes.elementAt(i)
								.toPlainTextString());
						//处理商品名称和通用名称
						if(key.indexOf("药品名称:")>-1){
							if(value!=null){
								temp = value.split("商品名称：");
								if(temp.length>1){
									temp1 = temp[1].split("通用名称：");
									if(temp1.length>1){
										key = "商品名称：";
										map.put("商品名称：",StringUtils.trim(temp1[0]));
										System.out.println(key+map.get(key));
										key = "通用名称：";
										map.put("通用名称：",StringUtils.trim(temp1[1]));
										System.out.println(key+map.get(key));
									}
								}
							}
						}else{
							map.put(key,value);
							System.out.println(key+map.get(key));
						}
						
						key = null;
						
					}
				}
				
				
			}
			
			list.add(map);
		}

		return list;
	}

	public static List<Map<String, String>> generateListData(File[] files)
			throws ParserException {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String, String> map = null;
		String key = null;
		String value = null;
		for (File file : files) {
			// String szContent = openFile(file);
			// Parser parser = new Parser(szContent);
			// NodeFilter tagNameFilter = new TagNameFilter("div");
			// NodeFilter attributeFilter = new
			// HasAttributeFilter("class","tab_c");
			// NodeFilter filter = new AndFilter(tagNameFilter,
			// attributeFilter);
			// NodeList nodes = parser.extractAllNodesThatMatch(filter);
			// Node divNode = (Node) nodes.elementAt(0);
			//
			// Node ulNode = divNode.getChildren().elementAt(1);
			// Map<String, String> map = new LinkedHashMap<String, String>();
			// String key = null;
			// NodeList liList = ulNode.getChildren();
			// for(int i=0;i<liList.size();i++){
			// if(StringUtils.isNotBlank(liList.elementAt(i).toHtml())){
			// if(key == null){
			// key = StringUtils.trim(liList.elementAt(i).toPlainTextString());
			// }else{
			// map.put(key,
			// StringUtils.trim(liList.elementAt(i).toPlainTextString()));
			// key = null;
			// }
			// }
			// }
			map = new HashMap<String, String>();
			String szContent = openFile(file);
			Parser parser = new Parser(szContent);
			NodeFilter tagNameFilter = new TagNameFilter("dl");
			NodeFilter attributeFilter = new HasAttributeFilter("class", "dl_h");
			NodeFilter filter = new AndFilter(tagNameFilter, attributeFilter);
			NodeList nodes = parser.extractAllNodesThatMatch(filter);

			Node[] dlNodes = new Node[nodes.size()];
			nodes.copyToNodeArray(dlNodes);
			Node node = null;
			for (int n = 0; n < dlNodes.length / 2; n++) {// 去掉重复的 display =
															// none 的数据
				node = dlNodes[n];
				NodeList dlChildren = node.getChildren();
				if (null != dlChildren && dlChildren.size() > 2) {
					key = StringUtils.trim(dlChildren.elementAt(1)
							.toPlainTextString());
					value = StringUtils.trim(dlChildren.elementAt(3)
							.toPlainTextString());
				}
				System.out.println(key + ":" + value);
				map.put(key, value);
			}

			System.out.println(file.getName() + "===========================");
			list.add(map);
		}
		return list;
	}

	public static void writeObject(String filePath, Object obj) {
		FileOutputStream os = null;
		ObjectOutputStream oos = null;
		try {
			os = new FileOutputStream(filePath);
			oos = new ObjectOutputStream(os);
			oos.writeObject(obj);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (oos != null)
					oos.close();
				if (os != null)
					os.close();
			} catch (IOException e) {
			}
		}
	}

	public static String openFile(File file) {
		try {
			BufferedReader bis = new BufferedReader(new InputStreamReader(
					new FileInputStream(file)));
			String szContent = "";
			String szTemp;

			while ((szTemp = bis.readLine()) != null) {
				szContent += szTemp + "\n";
			}
			bis.close();
			return szContent;
		} catch (Exception e) {
			return "";
		}
	}

}
