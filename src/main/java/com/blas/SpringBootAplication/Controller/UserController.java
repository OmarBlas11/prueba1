package com.blas.SpringBootAplication.Controller;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.blas.SpringBootAplication.Dto.ChangePasswordForm;
import com.blas.SpringBootAplication.Entity.Role;
import com.blas.SpringBootAplication.Entity.User;
import com.blas.SpringBootAplication.Exeptions.CustomeFieldValidationException;
import com.blas.SpringBootAplication.Exeptions.UserNameOridNotFound;
import com.blas.SpringBootAplication.Repository.RoleRepository;
import com.blas.SpringBootAplication.Repository.UserRepository;
import com.blas.SpringBootAplication.Service.UserService;

@Controller
public class UserController {
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserService userService;
	
	@GetMapping({"/","/login"})
	public String index() {
		return "index";
	}
	@GetMapping("/signup")
	public String signup(Model model) {
		Role role=roleRepository.findByName("USER");
		List<Role> roles=Arrays.asList(role);
		model.addAttribute("userForm", new User());
		model.addAttribute("roles", roles);
		model.addAttribute("signup", true);
		return "user-form/user-singup";
	}
	@PostMapping("/signup")
	public String PostSignUp(@Valid @ModelAttribute("userForm")User user, BindingResult result, ModelMap model) {
		Role role=roleRepository.findByName("USER");
		List<Role> roles=Arrays.asList(role);
		model.addAttribute("userForm", user);
		model.addAttribute("roles", roles);
		model.addAttribute("signup", true);
		if(result.hasErrors()) {
			return "user-form/user-singup";
		}else {
			try {
				userService.createUser(user);
			} catch (CustomeFieldValidationException cfve) {
				// TODO: handle exception
				result.rejectValue(cfve.getFieldName(), null, cfve.getMessage());
				return "user-form/user-singup";
			}
			catch (Exception e) {
				// TODO: handle exception
				model.addAttribute("formErrorMessage", e.getMessage());
				return "user-form/user-singup";
			}
		}
		return "index";
	}
	
	
	@GetMapping("/userForm")
	public String userform(Model model) {
		model.addAttribute("userForm", new User());
		model.addAttribute("roles", roleRepository.findAll());
		model.addAttribute("userList", userService.getAllUsers());
		model.addAttribute("listTab", "active");
		return "user-form/user-view";
	}
	
	@PostMapping("/userForm")
	public String createuser(@Valid @ModelAttribute("userForm")User user, BindingResult result, ModelMap model){
		if(result.hasErrors()) {
			model.addAttribute("userForm", user);
			model.addAttribute("formTab", "active");
		}else {
			try {
				userService.createUser(user);
				model.addAttribute("listTab", "active");
				model.addAttribute("userList", userService.getAllUsers());
				model.addAttribute("userForm", new User());
			} catch (CustomeFieldValidationException cfve) {
				// TODO: handle exception
				result.rejectValue(cfve.getFieldName(), null, cfve.getMessage());
				model.addAttribute("userForm", user);
				model.addAttribute("formTab", "active");
				model.addAttribute("userList", userService.getAllUsers());
				model.addAttribute("roles", roleRepository.findAll());
			}
			catch (Exception e) {
				// TODO: handle exception
				model.addAttribute("formErrorMessage", e.getMessage());
				model.addAttribute("userForm", user);
				model.addAttribute("formTab", "active");
				model.addAttribute("userList", userService.getAllUsers());
				model.addAttribute("roles", roleRepository.findAll());
			}
		}
		
		model.addAttribute("userList", userService.getAllUsers());
		model.addAttribute("roles", roleRepository.findAll());
		return "user-form/user-view";
	}
	
	@GetMapping("/editUser/{id}")
	public String getEditUserForm(Model model, @PathVariable(name="id")Long id) throws Exception{
		User userToEdit=userService.getUserById(id);
		
		model.addAttribute("userForm", userToEdit);
		model.addAttribute("roles", roleRepository.findAll());
		model.addAttribute("userList", userService.getAllUsers());
		model.addAttribute("formTab", "active");	
		model.addAttribute("editMode","true");
		model.addAttribute("passwordForm",new ChangePasswordForm(id));
		return "user-form/user-view";
	}
	
	@PostMapping("/editUser")
	public String postEditUser(@Valid @ModelAttribute("userForm")User user, BindingResult result, ModelMap model) {
		if(result.hasErrors()) {
			model.addAttribute("editMode", "true");
			model.addAttribute("formTab","active");
			model.addAttribute("userForm", user);
			model.addAttribute("passwordForm",new ChangePasswordForm(user.getId()));
			}else {
				try {
					userService.update(user);
					model.addAttribute("userForm", new User());
					model.addAttribute("listTab","active");
				} catch (Exception e) {
					// TODO: handle exception
					model.addAttribute("formErrorMessages", e.getMessage());
					model.addAttribute("useForm", user);
					model.addAttribute("formTab", "active");
					model.addAttribute("userList", userService.getAllUsers());
					model.addAttribute("roles", roleRepository.findAll());
					model.addAttribute("editMode","true");
					model.addAttribute("passwordForm",new ChangePasswordForm(user.getId()));
					
				}
			}
		
		model.addAttribute("userList", userService.getAllUsers());
		model.addAttribute("roles", roleRepository.findAll());
		return "user-form/user-view";
	}
	@GetMapping("/userForm/cancel")
	public String CancelEditUser(ModelMap model) {
		return "redirect:/userForm";
	}
	
	@GetMapping("deleteUser/{id}")
	public String deleteUser(Model model, @PathVariable(name="id")Long id) {
		try {
			userService.deleteUser(id);
			
		} catch (UserNameOridNotFound uoinf) {
			// TODO: handle exception
			model.addAttribute("listErrorMessage", uoinf.getMessage());
		}
		return userform(model);
	}
	
	@PostMapping("/editUser/changePassword")
	public ResponseEntity<String> CambiarPassword(@Valid @RequestBody ChangePasswordForm Form, Errors error) {
		try {
			if (error.hasErrors()) {
				String result=error.getAllErrors().stream().map(x-> x.getDefaultMessage()).collect(Collectors.joining(""));
				throw new Exception(result);
			}
			userService.changePassword(Form);
		} catch (Exception e) {
			// TODO: handle exception
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.ok("success");
	}
	
}
