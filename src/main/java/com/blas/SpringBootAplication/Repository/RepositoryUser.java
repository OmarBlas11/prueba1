package com.blas.SpringBootAplication.Repository;

import org.springframework.data.repository.CrudRepository;
import com.blas.SpringBootAplication.Entity.User;

public interface RepositoryUser extends CrudRepository<User, Long> {
	public User findByUsername(String username);
}
