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
	/*@Autowired     dispatcher-servlet.xml에서 본체가 사라지므로 주입방법이 없음 -> 새로운 주입방법 : @Autowired
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

		/*6) 이제 더이상 ModelAndView 사용하지 않음 ==> Model 클래스를  인자로 받아서 사용*/ 
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

		System.out.println("NoticeController 종료");

		return "notice.jsp";
	}


	@RequestMapping(value={"noticeDetail.do"}, method=RequestMethod.GET)
	public String noticeDetail(String seq, String pg, String f, String q, String hitUp, Model model, HttpServletRequest request){
		System.out.println("NoticeDetailController 시작");

		String urlquery="";
		if(q==null){
			q="";
		}else{
			urlquery = ChangeURL.getURLformat(q);
			System.out.println("Detail_urlquery="+urlquery);
		}

		//ModelAndView mv = new ModelAndView();
		HttpSession session = request.getSession();      /// HttpSession 객체 가져오기
		System.out.println(session.getAttribute("mid"));
		if(session.getAttribute("mid")==null ){
			request.getSession().setAttribute("returnURL", "/customer/noticeDetail.do?seq="+seq+"&pg="+pg+"&f="+f+"&q="+urlquery+"&hitUp=on");
			//mv.setViewName("redirect:../joinus/login.do");
			return "redirect:../joinus/login.do";
		}else{
			String mid = (String)session.getAttribute("mid");
			System.out.println("mid = "+ mid);
			// 조회수 올리기 ==> hitUp(seq) 함수 만들기, hitUp 파라메터 만들어서 전달
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
			/// 한글 파일이름 주소창에 들어갈 수 있는 문자셋으로 변경
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
			System.out.println("게시글 삭제 성공");
			return "redirect:notice.do?pg="+pg+"&f="+f+"&q="+urlquery;
		}else{
			System.out.println("게시글 삭제 실패");
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


	@RequestMapping(value={"noticeEdit.do"}, method=RequestMethod.POST)  /* 중요 : 해당 jsp에서 요청 uri변경 필요*/
	public String noticeEdit(String seq, String title, String content, String pg, String f, String q) {
		/*String seq, String title, String content ==> Notice n 으로 변경 가능
		 * ==> 주의 : 한글이 깨짐 request.setch... 안먹임 ==> 필터로 해결해야 함
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

		Notice n = new Notice();   /* Notice 는 데이터를 저장하는 객체고 자주 생성되고 없어지므로 필요할때마다 생성하고 버리는게 더 좋다 */
		n.setSeq(seq);
		n.setTitle(title);
		n.setContent(content);

		int af = ndao.updateNotice(n);
		if(af==1){
			System.out.println("게시글 수정에 성공하였습니다.");
			return "redirect:noticeDetail.do?seq="+seq+"&pg="+pg+"&f="+f+"&q="+urlquery;
		}else{
			System.out.println("게시글 수정에 실패하였습니다.");
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

		// 4) fileSrc : 파일명을 vo에 넣어서 db에 저장 -- vo수정, notice 테이블에 fileSrc 컬럼 추가
		String fileSrc = mulReq.getFilesystemName("file");      // 서버에 저장되는 이름 (같은 이름이 있을시에 원본파일 이름과 다를수 있음)
		String OriginalFileName = mulReq.getOriginalFileName("file"); // 애는 원본 파일 이름
		System.out.println("fileScr="+fileSrc);
		System.out.println("OriginalFileName="+OriginalFileName);


		// 5) 더이상 request 에서 파라메터를 얻어올수 없음 -> 제어권을 MultipartRequest 에 넘겼음 따라서 mulReq에서 가져와야함
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
		// 6) vo에 추가 ==> 디비에 컬럼명으로 FILESRC 추가 필요 ==> insertNotice함수도 변경 필요 ==> getNotice(), getNotices() 함수에 fileSrc 추가
		n.setFileSrc(fileSrc);

		//NoticeDAO dao = new NoticeDAO();
		int af = ndao.insertNotice(n);
		ModelAndView mv = new ModelAndView();
		if(af==1){
			System.out.println("게시글 등록 성공");
			//mv.setViewName("redirect:notice.do");
			return "redirect:notice.do";
		}else{
			System.out.println("게시글 등록 실패");
			return null;
		}
	}


	@RequestMapping(value={"download.do"}, method=RequestMethod.GET)
	public String download(String p, String f, HttpServletRequest request, HttpServletResponse response){
		// 1) 경로명과 파일명 파라메터로 받기
		//request.setCharacterEncoding("UTF-8");    // 이거 반드시 필요
		//String path = request.getParameter("p");
		//String fname = request.getParameter("f");
		System.out.println("fname="+f);


		// 2) 2개 더해서 리얼패스 얻기
		String urlPath = p + "/" + f;
		System.out.println("urlPath="+urlPath);
		String realPath = request.getServletContext().getRealPath(urlPath);
		System.out.println("realPath="+realPath);

		// 3) 헤더 설정 
		//response.setHeader("Content-Disposition", "attachment;filename="+fname);
		// 9) 한글 이름의 첨부파일은 오류 발생 -- 한글 이름 깨짐 변경 필요
		String newfname;
		try {
			newfname = new String(f.getBytes(), "ISO-8859-1");
			response.setHeader("Content-Disposition", "attachment;filename="+newfname);
			// 4) 인풋스프림 열기
			FileInputStream fis = new FileInputStream(realPath);
			// 5) 서블릿 아웃풋 스트림 열기
			ServletOutputStream sout = response.getOutputStream();

			// 6) 파일 복사 방식으로 다운로드
			byte[] buf = new byte[1024];
			int readData=0;
			while((readData=fis.read(buf)) != -1){
				sout.write(buf);
			}
			fis.close();
			sout.close();
		} catch (IOException e) {
			System.out.println("첨부 파일 다운로드중 다운로드 컨트롤러에서 오류 발생");
			e.printStackTrace();
		}
		
		return null;
	}






























}
