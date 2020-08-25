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
		//��ȡ�û�������ֻ��ź���֤��
		String phoneNo = req.getParameter("phone_no");
		String inputCode = req.getParameter("verify_code");
		if(null == phoneNo || null == inputCode) {
			return;
		}
		//ƴ�Ӵ�Redis�л�ȡ��֤���key
		String codeKey ="phoneno:"+phoneNo+":code";
		//����Jedis����
		Jedis jedis = new Jedis("192.168.1.101",6379);
		//��ȡredis�������֤��
		String redisCode = jedis.get(codeKey);
		//�ж��û��������֤��ʹ�Redis�л�ȡ����֤���Ƿ�һ��
		if(inputCode.equals(redisCode)) {
			jedis.del(codeKey);
			resp.getWriter().write("true");
		}
		//�ر�jedis
		jedis.close();
	}

}
