package com.sist.web.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;  /*@Autowired*/
import org.springframework.stereotype.Controller;				/*@Controller*/
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;  /*@RequestMapping()*/
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import com.sist.web.dao.NoticeDAO;
import com.sist.web.vo.Notice;

import web.sist.web.util.ChangeURL;

@Controller
@RequestMapping("/customer/*")
public class CustomerController {

	@Autowired
	private NoticeDAO ndao;
	/*@Autowired     dispatcher-servlet.xml���� ��ü�� ������Ƿ� ���Թ���� ���� -> ���ο� ���Թ�� : @Autowired
	public void setNdao(NoticeDAO ndao) {
		this.ndao = ndao;
	}*/


	@RequestMapping(value={"notice.do"}, method=RequestMethod.GET)
	public String notices(String pg, String f, String q, Model model){

		int ipg;
		if(pg!=null && !pg.equals("") && !pg.equals("null")){
			ipg = Integer.parseInt(pg);
		}else{
			ipg=1;
		}
		System.out.println("pg : "+pg);

		if(f==null || f.equals("") || f.equals("null") ){
			f="TITLE";
		}

		String urlquery="";;
		if(q==null){
			q="";
		}else{
			//urlquery = URLEncoder.encode(q, "UTF-8");
			urlquery = ChangeURL.getURLformat(q);
		}

		int sPage = ipg - (ipg - 1)%5;

		//NoticeDAO dao = new NoticeDAO();
		List<Notice> list = ndao.getNotices(ipg, f, q);
		int seqCount = ndao.getSeqCount(f, q);

		int finalPage = seqCount/10 + (seqCount%10==0?0:1);

		/*6) ���� ���̻� ModelAndView ������� ���� ==> Model Ŭ������  ���ڷ� �޾Ƽ� ���*/ 
		/*ModelAndView mv = new ModelAndView();
		mv.addObject("list", list);
		mv.addObject("pg", pg);
		mv.addObject("field", field);
		mv.addObject("query", query);
		mv.addObject("urlquery", urlquery);
		mv.addObject("sPage", sPage);
		mv.addObject("finalPage", finalPage);
		mv.setViewName("notice.jsp");*/

		model.addAttribute("list", list);
		model.addAttribute("pg", ipg);
		model.addAttribute("field", f);
		model.addAttribute("query", q);
		model.addAttribute("urlquery", urlquery);
		model.addAttribute("sPage", sPage);
		model.addAttribute("finalPage", finalPage);

		System.out.println("NoticeController ����");

		return "notice.jsp";
	}


	@RequestMapping(value={"noticeDetail.do"}, method=RequestMethod.GET)
	public String noticeDetail(String seq, String pg, String f, String q, String hitUp, Model model, HttpServletRequest request){
		System.out.println("NoticeDetailController ����");

		String urlquery="";
		if(q==null){
			q="";
		}else{
			urlquery = ChangeURL.getURLformat(q);
			System.out.println("Detail_urlquery="+urlquery);
		}

		//ModelAndView mv = new ModelAndView();
		HttpSession session = request.getSession();      /// HttpSession ��ü ��������
		System.out.println(session.getAttribute("mid"));
		if(session.getAttribute("mid")==null ){
			request.getSession().setAttribute("returnURL", "/customer/noticeDetail.do?seq="+seq+"&pg="+pg+"&f="+f+"&q="+urlquery+"&hitUp=on");
			//mv.setViewName("redirect:../joinus/login.do");
			return "redirect:../joinus/login.do";
		}else{
			String mid = (String)session.getAttribute("mid");
			System.out.println("mid = "+ mid);
			// ��ȸ�� �ø��� ==> hitUp(seq) �Լ� �����, hitUp �Ķ���� ���� ����
			//NoticeDAO dao = new NoticeDAO();
			if(hitUp!=null && !hitUp.equals("")){
				ndao.hitUp(seq);
			}

			Notice n = ndao.getNotice(seq);

			model.addAttribute("n", n);
			model.addAttribute("pg", pg);
			model.addAttribute("field", f);
			model.addAttribute("query", q);
			model.addAttribute("urlquery", urlquery);
			/// �ѱ� �����̸� �ּ�â�� �� �� �ִ� ���ڼ����� ����
			if(n.getFileSrc() != null){
				String urlFileName = ChangeURL.getURLformat(n.getFileSrc());
				request.setAttribute("urlFileName", urlFileName);
			}

			//mv.setViewName("noticeDetail.jsp");
			return "noticeDetail.jsp";
		}
	}

