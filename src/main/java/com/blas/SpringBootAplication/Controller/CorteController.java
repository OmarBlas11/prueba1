package com.blas.SpringBootAplication.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/corte")
public class CorteController {
	
	@GetMapping("/formcorte")
	public String FormCorte(Model model) {
		model.addAttribute("corteTab","true");
		//return "user-form/user-view";
		return "corte-form/corte-form";
	}
}
