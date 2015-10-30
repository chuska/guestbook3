package com.bit2015.guestbook3.dao;

import java.lang.reflect.Member;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import oracle.jdbc.pool.OracleDataSource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bit2015.guestbook3.vo.GuestbookVo;

@Repository("gbDao")
public class GuestbookDao {
	@Autowired
	private OracleDataSource oracleDataSource;
	@Autowired
	private SqlSession sqlSession;

	public List<GuestbookVo> getList() {
		List<GuestbookVo> list = sqlSession.selectList("guestbook.list2");
		return list;
	}

	public GuestbookVo get(Long no) {
		GuestbookVo vo = sqlSession.selectOne("guestbook.nolist", no);
		return vo;
	}

	public List<GuestbookVo> getList(int page) {
		List<GuestbookVo> list = new ArrayList<GuestbookVo>();

		try {
			// 1. Connection 가져오기
			Connection connection = oracleDataSource.getConnection();

			// 2. Statement 준비
			String sql = "select * from(select rownum as r, A.* from(select no, name, message, to_char(reg_date, 'yyyy-mm-dd hh:mi:ss') from guestbook order by reg_date desc ) A ) where ?<=r and r<=?";
			PreparedStatement pstmt = connection.prepareStatement(sql);

			// 3. binding
			pstmt.setInt(1, (page - 1) * 5 + 1);
			pstmt.setInt(2, page * 5);
			// 4. SQL문 실행
			ResultSet rs = pstmt.executeQuery();

			// 4. row 가져오기
			while (rs.next()) {
				Long no = rs.getLong(2);
				String name = rs.getString(3);
				String message = rs.getString(4);
				String regDate = rs.getString(5);
				GuestbookVo vo = new GuestbookVo();
				vo.setNo(no);
				vo.setName(name);
				vo.setMessage(message);
				vo.setRegDate(regDate);
				list.add(vo);
			}
			// 5. 자원 정리
			rs.close();
			pstmt.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("SQL 오류-" + e);
		}
		return list;
	}

	public Long insert(GuestbookVo vo) {
		sqlSession.insert("guestbook.insert", vo);
		return vo.getNo();
	}

	public boolean delete(GuestbookVo vo) {
		sqlSession.delete("guestbook.delete", vo);
		return sqlSession.delete("guestbook.delete", vo) >= 1;
	}
}
