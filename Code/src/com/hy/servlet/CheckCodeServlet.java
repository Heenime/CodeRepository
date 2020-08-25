package com.hy.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import redis.clients.jedis.Jedis;

public class CheckCodeServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//获取用户输入的手机号和验证码
		String phoneNo = req.getParameter("phone_no");
		String inputCode = req.getParameter("verify_code");
		if(null == phoneNo || null == inputCode) {
			return;
		}
		//拼接从Redis中获取验证码的key
		String codeKey ="phoneno:"+phoneNo+":code";
		//创建Jedis对象
		Jedis jedis = new Jedis("192.168.1.101",6379);
		//获取redis保存的验证码
		String redisCode = jedis.get(codeKey);
		//判断用户输入的验证码和从Redis中获取的验证码是否一致
		if(inputCode.equals(redisCode)) {
			jedis.del(codeKey);
			resp.getWriter().write("true");
		}
		//关闭jedis
		jedis.close();
	}

}
