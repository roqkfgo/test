package com.sist.web.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.sist.web.vo.Member;


//@Component
@Repository
public class MemberDAO {
	
	// 而⑤꽖�뀡 �뿰寃�
	public Connection getConn(){
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@211.238.142.132:1521:orcl";
		//private String url = "jdbc:oracle:thin:@211.238.142.15:1521:orcl";
		String user = "JOINUS";
		String pwd = "TJDDUF";
		
		Connection con = null;
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, user, pwd);
		} catch (ClassNotFoundException e) {
			System.out.println("�뱶�씪�씠踰� 濡쒕뱶�뿉 �떎�뙣�븯���뒿�땲�떎.");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("�젒�냽�뿉 �떎�뙣�븯���뒿�땲�떎. 怨꾩젙紐낃낵 鍮꾨쾲�쓣 �솗�씤�븯�꽭�슂.");
			e.printStackTrace();
		}
		return con;
	}
	
	//�쉶�썝�젙蹂� 議고쉶
	public Member getMember(String mid){    // �떆利�2�뿉�꽌 �궗�슜
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Member m=null;
		
		String sql = "SELECT * FROM JOINUS WHERE MID=?";
		con = getConn();
		try {
			ps=con.prepareStatement(sql);
			ps.setString(1, mid);
			rs=ps.executeQuery();   //議고쉶�씤 SELECT 臾몄뿉�꽌留� �궗�슜
			if(rs.next()){
				m = new Member();    // 媛앹껜�쓽 �깮�꽦 �쐞移� 留ㅼ슦 以묒슂
				m.setMid(rs.getString("mid"));
				m.setPwd(rs.getString("pwd"));
				m.setName(rs.getString("name"));
				m.setGender(rs.getString("gender"));
				m.setAge(rs.getInt("age"));
				m.setBirthday(rs.getString("birthday"));
				m.setIsLunar(rs.getString("islunar"));
				m.setPhone(rs.getString("phone"));
				m.setRegdate(rs.getString("regdate"));
				m.setHabit(rs.getString("habit"));
			}
		} catch (SQLException e) {
			System.out.println("�쉶�썝 議고쉶以� �삤瑜섍� 諛쒖깮�븯���뒿�땲�떎.");
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				ps.close();
				con.close();
			} catch (SQLException e) {
				System.out.println("�젒�냽�빐�젣�뿉 �떎�뙣�븯���뒿�땲�떎.");
				e.printStackTrace();
			}
		}
		return m;
	}
	
	// �쉶�썝 異붽�
	public int addMember(Member m){
		Connection con = null;
		PreparedStatement ps=null;
		int af =0;
		
		String sql = "INSERT INTO JOINUS(MID, PWD, NAME, GENDER, AGE, BIRTHDAY, ISLUNAR, PHONE, HABIT, REGDATE) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE)";
		con=getConn();
		try {
			ps=con.prepareStatement(sql);
			ps.setString(1, m.getMid());
			ps.setString(2, m.getPwd());
			ps.setString(3, m.getName());
			ps.setString(4, m.getGender());
			ps.setInt(5, m.getAge());
			ps.setString(6, m.getBirthday());
			ps.setString(7, m.getIsLunar());
			ps.setString(8, m.getPhone());
			ps.setString(9, m.getHabit());
			
			af = ps.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("�쉶�썝 異붽�以� �삤瑜섍� 諛쒖깮�븯���뒿�땲�떎.");
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				con.close();
			} catch (SQLException e) {
				System.out.println("�젒�냽�빐�젣�뿉 �떎�뙣�븯���뒿�땲�떎.");
				e.printStackTrace();
			}
		}
		return af;
	}
	
	// �쉶�썝 �젙蹂� �닔�젙
	public int updateMember(Member m) {
		Connection con = null;
		PreparedStatement ps=null;
		int af=0;
		String sql = "UPDATE JOINUS SET PWD=?, AGE=?, BIRTHDAY=?, PHONE=?, HABIT=?, GENDER=?, NAME=? WHERE MID=?";
		con=getConn();
		try {
			ps = con.prepareStatement(sql);
			ps.setString(1, m.getPwd());
			ps.setInt(2, m.getAge());
			ps.setString(3, m.getBirthday());
			ps.setString(4, m.getPhone());
			ps.setString(5, m.getHabit());
			ps.setString(6, m.getGender());
			ps.setString(7, m.getName());
			ps.setString(8, m.getMid());
			
			af = ps.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("�쉶�썝 �젙蹂� �닔�젙以� �삤瑜섍� 諛쒖깮�븯���뒿�땲�떎.");
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				con.close();
			} catch (SQLException e) {
				System.out.println("�젒�냽�빐�젣�뿉 �떎�뙣�븯���뒿�땲�떎.");
				e.printStackTrace();
			}
		}
		return af;
	}
	
	// �쉶�썝 �궘�젣
	public int delMember(Member m) {
		Connection con = null;
		PreparedStatement ps=null;
		int af=0;
		
		String sql = "DELETE JOINUS WHERE MID=?";
//		String sql2 = "DELETE ACCOUNTS WHERE OWNER=?";
//		String sql3 = "DELETE DEALS WHERE FACCOUNTNUM=?";
		con=getConn();
		try {
			
			ps = con.prepareStatement(sql);
			ps.setString(1, m.getMid());
			af = ps.executeUpdate();
			
			/*if(af==1){
				System.out.println();
				System.out.println("�쉶�썝�궘�젣�뿉 �꽦怨듯븯���뒿�땲�떎.");
			}else{
				System.out.println();
				System.out.println("�쉶�썝�궘�젣�뿉 �떎�뙣�븯���뒿�땲�떎.");
			}*/
		} catch (SQLException e) {
			System.out.println("�쉶�썝 �궘�젣以� �삤瑜섍� 諛쒖깮�븯���뒿�땲�떎.");
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				con.close();
			} catch (SQLException e) {
				System.out.println("�젒�냽�빐�젣�뿉 �떎�뙣�븯���뒿�땲�떎.");
				e.printStackTrace();
			}
		}
		return af;
	}
	
	
	// �쉶�썝 紐⑸줉 議고쉶
	public List<Member> getMembers(){
		Connection con = null;
		Statement st=null;
		//PreparedStatement ps=null;
		ResultSet rs = null;
		List<Member> list = null;
		
		String sql = "SELECT * FROM JOINUS";
		//String sql = "SELECT * FROM MEMBERS ORDER BY "+col;
		con=getConn();
		try {
			st = con.createStatement();
			rs=st.executeQuery(sql);
			
			list= new ArrayList<Member>();
			while(rs.next()){
				Member m = new Member();   // 媛앹껜�쓽 �깮�꽦 �쐞移� 留ㅼ슦 以묒슂
				m.setMid(rs.getString("mid"));
				m.setPwd(rs.getString("pwd"));
				m.setName(rs.getString("name"));
				m.setGender(rs.getString("gender"));
				m.setAge(rs.getInt("age"));
				m.setBirthday(rs.getString("birthday"));
				m.setIsLunar(rs.getString("islunar"));
				m.setPhone(rs.getString("phone"));
				m.setRegdate(rs.getString("regdate"));
				m.setHabit(rs.getString("habit"));
				
				list.add(m);
				
			}
		} catch (SQLException e) {
			System.out.println("�쉶�썝 紐⑸줉 議고쉶以� �삤瑜섍� 諛쒖깮�븯���뒿�땲�떎.");
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				st.close();
				con.close();
				
			} catch (SQLException e) {
				System.out.println("�젒�냽�빐�젣�뿉 �떎�뙣�븯���뒿�땲�떎.");
				e.printStackTrace();
			}  
		}
		return list;
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
