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
		//ƴ����Redis�б�����֤��ʱ��key
		String codeKey ="phoneno:"+phoneNo+":code";
		//ƴ����Redis�б����������key
		String countKey = "phoneno:"+phoneNo+":count";
		System.out.println(codeKey);
		//����Jedis����
		Jedis jedis = new Jedis("192.168.1.101",6379);
		String redisCount = jedis.get(countKey);
		//�жϼ�������ֵ
		if(redisCount == null) {
			//֤���ǵ�һ�η�����֤�룬��ʱ��Redis�б����������ֵΪ1����������Чʱ��Ϊ24Сʱ������ÿ�����ֻ�ܷ�������֤��
			jedis.setex(countKey, 60*60*24, "1");
		}else if("3".equals(redisCount)) {
			resp.getWriter().write("limit");
			jedis.close();
			return;
		}else {
			//��redis�еļ�������ֵ��һ
			jedis.incr(countKey);
		}
		//����6λ��֤��
		String code=getCode(6);
		System.out.println(code);
		
		//��Redis�б���
		jedis.setex(codeKey, 120,code);
		//���������Ӧһ���ַ���true
		resp.getWriter().write("true");
		//�ر�jedis
		jedis.close();
	}
	
	//���������֤��ĵķ���
	private String getCode(int len) {
		String code="";
		for(int i=0;i<len;i++) {
			int rand = new Random().nextInt(10);
			code+=rand;
		}
		return code;
	}

}
