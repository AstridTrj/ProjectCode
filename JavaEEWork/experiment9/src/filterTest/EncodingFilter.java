package filterTest;

import java.io.IOException;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;

@WebFilter(filterName="EncodingFilter", urlPatterns="/*",
				initParams={@WebInitParam(name="encoding", value="utf-8"),
									@WebInitParam(name="textType", value="text/html;charset=utf-8")
								},
				dispatcherTypes={DispatcherType.REQUEST, DispatcherType.FORWARD})
public class EncodingFilter implements Filter {
	private FilterConfig filterConfig = null;
	
	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		//方式一：请求和响应编码的直接设置
		//请求内容的编码设置
		//request.setCharacterEncoding("utf-8");
		//页面响应内容的编码设置
		//response.setContentType("text/html;charset=utf-8");
		
		//方式二：通过获取局部参数进行设置
		String encoding = this.filterConfig.getInitParameter("encoding");
		request.setCharacterEncoding(encoding);
		String textType = this.filterConfig.getInitParameter("textType");
		response.setContentType(textType);
		
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		this.filterConfig = arg0;
	}

}
