package com.hy.servlet;

import java.io.IOException;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import redis.clients.jedis.Jedis;

public class SendCodeServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String phoneNo = req.getParameter("phone_no");
		if(phoneNo == null) {
			return;
		}
		//拼接向Redis中保存验证码时的key
		String codeKey ="phoneno:"+phoneNo+":code";
		//拼接向Redis中保存计数器的key
		String countKey = "phoneno:"+phoneNo+":count";
		System.out.println(codeKey);
		//创建Jedis对象
		Jedis jedis = new Jedis("192.168.1.101",6379);
		String redisCount = jedis.get(countKey);
		//判断计数器的值
		if(redisCount == null) {
			//证明是第一次发送验证码，此时在Redis中保存计数器的值为1，并设置有效时间为24小时，做到每天最多只能发三次验证码
			jedis.setex(countKey, 60*60*24, "1");
		}else if("3".equals(redisCount)) {
			resp.getWriter().write("limit");
			jedis.close();
			return;
		}else {
			//将redis中的计数器的值加一
			jedis.incr(countKey);
		}
		//生成6位验证码
		String code=getCode(6);
		System.out.println(code);
		
		//向Redis中保存
		jedis.setex(codeKey, 120,code);
		//给浏览器响应一个字符串true
		resp.getWriter().write("true");
		//关闭jedis
		jedis.close();
	}
	
	//随机生成验证码的的方法
	private String getCode(int len) {
		String code="";
		for(int i=0;i<len;i++) {
			int rand = new Random().nextInt(10);
			code+=rand;
		}
		return code;
	}

}
