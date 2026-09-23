package cn.seehoo.spg.bizcom.request;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;

/**
 * @author chenjun
 * 
 * http请求
 */
public class HttpRequestPackage extends HttpServletRequestWrapper {
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequestPackage.class);
	/** 请求包体 */
	private String reqeustBody;
	/** 请求头 */
	private Map<String, String> requestHeaders = new HashMap<>();
    /** form字段*/
	private Map<String, String> form = new HashMap<>();
	
	public HttpRequestPackage(HttpServletRequest request) {
		super(request);
		try {
			// 获取头信息
			Enumeration<String> enu = request.getHeaderNames();
			while(enu.hasMoreElements()) {
				String name = enu.nextElement();
				String value = request.getHeader(name);
				requestHeaders.put(name, value);
				LOGGER.debug(">>>>>>[请求包装],请求头名称={},请求头值={}", name, value);
			}
			// 请求表单
			Enumeration<String> paramEnu = request.getParameterNames();
			while(paramEnu.hasMoreElements()) {
				String paramName = paramEnu.nextElement();
				String paramValue = request.getParameter(paramName);
				form.put(paramName, paramValue);
				LOGGER.debug(">>>>>>[请求包装],请求表单名称={},请求表单值={}", paramName, paramValue);
			}
			// 获取请求体信息
			byte[] bytes = IoUtil.readBytes(request.getInputStream(), true);
			String body = new String(bytes, "UTF-8");
			this.reqeustBody = body;
			LOGGER.debug(">>>>>>[请求包装],请求体={}", this.reqeustBody);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}
	
	public String getReqeustBody() {
		return this.reqeustBody;
	}
	public Map<String, String> getRequestHeaders() {
		return requestHeaders;
	}
	
	// 设置请求头
	public void setHeader(String name, String value) {
		this.requestHeaders.put(name, value);
	}

	@Override
	public String getHeader(String name) {
		String value = this.requestHeaders.get(name);
		if (StrUtil.isNotEmpty(value)) {
			return value;
		}
		return this.requestHeaders.get(name.toLowerCase());
	}

	@Override
	public Enumeration<String> getHeaders(String name) {
		return super.getHeaders(name);
	}

	@Override
	public Enumeration<String> getHeaderNames() {
		Enumeration<String> enu = new Enumeration<String>() {
			Iterator<String> it = requestHeaders.keySet().iterator();
			
			@Override
			public boolean hasMoreElements() {
				return it.hasNext();
			}

			@Override
			public String nextElement() {
				return it.next();
			}
		};
		return enu;
	}

	@Override
	public String getParameter(String name) {
		String value = form.get(name);
		return value;
	}
	
	@Override
	public Map<String, String[]> getParameterMap() {
		return super.getParameterMap();
	}

	@Override
	public Enumeration<String> getParameterNames() {
		return super.getParameterNames();
	}

	@Override
	public String[] getParameterValues(String name) {
		String value = form.get(name);
		if (StrUtil.isNotEmpty(value)) {
			return new String[] {value};
		}
		// 处理json
		if (reqeustBody.indexOf("{") != -1) {
			JSONObject json = JSON.parseObject(reqeustBody);
			value = json.getString(name);
			return new String[] {value};
		}
		// 处理名值对
		String[] array = reqeustBody.split("&");
		for (String a : array ) {
			String[] name$value = a.split("=");
			if (name.equals(name$value[0])) {
				return new String[] {name$value[1]};
			}
		}
		return null;
	}
	
	@Override
	public ServletInputStream getInputStream() throws IOException {
		
		return new ServletInputStream() {
			String body = reqeustBody;
			ByteArrayInputStream in = new ByteArrayInputStream(body.getBytes("UTF-8"));
			
			@Override
			public int read() throws IOException {
				return in.read();
			}
			
			@Override
			public void setReadListener(ReadListener listener) {
				
			}
			
			@Override
			public boolean isReady() {
				return true;
			}
			
			@Override
			public boolean isFinished() {
				return true;
			}
		};
	}
}
