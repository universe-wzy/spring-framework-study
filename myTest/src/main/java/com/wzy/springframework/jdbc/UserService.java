package com.wzy.springframework.jdbc;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 描述:
 *
 * @author wangzhenyu
 * @create 2019-05-16 23:59
 */
@Transactional(propagation = Propagation.REQUIRED)
public interface UserService {

	void save(User user);

	List<User> getUsers();
}
