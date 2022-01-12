package com.blas.SpringBootAplication.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.blas.SpringBootAplication.Dto.ChangePasswordForm;
import com.blas.SpringBootAplication.Entity.User;
import com.blas.SpringBootAplication.Exeptions.CustomeFieldValidationException;
import com.blas.SpringBootAplication.Exeptions.UserNameOridNotFound;
import com.blas.SpringBootAplication.Repository.UserRepository;

@Service
public class UserServiceImplement implements UserService {
	
	@Autowired
	UserRepository repository;
	


	BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder(4);
	
	@Override
	public Iterable<User> getAllUsers() {
		// TODO Auto-generated method stub
		return repository.findAll();
	}
	
	private boolean checkUsernameAvailable(User user) throws Exception {
		Optional<User> userFound = repository.findByUsername(user.getUsername());
		if (userFound.isPresent()) {
			throw new CustomeFieldValidationException("Username no disponible","username");
		}
		return true;
	}
	
	private boolean checkPasswprdValid(User user) throws Exception{
		
		if (user.getConfirmPassword() == null || user.getConfirmPassword().isEmpty()) {
			throw new CustomeFieldValidationException("Confirm Password es obligatorio","confirmPassword");
		}
		
		if(!user.getPassword().equals(user.getConfirmPassword())) {
			throw new CustomeFieldValidationException("Las contraseñas no coinciden","password");
		}
		return true;
	}

	@Override
	public User createUser(User user) throws Exception {
		// TODO Auto-generated method stub
		if(checkUsernameAvailable(user) && checkPasswprdValid(user)){
			String encodPassword=bCryptPasswordEncoder.encode(user.getPassword());
			user.setPassword(encodPassword);
			user=repository.save(user);
		}
		return user;
	}

	@Override
	public User getUserById(Long id) throws UserNameOridNotFound {
		// TODO Auto-generated method stub
		User user=repository.findById(id).orElseThrow(()-> new UserNameOridNotFound("El ID del Usuario no existe"));
		
		return user;
	}

	@Override
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	public User update(User userFrom) throws Exception {
		// TODO Auto-generated method stub
		User UserTo=getUserById(userFrom.getId());
		mapUser(userFrom, UserTo);
		
		return repository.save(UserTo);
	}

	protected void mapUser(User from, User userTo) {
		// TODO Auto-generated method stub
		userTo.setUsername(from.getUsername());
		userTo.setFirstName(from.getFirstName());
		userTo.setLastName(from.getLastName());
		userTo.setEmail(from.getEmail());
		userTo.setRoles(from.getRoles());
	}

	@Override
	public void deleteUser(Long id) throws UserNameOridNotFound {
		// TODO Auto-generated method stub
		User user=getUserById(id);
		repository.delete(user);
	}

	@Override
	public User changePassword(ChangePasswordForm form) throws Exception {
		// TODO Auto-generated method stub
		User userTo=getUserById(form.getId());
		if (!loggedUserHasRole() && !form.getCurrentPassword().equals(userTo.getPassword())) {
			throw new Exception("LA CONTRASEÑA ACTUAL NO ES CORRECTA");
		}
		if (form.getCurrentPassword().equals(form.getNewPassword())) {
			throw new Exception("LA CONTRASEÑA NUEVA NO PUEDE SER IGUAL A LA CONRASEÑA ACTUAL");
		}
		if (!form.getNewPassword().equals(form.getConfirmPassword())) {
			throw new Exception("LAS CONTRASEÑAS NO COINCIDEN");
			
		}
		String encodPassword=bCryptPasswordEncoder.encode(form.getNewPassword());
		userTo.setPassword(encodPassword);
		return repository.save(userTo);
	}
	public boolean loggedUserHasRole() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserDetails loggedUser = null;
		if (principal instanceof UserDetails) {
			loggedUser = (UserDetails) principal;
		
			loggedUser.getAuthorities().stream()
					.filter(x -> "ADMIN".equals(x.getAuthority() ))      
					.findFirst().orElse(null); //loggedUser = null;
		}
		return loggedUser != null ?true :false;
	}

//	private boolean isLoggedUserADMIN() {
//		// Obtener el usuario logeado
//		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//
//		UserDetails loggedUser = null;
//		Object roles = null;
//
//		// Verificar que ese objeto traido de sesion es el usuario
//		if (principal instanceof UserDetails) {
//			loggedUser = (UserDetails) principal;
//
//			roles = loggedUser.getAuthorities().stream().filter(x -> "ROLE_ADMIN".equals(x.getAuthority())).findFirst()
//					.orElse(null);
//		}
//		return roles != null ? true : false;
//	}
	
}
