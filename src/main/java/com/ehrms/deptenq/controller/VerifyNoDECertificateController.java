package com.ehrms.deptenq.controller;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ehrms.deptenq.repository.IGlobalOrgRepository;
import com.ehrms.deptenq.repository.Service_GroupRepository;

@Controller
@RequestMapping("departmentalEnquiry")
public class VerifyNoDECertificateController {
	
	@Autowired
	private Service_GroupRepository groupRepo;
	
	@Autowired
	private IGlobalOrgRepository orgRepo;
	
	
	@GetMapping("/verifynode")
	public String verifynode(Model model, RedirectAttributes redirect) {
		model.addAttribute("serviceGroup", groupRepo.findByIdIn(Arrays.asList(1L,2L),Sort.by("id")));
		model.addAttribute("orglist", orgRepo.findByActive(true, Sort.by("id")));
		
		return "DeptInquiryViews/verifynode";
	}

}
