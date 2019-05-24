package com.wzy.springframework.jdbc;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 描述:
 *
 * @author wangzhenyu
 * @create 2019-05-16 23:58
 */
public class UserRowMapper implements RowMapper {

	@Override
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new User(rs.getInt("id"), rs.getString("name"),
				rs.getInt("age"), rs.getString("sex"));
	}
}
