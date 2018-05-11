package com.fenlibao.p2p.util.dm.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SendMessage {

	/**
	 * 模拟同步交易发送报文
	 */
	public static void main(String[] args) {
	
		URL postUrl = null;
		try {
			//content为加密签名后的报文
			String content ="001X11          00000256679C6D1C89B94D88F0F1D4053738614791A22320B4D3AABE19010233CBB010D571632D09A5B4B1EA13A99A89E28078388B6636852D9CE0BFB843403B160D87377D664B07C1003339D9E33A536202C4F0BAE9C1D3E166270D476B17963EA7B8CD586374A152B0F4F5083C69FB0B5D820D6223CA5959ED0175C3C52B6A390F0688<?xml version=\"1.0\" encoding=\"UTF-8\"?><Document><header><channelCode>P2P001</channelCode><channelFlow>OG012016045333cg1AlM</channelFlow><channelDate>20160115</channelDate><channelTime>120059</channelTime><encryptData></encryptData></header><body><TRANSCODE>OGW00041</TRANSCODE><XMLPARA>B5/peGrdJWHl7thgh5t0p3YuotQyoiscex3SrQazTusl0/+4mCZB4d4IWOAxPhDPyGDChLT457ltW4Q/E1qoe4TS4wBxQNgwWPx9QhYnU0UTVfCWiibXsGZVB8FQ3Z3VfMaIi1bA8WfvSbisM+0ilG/3T+JHJEwdcjc+NWyfxFjKRQEe6Mv3EA==</XMLPARA></body></Document>";
			
			postUrl = new URL("http://183.63.131.106:40015/extServiceTest/ghbExtService.do"); //URL请求地址
			HttpURLConnection urlcon =  (HttpURLConnection) postUrl.openConnection();
			int contentLength = content.getBytes().length;  //获取报文长度
			urlcon.setConnectTimeout(1000*15);
			urlcon.setReadTimeout(1000*60*2);
			urlcon.setRequestMethod("POST");  //post请求方式
			urlcon.setUseCaches(false);       //post请求不能使用缓存
			urlcon.setRequestProperty("Content-Length", String.valueOf(contentLength));
			urlcon.setRequestProperty("Content-Type", "application/xml; charset=UTF-8");
			urlcon.setDoInput(true);  //默认为true
			urlcon.setDoOutput(true); //默认为true
			//urlcon.connect();       //urlcon.getOutputStream()会隐含的进行connect();
			DataOutputStream output =  new DataOutputStream(urlcon.getOutputStream());
			output.writeBytes(content);
			output.flush();
			output.close();
			
			BufferedReader buffer =new BufferedReader(new InputStreamReader(urlcon.getInputStream()));
			StringBuffer strBuffer = new StringBuffer();
			String str = null;
			while((str=buffer.readLine())!=null){
				strBuffer.append(str);
			}
			System.out.println("返回报文为： "+strBuffer);
			buffer.close();
			urlcon.disconnect();
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			//请第三方公司去完善
		}
		
	}

}
