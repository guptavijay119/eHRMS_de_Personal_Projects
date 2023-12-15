package com.ehrms.deptenq.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ehrms.deptenq.models.Designation;
import com.ehrms.deptenq.models.Districts;
import com.ehrms.deptenq.models.Division;
import com.ehrms.deptenq.models.Gender;
import com.ehrms.deptenq.models.GlobalOrg;
import com.ehrms.deptenq.models.InquiryOfficerList;
import com.ehrms.deptenq.models.Role;
import com.ehrms.deptenq.models.SecretarySubdepartmentMap;
import com.ehrms.deptenq.models.Service_Group;
import com.ehrms.deptenq.models.SubDepartment;
import com.ehrms.deptenq.models.User;
import com.ehrms.deptenq.models.WitnessData;
import com.ehrms.deptenq.modelsDto.DesignationDto;
import com.ehrms.deptenq.modelsDto.InquiryOfficerListDto;
import com.ehrms.deptenq.modelsDto.SecretarySubdepartmentMapDto;
import com.ehrms.deptenq.modelsDto.SubDepartmentDto;
import com.ehrms.deptenq.modelsDto.UserDto;
import com.ehrms.deptenq.modelsDto.WitnessDataDto;
import com.ehrms.deptenq.pims.PimsEmployee;
import com.ehrms.deptenq.repository.DesignationRepository;
import com.ehrms.deptenq.repository.DistrictsRepository;
import com.ehrms.deptenq.repository.GenderRepository;
import com.ehrms.deptenq.repository.IDivisionRepository;
import com.ehrms.deptenq.repository.IGlobalOrgRepository;
import com.ehrms.deptenq.repository.InquiryOfficerListRepository;
import com.ehrms.deptenq.repository.PimsEmployeeRepository;
import com.ehrms.deptenq.repository.RolesRepository;
import com.ehrms.deptenq.repository.SecretarySubdepartmentMapRepository;
import com.ehrms.deptenq.repository.Service_GroupRepository;
import com.ehrms.deptenq.repository.SubDepartmentRepository;
import com.ehrms.deptenq.repository.UserRepository;
import com.ehrms.deptenq.repository.WitnessDataRepository;


@Controller
@RequestMapping("departmentalEnquiry")
public class UserCreationController {
	
	
	
	@Autowired
	private SubDepartmentRepository subDepartment;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RolesRepository roleRepo;
		
	@Autowired
	private PimsEmployeeRepository empRepo;
	
	@Autowired
	private SecretarySubdepartmentMapRepository secSubMapRepo;
	
	@Autowired
	private IGlobalOrgRepository globRepo;
	
	@Autowired
	private Service_GroupRepository groupRepo;
	
	@Autowired
	private DesignationRepository desigRepo;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private SubDepartmentRepository subRepo;
	
	@Autowired
	private IDivisionRepository divRepo;
	
	@Autowired
	private InquiryOfficerListRepository inqlistRepo;
	
	@Autowired
	private DistrictsRepository distRepo;
	
	@Autowired
	private WitnessDataRepository witRepo;
	
	@Autowired
	private GenderRepository genRepo;

	
	
