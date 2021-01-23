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
import javax.servlet.http.HttpServletResponse;


@WebFilter(filterName="LoginFilter", urlPatterns="/LoginCheck")
public class LoginFilter implements Filter {

	@Override
	public void destroy() {}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		//强制转换为Http中的请求和响应对象
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)resp;
		
		String user = request.getParameter("user");
		if (user == null) {
			System.out.println("no param user");
		}
		//判断用户是否具有登录权限user.equals("user")
		//暂定有权限用户为user
		if(!user.equals("user")){
			request.getRequestDispatcher("/unauthorized.jsp").forward(request, response);
		}
		else{
			chain.doFilter(request, response);
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {}

}
