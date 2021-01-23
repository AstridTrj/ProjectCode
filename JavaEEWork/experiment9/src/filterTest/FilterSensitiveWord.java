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
		//��requestת���󴫵�
		chain.doFilter(new NewRequest(req), resp);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		this.filterConfig = arg0;
	}
}

class NewRequest extends HttpServletRequestWrapper{
	//���дʻ㶨��
	private String[] words = {"SB", "��ƨ", "�Բ�"};
	public NewRequest(HttpServletRequest request) {
		super(request);
		// TODO Auto-generated constructor stub
	}
	//��дgetParameter�����˵����д�
	@Override
	public String getParameter(String name) {
		//���д��滻
		String com = super.getParameter(name);
		for (String word : words) {
			com = com.replace(word, "**");
		}
		//�����滻������Դ�
		return com;
	}
	
}
