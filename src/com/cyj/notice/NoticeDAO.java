package com.cyj.notice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.cyj.util.DBConnector;

public class NoticeDAO {
	
	//getCount 전체 글의 갯수를 가지고 옴
	public int getCount(String kind, String search) throws Exception {
		Connection con = DBConnector.getConnect();
		String sql = "select count(num) from notice "
				+ "where "+kind+" like ?";
		PreparedStatement st = con.prepareStatement(sql);
		st.setString(1, "%"+search+"%");
		ResultSet rs = st.executeQuery();
		rs.next();
		int result = rs.getInt(1);
		DBConnector.disConnect(rs, st, con);
		return result;
	}
	
	
	public static void main(String[] args) throws Exception {
		NoticeDAO nDAO = new NoticeDAO();
		for(int i=0;i<150;i++) {
			NoticeDTO nDTO = new NoticeDTO();
			nDTO.setTitle("title" + i);
			nDTO.setContents("contents" + i);
			nDTO.setWriter("writer" + i);
			nDAO.insert(nDTO);
			if(i%10==0) {
				Thread.sleep(500);
			}
		}
		System.out.println("Done");
	}
	
	//write
	public int write() throws Exception{
		Connection con = DBConnector.getConnect();
		String sql = "";
		PreparedStatement st = con.prepareStatement(sql);
		
		int result = st.executeUpdate();
		
		DBConnector.disConnect(st, con);
		return result;
	}
	
	//insert
	public int insert(NoticeDTO nDTO) throws Exception{
		Connection con = DBConnector.getConnect();
		String sql="insert into notice values (notice_seq.nextval, ?, ?, ?, sysdate, 0)";
		PreparedStatement st = con.prepareStatement(sql);
		st.setString(1, nDTO.getTitle());
		st.setString(2, nDTO.getContents());
		st.setString(3, nDTO.getWriter());
		int result = st.executeUpdate();
		DBConnector.disConnect(st, con);
		return result;
	}
	
	//selectOne
	public NoticeDTO selectOne(int num) throws Exception{
		Connection con = DBConnector.getConnect();
		String sql="select * from notice where num=?";
		PreparedStatement st = con.prepareStatement(sql);
		st.setInt(1, num);		
		NoticeDTO nDTO = null;
		ResultSet rs = st.executeQuery();
		if(rs.next()) {
			nDTO = new NoticeDTO();
		}
		
		DBConnector.disConnect(rs, st, con);
		return nDTO;
	}
	
	//selectList(descending number order)
	public List<NoticeDTO> selectList(int startRow, int lastRow, String kind, String search) throws Exception{
		Connection con = DBConnector.getConnect();
		String sql="select * from "
				+ "(select rownum R, N.* from "
				+ "(select num, title, writer, reg_date, hit from notice "
				+ "where "+kind+" like ? "
				+ "order by num desc) N) "
				+ "where R between ? and ?";
		PreparedStatement st = con.prepareStatement(sql);
		
		st.setString(1, "%"+search+"%");
		st.setInt(2, startRow);
		st.setInt(3, lastRow);
		
		ResultSet rs = st.executeQuery();
		ArrayList<NoticeDTO> ar = new ArrayList<>();
		NoticeDTO nDTO = null;
		
		while(rs.next()) {
			nDTO = new NoticeDTO();
			nDTO.setNum(rs.getInt("num"));
			nDTO.setTitle(rs.getString("title"));
			nDTO.setWriter(rs.getString("writer"));
			nDTO.setReg_date(rs.getDate("reg_date"));
			nDTO.setHit(rs.getInt("hit"));
			ar.add(nDTO);
		}
		
		DBConnector.disConnect(rs, st, con);
		return ar;
	}
	
	//delete
	public int delete() throws Exception{
		Connection con = DBConnector.getConnect();
		String sql="";
		PreparedStatement st = con.prepareStatement(sql);
		
		int result = st.executeUpdate();
		
		DBConnector.disConnect(st, con);
		return result;
	}
	
	//update
	public int update() throws Exception{
		Connection con = DBConnector.getConnect();
		String sql="";
		PreparedStatement st = con.prepareStatement(sql);
		
		int result = st.executeUpdate();
		
		DBConnector.disConnect(st, con);
		return result;
	}

}