	@GetMapping("/getSubDepartmentList/{page}")
	public String getSubDepartmentList(Model model, RedirectAttributes redirect, @PathVariable("page") int page) {
		int index = 1;

		if (page != 0) {
			index = (page * 10) - (10 - 1);
			model.addAttribute("pageIndex", index);

		}

		if (page < 0) {

			page = 1;
		}

//		Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "id"));
//		Page<SubDepartment> noticePages = null;
		
//		noticePages = subDepartment.findAll(pageable);


//		model.addAttribute("subDepartmentList", noticePages);
//		model.addAttribute("currentPage", page);
//		int begin = Math.max(1, page - 5);
//		if (page >= 5) {
//			begin = page;
//		}
//		int end = Math.min(begin + 4, noticePages.getTotalPages());
//		model.addAttribute("begin", begin);
//		model.addAttribute("end", end);
//		model.addAttribute("totalPages", noticePages.getTotalPages());
//		model.addAttribute("totalItems", noticePages.getTotalElements());
//		model.addAttribute("noticelist", noticePages.getContent());
		model.addAttribute("noticelist", subDepartment.findAll());
		
		model.addAttribute("globalOrgList", globRepo.findByActive(true));
		
		return "mastertable/SubDepartment";
	}
	
	
	@PostMapping("/updateSubDepartment")
	@ResponseBody
	public ResponseEntity<?> updateSubDepartment(SubDepartmentDto obb) {
		if(obb.getId() != null && StringUtils.isNumeric(obb.getId())) {
			SubDepartment sub = subDepartment.findById(Long.valueOf(obb.getId())).orElse(new SubDepartment());
			sub.setSubdepartmentNameEn(obb.getSubDepartment());
			sub.setGlobalorg(globRepo.findById(obb.getGlobalOrg()).orElse(new GlobalOrg()));
			sub.setActive(obb.isActive());
			if(!obb.isActive()) {
				sub.setGlobalorg(null);
			}
			subDepartment.save(sub);
			return ResponseEntity.ok("Updated Successfully");
		} else {
			SubDepartment sub = new SubDepartment();
			sub.setSubdepartmentNameEn(obb.getSubDepartment());
			sub.setGlobalorg(globRepo.findById(obb.getGlobalOrg()).orElse(new GlobalOrg()));
			sub.setActive(obb.isActive());
			subDepartment.save(sub);
			return ResponseEntity.ok("Saved Successfully");
		}
		
		
//		return ResponseEntity.ok("Saved Successfully");
	}
	
	
	@GetMapping("/getUserList/{page}")
	public String getUserList(Model model, RedirectAttributes redirect, @PathVariable("page") int page) {
		int index = 1;

		if (page != 0) {
			index = (page * 10) - (10 - 1);
			model.addAttribute("pageIndex", index);

		}

		if (page < 0) {

			page = 1;
		}

//		Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "id"));
//		Page<User> noticePages = null;
		
//		noticePages = userRepo.findByProcessTypeAndPimsEmployeeIsNotNull("de",pageable);


//		model.addAttribute("subDepartmentList", noticePages);
//		model.addAttribute("currentPage", page);
//		int begin = Math.max(1, page - 5);
//		if (page >= 5) {
//			begin = page;
//		}
//		int end = Math.min(begin + 4, noticePages.getTotalPages());
//		model.addAttribute("begin", begin);
//		model.addAttribute("end", end);
//		model.addAttribute("totalPages", noticePages.getTotalPages());
//		model.addAttribute("totalItems", noticePages.getTotalElements());
//		model.addAttribute("noticelist", noticePages.getContent());
		model.addAttribute("noticelist", userRepo.findByProcessTypeAndPimsEmployeeIsNotNull("de"));
		model.addAttribute("globalOrgList", globRepo.findByActive(true));
//		model.addAttribute("subDepartmentList", subDepartment.findByActive(true));
//		model.addAttribute("roleList", roleRepo.findByIdIn(Arrays.asList(1L,48L)));
//		List<User> userPimsList = userRepo.findByPimsEmployeeNotNull();
//		List<Long> pimsIdList = new ArrayList<>();
//		userPimsList.stream().forEach(s->pimsIdList.add(s.getPimsEmployee().getId()));
//		model.addAttribute("pimsList1", empRepo.findByIdNotIn(pimsIdList));
//		model.addAttribute("pimsList2", empRepo.findAll());
		
		return "mastertable/UserList";
	}
	
	
	@PostMapping("/updateUser")
	@ResponseBody
	public ResponseEntity<?> updateUser(UserDto obb) {
		if(obb.getId() != null && StringUtils.isNumeric(obb.getId())) {
			User sub = userRepo.findById(Long.valueOf(obb.getId())).orElse(new User());
//			if(obb.getSubDepartment() != null && obb.getSubDepartment()>0) {
//				sub.setSubDepartment(subDepartment.findById(obb.getSubDepartment()).orElse(new SubDepartment()));
//			}
//			if(obb.getGlobalOrg() != null && obb.getGlobalOrg()>0) {
//				if(obb.getPimsid1() != null && obb.getPimsid1()>0) {
//					PimsEmployee pims = empRepo.findById(obb.getPimsid1()).orElse(new PimsEmployee());
//					pims.setGlobalOrgId(globRepo.findById(obb.getGlobalOrg()).orElse(new GlobalOrg()));
//					sub.setPimsEmployee(pims);
//				}
//				if(obb.getPimsid2() != null && obb.getPimsid2()>0) {
//					PimsEmployee pims = empRepo.findById(obb.getPimsid2()).orElse(new PimsEmployee());
//					pims.setGlobalOrgId(globRepo.findById(obb.getGlobalOrg()).orElse(new GlobalOrg()));
//					sub.setPimsEmployee(pims);
//				}
//			}
//			sub.setGlobalorg(globRepo.findById(obb.getGlobalOrg()).orElse(new GlobalOrg()));
				
			sub.setActive(obb.isActive());
//			sub.setEmail(obb.getEmail());
			sub = userRepo.save(sub);
			if(obb.getLoginType().equalsIgnoreCase("Secretary")) {

				if(!obb.getSecsubList().isEmpty()) {
					List<SecretarySubdepartmentMapDto> subdtolist = obb.getSecsubList();
					for(SecretarySubdepartmentMapDto dd : subdtolist) {
						if(secSubMapRepo.existsBySubDepartmentIdAndUsersecId(Long.valueOf(dd.getSubdepartmentid()),Long.valueOf(sub.getId()))) {
							SecretarySubdepartmentMap map = secSubMapRepo.findBySubDepartmentIdAndUsersecId(Long.valueOf(dd.getSubdepartmentid()),Long.valueOf(sub.getId()));
							map.setActive(Boolean.parseBoolean(dd.getSelection()));
							map.setUsersec(userRepo.findById(Long.valueOf(sub.getId())).orElse(new User()));
							secSubMapRepo.save(map);
						} else {
							SecretarySubdepartmentMap map = new SecretarySubdepartmentMap();
							map.setUsersec(userRepo.findById(Long.valueOf(sub.getId())).orElse(new User()));
							map.setSubDepartment(subDepartment.findById(Long.valueOf(dd.getSubdepartmentid())).orElse(new SubDepartment()));
							map.setActive(Boolean.parseBoolean(dd.getSelection()));
							secSubMapRepo.save(map);
						}
					}
				}
			}	
			
			return ResponseEntity.ok("Updated Successfully");
		} else {
			User sub = new User();
			if(obb.getLoginType() != null) {			
				if(obb.getLoginType().equalsIgnoreCase("SubDepartment") && obb.getSubDepartment() != null && obb.getSubDepartment()>0) {
					sub.setSubDepartment(subDepartment.findById(obb.getSubDepartment()).orElse(new SubDepartment()));
					Set<Role> roleList = roleRepo.findByIdIn(Arrays.asList(1L));
					sub.setRoles(roleList);
				}
				if(obb.getLoginType().equalsIgnoreCase("Secretary")) {
					Set<Role> roleList = roleRepo.findByIdIn(Arrays.asList(51L));
					sub.setRoles(roleList);					
				}				
				if(obb.getLoginType().equalsIgnoreCase("Department")) {
					Set<Role> roleList = roleRepo.findByIdIn(Arrays.asList(1L,48L));
					sub.setRoles(roleList);
				}
				sub.setLoginType(obb.getLoginType());								
			}
			if(obb.getGlobalOrg() != null && obb.getGlobalOrg()>0) {
				List<PimsEmployee> userList = empRepo.findByGlobalOrgIdId(obb.getGlobalOrg());
//				if(obb.getPimsid1() != null && obb.getPimsid1()>0) {
//					PimsEmployee pims = empRepo.findById(obb.getPimsid1()).orElse(new PimsEmployee());
//					pims.setGlobalOrgId(globRepo.findById(obb.getGlobalOrg()).orElse(new GlobalOrg()));
//					sub.setPimsEmployee(pims);
//				}
//				if(obb.getPimsid2() != null && obb.getPimsid2()>0) {
//					PimsEmployee pims = empRepo.findById(obb.getPimsid2()).orElse(new PimsEmployee());
//					pims.setGlobalOrgId(globRepo.findById(obb.getGlobalOrg()).orElse(new GlobalOrg()));
//					sub.setPimsEmployee(pims);
//				}
				if(!userList.isEmpty()) {
					sub.setPimsEmployee(userList.get(0));
				}
			}
//			Set<Role> roleList = roleRepo.findByIdIn(Arrays.asList(1L));
//			sub.setRoles(roleList);
			sub.setPassword(encoder.encode("ehrms"));
			sub.setProcessType("de");
//			sub.setGlobalorg(globRepo.findById(obb.getGlobalOrg()).orElse(new GlobalOrg()));
			sub.setActive(obb.isActive());
			sub.setEmail(obb.getEmail());
			sub = userRepo.save(sub);
			if(obb.getLoginType().equalsIgnoreCase("Secretary")) {

				if(!obb.getSecsubList().isEmpty()) {
					List<SecretarySubdepartmentMapDto> subdtolist = obb.getSecsubList();
					for(SecretarySubdepartmentMapDto dd : subdtolist) {
						if(secSubMapRepo.existsBySubDepartmentIdAndUsersecId(Long.valueOf(dd.getSubdepartmentid()),Long.valueOf(sub.getId()))) {
							SecretarySubdepartmentMap map = secSubMapRepo.findBySubDepartmentIdAndUsersecId(Long.valueOf(dd.getSubdepartmentid()),Long.valueOf(sub.getId()));
							map.setActive(Boolean.parseBoolean(dd.getSelection()));
							map.setUsersec(userRepo.findById(Long.valueOf(sub.getId())).orElse(new User()));
							secSubMapRepo.save(map);
						} else {
							SecretarySubdepartmentMap map = new SecretarySubdepartmentMap();
							map.setUsersec(userRepo.findById(Long.valueOf(sub.getId())).orElse(new User()));
							map.setSubDepartment(subDepartment.findById(Long.valueOf(dd.getSubdepartmentid())).orElse(new SubDepartment()));
							map.setActive(Boolean.parseBoolean(dd.getSelection()));
							secSubMapRepo.save(map);
						}
					}
				}
			}		
			
			
			return ResponseEntity.ok("Saved Successfully");
		}
		
		
//		return ResponseEntity.ok("Saved Successfully");
	}
	
	
	
