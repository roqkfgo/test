package com.sist.web.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.sist.web.dao.MemberDAO;
import com.sist.web.vo.Member;

import web.sist.web.util.CookieManager;

/*1) ��Ʈ�ѷ� ������̼�, url ���� ����*/
@Controller
@RequestMapping("/joinus/*")
public class JoinusController {

	/* MemberDAO , @Autowired*/
	@Autowired
	private MemberDAO mdao;
	/*public void setMdao(MemberDAO mdao) {
		this.mdao = mdao;
	}*/

	@RequestMapping(value={"login.do"}, method=RequestMethod.GET)
	public String login(HttpServletRequest request, Model model){
		Cookie[] coos = request.getCookies();   /*request ���� ��Ű �о ��Ʈ�� �迭�� �ޱ�*/
		String cookieMid = CookieManager.getCookie(coos, "mid");
		String cookiePwd = CookieManager.getCookie(coos, "pwd");


		if(cookieMid!=null && !cookieMid.equals("")){
			model.addAttribute("cookieMid", cookieMid);
		}
		if(cookiePwd!=null && !cookiePwd.equals("")){
			model.addAttribute("cookiePwd", cookiePwd);
		}

		/*ModelAndView ��ü �����*/
		//ModelAndView mv = new ModelAndView();
		String error = request.getParameter("error");
		if(error !=null && !error.equals("")){
			if(error.equals("IDx")){
				error="���̵� Ʋ�Ƚ��ϴ�.";
			}else if(error.equals("PWDx")){
				error="��й�ȣ�� Ʋ�Ƚ��ϴ�.";
			}else{
				error="�� �� ���� ������ �α��ο� �����Ͽ����ϴ�.";
			}
			model.addAttribute("error", error);
		}

		//mv.setViewName("login.jsp");
		return "login.jsp";
	}


	@RequestMapping(value={"login.do"}, method=RequestMethod.POST)
	public String login(String mid, String pwd, String checkBoxMid, HttpServletRequest request, HttpServletResponse response){
		//String mid = request.getParameter("mid");
		//String pwd = request.getParameter("pwd");
		// D) checkBoxMid, checkBoxPwd �ޱ�
		//String checkBoxMid = request.getParameter("checkBoxMid");  // check=on, �ƴϸ� null ��ȯ
		//String checkBoxPwd = request.getParameter("checkBoxPwd");

		System.out.println("mid= "+mid);
		System.out.println("pwd= "+pwd);

		//MemberDAO dao= new MemberDAO();
		Member m = mdao.getMember(mid);

		ModelAndView mv = new ModelAndView();
		if(m==null){
			//mv.setViewName("redirect:login.do?error=IDx");
			return "redirect:login.do?error=IDx";
		}else if(!m.getPwd().equals(pwd)){
			//mv.setViewName("redirect:login.do?error=PWDx");
			return "redirect:login.do?error=PWDx";
		}else{
			System.out.println("�α��� ����");
			// A) �α��ν� ���ǿ� mid ����    
			request.getSession().setAttribute("mid", mid);
			Cookie ck = null; 
			Cookie ck2 = null;
			//  ===> ��Ű ����Ǵ� ���� : C:\Users\SaintClass\AppData\Local\Microsoft\Windows\INetCache
			//       ���ϸ� : joinus/(��Ʈ�ѷ��� �Ҽӵ� ������ ���) - ���ͳ� �ּ� : Cookie:saintclass@localhost/the4st_ch11_Session/joinus/ 
			if(checkBoxMid != null && !checkBoxMid.equals("")){
				// C) ��Ű �����  ===> D) LoginController���� ��Ű �о����
				ck = new Cookie("mid", mid);  // ��Ű ��ü ����
				ck.setMaxAge(60*60*24*30);    // ��Ű ���� �Ⱓ ����
				ck.setPath("/");
				response.addCookie(ck);       // response �� Ŭ���̾�Ʈ�� ��Ű ������
				System.out.println("mid ��Ű����");

				ck2 = new Cookie("pwd", m.getPwd());  // ��Ű ��ü ����
				ck2.setMaxAge(60*60*24*30);    // ��Ű ���� �Ⱓ ����
				ck2.setPath("/");    // ��Ű�� ���� ���� : 
				response.addCookie(ck2);       // response �� Ŭ���̾�Ʈ�� ��Ű ������
				System.out.println("pwd ��Ű����");

			}else{
				// ) ��Ű �Ӽ� �ʱ�ȭ
				ck = new Cookie("mid", null);  // ��Ű ��ü ����(�Ӽ����� ����)
				ck.setMaxAge(0);               // ��Ű �����Ⱓ ���ֱ�
				response.addCookie(ck);        // response �� Ŭ���̾�Ʈ�� ��Ű ������
				System.out.println("mid ��Ű����");

				ck2 = new Cookie("pwd", null);  
				ck2.setMaxAge(0);               
				response.addCookie(ck2);        
				System.out.println("pwd ��Ű����");
			}

			String returnURL = (String)request.getSession().getAttribute("returnURL");
			if(returnURL!=null && !returnURL.equals("")){
				//mv.setViewName("redirect:"+returnURL);
				return "redirect:"+returnURL;
			}

			//mv.setViewName("redirect:welcomelogin.do");
			return "redirect:welcomelogin.do";
		}
	}

	
	@RequestMapping(value={"join.do"}, method=RequestMethod.GET)
	public String join(){
		return "join.jsp";
	}

	
	@RequestMapping(value={"join.do"}, method=RequestMethod.POST)
	/* form �±׿� �ִ� name �Ӽ��� ���� ���ļ� �ڵ����� �Էµ�*/
	public String join(Member m){

		System.out.println("m.getBirthday()="+m.getBirthday());
		int af = mdao.addMember(m);
		if(af == 1){
			return "redirect:login.do";
		}else{
			return null;
		}
	}


