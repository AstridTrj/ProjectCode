package filter;

import java.io.IOException;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

@WebFilter(filterName="EncodingFilter", urlPatterns="/*", 
						dispatcherTypes={DispatcherType.REQUEST, DispatcherType.FORWARD})
public class EncodingFilter implements Filter {

	@Override
	public void destroy() {}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		//π˝¬À∆˜£¨ ±‡¬Î…Ë÷√
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

}
