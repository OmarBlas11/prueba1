package com.blas.SpringBootAplication.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.blas.SpringBootAplication.Entity.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long>{
	public Role findByName(String role);
}
