package filterTest;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

@WebFilter(filterName="FilterSensitiveWord", urlPatterns="/SensitiveWord")
public class FilterSensitiveWord implements Filter {
	private FilterConfig filterConfig = null;
	@Override
	public void destroy() {}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse resp = (HttpServletResponse)response;
		//将request转换后传递
		chain.doFilter(new NewRequest(req), resp);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		this.filterConfig = arg0;
	}
}

class NewRequest extends HttpServletRequestWrapper{
	//敏感词汇定义
	private String[] words = {"SB", "狗屁", "卧槽"};
	public NewRequest(HttpServletRequest request) {
		super(request);
		// TODO Auto-generated constructor stub
	}
	//重写getParameter，过滤掉敏感词
	@Override
	public String getParameter(String name) {
		//敏感词替换
		String com = super.getParameter(name);
		for (String word : words) {
			com = com.replace(word, "**");
		}
		//返回替换后的属性串
		return com;
	}
	
}