	@RequestMapping(value={"welcomelogin.do"}, method=RequestMethod.GET)
	public String welcomelogin(){
		return "welcomelogin.jsp";
	}

	@RequestMapping(value={"memberUpdate.do"}, method=RequestMethod.GET)
	public String memberUpdate(Model model, HttpServletRequest request){
		String mid = (String) request.getSession().getAttribute("mid");
		//MemberDAO dao = new MemberDAO();
		Member m = mdao.getMember(mid);
		//ModelAndView mv = new ModelAndView();
		System.out.println("m.getHabit()="+m.getHabit());

		/*String year = m.getBirthday().substring(0, 4);
		String month = m.getBirthday().substring(5, 7);
		String day = m.getBirthday().substring(8);*/

		model.addAttribute("m", m);
		/*model.addAttribute("year", year);
		model.addAttribute("month", month);
		model.addAttribute("day", day);*/

		//mv.setViewName("memberUpdate.jsp");
		return "memberUpdate.jsp";
	}


	@RequestMapping(value={"memberUpdate.do"}, method=RequestMethod.POST)
	public String memberUpdate(HttpServletRequest request, Member m){

		int af = mdao.updateMember(m);
		System.out.println("af="+af);
		if (af == 1) {
			//mv.setViewName("redirect:memberUpdate.do?mid="+mid);
			return "redirect:memberUpdate.do";
		} else {
			System.out.println("ȸ������ ������ ������ �߻��Ͽ����ϴ�.");
			return null;
		}
	}


	@RequestMapping(value={"idcheck.do"}, method=RequestMethod.POST)
	@ResponseBody
	public String idcheck(String mid){
		System.out.println("mid="+mid);
		
		Member m = mdao.getMember(mid);
		if(m==null){
			/*model.addAttribute("check", "1");
			System.out.println(m);*/
			return "yes";
		}else{
			/*model.addAttribute("check", "0");
			System.out.println(m);*/
			return "no";
		}
	}
	
	
	@RequestMapping(value={"memberData.do"})
	@ResponseBody
	public String memberData(String mid, String pwd){

		System.out.println("mid="+mid);
		System.out.println("pwd="+pwd);
		
		Member m = mdao.getMember(mid);
		if(m==null){
			String ajaxData = "{\"error\":\"midError\"}";
			return ajaxData;
		}else if(!m.getPwd().equals(pwd)){
			String ajaxData = "{\"error\":\"pwdError\"}";
			return ajaxData;
		}else{
			Gson gson = new Gson();
			String ajaxData = gson.toJson(m);
			System.out.println(ajaxData);
			return ajaxData;
		}
	}

	







}
