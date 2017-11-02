package com.sist.web.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Repository;

import com.sist.web.vo.Notice;


//@Component
@Repository
public class NoticeDAO {

	String driver = "oracle.jdbc.driver.OracleDriver";
	String url = "jdbc:oracle:thin:@211.238.142.132:1521:orcl";
	String user = "JOINUS";
	String pwd = "TJDDUF";
	
	@Autowired
	private JdbcTemplate jt;
	

	// 而⑤꽖�뀡 �뿰寃�
	public Connection getConn(){

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

	// 議고쉶�닔 �삱由ш린
	public int hitUp(String seq){
		Connection con = null;
		PreparedStatement ps = null;
		int af=0;
		
		String sql = "UPDATE NOTICES SET HIT=HIT+1 WHERE SEQ=?";
		con = getConn();
		
		try {
			ps = con.prepareStatement(sql);
			ps.setString(1, seq);
			af=ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("hit�닔 �뾽�뜲�씠�듃以� �삤瑜섍� 諛쒖깮�븯���뒿�땲�떎.");
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
	
	
	public int getSeqCount(String field, String query){
		Connection con = null;
		PreparedStatement ps=null;
		ResultSet rs = null;
		int count=0;

		String sql = "SELECT COUNT(SEQ) CNT FROM NOTICES WHERE "+field+" LIKE ?";
		con=getConn();

		try {
			ps = con.prepareStatement(sql);
			ps.setString(1, "%"+query+"%");
			rs=ps.executeQuery();
			
			if(rs.next()){
				count = rs.getInt("cnt");
				System.out.println("count="+count);
			}
		} catch (SQLException e) {
			System.out.println("寃뚯떆湲� 媛��닔 議고쉶以� �삤瑜섍� 諛쒖깮 �븯���뒿�땲�떎.");
			e.printStackTrace();
		}finally {
			try {
				rs.close();
				ps.close();
				con.close();
			} catch (SQLException e) {
				System.out.println("�젒�냽�빐�젣�뿉 �떎�뙣�븯���뒿�땲�떎.");
				e.printStackTrace();
			}  
		}
		return count;
	}

	public List<Notice> getNotices(int pg, String field, String query){
		System.out.println("pg="+pg);
		System.out.println("field="+field);
		System.out.println("query="+query);
		
		Connection con = null;
		//Statement st=null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Notice> list = null;
		
		int startRN = 1 + (pg-1)*10;   

		String sql = "SELECT * FROM (SELECT ROWNUM NUM, N.* FROM (SELECT * FROM NOTICES WHERE "+field+" LIKE ? ORDER BY TO_NUMBER(SEQ) DESC) N) WHERE NUM BETWEEN ? AND ?";
		con=getConn();
		try {
			
			ps = con.prepareStatement(sql);
			ps.setString(1, "%"+query+"%");
			ps.setInt(2, startRN);
			ps.setInt(3, startRN+10);
			rs=ps.executeQuery();

			list= new ArrayList<Notice>();
			while(rs.next()){
				Notice n = new Notice();   
				n.setSeq(rs.getString("seq"));
				n.setTitle(rs.getString("title"));
				n.setWriter(rs.getString("writer"));
				n.setContent(rs.getString("content"));
				n.setRegdate(rs.getString("regdate"));
				n.setHit(rs.getInt("hit"));
				n.setFileSrc(rs.getString("filesrc"));

				list.add(n);

			}
		} catch (SQLException e) {
			System.out.println("�쉶�썝 紐⑸줉 議고쉶以� �삤瑜섍� 諛쒖깮�븯���뒿�땲�떎.");
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
		return list;
	}

	public Notice getNotice(String seq){    
		System.out.println("jdbcTemplate 사용해서 getNotice() 호출");
//		JdbcTemplate jt = new JdbcTemplate();
//		DriverManagerDataSource ds = new DriverManagerDataSource();
//		ds.setDriverClassName(driver);
//		ds.setUrl(url);
//		ds.setUsername(user);
//		ds.setPassword(pwd);
//		
//		jt.setDataSource(ds);
//		
		String sql = "SELECT * FROM NOTICES WHERE SEQ=?";
		return jt.queryForObject(sql, new Object[] {seq}, new BeanPropertyRowMapper<Notice>(Notice.class));
		

	}

	public int updateNotice(Notice n) {
		Connection con = null;
		PreparedStatement ps=null;
		int af=0;
		String sql = "UPDATE NOTICES SET TITLE=?, CONTENT=? WHERE SEQ=?";
		con=getConn();
		try {
			ps = con.prepareStatement(sql);
			ps.setString(1, n.getTitle());
			ps.setString(2, n.getContent());
			ps.setString(3, n.getSeq());

			af = ps.executeUpdate();
			System.out.println(af);
			if(af==1){
				System.out.println("寃뚯떆湲� �닔�젙�뿉 �꽦怨듯븯���뒿�땲�떎.");
			}else{
				System.out.println("寃뚯떆湲� �닔�젙�뿉 �떎�뙣�븯���뒿�땲�떎.");
			}
		} catch (SQLException e) {
			System.out.println("寃뚯떆湲� �닔�젙以� �삤瑜섍� 諛쒖깮�븯���뒿�땲�떎.");
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

	public int delNotice(String seq) {
		Connection con = null;
		PreparedStatement ps=null;
		int af=0;

		String sql = "DELETE NOTICES WHERE SEQ=?";
		con=getConn();
		try {

			ps = con.prepareStatement(sql);
			ps.setString(1, seq);
			af = ps.executeUpdate();

		} catch (SQLException e) {
			System.out.println("寃뚯떆湲� �궘�젣以� �삤瑜섍� 諛쒖깮�븯���뒿�땲�떎.");
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

	public int insertNotice(Notice n){
		Connection con = null;
		PreparedStatement ps=null;
		int af =0;

		String sql = "INSERT INTO NOTICES(SEQ, TITLE, WRITER, CONTENT, REGDATE, HIT, FILESRC) VALUES((SELECT MAX(TO_NUMBER(SEQ))+1 FROM NOTICES), ?, ?, ?, SYSDATE, 0, ?)";
		con=getConn();
		try {
			ps=con.prepareStatement(sql);
			ps.setString(1, n.getTitle());
			ps.setString(2, n.getWriter());
			ps.setString(3, n.getContent());
			ps.setString(4, n.getFileSrc());

			af = ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("寃뚯떆湲� �벑濡앹쨷�삤瑜섍� 諛쒖깮�븯���뒿�땲�떎.");
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

}