	@GetMapping("getSubDepartmentListJson/{globid}")
	@ResponseBody
	public ResponseEntity<?> getSubDepartmentListJson(@PathVariable String globid) {
		List<SubDepartmentDto> dtoList = new ArrayList<>();
		if(globid != null) {
			List<SubDepartment> subDepartmentList = subRepo.findByGlobalorgIdAndActive(Long.valueOf(globid), true);
			if(!subDepartmentList.isEmpty()) {
				subDepartmentList.parallelStream().forEach(s->{
					SubDepartmentDto dto = new SubDepartmentDto();
					dto.setId(s.getId().toString());
					dto.setSubDepartment(s.getSubdepartmentNameEn());
					dtoList.add(dto);
				});
			}
		}
		
		return ResponseEntity.ok(dtoList);
	}
	
	

	@GetMapping("getusersecsubdep/{userid}")
	@ResponseBody
	public ResponseEntity<?> getusersecsubdep(@PathVariable String userid) {
		List<SubDepartmentDto> dtoList = new ArrayList<>();
		if(userid != null) {
			List<SecretarySubdepartmentMap> subDepartmentList = secSubMapRepo.findByUsersecId(Long.valueOf(userid));
			if(!subDepartmentList.isEmpty()) {
				subDepartmentList.parallelStream().forEach(s->{
					SubDepartmentDto dto = new SubDepartmentDto();
					dto.setId(s.getSubDepartment().getId().toString());
					dto.setSubDepartment(s.getSubDepartment().getSubdepartmentNameEn());
					dto.setActive(s.isActive());
					dtoList.add(dto);
				});
			}
		}
		
		return ResponseEntity.ok(dtoList);
	}
	
	
	