	@RequestMapping(value={"noticeDelProc.do"}, method=RequestMethod.GET)
	public String noticeDel(String seq, String pg, String f, String q) {

		String urlquery="";
		if(q==null){
			q="";
		}else{
			urlquery = ChangeURL.getURLformat(q);
			System.out.println("delProc_urlquery="+urlquery);
		}

		int af = ndao.delNotice(seq);
		System.out.println("af = "+ af);

		if(af == 1){
			System.out.println("�Խñ� ���� ����");
			return "redirect:notice.do?pg="+pg+"&f="+f+"&q="+urlquery;
		}else{
			System.out.println("�Խñ� ���� ����");
			return null;
		}
	}


	@RequestMapping(value={"noticeEdit.do"}, method=RequestMethod.GET)
	public String noticeEdit(String seq, String pg, String f, String q, Model model) {

		String urlquery="";
		if(q==null){
			q="";
		}else{
			urlquery = ChangeURL.getURLformat(q);
			System.out.println("edit_urlquery="+urlquery);
		}

		Notice n = ndao.getNotice(seq);

		model.addAttribute("n", n);
		model.addAttribute("pg", pg);
		model.addAttribute("field", f);
		model.addAttribute("query", q);
		model.addAttribute("urlquery", urlquery);

		return "noticeEdit.jsp";
	}


	@RequestMapping(value={"noticeEdit.do"}, method=RequestMethod.POST)  /* �߿� : �ش� jsp���� ��û uri���� �ʿ�*/
	public String noticeEdit(String seq, String title, String content, String pg, String f, String q) {
		/*String seq, String title, String content ==> Notice n ���� ���� ����
		 * ==> ���� : �ѱ��� ���� request.setch... �ȸ��� ==> ���ͷ� �ذ��ؾ� ��
		 * */
		
		/*String seq = request.getParameter("seq");
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String pg = request.getParameter("pg");
		String field = request.getParameter("f");
		String query = request.getParameter("q");*/
		System.out.println("delProc_query = "+q);

		String urlquery="";
		if(q==null){
			q="";
		}else{
			urlquery = ChangeURL.getURLformat(q);
			System.out.println("delProc_urlquery = "+urlquery);
		}

		Notice n = new Notice();   /* Notice �� �����͸� �����ϴ� ��ü�� ���� �����ǰ� �������Ƿ� �ʿ��Ҷ����� �����ϰ� �����°� �� ���� */
		n.setSeq(seq);
		n.setTitle(title);
		n.setContent(content);

		int af = ndao.updateNotice(n);
		if(af==1){
			System.out.println("�Խñ� ������ �����Ͽ����ϴ�.");
			return "redirect:noticeDetail.do?seq="+seq+"&pg="+pg+"&f="+f+"&q="+urlquery;
		}else{
			System.out.println("�Խñ� ������ �����Ͽ����ϴ�.");
			return null;
		}
	}


	@RequestMapping(value={"noticeReg.do"}, method=RequestMethod.GET)
	public String noticeReg(String seq, String pg, String f, String q, Model model, HttpServletRequest request) {

		HttpSession session = request.getSession();
		if(session.getAttribute("mid")==null ){
			session.setAttribute("returnURL", "/customer/noticeReg.do");
			return "redirect:../joinus/login.do";
		}else{
			System.out.println("Reg_query = "+q);

			String urlquery="";
			if(q==null){
				q="";
			}else{
				urlquery = ChangeURL.getURLformat(q);
				System.out.println("Reg_urlquery = "+urlquery);
			}

			model.addAttribute("pg", pg);
			model.addAttribute("field", f);
			model.addAttribute("query", q);
			model.addAttribute("urlquery", urlquery);

			return "noticeReg.jsp";
		}
	}


