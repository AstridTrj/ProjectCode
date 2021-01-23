package filterTest;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

@WebFilter(filterName="AFilter", urlPatterns="/form.jsp")
public class AFilter implements Filter {

	@Override
	public void destroy() {}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain chain)
			throws IOException, ServletException {
		System.out.println("Before Filter A ...");
		chain.doFilter(arg0, arg1);
		System.out.println("After Filter A ...");
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {}

}