	@GetMapping("/getDesigList/{page}")
	public String getDesigList(Model model, RedirectAttributes redirect, @PathVariable("page") int page) {
		int index = 1;

		if (page != 0) {
			index = (page * 10) - (10 - 1);
			model.addAttribute("pageIndex", index);

		}

		if (page < 0) {

			page = 1;
		}

		model.addAttribute("desigList", desigRepo.findAll());
		model.addAttribute("groupList", groupRepo.findByOrderById());
		return "mastertable/desigList";
	}
	
	
	@PostMapping("/updateDesignation")
	@ResponseBody
	public ResponseEntity<?> updateDesignation(DesignationDto obb) {
		if(obb.getDesignation_id() != null) {
			Designation sub = desigRepo.findById(Long.valueOf(obb.getDesignation_id())).orElse(new Designation());
			sub.setDesignation_name_en(obb.getDescription()+" ("+obb.getDesignationCode()+")");
			sub.setDesignation_name_hi(sub.getDesignation_name_en());
			sub.setDescription(obb.getDescription());
			sub.setDesignationCode(obb.getDesignationCode());
			sub.setServiceGroupId(groupRepo.findById(obb.getServiceGroupId()).orElse(new Service_Group()));
			sub.setActive(obb.isActive());
			desigRepo.save(sub);
			return ResponseEntity.ok("Updated Successfully");
		} else {
			Designation sub = new Designation();
			sub.setDesignation_name_en(obb.getDescription()+" ("+obb.getDesignationCode()+")");
			sub.setDesignation_name_hi(sub.getDesignation_name_en());
			sub.setDescription(obb.getDescription());
			sub.setDesignationCode(obb.getDesignationCode());
			sub.setServiceGroupId(groupRepo.findById(obb.getServiceGroupId()).orElse(new Service_Group()));
			sub.setActive(obb.isActive());
			desigRepo.save(sub);
			return ResponseEntity.ok("Saved Successfully");
		}
		
		
//		return ResponseEntity.ok("Saved Successfully");
	}
	
	
	