	@RequestMapping(value={"noticeReg.do"}, method=RequestMethod.POST)
	public String noticeReg(HttpServletRequest request) throws IOException{

		String path = "/customer/upload";
		String realPath = request.getServletContext().getRealPath(path);
		System.out.println("realPath="+realPath);

		MultipartRequest mulReq = new MultipartRequest(request, realPath, 10*1024*1024, "UTF-8", new DefaultFileRenamePolicy());

		// 4) fileSrc : ���ϸ��� vo�� �־ db�� ���� -- vo����, notice ���̺� fileSrc �÷� �߰�
		String fileSrc = mulReq.getFilesystemName("file");      // ������ ����Ǵ� �̸� (���� �̸��� �����ÿ� �������� �̸��� �ٸ��� ����)
		String OriginalFileName = mulReq.getOriginalFileName("file"); // �ִ� ���� ���� �̸�
		System.out.println("fileScr="+fileSrc);
		System.out.println("OriginalFileName="+OriginalFileName);


		// 5) ���̻� request ���� �Ķ���͸� ���ü� ���� -> ������� MultipartRequest �� �Ѱ��� ���� mulReq���� �����;���
		String mid = (String) request.getSession().getAttribute("mid");
		String title = mulReq.getParameter("title");
		String content =  mulReq.getParameter("content");

		System.out.println("mid = "+ mid);
		System.out.println("title = "+ title);
		System.out.println("content = "+ content);

		Notice n = new Notice();
		n.setWriter(mid);
		n.setTitle(title);
		n.setContent(content);
		// 6) vo�� �߰� ==> ��� �÷������� FILESRC �߰� �ʿ� ==> insertNotice�Լ��� ���� �ʿ� ==> getNotice(), getNotices() �Լ��� fileSrc �߰�
		n.setFileSrc(fileSrc);

		//NoticeDAO dao = new NoticeDAO();
		int af = ndao.insertNotice(n);
		ModelAndView mv = new ModelAndView();
		if(af==1){
			System.out.println("�Խñ� ��� ����");
			//mv.setViewName("redirect:notice.do");
			return "redirect:notice.do";
		}else{
			System.out.println("�Խñ� ��� ����");
			return null;
		}
	}


	@RequestMapping(value={"download.do"}, method=RequestMethod.GET)
	public String download(String p, String f, HttpServletRequest request, HttpServletResponse response){
		// 1) ��θ�� ���ϸ� �Ķ���ͷ� �ޱ�
		//request.setCharacterEncoding("UTF-8");    // �̰� �ݵ�� �ʿ�
		//String path = request.getParameter("p");
		//String fname = request.getParameter("f");
		System.out.println("fname="+f);


		// 2) 2�� ���ؼ� �����н� ���
		String urlPath = p + "/" + f;
		System.out.println("urlPath="+urlPath);
		String realPath = request.getServletContext().getRealPath(urlPath);
		System.out.println("realPath="+realPath);

		// 3) ��� ���� 
		//response.setHeader("Content-Disposition", "attachment;filename="+fname);
		// 9) �ѱ� �̸��� ÷�������� ���� �߻� -- �ѱ� �̸� ���� ���� �ʿ�
		String newfname;
		try {
			newfname = new String(f.getBytes(), "ISO-8859-1");
			response.setHeader("Content-Disposition", "attachment;filename="+newfname);
			// 4) ��ǲ������ ����
			FileInputStream fis = new FileInputStream(realPath);
			// 5) ���� �ƿ�ǲ ��Ʈ�� ����
			ServletOutputStream sout = response.getOutputStream();

			// 6) ���� ���� ������� �ٿ�ε�
			byte[] buf = new byte[1024];
			int readData=0;
			while((readData=fis.read(buf)) != -1){
				sout.write(buf);
			}
			fis.close();
			sout.close();
		} catch (IOException e) {
			System.out.println("÷�� ���� �ٿ�ε��� �ٿ�ε� ��Ʈ�ѷ����� ���� �߻�");
			e.printStackTrace();
		}
		
		return null;
	}






























}
