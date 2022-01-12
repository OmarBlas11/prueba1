package com.blas.SpringBootAplication.Service;



import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.blas.SpringBootAplication.Dto.ChangePasswordForm;
import com.blas.SpringBootAplication.Entity.User;
import com.blas.SpringBootAplication.Exeptions.UserNameOridNotFound;

@Service
public interface UserService{
	
	public Iterable<User> getAllUsers();

	public User createUser(User user) throws Exception;
	
	public User getUserById(Long id) throws Exception;
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	public User update(User user) throws Exception;
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	public void deleteUser(Long id) throws UserNameOridNotFound;
	
	public User changePassword(ChangePasswordForm form) throws Exception;
}
