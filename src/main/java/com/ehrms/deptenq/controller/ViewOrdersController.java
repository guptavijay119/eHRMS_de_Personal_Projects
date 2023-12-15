package com.ehrms.deptenq.controller;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ehrms.deptenq.models.DErules;
import com.ehrms.deptenq.models.SuperAdminOrders;
import com.ehrms.deptenq.models.Templates;
import com.ehrms.deptenq.modelsDto.SuperAdminOrdersDto;
import com.ehrms.deptenq.repository.DERulesRepository;
import com.ehrms.deptenq.repository.SuperAdminOrdersRepository;
import com.ehrms.deptenq.repository.TemplatesRepository;
import com.ehrms.deptenq.service.FileService;

@Controller
@RequestMapping("departmentalEnquiry")
public class ViewOrdersController {

	@Autowired
	private SuperAdminOrdersRepository orderRepo;

	@Autowired
	private DERulesRepository deruleRepo;
	
	@Autowired
	private TemplatesRepository tempRepo;

	@Autowired
	private FileService fileService;

	@GetMapping("/getOrders")
	public String getOrders(Model model) {
		model.addAttribute("ruleList", deruleRepo.findAll(Sort.by(Direction.ASC, "id")));
		model.addAttribute("orderList", orderRepo.findAll(Sort.by(Direction.DESC, "orderDate")));
		return "DeptInquiryViews/rulesandorders";
	}
	
	
	@GetMapping("/getTemplates")
	public String getTemplates(Model model) {
		model.addAttribute("templatelist", tempRepo.findAll(Sort.by(Direction.ASC, "id")));
		return "DeptInquiryViews/templates";
	}

	@GetMapping("/getUploadOrder")
	public String getUploadOrder(Model model) {
		model.addAttribute("uploadorder", new SuperAdminOrders());
		return "DeptInquiryViews/UploadScreenGad";
	}

	@PostMapping("/uploadOrderGad")
	public String uploadOrder(@ModelAttribute(name = "uploadorder") SuperAdminOrdersDto orderDto, Model model,
			RedirectAttributes redirec,MultipartFile file) {
		if (file != null && !file.isEmpty()
				&& StringUtils.equalsIgnoreCase(file.getContentType(), MediaType.APPLICATION_PDF_VALUE)) {
			SuperAdminOrders order = new SuperAdminOrders();
			order.setContentType(file.getContentType());
			order.setOrderDate(orderDto.getOrderDate());
			order.setSubject(orderDto.getSubject());
			order.setOrderName(UUID.randomUUID().toString() + "_" + file.getOriginalFilename());
			String filePath = "departmentalEnquiry/adminorders/";
			order.setFilePath(filePath);
			fileService.fileUpload(filePath, file, order.getOrderName());
			orderRepo.save(order);
			redirec.addFlashAttribute("message", "Uploaded Successfully");
			return "redirect:/departmentalEnquiry/getOrders";
		} else {
			redirec.addFlashAttribute("message", "Please upload proper file format");
			return "redirect:/departmentalEnquiry/getOrders";
		}
	}
	
	
	@GetMapping("/getorderpdf/{id}")
	public ResponseEntity<ByteArrayResource> getFile(@PathVariable String id) {
		SuperAdminOrders order = orderRepo.findById(Long.valueOf(id)).orElse(new SuperAdminOrders());
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition",
				"inline; filename=" + "file.pdf");

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(fileService.getFile(order.getFilePath(), order.getOrderName().trim()));
	}
	
	
	@GetMapping("/getRulepdf/{id}")
	public ResponseEntity<ByteArrayResource> getFileRule(@PathVariable String id) {
		DErules order = deruleRepo.findById(Long.valueOf(id)).orElse(new DErules());
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition",
				"inline; filename=file.pdf");

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(fileService.getFile("rules/", order.getRuleName().trim()+".pdf"));
	}
	
	
	@GetMapping("/getTemplate/{id}")
	public ResponseEntity<ByteArrayResource> getTemplate(@PathVariable String id) {
		Templates order = tempRepo.findById(Long.valueOf(id)).orElse(new Templates());
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition",
				"inline; filename=extension.docx");

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(fileService.getFile(order.getFilePath(), order.getOrderName().trim()));
	}

}