	@GetMapping("/getInqList/{page}")
	public String getInqList(Model model, RedirectAttributes redirect, @PathVariable("page") int page) {
		int index = 1;

		if (page != 0) {
			index = (page * 10) - (10 - 1);
			model.addAttribute("pageIndex", index);

		}

		if (page < 0) {

			page = 1;
		}

		model.addAttribute("inqList", inqlistRepo.findAll(Sort.by("order")));
		model.addAttribute("divList", divRepo.findByActive(true, Sort.by("order")));
		return "mastertable/inqList";
	}
	
	
	@PostMapping("/updateInqList")
	@ResponseBody
	public ResponseEntity<?> updateInqList(InquiryOfficerListDto obb) {
		if(obb.getId() != null) {
			InquiryOfficerList sub = inqlistRepo.findById(obb.getId()).orElse(new InquiryOfficerList());
			sub.setInquiryOfficerlist(obb.getInquiryOfficerlist());
			sub.setActive(obb.isActive());
			sub.setDateofBirth(obb.getDateofBirth());
			sub.setDateofretirement(obb.getDateofretirement());
			sub.setDesignation(obb.getDesignation());
			sub.setEmail(obb.getEmail());
			sub.setFirstName(obb.getFirstName());
			sub.setMiddelName(obb.getMiddelName());
			sub.setLastName(obb.getLastName());
			sub.setMobileNumber(obb.getMobileNumber());
			sub.setLastOfficeName(obb.getLastOfficeName());
			sub.setOrder(obb.getOrder());
			if(obb.getDivision() != null) {
				sub.setDivision(divRepo.findById(obb.getDivision()).orElse(new Division()));
			}
			if(obb.getDistrict() != null) {
				sub.setDistrict(distRepo.findById(obb.getDistrict()).orElse(new Districts()));
			}
			
			inqlistRepo.save(sub);
			return ResponseEntity.ok("Updated Successfully");
		} else {
			InquiryOfficerList sub = new InquiryOfficerList();
			sub.setInquiryOfficerlist(obb.getInquiryOfficerlist());
			sub.setActive(obb.isActive());
			sub.setDateofBirth(obb.getDateofBirth());
			sub.setDateofretirement(obb.getDateofretirement());
			sub.setDesignation(obb.getDesignation());
			sub.setEmail(obb.getEmail());
			sub.setFirstName(obb.getFirstName());
			sub.setMiddelName(obb.getMiddelName());
			sub.setLastName(obb.getLastName());
			sub.setMobileNumber(obb.getMobileNumber());
			sub.setLastOfficeName(obb.getLastOfficeName());
			sub.setOrder(obb.getOrder());
			if(obb.getDivision() != null) {
				sub.setDivision(divRepo.findById(obb.getDivision()).orElse(new Division()));
			}
			if(obb.getDistrict() != null) {
				sub.setDistrict(distRepo.findById(obb.getDistrict()).orElse(new Districts()));
			}
			inqlistRepo.save(sub);
			return ResponseEntity.ok("Saved Successfully");
		}
		
		
//		return ResponseEntity.ok("Saved Successfully");
	}
	
	
	
	

