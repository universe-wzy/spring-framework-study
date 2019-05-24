package com.wzy.springframework.postprocessor;

/**
 * 描述:
 *
 * @author wangzhenyu
 * @create 2019-05-13 10:18
 */
public class SimplePostProcessor {
	private String connectionString;
	private String username;
	private String password;

	public String getConnectionString() {
		return connectionString;
	}

	public void setConnectionString(String connectionString) {
		this.connectionString = connectionString;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "SimplePostProcessor{" +
				"connectionString='" + connectionString + '\'' +
				", username='" + username + '\'' +
				", password='" + password + '\'' +
				'}';
	}
}
