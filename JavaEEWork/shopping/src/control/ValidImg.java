package control;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name="ValidImg", urlPatterns="/ValidImg")
public class ValidImg extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		GenValidCode validCode = new GenValidCode();
		BufferedImage image = validCode.getValidImage();
		String code = validCode.getValidCode();
		req.getSession().setAttribute("validcode", code);
		validCode.output(image, resp.getOutputStream());
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.doGet(req, resp);
	}
}
