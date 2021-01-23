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
		//��ʽһ���������Ӧ�����ֱ������
		//�������ݵı�������
		//request.setCharacterEncoding("utf-8");
		//ҳ����Ӧ���ݵı�������
		//response.setContentType("text/html;charset=utf-8");
		
		//��ʽ����ͨ����ȡ�ֲ�������������
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