	@GetMapping("/getWitList/{page}")
	public String getWitList(Model model, RedirectAttributes redirect, @PathVariable("page") int page) {
		int index = 1;

		if (page != 0) {
			index = (page * 10) - (10 - 1);
			model.addAttribute("pageIndex", index);

		}

		if (page < 0) {

			page = 1;
		}

		model.addAttribute("witList", witRepo.findAll());
		model.addAttribute("divList", divRepo.findByActive(true, Sort.by("order")));
		model.addAttribute("groupList", groupRepo.findByOrderById());
		model.addAttribute("orgList", globRepo.findByActive(true, Sort.by("id")));
		model.addAttribute("genList", genRepo.findByActiveOrderById(true));
		return "mastertable/witList";
	}
	
	
	@PostMapping("/updateWitList")
	@ResponseBody
	public ResponseEntity<?> updateWitList(WitnessDataDto obb) {
		if(obb.getId() != null) {
			WitnessData sub = witRepo.findById(obb.getId()).orElse(new WitnessData());
		
			sub.setActive(obb.isActive());
//			sub.setBirthDate(obb.getBirthDate());
//			sub.setSuperannuationDate(obb.getSuperannuationDate());
			sub.setEmail(obb.getEmail());
			sub.setFirstName(obb.getFirstName());
			sub.setMiddleName(obb.getMiddleName());
			sub.setLastName(obb.getLastName());
			sub.setSevarthId(obb.getSevarthid());
			sub.setMobino(obb.getMobino());
			
			sub.setOfficeName(obb.getOfficeName());
			
			if(obb.getDistid() != null) {
				sub.setDistrict(distRepo.findById(obb.getDistid()).orElse(new Districts()));
			}
			if(obb.getDivid() != null) {
				sub.setRevenueDivision(divRepo.findById(obb.getDivid()).orElse(new Division()));
			}
			
			if(obb.getGroupid() != null) {
				sub.setServicegroup(groupRepo.findById(obb.getGroupid()).orElse(new Service_Group()));
			}
			
			if(obb.getDesigid() != null) {
				sub.setDesignation(desigRepo.findById(obb.getDesigid()).orElse(new Designation()));
			}
			
			if(obb.getOrgid() != null) {
				sub.setGlobalorg(globRepo.findById(obb.getOrgid()).orElse(new GlobalOrg()));
			}
			
			if(obb.getGenderid() != null) {
				sub.setGender(genRepo.findById(obb.getGenderid()).orElse(new Gender()));
			}
			
			witRepo.save(sub);
			return ResponseEntity.ok("Updated Successfully");
		} else {
			WitnessData sub = new WitnessData();
			sub.setActive(obb.isActive());
//			sub.setBirthDate(obb.getBirthDate());
//			sub.setSuperannuationDate(obb.getSuperannuationDate());
			sub.setEmail(obb.getEmail());
			sub.setFirstName(obb.getFirstName());
			sub.setMiddleName(obb.getMiddleName());
			sub.setLastName(obb.getLastName());
			sub.setSevarthId(obb.getSevarthid());
			sub.setMobino(obb.getMobino());
			
			sub.setOfficeName(obb.getOfficeName());
			
			if(obb.getDistid() != null) {
				sub.setDistrict(distRepo.findById(obb.getDistid()).orElse(new Districts()));
			}
			if(obb.getDivid() != null) {
				sub.setRevenueDivision(divRepo.findById(obb.getDivid()).orElse(new Division()));
			}
			
			if(obb.getGroupid() != null) {
				sub.setServicegroup(groupRepo.findById(obb.getGroupid()).orElse(new Service_Group()));
			}
			
			if(obb.getDesigid() != null) {
				sub.setDesignation(desigRepo.findById(obb.getDesigid()).orElse(new Designation()));
			}
			
			if(obb.getOrgid() != null) {
				sub.setGlobalorg(globRepo.findById(obb.getOrgid()).orElse(new GlobalOrg()));
			}
			
			if(obb.getGenderid() != null) {
				sub.setGender(genRepo.findById(obb.getGenderid()).orElse(new Gender()));
			}
			
			witRepo.save(sub);
			return ResponseEntity.ok("Saved Successfully");
		}
		
		
//		return ResponseEntity.ok("Saved Successfully");
	}
	
	
	@GetMapping("resetpassword")
	public ResponseEntity<?> resetpassword(@RequestParam(name = "id",required = false) Long id) {
		if(id != null && userRepo.existsByIdAndProcessType(id,"de")) {
			User user = userRepo.findById(id).orElse(new User());
			user.setPassword(encoder.encode("ehrms"));
			userRepo.save(user);
		} else {
			return ResponseEntity.ok("Incorrect user id");
		}
		
		return ResponseEntity.ok("Password reset to default successfully");
	}
	
	

}
