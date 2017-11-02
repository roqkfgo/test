package web.sist.web.util;

import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;


public class DownloadController implements Controller {

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 1) 寃쎈줈紐낃낵 �뙆�씪紐� �뙆�씪硫뷀꽣濡� 諛쏄린
		request.setCharacterEncoding("UTF-8");    // �씠嫄� 諛섎뱶�떆 �븘�슂
		String path = request.getParameter("p");
		String fname = request.getParameter("f");
		System.out.println("fname="+fname);


		// 2) 2媛� �뜑�빐�꽌 由ъ뼹�뙣�뒪 �뼸湲�
		String urlPath = path + "/" + fname;
		System.out.println("urlPath="+urlPath);
		String realPath = request.getServletContext().getRealPath(urlPath);
		System.out.println("realPath="+realPath);

		// 3) �뿤�뜑 �꽕�젙 
		//response.setHeader("Content-Disposition", "attachment;filename="+fname);
		// 9) �븳湲� �씠由꾩쓽 泥⑤��뙆�씪�� �삤瑜� 諛쒖깮 -- �븳湲� �씠由� 源⑥쭚 蹂�寃� �븘�슂
		String newfname = new String(fname.getBytes(), "ISO-8859-1");
		response.setHeader("Content-Disposition", "attachment;filename="+newfname);
		// 4) �씤�뭼�뒪�봽由� �뿴湲�
		FileInputStream fis = new FileInputStream(realPath);
		// 5) �꽌釉붾┸ �븘�썐�뭼 �뒪�듃由� �뿴湲�
		ServletOutputStream sout = response.getOutputStream();

		// 6) �뙆�씪 蹂듭궗 諛⑹떇�쑝濡� �떎�슫濡쒕뱶
		byte[] buf = new byte[1024];
		int readData=0;
		while((readData=fis.read(buf)) != -1){
			sout.write(buf);
		}
		fis.close();
		sout.close();


		/*ModelAndView mv = new ModelAndView();
		mv.setViewName("void:");    �씠�젣 �씠嫄� �븞�맖 
		===> return null;   �씠硫� �맖*/
		return null;
	}

}
