package com.ehrms.deptenq.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.ehrms.deptenq.models.CaseDetails;
import com.ehrms.deptenq.models.CourtCaseDetails;
import com.ehrms.deptenq.models.Designation;
import com.ehrms.deptenq.models.DetailsKeptAbeyanceCases;
import com.ehrms.deptenq.models.Districts;
import com.ehrms.deptenq.models.EmployeeDetails;
import com.ehrms.deptenq.models.FinalOutcomeDetails;
import com.ehrms.deptenq.models.GlobalOrg;
import com.ehrms.deptenq.models.InquiryOfficerDetails;
import com.ehrms.deptenq.models.InquiryOfficerList;
import com.ehrms.deptenq.models.MisconductTypesMaster;
import com.ehrms.deptenq.models.NoDECertificate;
import com.ehrms.deptenq.models.PortalVersion;
import com.ehrms.deptenq.models.PresentingOfficerDetails;
import com.ehrms.deptenq.models.ProsecutionProposalDetails;
import com.ehrms.deptenq.models.ReInstatedDetails;
import com.ehrms.deptenq.models.Role;
import com.ehrms.deptenq.models.RulesApplicableMaster;
import com.ehrms.deptenq.models.Service_Group;
import com.ehrms.deptenq.models.SubDepartment;
import com.ehrms.deptenq.models.SuspensionDetails;
import com.ehrms.deptenq.models.User;
import com.ehrms.deptenq.modelsDto.ChangePassword;
import com.ehrms.deptenq.modelsDto.CustomDto;
import com.ehrms.deptenq.repository.CaseDetailsRepository;
import com.ehrms.deptenq.repository.DesignationRepository;
import com.ehrms.deptenq.repository.DetailsKeptAbeyanceCasesRepository;
import com.ehrms.deptenq.repository.DistrictsRepository;
import com.ehrms.deptenq.repository.FinalOutcomeRepository;
import com.ehrms.deptenq.repository.ICourtCaseDetailsRepository;
import com.ehrms.deptenq.repository.IEmployeeDetailsRepository;
import com.ehrms.deptenq.repository.IGlobalOrgRepository;
import com.ehrms.deptenq.repository.IPresentingOfficerRepository;
import com.ehrms.deptenq.repository.IProsecutionProposalDetailsRepository;
import com.ehrms.deptenq.repository.IReInstatedRepository;
import com.ehrms.deptenq.repository.InquiryOfficerListRepository;
import com.ehrms.deptenq.repository.InquiryOfficerRepository;
import com.ehrms.deptenq.repository.LoginAuditRepository;
import com.ehrms.deptenq.repository.MisconductTypesMasterRepository;
import com.ehrms.deptenq.repository.NoDeCertificateRepository;
import com.ehrms.deptenq.repository.PortalVersionRepository;
import com.ehrms.deptenq.repository.RulesApplicableMasterRepository;
import com.ehrms.deptenq.repository.Service_GroupRepository;
import com.ehrms.deptenq.repository.SubDepartmentRepository;
import com.ehrms.deptenq.repository.SuspensionDetailsRepository;
import com.ehrms.deptenq.repository.UserRepository;
import com.ehrms.deptenq.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;


@RestController
@RequestMapping("departmentalEnquiry")
public class DeRestControllerAPI {

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private IGlobalOrgRepository orgRepo;
	
	@Autowired
	private CaseDetailsRepository caseDetailsRepo;

	@Autowired
	private IEmployeeDetailsRepository empRepo;
	
	@Autowired
	private LoginAuditRepository logAudRepo;

	@Autowired
	private SuspensionDetailsRepository suspensionRepo;
	
	@Autowired
	private UserService userService;

	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private Service_GroupRepository groupRepo;
	
	@Autowired
	private DesignationRepository desigRepo;

	@Autowired
	private IReInstatedRepository reinstRepo;
	
	@Autowired
	private InquiryOfficerListRepository inqListRepo;

	@Autowired
	private InquiryOfficerRepository inqOffRepo;
	
	@Autowired
	private IPresentingOfficerRepository presentOffRepo;
	
	@Autowired
	private FinalOutcomeRepository finalOutRepo;
	
	@Autowired
	private DetailsKeptAbeyanceCasesRepository abeyRepo;
	
	@Autowired
	private ICourtCaseDetailsRepository courtCaseRepo;
	
	@Autowired
	private IProsecutionProposalDetailsRepository proRepo;
	
	@Autowired
	private RulesApplicableMasterRepository ruleMasterRepo;
	
	@Autowired
	private MisconductTypesMasterRepository misconductTypesmasterRepo;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private DistrictsRepository districtRepo;
	
	@Autowired
	private SubDepartmentRepository subDepartmentRepo;
	
	@Autowired
	private PortalVersionRepository portRepo;
	
	@Autowired
	private IProsecutionProposalDetailsRepository proseRepo;
	
	private static final String SUPERADMIN = "ROLE_SUPERADMIN";
	
	private static final String COMMONSERVICEIP = "localhost:8075/commonservice";

	
	
	public static final String COMMONSERVICEURLGET =  "http://"+COMMONSERVICEIP+"/getsevarthlist";
	
	private static final Logger LOG = LoggerFactory.getLogger(DepartmentWiseReportsController.class);
	
	/* code written by vijay on 20/10/2023 */
	@Autowired
	private NoDeCertificateRepository  nodecertificaterepository;
	
	
      // Added on 12-10-2022 as per DE Reports Screen For misconduct type
	@GetMapping(value = "getMisconductListjson")
	public ResponseEntity<?> getCaseDetailIds(@RequestParam("q") final String input) {
	
//		List<RulesApplicableMaster> empDetail = ruleMasterRepo.findByRuleNameContainingIgnoreCase(input);
		List<MisconductTypesMaster> misconductempdetails=misconductTypesmasterRepo.findByMisconductNameContainingIgnoreCase(input);
		List<String> idList = new ArrayList<>();
		misconductempdetails.stream().forEach(s -> {
			idList.add(s.getMisconductName());
		});
		return new ResponseEntity<>(idList, HttpStatus.OK);
	}  //methods ends here
	
	@GetMapping(value = "getRuleListjson")
	public ResponseEntity<?> getCaseDetailId(@RequestParam("q") final String input) {
		List<RulesApplicableMaster> empDetail = ruleMasterRepo.findByRuleNameContainingIgnoreCase(input);
		List<String> idList = new ArrayList<>();
		empDetail.stream().forEach(s -> {
			idList.add(s.getRuleName());
		});
		return new ResponseEntity<>(idList, HttpStatus.OK);
	}
	
	@GetMapping(value = "getVisitorCount")
	public ResponseEntity<?> getVisitorCount() {
//		User currentUser = userService.getCurrentUserPojo();
//		if(currentUser != null) {
			ObjectNode obb = objectMapper.createObjectNode();
			obb.put("visitorCount", logAudRepo.countDistinctEmailBySuccess(true));
			obb.put("siteupdatedate", portRepo.findById(1L).orElse(new PortalVersion()).getSiteupdateDate().toString());
			return new ResponseEntity<>(obb, HttpStatus.OK);
//		} else {
//			return new ResponseEntity<>(0, HttpStatus.OK);
//		}
	}
	
	
	@GetMapping(value = "getDistrictjson/{id}")
	public ResponseEntity<?> getDistrict(@PathVariable(name = "id") final String input) {
		List<Districts> districtList = districtRepo.findByDivisionIdIdAndActive(Long.valueOf(input),true,Sort.by("districtName"));	
		List<ObjectNode> idList = new ArrayList<>();
		districtList.stream().forEach(s -> {
			ObjectNode obb = objectMapper.createObjectNode();
			obb.put("id", s.getId());
			obb.put("name", s.getDistrictName());
			idList.add(obb);
		});
		return new ResponseEntity<>(idList, HttpStatus.OK);
	}
	
	
	
	@GetMapping(value = "getCaseListIdjson")
	public ResponseEntity<?> getCaseListIdJson(@RequestParam("q") final String input) {
		User currentUser = userService.getCurrentUserPojo();
		List<CaseDetails> empDetail = null;
		if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList()).contains(SUPERADMIN)) {
			empDetail = caseDetailsRepo.findCustomSuper(input);
		} else if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_DEPARTMENTADMIN")|| currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SECRETARY")) {
//			List<SubDepartment> subDepartmentList = ;
			List<SubDepartment> subList = currentUser.getSecsubList().stream().filter(s -> s.isActive())
					.map(s -> s.getSubDepartment()).collect(Collectors.toList());
			List<User> userList = new ArrayList<>();
			if(subList.isEmpty()) {
				userList = userRepository.findBySubDepartmentIn(
						subDepartmentRepo.findByGlobalorg(currentUser.getPimsEmployee().getGlobalOrgId()));
				userList.add(currentUser);
			} else {
				userList = userRepository.findBySubDepartmentIn(subList);
			}
			List<Long> userIdList = userList.parallelStream().map(s->s.getId()).collect(Collectors.toList());
			empDetail = caseDetailsRepo.findByIdInAndCurrentUserInCustom(input.trim(),userIdList);
		} else if(currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SECRETARYOLD")) {
			GlobalOrg og = currentUser.getPimsEmployee().getGlobalOrgId();
			empDetail = caseDetailsRepo.findByIdInAndGlobalOrgCustom(input.trim(), og.getId());
		}else {
			empDetail = caseDetailsRepo.findByIdInAndCurrentUserCustom(input.trim(),currentUser.getId());		
		}
		List<String> idList = new ArrayList<>();
		empDetail.stream().forEach(s -> {
			idList.add(s.getId().toString());
		});
		return new ResponseEntity<>(idList, HttpStatus.OK);
	}
	
	
	@GetMapping(value = "getCaseListjson")
	public ResponseEntity<?> getRuleList(@RequestParam("q") final String input) {
		User currentUser = userService.getCurrentUserPojo();
		List<CaseDetails> empDetail = null;
		if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList()).contains(SUPERADMIN)) {
			empDetail = caseDetailsRepo.findByCaseNoContainingIgnoreCase(input);
		} else if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_DEPARTMENTADMIN")) {
//			List<SubDepartment> subDepartmentList = ;
			List<User> userList = userRepository.findBySubDepartmentIn(
					subDepartmentRepo.findByGlobalorg(currentUser.getPimsEmployee().getGlobalOrgId()));
			userList.add(currentUser);
			empDetail = caseDetailsRepo.findByCaseNoContainingIgnoreCaseAndCurrentUserIn(input.trim(),userList);
		} else {
			empDetail = caseDetailsRepo.findByCaseNoContainingIgnoreCaseAndCurrentUser(input.trim(),currentUser);		
		}
		List<String> idList = new ArrayList<>();
		empDetail.stream().forEach(s -> {
			idList.add(s.getCaseNo());
		});
		return new ResponseEntity<>(idList, HttpStatus.OK);
	}
	
	
	@GetMapping(value = "getFileNojson")
	public ResponseEntity<?> getFileNo(@RequestParam("q") final String input) {
		User currentUser = userService.getCurrentUserPojo();
		List<SuspensionDetails> empDetail = null;
		if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList()).contains(SUPERADMIN)) {
			empDetail = suspensionRepo.findByFileNoContainingIgnoreCase(input);
		} else if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_DEPARTMENTADMIN")) {
			List<User> userList = userRepository.findBySubDepartmentIn(
					subDepartmentRepo.findByGlobalorg(currentUser.getPimsEmployee().getGlobalOrgId()));
			userList.add(currentUser);
			empDetail = suspensionRepo.findByFileNoContainingIgnoreCaseAndCurrentUserIn(input.trim(),userList);		
		} else {
			empDetail = suspensionRepo.findByFileNoContainingIgnoreCaseAndCurrentUser(input.trim(),currentUser);		
		}
		List<String> idList = new ArrayList<>();
		empDetail.stream().forEach(s -> {
			idList.add(s.getFileNo());
		});
		return new ResponseEntity<>(idList, HttpStatus.OK);
	}
	

	@GetMapping("/getempdetailJsons")
	public ObjectNode getEmpDetailsJson(@RequestParam(name = "caseid",required = false) String caseid) {
		if (caseid != null) {
			User currentUser = userService.getCurrentUserPojo();
			List<EmployeeDetails> empDList = null;
			if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList()).contains(SUPERADMIN)) {
				empDList = empRepo.findByCasedetailsCaseNo(caseid);
			} else {
				empDList = empRepo.findByCasedetailsCaseNoAndCurrentUser(caseid,currentUser);
			}
			ObjectNode obb = objectMapper.createObjectNode();
			if (empDList != null && !empDList.isEmpty()) {
				EmployeeDetails empD =empDList.get(0);
				if(empD.getCasedetails().getEmployeeType() != null && empD.getCasedetails().getEmployeeType().getId()==1) {
					obb.put("showaddmore", false);
				} else {
					obb.put("showaddmore", true);
				}
				obb.put("id", empD.getId());
				obb.put("empcaseid", empD.getCasedetails().getId());

				obb.put("empgroupid", empD.getSevarthId());
				obb.put("empid", empD.getEmployeeId());
				obb.put("firstname", empD.getFirstName());
				obb.put("middleName", empD.getMiddleName());
				obb.put("lastName", empD.getLastName());
				//changes done as per DE requirements
				obb.put("subDepartmentName", empD.getSubDepartment());
				
				if (empD.getGender() != null) {
					obb.put("gender", empD.getGender().getId());
				} else {
					obb.put("gender", "");
				}
				if (empD.getJoinCurrentOrgdate() != null) {
					obb.put("joinCurrentOrgdate", empD.getJoinCurrentOrgdate().toString());
				} else {
					obb.put("joinCurrentOrgdate", "");
				}
				if (empD.getLocaldate() != null) {
					obb.put("localdate", empD.getLocaldate().toString());
				} else {
					obb.put("localdate", "");
				}
				if (empD.getSuperannuationDate() != null) {
					obb.put("superannuationDate", empD.getSuperannuationDate().toString());
				} else {
					obb.put("superannuationDate", "");
				}
				if (empD.getRevenueDivision() != null) {
					obb.put("revenueDivision", empD.getRevenueDivision().getId());
				} else {
					obb.put("revenueDivision", "");
				}
				if (empD.getGlobalorg() != null) {
					obb.put("globalorg", empD.getGlobalorg().getId());
				} else {
					obb.put("globalorg", "");
				}
				/*
				 * if (empD.getSubDepartment() != null) { obb.put("subDepartmentName",
				 * empD.getId()); } else { obb.put("subDepartmentName", ""); }
				 */

				if (empD.getServicegroup() != null) {
					obb.put("servicegroup", empD.getServicegroup().getId());
				} else {
					obb.put("servicegroup", "");
				}

				if (empD.getDesignation() != null) {
					obb.put("designation", empD.getDesignation().getDesignation_id());
				} else {
					obb.put("designation", "");
				}
			} else {
				obb.put("error", "Data not found");
			}

			return obb;
		}
		return null;
	}
	
	@GetMapping("/getfilenodetailJsons")
	public ObjectNode getfilenodetailsJson(@RequestParam(name = "caseid",required = false) String caseid) {
		if (caseid != null) {
			User currentUser = userService.getCurrentUserPojo();
			ObjectNode obb = objectMapper.createObjectNode();
			SuspensionDetails empD = suspensionRepo.findByFileNoAndCurrentUser(caseid,currentUser);
			if(empD != null) {
				obb.put("sevarthId", empD.getSevarthId());
				obb.put("firstname", empD.getFirstName());
				obb.put("middleName", empD.getMiddleName());
				obb.put("lastName", empD.getLastName());
			}
			return obb;
		}
		return null;
	}
	
	@GetMapping("/getEmployeeDetailsJson")
	public ObjectNode getEmployeeDetailsJson(@RequestParam(name = "caseid",required = false) String caseid) {
		if (caseid != null) {
			User currentUser = userService.getCurrentUserPojo();
			ObjectNode obb = objectMapper.createObjectNode();
			EmployeeDetails empD = empRepo.findByIdAndCurrentUser(Long.valueOf(caseid),currentUser);
			if(empD != null) {
				obb.put("sevarthId", empD.getSevarthId());
				obb.put("firstName", empD.getFirstName());
				obb.put("middleName", empD.getMiddleName());
				obb.put("lastName", empD.getLastName());
			}
			return obb;
		}
		return null;
	}

	@GetMapping("/getcasedetailJsons")
	public ObjectNode getCaseDetailsJson(@RequestParam(name = "caseid",required = false) String caseid) {
		if (caseid != null) {
			User currentUser = userService.getCurrentUserPojo();
			CaseDetails caseDetail = null;
			if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList()).contains(SUPERADMIN)) {
				caseDetail = caseDetailsRepo.findByCaseNo(caseid);
			} else {
				caseDetail = caseDetailsRepo.findByCaseNoAndCurrentUser(caseid,currentUser);
			}
			ObjectNode obb = objectMapper.createObjectNode();
			if (caseDetail != null) {
				obb.put("id", caseDetail.getId());
				obb.put("caseNo", caseDetail.getCaseNo());
				obb.put("fileSubject", caseDetail.getFileSubject());
				if (caseDetail.getMisConductType() != null) {
					obb.put("misConductType", caseDetail.getMisConductType().getId());
				} else {
					obb.put("misConductType", "");
				}
				if (caseDetail.getRuleApplicable() != null) {
					obb.put("ruleApplicable", caseDetail.getRuleApplicable().getId());
				} else {
					obb.put("ruleApplicable", "");
				}
				if (caseDetail.getEmployeeType() != null) {
					obb.put("delinquentsInvolved", caseDetail.getEmployeeType().getId());
				} else {
					obb.put("delinquentsInvolved", "");
				}
				obb.put("totalNoOfEmployee", caseDetail.getTotalNoOfEmployee());
				obb.put("caseSummary", caseDetail.getCaseSummary());
			} else {
				obb.put("error", "Data not found");
			}

			return obb;
		}
		return null;
	}

	@GetMapping("/getsuspensionJsons")
	public ObjectNode getSuspensionJson(@RequestParam(name = "caseid",required = false) String caseid) {
		if (caseid != null) {
			User currentUser = userService.getCurrentUserPojo();
			List<SuspensionDetails> empDList = null;
			if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList()).contains(SUPERADMIN)) {
				empDList = suspensionRepo.findByCasedetailsCaseNo(caseid);
			} else {
				empDList = suspensionRepo.findByCasedetailsCaseNoAndCurrentUser(caseid,currentUser);
			}
			ObjectNode obb = objectMapper.createObjectNode();
			if (empDList != null && !empDList.isEmpty()) {
				SuspensionDetails empD = empDList.get(0);
				obb.put("id", empD.getId());
				obb.put("employeeSuspendedNo", empD.isEmployeeSuspended());
				obb.put("SusCaseId", empD.getCasedetails().getCaseNo());
				obb.put("sevarthId", empD.getSevarthId());
				obb.put("employeeId", empD.getEmployeeId());
				obb.put("firstname", empD.getFirstName());
				obb.put("middleName", empD.getMiddleName());
				obb.put("lastName", empD.getLastName());
				if (empD.getSusUnderRule() != null) {
					obb.put("underRule", empD.getSusUnderRule().getId());
				} else {
					obb.put("underRule", "");
				}
				if (empD.getDateOfOrder() != null) {
					obb.put("dateOfOrder", empD.getDateOfOrder().toString());
				} else {
					obb.put("dateOfOrder", "");
				}
				if (empD.getDateofActualSuspension() != null) {
					obb.put("dateofActualSuspension", empD.getDateofActualSuspension().toString());
				} else {
					obb.put("dateofActualSuspension", "");
				}
				if (empD.getDateOfOrder1() != null) {
					obb.put("dateOfOrder1", empD.getDateOfOrder1().toString());
				} else {
					obb.put("dateOfOrder1", "");
				}

				if (empD.getDateofDeemedSuspension() != null) {
					obb.put("dateofDeemedSuspension", empD.getDateofDeemedSuspension().toString());
				} else {
					obb.put("dateofDeemedSuspension", "");
				}

				obb.put("employeeSuspended", empD.isEmployeeSuspended());
				obb.put("whetherSuspensionIsExtnd3Months", empD.isWhetherSuspensionIsExtnd3Months());
				obb.put("periodofExtension", empD.getPeriodofExtension());
				obb.put("periodofExtensionInDays", empD.getPeriodofExtensionInDays());

				if (empD.getDateofExtensionOrder() != null) {
					obb.put("dateofExtensionOrder", empD.getDateofExtensionOrder().toString());
				} else {
					obb.put("dateofExtensionOrder", "");
				}

				return obb;
			} else {
				obb.put("error", "Data not found");
				return obb;
			}
		}
		return null;
	}

	@GetMapping("/getreinstatedJsons")
	public ObjectNode getReinstatedJson(@RequestParam(name = "caseid",required = false) String caseid) {
		if (caseid != null) {
			User currentUser = userService.getCurrentUserPojo();
			List<ReInstatedDetails> empDList = null;
			if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList()).contains(SUPERADMIN)) {
				empDList = reinstRepo.findByCasedetailsCaseNo(caseid);
			} else {
				empDList = reinstRepo.findByCasedetailsCaseNoAndCurrentUser(caseid,currentUser);
			}
			ObjectNode obb = objectMapper.createObjectNode();
			if (empDList != null && !empDList.isEmpty()) {
				ReInstatedDetails empD = empDList.get(0);
				obb.put("id", empD.getId());
				obb.put("casedetails", empD.getCasedetails().getCaseNo());
				obb.put("sevarthId", empD.getSevarthId());
				obb.put("empid", empD.getEmployeeId());
				obb.put("firstname", empD.getFirstName());
				obb.put("middleName", empD.getMiddleName());
				obb.put("lastName", empD.getLastName());
				obb.put("employeeReInstated", empD.isEmployeeReInstated());
				if (empD.getReInstatedOrderDate() != null) {
					obb.put("reInstatedOrderDate", empD.getReInstatedOrderDate().toString());
				} else {
					obb.put("reInstatedOrderDate", "");
				}

				return obb;
			} else {
				obb.put("error", "Data not found");
				return obb;
			}
		}
		return null;
	}

	@GetMapping("/getinqofficerJsons")
	public ObjectNode getinqofficerJson(@RequestParam(name = "caseid",required = false) String caseid) {
		if (caseid != null) {
			User currentUser = userService.getCurrentUserPojo();
			InquiryOfficerDetails empD = null;
			if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList()).contains(SUPERADMIN)) {
				empD = inqOffRepo.findByCasedetails(caseDetailsRepo.findByCaseNo(caseid));
			} else {
				empD = inqOffRepo.findByCasedetails(caseDetailsRepo.findByCaseNoAndCurrentUser(caseid,currentUser));
			}
			ObjectNode obb = objectMapper.createObjectNode();
			if (empD != null) {
				obb.put("id", empD.getId());
				obb.put("casedetails", empD.getCasedetails().getCaseNo());
				obb.put("inquiryOfficerAppointed", empD.isInquiryOfficerAppointed());
				if (empD.getAppointmentDate() != null) {
					obb.put("appointmentDate", empD.getAppointmentDate().toString());
				} else {
					obb.put("appointmentDate", "");
				}

				obb.put("firstname", empD.getFirstName());
				obb.put("middleName", empD.getMiddleName());
				obb.put("lastName", empD.getLastName());
				obb.put("listOfInquiryOfficer", empD.getListOfInquiryOfficer().getInquiryOfficerlist());
				obb.put("mobileNumber", empD.getMobileNumber());
				obb.put("email", empD.getEmail());

				return obb;
			} else {
				obb.put("error", "Data not found");
				return obb;
			}
		}
		return null;
	}
	
	@GetMapping("/getinqofficerList")
	public List<ObjectNode> getinqofficerList(@RequestParam(name = "caseid",required = false) String caseid) {
		List<ObjectNode> obbList = new ArrayList<>();
		if (caseid != null) {
			List<InquiryOfficerList> inqList = inqListRepo.findByDesignationContainingIgnoreCase(caseid);
			
			if(!inqList.isEmpty()) {
				inqList.forEach(s->{
					ObjectNode obb = objectMapper.createObjectNode();					
					obb.put("id", s.getId());
					obb.put("firstName", s.getFirstName());
					obb.put("lastName", s.getLastName());
					obb.put("middleName", s.getMiddelName());
					obb.put("app", s.getAppellation());
					obb.put("email", s.getEmail());
					obb.put("mobile", s.getMobileNumber());
					obbList.add(obb);					
				});
				return obbList;
			}
			
			
		}
		return obbList;
	}
	
	@GetMapping("/getinqofficerListOne")
	public ObjectNode getinqofficerListOne(@RequestParam(name = "caseid",required = false) String caseid) {
		ObjectNode obb = objectMapper.createObjectNode();	
		if (caseid != null) {
			InquiryOfficerList s = inqListRepo.findById(Long.valueOf(caseid)).orElse(new InquiryOfficerList());
			obb.put("id", s.getId());
			obb.put("firstName", s.getFirstName());
			obb.put("lastName", s.getLastName());
			obb.put("middleName", s.getMiddelName());
			obb.put("app", s.getAppellation());
			obb.put("email", s.getEmail());
			obb.put("mobile", s.getMobileNumber());
			obb.put("designation",s.getDesignation());			
		}
		return obb;
	}
	
	
	@GetMapping("/getpresentingofficerJsons")
	public ObjectNode getpresentingofficerJson(@RequestParam(name = "caseid",required = false) String caseid) {
		if (caseid != null) {
			User currentUser = userService.getCurrentUserPojo();
			PresentingOfficerDetails empD = null;
			if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList()).contains(SUPERADMIN)) {
				 empD = presentOffRepo.findByCasedetails(caseDetailsRepo.findByCaseNo(caseid));
			} else {
				 empD = presentOffRepo.findByCasedetails(caseDetailsRepo.findByCaseNoAndCurrentUser(caseid,currentUser));
			}
			ObjectNode obb = objectMapper.createObjectNode();
			if (empD != null) {
				obb.put("id", empD.getId());
				obb.put("casedetails", empD.getCasedetails().getCaseNo());
				obb.put("presentingOfficerAppointed", empD.isPresentingOfficerAppointed());
				if (empD.getGlobalorg() != null) {
					obb.put("globalorg", empD.getGlobalorg().getId());
				} else {
					obb.put("globalorg", "");
				}
				
				if (empD.getSubDepartment() != null) {
					obb.put("subDepartmentName", empD.getSubDepartment().getId());
				} else {
					obb.put("subDepartmentName", "");
				}
				if (empD.getDesignation() != null) {
					obb.put("designation", empD.getDesignation().getDesignation_id());
				} else {
					obb.put("designation", "");
				}

				obb.put("firstname", empD.getFirstName());
				obb.put("middleName", empD.getMiddleName());
				obb.put("lastName", empD.getLastName());
				obb.put("mobileNumber", empD.getMobileNumber());
				obb.put("email", empD.getEmail());
				if(empD.getRevenueDivision() != null) {
					obb.put("revenueDivision", empD.getRevenueDivision().getId());
				} else {
					obb.put("revenueDivision", "");
				}

				return obb;
			} else {
				obb.put("error", "Data not found");
				return obb;
			}
		}
		return null;
	}

	
	@GetMapping("/getfinaloutcomeJsons")
	public ObjectNode getfinaloutcomeJsons(@RequestParam(name = "caseid",required = false) String caseid) {
		if (caseid != null) {
			User currentUser = userService.getCurrentUserPojo();
			List<FinalOutcomeDetails> empDList = null;
			if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList()).contains(SUPERADMIN)) {
				empDList = finalOutRepo.findByCasedetails(caseDetailsRepo.findByCaseNo(caseid));
			} else {
				empDList = finalOutRepo.findByCasedetails(caseDetailsRepo.findByCaseNoAndCurrentUser(caseid,currentUser));
			}
			ObjectNode obb = objectMapper.createObjectNode();
			if (empDList != null && !empDList.isEmpty() ) {
				FinalOutcomeDetails empD = empDList.get(0);
				obb.put("id", empD.getId());
				obb.put("casedetails", empD.getCasedetails().getCaseNo());
				obb.put("inquiryReportReceived", empD.isInquiryReportReceived());
				if (empD.getDateofReceipt() != null) {
					obb.put("dateofReceipt", empD.getDateofReceipt().toString());
				} else {
					obb.put("dateofReceipt", "");
				}
				
				if (empD.getCasePendingWith()!= null) {
					obb.put("reason", empD.getCasePendingWith().getId());
				} else {
					obb.put("reason", "");
				}
				if (empD.getDecisionTaken() != null) {
					obb.put("decisionTaken", empD.getDecisionTaken().getId());
				} else {
					obb.put("decisionTaken", "");
				}
				
				if (empD.getTypeOfPenaltyInflicted() != null) {
					obb.put("getTypeOfPenaltyInflicted", empD.getTypeOfPenaltyInflicted().getId());
				} else {
					obb.put("getTypeOfPenaltyInflicted", "");
				}

				obb.put("whetherCaseFinallyDecided", empD.isWhetherCaseFinallyDecided());


				return obb;
			} else {
				obb.put("error", "Data not found");
				return obb;
			}
		}
		return null;
	}
	
	@GetMapping("/getAbeyanceCasesdetailsJsons")
	public ObjectNode getAbeyanceCasesdetailsJsons(@RequestParam(name = "caseid",required = false) String caseid) {
		if (caseid != null) {
			User currentUser = userService.getCurrentUserPojo();
			DetailsKeptAbeyanceCases empD = null;
			if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList()).contains(SUPERADMIN)) {
				empD = abeyRepo.findByCasedetails(caseDetailsRepo.findByCaseNo(caseid));
			} else {
				empD = abeyRepo.findByCasedetails(caseDetailsRepo.findByCaseNoAndCurrentUser(caseid,currentUser));
			}
			ObjectNode obb = objectMapper.createObjectNode();
			if (empD != null) {
				obb.put("id", empD.getId());
				obb.put("casedetails", empD.getCasedetails().getCaseNo());
				obb.put("sevarthId", empD.getSevarthId());
				obb.put("empid", empD.getEmployeeId());
				obb.put("firstname", empD.getFirstName());
				obb.put("middleName", empD.getMiddleName());
				obb.put("lastName", empD.getLastName());

				if (empD.getReasonreasonKeepingInAbeyance() != null) {
					obb.put("reason", empD.getReasonreasonKeepingInAbeyance().getId());
				} else {
					obb.put("reason", "");
				}
				obb.put("casesKeptinAbeyance", empD.isCasesKeptinAbeyance());

				return obb;
			} else {
				obb.put("error", "Data not found");
				return obb;
			}
		}
		return null;
	}
	
	
	@GetMapping("/getcourtCasedetailsJsons")
	public ObjectNode getcourtCasedetailsJsons(@RequestParam(name = "caseid",required = false) String caseid) {
		if (caseid != null) {
			User currentUser = userService.getCurrentUserPojo();
			List<CourtCaseDetails> empDList = null;
			if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList()).contains(SUPERADMIN)) {
				empDList = courtCaseRepo.findByCasedetailsCaseNo(caseid);
			} else {
				empDList = courtCaseRepo.findByCasedetailsCaseNoAndCurrentUser(caseid,currentUser);
			}
			ObjectNode obb = objectMapper.createObjectNode();
			if (empDList != null && !empDList.isEmpty()) {
				CourtCaseDetails empD = empDList.get(0);
				obb.put("id", empD.getId());
				obb.put("casedetails", empD.getCasedetails().getCaseNo());
				obb.put("sevarthId", empD.getSevarthId());
				obb.put("anyRelatedCourtCaseExist", empD.isAnyRelatedCourtCaseExist());
				obb.put("firstname", empD.getFirstName());
				obb.put("middleName", empD.getMiddleName());
				obb.put("lastName", empD.getLastName());

				if (empD.getCourtName() != null) {
					obb.put("courtName", empD.getCourtType().getId());
				} else {
					obb.put("courtName", "");
				}
				
				if (empD.getDateOfFinalOrder() != null) {
					obb.put("dateOfFinalOrder", empD.getDateOfFinalOrder().toString());
				} else {
					obb.put("dateOfFinalOrder", "");
				}
				
				if (empD.getStatus() != null) {
					obb.put("status", empD.getStatus());
				} else {
					obb.put("status", "");
				}
				obb.put("courtCaseNo", empD.getCourtCaseNo());

				return obb;
			} else {
				obb.put("error", "Data not found");
				return obb;
			}
		}
		return null;
	}
	
	@GetMapping("/getprosecutionJsons")
	public ObjectNode getprosecutionJsons(@RequestParam(name = "caseid",required = false) String caseid) {
		if (caseid != null) {
			User currentUser = userService.getCurrentUserPojo();
			ProsecutionProposalDetails empD = null;
			if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList()).contains(SUPERADMIN)) {
				empD = proRepo.findByCasedetails(caseDetailsRepo.findByCaseNo(caseid)); 
			} else {
				empD = proRepo.findByCasedetails(caseDetailsRepo.findByCaseNoAndCurrentUser(caseid,currentUser));
			}
			ObjectNode obb = objectMapper.createObjectNode();
			if (empD != null) {
				obb.put("id", empD.getId());
				obb.put("casedetails", empD.getCasedetails().getCaseNo());
				obb.put("prosecutionProposalReceived", empD.isProsecutionProposalReceived());
				if (empD.getDateOfReceipt() != null) {
					obb.put("dateOfReceipt", empD.getDateOfReceipt().toString());
				} else {
					obb.put("dateOfReceipt", "");
				}
				
				if (empD.getDateOfOrderReceipt()!= null) {
					obb.put("dateOfOrderReceipt", empD.getDateOfOrderReceipt().toString());
				} else {
					obb.put("dateOfOrderReceipt", "");
				}
				if (empD.getDateOfCommunication() != null) {
					obb.put("dateOfCommunication", empD.getDateOfCommunication().toString());
				} else {
					obb.put("dateOfCommunication", "");
				}
				
				if (empD.getDateOfOrderoftheCourt() != null) {
					obb.put("dateOfOrderoftheCourt", empD.getDateOfOrderoftheCourt().toString());
				} else {
					obb.put("dateOfOrderoftheCourt", "");
				}

				obb.put("statusForGrantedDeniedPending", empD.getStatusForGrantedDeniedPending());
				obb.put("whetherCaseFiledIntheCourt", empD.isWhetherCaseFiledIntheCourt());
				obb.put("status", empD.getStatus());
				return obb;
			} else {
				obb.put("error", "Data not found");
				return obb;
			}
		}
		return null;
	}
	
	
	@GetMapping("/selectdesignationdata/{id}")
    public ResponseEntity<?> getDesignation(@PathVariable long id) {
    	Service_Group group = groupRepo.findById(id).orElse(new Service_Group());
    	List<ObjectNode> idList = new ArrayList<>();
    	
		List<Designation> desiList = desigRepo.findByServiceGroupId(group);
	
		desiList.stream().forEach(s->{
			ObjectNode obb = objectMapper.createObjectNode();
			obb.put("id", s.getDesignation_id());
			obb.put("name", s.getDesignation_name_en());
			idList.add(obb);
    	});
    	return new ResponseEntity<>(idList,HttpStatus.OK);
    }
	
	
	@PostMapping("/changepassword")
	public ResponseEntity<?> updateSubDepartment(ChangePassword obb) {
		if(StringUtils.isNotBlank(obb.getOldpassword()) && StringUtils.isNotBlank(obb.getNewpassword()) && StringUtils.isNotBlank(obb.getConfirmnewpassword())) {
			if(obb.getNewpassword().equals(obb.getConfirmnewpassword())) {
				User currentUser = userService.getCurrentUserPojo();
				if(passwordEncoder.matches(obb.getOldpassword(), currentUser.getPassword())) {
					currentUser.setPassword(passwordEncoder.encode(obb.getNewpassword()));
					userRepository.save(currentUser);
					return ResponseEntity.ok("Updated Successfully");
				}
			} else {
				return ResponseEntity.ok("New Password and Confirm New Password not matched");
			}
			
		} else {
			
			return ResponseEntity.ok("Old Password or New Password can not be blank");
		}
		
		
		return ResponseEntity.ok("Saved Successfully");
	}
	

	@GetMapping("/getTenantId")
	public ResponseEntity<?> getTenantId(@RequestParam(name = "caseid",required = false) String caseid) {
		if(caseid != null) {
			GlobalOrg org = orgRepo.findByGlobalOrgName(caseid);
			if(org != null) {
				return ResponseEntity.ok(org.getTenantId());
			} else {
				return ResponseEntity.ok("");
			}
		} else {
			return ResponseEntity.ok("");
		}
	}
	
	@SuppressWarnings("rawtypes")
	@GetMapping(value = "getdeinformation")
	public ResponseEntity<?> getdeinformation(@RequestParam("caseid") final String input) {
		List<GlobalOrg> orgList = orgRepo.findByActive(true, Sort.by("id"));
		List<ObjectNode> obbList =new ArrayList<>();
		List<Boolean> idList = new ArrayList<>();
		for(GlobalOrg org : orgList) {
//			if(org.getId()!=701) {
//				continue;
//			}
					ResponseEntity<Boolean> data = restTemplate
							.getForEntity("http://localhost:8075/commonservice/serviceAPI/checksevarthbyorg/"
									+ org.getId() + "?q=" + StringUtils.upperCase(input), Boolean.class);
					idList.add(data.getBody());

		}
		boolean notguilty = false;
		if(!idList.contains(false)) {
			notguilty = true;
		}
		List<EmployeeDetails> empList = empRepo.findDistinctCasedetailsBySevarthIdAndFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsDecisionTakenId(input.trim(),true,2L);
		
		List<EmployeeDetails> empList2 = empRepo.findDistinctCasedetailsBySevarthIdAndFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsDecisionTakenId(input.trim(),true,1L);
		
		
		List<EmployeeDetails> empList4 = empRepo.findDistinctCasedetailsBySevarthIdAndFinalOutcomeDetailsWhetherCaseFinallyDecided(input.trim(),false);
		
		List<EmployeeDetails> empList5 = empRepo.findDistinctCasedetailsBySevarthIdAndFinalOutcomeDetailsIsNullOrSevarthIdAndFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsDecisionTakenIdOrSevarthIdAndFinalOutcomeDetailsWhetherCaseFinallyDecided(input.trim(),input.trim(),true,1L,input.trim(),false);
		
		List<SuspensionDetails> empList6 = suspensionRepo.findDistinctFileNoBySevarthIdAndEmpDataIsNullAndFileNoIsNotNullAndAndFileNoIsNotAndCasedetailsIsNull(input.trim(),"");
//		
		if((!empList.isEmpty() && empList2.isEmpty() && empList4.isEmpty())) {
			empList.stream().forEach(s->{
				ObjectNode obb = objectMapper.createObjectNode();
				obb.put("employeeName", s.getFirstName()+" "+s.getMiddleName()+" "+s.getLastName());
				obb.put("sevarthid", s.getSevarthId());
				obb.put("caseno", s.getCasedetails().getCaseNo());
				obb.put("caseid", s.getCasedetails().getId());
				if(s.getDesignation() != null) {
					obb.put("designationName", s.getDesignation().getDesignation_name_en());
				} else {
					obb.put("designationName", "");
				}
				if(s.getFinalOutcomeDetails() != null && s.getFinalOutcomeDetails().getDecisionTaken() != null) {
					obb.put("finaldecision",s.getFinalOutcomeDetails().getDecisionTaken().getDecisionNameHindiEn());
				} else {
					obb.put("finaldecision","");
				}
				obb.put("department", s.getCasedetails().getCurrentUser().getPimsEmployee().getGlobalOrgId().getGlobalOrgName());
				if(s.getCasedetails().getCurrentUser().getSubDepartment() != null) {
					obb.put("subdepartment", s.getCasedetails().getCurrentUser().getSubDepartment().getSubdepartmentNameEn());
				} else {
					obb.put("subdepartment", "");
				}
				obb.put("vigilancestatus", "nodatafound");
				obbList.add(obb);
			});
		} else if(empList5 != null && !empList5.isEmpty()) {
			empList5.stream().forEach(s->{
//			EmployeeDetails s = empList5.get(0);
				ObjectNode obb = objectMapper.createObjectNode();
				obb.put("employeeName", s.getFirstName()+" "+s.getMiddleName()+" "+s.getLastName());
				obb.put("sevarthid", s.getSevarthId());
				obb.put("caseno", s.getCasedetails().getCaseNo());
				obb.put("caseid", s.getCasedetails().getId());
				if(s.getDesignation() != null) {
					obb.put("designationName", s.getDesignation().getDesignation_name_en());
				} else {
					obb.put("designationName", "");
				}
				
				if(s.getFinalOutcomeDetails() != null && s.getFinalOutcomeDetails().getDecisionTaken() != null) {
					obb.put("finaldecision",s.getFinalOutcomeDetails().getDecisionTaken().getDecisionNameHindiEn());
				} else {
					if(s.getCasedetails().getPendingWith() != null) {
						obb.put("finaldecision",s.getCasedetails().getPendingWith());
					} else {
						obb.put("finaldecision","");
					}
				}
				obb.put("department", s.getCasedetails().getCurrentUser().getPimsEmployee().getGlobalOrgId().getGlobalOrgName());
				if(s.getCasedetails().getCurrentUser().getSubDepartment() != null) {
					obb.put("subdepartment", s.getCasedetails().getCurrentUser().getSubDepartment().getSubdepartmentNameEn());
				} else {
					obb.put("subdepartment", "");
				}
				obb.put("vigilancestatus", "pendingde");
				obbList.add(obb);
			});
		} else if(empList6 != null && !empList6.isEmpty()) {
			empList6.stream().forEach(s->{
//				SuspensionDetails s = empList6.get(0);
				ObjectNode obb = objectMapper.createObjectNode();
				obb.put("employeeName", s.getFirstName()+" "+s.getMiddleName()+" "+s.getLastName());
				obb.put("sevarthid", s.getSevarthId());
				obb.put("caseno", s.getFileNo());
				obb.put("caseid", s.getId());
				obb.put("finaldecision","Chargesheet not issued");
				obb.put("designationName", "");
				
				obb.put("department", s.getGlobalorg().getGlobalOrgName());
				obb.put("subdepartment", "");
				
				obb.put("vigilancestatus", "withoutdesuspension");
				obbList.add(obb);
			});
		} else {
			List<Object> obj = null;
			for(GlobalOrg org : orgList) {

				ResponseEntity<ArrayList> data = restTemplate
						.getForEntity("http://localhost:8075/commonservice/serviceAPI/getempinfosevarthbyorg/"
								+ org.getId() + "?q=" + StringUtils.upperCase(input), ArrayList.class);
				if(data.getBody() != null && !data.getBody().isEmpty()) {
					obj = data.getBody();
					break;
				}

			}
			if(obj != null && !obj.isEmpty()) {
				ObjectNode obb = objectMapper.createObjectNode();
				obb.put("employeeName", obj.get(4)+" "+obj.get(5)+" "+obj.get(6));
				obb.put("sevarthid", obj.get(3).toString());
				obb.put("caseno", "");
				obb.put("caseid", "");
				if(obj.get(2) != null) {
					obb.put("designationName", obj.get(2).toString());
				} else {
					obb.put("designationName", "");
				}
				
				obb.put("finaldecision","");
				
				obb.put("department", orgRepo.findById(Long.valueOf(obj.get(10).toString())).orElse(new GlobalOrg()).getGlobalOrgName());
				obb.put("subdepartment", "");
				obb.put("vigilancestatus", "nodatafound");
				obbList.add(obb);
			}
			System.out.println();
		}
		return ResponseEntity.ok(obbList);
	}
	
	
	@SuppressWarnings("rawtypes")
	@GetMapping(value = "getSevarthlist")
	public ResponseEntity<?> getSevarthlist(@RequestParam("q") final String input) {
		List<GlobalOrg> orgList = orgRepo.findByActive(true, Sort.by("id"));
		Set<String> idList = new HashSet<>();
		ExecutorService executorService = Executors.newCachedThreadPool();
//		Map<String,List<String>> mapList = new HashMap<>();
		Collection<Callable<String>> callables = new ArrayList<>();
		long starttime = System.currentTimeMillis();
		for(GlobalOrg org : orgList) {
			callables.add(new Callable<String>() {
				@SuppressWarnings("unchecked")
				@Override
				public String call() throws Exception {
//			System.out.println(org.getId());
//			if(org.getId()==701) {
					ResponseEntity<ArrayList> data = restTemplate
							.getForEntity("http://localhost:8075/commonservice/serviceAPI/getsevarthlistbyorg/"
									+ org.getId() + "?q=" + StringUtils.upperCase(input), ArrayList.class);
//					mapList.put(org.getGlobalOrgName(), data.getBody());
					idList.addAll(data.getBody());
//			}
					return null;
				}
			
			});
//			}
		}
		if(idList.isEmpty()) {
			List<EmployeeDetails> empList = empRepo.findBySevarthIdContainingIgnoreCase(input);
			if(!empList.isEmpty()) {
				idList.addAll(empList.parallelStream().map(s->s.getSevarthId()).collect(Collectors.toList()));
			}
		}
		List<SuspensionDetails> susList = suspensionRepo.findBySevarthIdContainingIgnoreCaseAndCasedetailsIsNull(input);
		if(!susList.isEmpty()) {
			idList.addAll(susList.parallelStream().map(s->s.getSevarthId()).collect(Collectors.toList()));
		}
		
		List<ProsecutionProposalDetails> proseList = proseRepo.findBySevarthIdContainingIgnoreCaseAndCasedetailsIsNullAndStatusForGrantedDeniedPendingAndWhetherCaseFiledIntheCourtAndStatusIsNotNullAndCourtNameIsNotNullAndCourtCaseNoIsNotNull(input.trim(),"Sanction Granted",true);
		if(!proseList.isEmpty()) {
			idList.addAll(susList.parallelStream().map(s->s.getSevarthId()).collect(Collectors.toList()));
		}
		
		try {
			long starttime3 = System.currentTimeMillis();
			List<Future<String>> taskFutureList = executorService.invokeAll(callables);
			for (Future<String> future : taskFutureList) {
				try {
					future.get();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			long endtime3 = System.currentTimeMillis();
			System.out.println("Time required for final step:"+TimeUnit.MILLISECONDS.toSeconds(endtime3-starttime3));
		} catch (Exception e) {
			e.printStackTrace();
		}
			finally{
			  executorService.shutdown(); 
				}
		long endtime = System.currentTimeMillis();
//			System.out.println(endtime);
		System.out.println("Time Difference in seconds superadmin dashboard:"+TimeUnit.MILLISECONDS.toSeconds(endtime-starttime));
		

		return new ResponseEntity<>(idList, HttpStatus.OK);
	}
	
	
	
	@SuppressWarnings("rawtypes")
	@GetMapping(value = "getSurnamelist")
	public ResponseEntity<?> getSurnamelist(@RequestParam("q") final String input) {
		List<GlobalOrg> orgList = orgRepo.findByActive(true, Sort.by("id"));
		Set<String> idList = new HashSet<>();
		
		
		
		ExecutorService executorService = Executors.newCachedThreadPool();
//		Map<String,List<String>> mapList = new HashMap<>();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		
		Collection<Callable<String>> callables = new ArrayList<>();
		long starttime = System.currentTimeMillis();
		for(GlobalOrg org : orgList) {
			callables.add(new Callable<String>() {
				@SuppressWarnings("unchecked")
				@Override
				public String call() throws Exception {
//			System.out.println(org.getId());
//			if(org.getId()==701) {
					ResponseEntity<ArrayList> data = restTemplate
							.getForEntity("http://localhost:8075/commonservice/serviceAPI/getsurnamelistbyorg/"
									+ org.getId() + "?q=" + StringUtils.upperCase(input.trim()), ArrayList.class);
//					mapList.put(org.getGlobalOrgName(), data.getBody());
					
					idList.addAll(data.getBody());
//			}
					return null;
				}
			
			});
//			}
		}
		if(idList.isEmpty()) {
			List<EmployeeDetails> empList = empRepo.findByLastNameContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrMiddleNameContainingIgnoreCaseOrderByCasedetailsGlobalorgId(input.trim(),input.trim(),input);
			if(!empList.isEmpty()) {
				List<String> sd = new ArrayList<>();
				empList.stream().forEach(s->{
					String dob = "";
					if(s.getLocaldate() != null) {
						dob = s.getLocaldate().format(dtf);
					}
					String ss = s.getSevarthId()+"----"+s.getFirstName()+" "+s.getMiddleName()+" "+s.getLastName()+"----"+s.getCasedetails().getGlobalorg().getGlobalOrgName().split("[(]")[0]+"----"+s.getCasedetails().getGlobalorg().getId()+"----"+dob+"----DE MODULE";
					sd.add(ss);
				});
				idList.addAll(sd);
			}
		}
		List<SuspensionDetails> susList = suspensionRepo.findByLastNameContainingIgnoreCaseAndCasedetailsIsNullOrFirstNameContainingIgnoreCaseAndCasedetailsIsNullOrMiddleNameContainingIgnoreCaseAndCasedetailsIsNull(input.trim(),input.trim(),input);
		if(!susList.isEmpty()) {
			idList.addAll(susList.stream().map(s->s.getSevarthId()+"----"+s.getFirstName()+" "+s.getMiddleName()+" "+s.getLastName()+"----"+s.getGlobalorg().getGlobalOrgName().split("[(]")[0]+"----"+s.getGlobalorg().getId()+"-----DOB"+"----Without DE-Suspension").collect(Collectors.toList()));
		}
		
		List<ProsecutionProposalDetails> prosList = proseRepo.findByLastNameContainingIgnoreCaseAndCasedetailsIsNullAndStatusForGrantedDeniedPendingAndWhetherCaseFiledIntheCourtAndStatusIsNotNullAndCourtNameIsNotNullAndCourtCaseNoIsNotNullOrFirstNameContainingIgnoreCaseAndCasedetailsIsNullAndStatusForGrantedDeniedPendingAndWhetherCaseFiledIntheCourtAndStatusIsNotNullAndCourtNameIsNotNullAndCourtCaseNoIsNotNullOrMiddleNameContainingIgnoreCaseAndCasedetailsIsNullAndStatusForGrantedDeniedPendingAndWhetherCaseFiledIntheCourtAndStatusIsNotNullAndCourtNameIsNotNullAndCourtCaseNoIsNotNull(input.trim(),"Sanction Granted",true,input.trim(),"Sanction Granted",true,input.trim(),"Sanction Granted",true);
		if(!prosList.isEmpty()) {
			idList.addAll(prosList.stream().map(s->s.getSevarthId()+"----"+s.getFirstName()+" "+s.getMiddleName()+" "+s.getLastName()+"----"+s.getGlobalorg().getGlobalOrgName().split("[(]")[0]+"----"+s.getGlobalorg().getId()+"-----DOB"+"----Without DE-Prosecution").collect(Collectors.toList()));
		}
		
		try {
			long starttime3 = System.currentTimeMillis();
			List<Future<String>> taskFutureList = executorService.invokeAll(callables);
			for (Future<String> future : taskFutureList) {
				try {
					future.get();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			long endtime3 = System.currentTimeMillis();
			System.out.println("Time required for final step:"+TimeUnit.MILLISECONDS.toSeconds(endtime3-starttime3));
		} catch (Exception e) {
			e.printStackTrace();
		}
			finally{
			  executorService.shutdown(); 
				}
		long endtime = System.currentTimeMillis();
//			System.out.println(endtime);
		System.out.println("Time Difference in seconds superadmin dashboard:"+TimeUnit.MILLISECONDS.toSeconds(endtime-starttime));
		

		return new ResponseEntity<>(idList, HttpStatus.OK);
	}
	
	
	
	@SuppressWarnings("rawtypes")
	@PostMapping(value = "getdeinformationbysurname")
	public ResponseEntity<?> getdeinformationbysurname(CustomDto dto) {
		List<GlobalOrg> orgList = orgRepo.findByActive(true, Sort.by("id"));
		String input = dto.getCaseid();
		String[] empdatalist = input.split("----");
		String sevarthid = empdatalist[0];
		String employeeName = empdatalist[1];
		String orgid = empdatalist[3];
		String dateofbirth = empdatalist[4];
		String datatype = empdatalist[5];
		DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		
		String[] employeeNameArray = employeeName.split("\\s+");
		String firstName = "";
		String middleName = "";
		String lastName = "";
		
		if(employeeNameArray.length==4) {
			firstName = employeeNameArray[1];
			middleName = employeeNameArray[2];
			lastName = employeeNameArray[3];
		}

		if(employeeNameArray.length==1) {
			firstName = employeeNameArray[0];
		} 
		if(employeeNameArray.length==2) {
			firstName = employeeNameArray[0];
			lastName = employeeNameArray[1];			
		} 
		if(employeeNameArray.length==3) {
			firstName = employeeNameArray[0];
			middleName = employeeNameArray[1];
			lastName = employeeNameArray[2];
		}
		
		List<ObjectNode> obbList =new ArrayList<>();
		List<Boolean> idList = new ArrayList<>();
		if(datatype != null && datatype.equalsIgnoreCase("ServiceBook") && orgid != null) {
			for(GlobalOrg org : orgList) {
//				if(org.getId()!=Long.valueOf("701")) {
//					continue;
//				}
						ResponseEntity<Boolean> data = restTemplate
								.getForEntity("http://localhost:8075/commonservice/serviceAPI/checksevarthbyorg/"
										+ org.getId() + "?q=" + StringUtils.upperCase(sevarthid), Boolean.class);
						idList.add(data.getBody());
	
			}
		}
		boolean notguilty = false;
		if(!idList.contains(false)) {
			notguilty = true;
		}
		if(datatype != null && datatype.equalsIgnoreCase("DE Module")) {
			
			
			
			List<EmployeeDetails> empList = empRepo.findDistinctCasedetailsByFirstNameContainingIgnoreCaseAndMiddleNameContainingIgnoreCaseAndLastNameContainingIgnoreCaseAndLocaldateAndCasedetailsGlobalorgIdAndFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsDecisionTakenId(firstName,middleName,lastName,LocalDate.parse(dateofbirth,df),Long.valueOf(orgid),true,2L);
			
			List<EmployeeDetails> empList2 = empRepo.findDistinctCasedetailsByFirstNameContainingIgnoreCaseAndMiddleNameContainingIgnoreCaseAndLastNameContainingIgnoreCaseAndLocaldateAndCasedetailsGlobalorgIdAndFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsDecisionTakenId(firstName,middleName,lastName,LocalDate.parse(dateofbirth,df),Long.valueOf(orgid),true,1L);
			
			
			List<EmployeeDetails> empList4 = empRepo.findDistinctCasedetailsByFirstNameContainingIgnoreCaseAndMiddleNameContainingIgnoreCaseAndLastNameContainingIgnoreCaseAndLocaldateAndFinalOutcomeDetailsWhetherCaseFinallyDecided(firstName,middleName,lastName,LocalDate.parse(dateofbirth,df),false);
			
			List<EmployeeDetails> empList5 = empRepo.findDistinctCasedetailsByFirstNameContainingIgnoreCaseAndMiddleNameContainingIgnoreCaseAndLastNameContainingIgnoreCaseAndLocaldateAndCasedetailsGlobalorgIdAndFinalOutcomeDetailsIsNullOrFirstNameContainingIgnoreCaseAndMiddleNameContainingIgnoreCaseAndLastNameContainingIgnoreCaseAndLocaldateAndCasedetailsGlobalorgIdAndFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsDecisionTakenIdOrFirstNameContainingIgnoreCaseAndMiddleNameContainingIgnoreCaseAndLastNameContainingIgnoreCaseAndLocaldateAndCasedetailsGlobalorgIdAndFinalOutcomeDetailsWhetherCaseFinallyDecided(firstName,middleName,lastName,LocalDate.parse(dateofbirth,df),Long.valueOf(orgid),firstName,middleName,lastName,LocalDate.parse(dateofbirth,df),Long.valueOf(orgid),true,1L,firstName,middleName,lastName,LocalDate.parse(dateofbirth,df),Long.valueOf(orgid),false);
			if((!empList.isEmpty() && empList2.isEmpty() && empList4.isEmpty())) {
				empList.stream().forEach(s->{
					ObjectNode obb = objectMapper.createObjectNode();
					obb.put("employeeName", s.getFirstName()+" "+s.getMiddleName()+" "+s.getLastName());
					obb.put("sevarthid", s.getSevarthId());
					obb.put("caseno", s.getCasedetails().getCaseNo());
					obb.put("caseid", s.getCasedetails().getId());
					if(s.getDesignation() != null) {
						obb.put("designationName", s.getDesignation().getDesignation_name_en());
					} else {
						obb.put("designationName", "");
					}
					if(s.getFinalOutcomeDetails() != null && s.getFinalOutcomeDetails().getDecisionTaken() != null) {
						obb.put("finaldecision",s.getFinalOutcomeDetails().getDecisionTaken().getDecisionNameHindiEn());
					} else {
						obb.put("finaldecision","");
					}
					obb.put("orgid",orgid);
					obb.put("birthdate",dateofbirth);
					obb.put("department", s.getCasedetails().getCurrentUser().getPimsEmployee().getGlobalOrgId().getGlobalOrgName());
					if(s.getCasedetails().getCurrentUser().getSubDepartment() != null) {
						obb.put("subdepartment", s.getCasedetails().getCurrentUser().getSubDepartment().getSubdepartmentNameEn());
					} else {
						obb.put("subdepartment", "");
					}
					obb.put("vigilancestatus", "nodatafound");
					obbList.add(obb);
				});
			} else if(empList5 != null && !empList5.isEmpty()) {
				empList5.stream().forEach(s->{
//				EmployeeDetails s = empList5.get(0);
					ObjectNode obb = objectMapper.createObjectNode();
					obb.put("employeeName", s.getFirstName()+" "+s.getMiddleName()+" "+s.getLastName());
					obb.put("sevarthid", s.getSevarthId());
					obb.put("caseno", s.getCasedetails().getCaseNo());
					obb.put("caseid", s.getCasedetails().getId());
					if(s.getDesignation() != null) {
						obb.put("designationName", s.getDesignation().getDesignation_name_en());
					} else {
						obb.put("designationName", "");
					}
					if(s.getFinalOutcomeDetails() != null && s.getFinalOutcomeDetails().getDecisionTaken() != null) {
						obb.put("finaldecision",s.getFinalOutcomeDetails().getDecisionTaken().getDecisionNameHindiEn());
					} else {
						if(s.getCasedetails().getPendingWith() != null) {
							obb.put("finaldecision",s.getCasedetails().getPendingWith());
						} else {
							obb.put("finaldecision","");
						}
					}
					obb.put("orgid",orgid);
					obb.put("birthdate",dateofbirth);
					obb.put("department", s.getCasedetails().getCurrentUser().getPimsEmployee().getGlobalOrgId().getGlobalOrgName());
					if(s.getCasedetails().getCurrentUser().getSubDepartment() != null) {
						obb.put("subdepartment", s.getCasedetails().getCurrentUser().getSubDepartment().getSubdepartmentNameEn());
					} else {
						obb.put("subdepartment", "");
					}
					obb.put("vigilancestatus", "pendingde");
					obbList.add(obb);
				});
			}
		}
		if(datatype != null && datatype.equalsIgnoreCase("Without DE-Suspension")) {
			List<SuspensionDetails> empList6 = suspensionRepo.findDistinctFileNoByFirstNameContainingIgnoreCaseAndMiddleNameContainingIgnoreCaseAndLastNameContainingIgnoreCaseAndGlobalorgIdAndEmpDataIsNullAndFileNoIsNotNullAndAndFileNoIsNotAndCasedetailsIsNull(firstName,middleName,lastName,Long.valueOf(orgid),"");
//			
			if(empList6 != null && !empList6.isEmpty()) {
				empList6.stream().forEach(s->{
//					SuspensionDetails s = empList6.get(0);
					ObjectNode obb = objectMapper.createObjectNode();
					obb.put("employeeName", s.getFirstName()+" "+s.getMiddleName()+" "+s.getLastName());
					obb.put("sevarthid", s.getSevarthId());
					obb.put("caseno", s.getFileNo());
					obb.put("caseid", s.getId());
					obb.put("finaldecision","Chargesheet not issued");
					obb.put("designationName", "");
					
					obb.put("department", s.getGlobalorg().getGlobalOrgName());
					obb.put("subdepartment", "");
					obb.put("orgid",orgid);
					obb.put("birthdate",dateofbirth);
					obb.put("vigilancestatus", "withoutdesuspension");
					obbList.add(obb);
				});
			}
		}
		if(datatype != null && datatype.equalsIgnoreCase("Without DE-Prosecution")) {
			List<ProsecutionProposalDetails> empList6 = proseRepo.findDistinctFileNoByFirstNameContainingIgnoreCaseAndMiddleNameContainingIgnoreCaseAndLastNameContainingIgnoreCaseAndGlobalorgIdAndEmpDataIsNullAndFileNoIsNotNullAndAndFileNoIsNotAndCasedetailsIsNullAndStatusForGrantedDeniedPendingAndWhetherCaseFiledIntheCourtAndStatusIsNotNullAndCourtNameIsNotNullAndCourtCaseNoIsNotNull(firstName,middleName,lastName,Long.valueOf(orgid),"","Sanction Granted",true);
//			
			if(empList6 != null && !empList6.isEmpty()) {
				empList6.stream().forEach(s->{
//					SuspensionDetails s = empList6.get(0);
					ObjectNode obb = objectMapper.createObjectNode();
					obb.put("employeeName", s.getFirstName()+" "+s.getMiddleName()+" "+s.getLastName());
					obb.put("sevarthid", s.getSevarthId());
					obb.put("caseno", s.getFileNo());
					obb.put("caseid", s.getId());
					obb.put("finaldecision",s.getStatus());
					obb.put("designationName", "");
					
					obb.put("department", s.getGlobalorg().getGlobalOrgName());
					obb.put("subdepartment", "");
					obb.put("orgid",orgid);
					obb.put("birthdate",dateofbirth);
					obb.put("vigilancestatus", "withoutdeprosecution");
					obbList.add(obb);
				});
			}
		}
		
		if(obbList.isEmpty() && sevarthid != null && orgid != null) {
			List<Object> obj = null;
			for(GlobalOrg org : orgList) {
//				if(org.getId()!=Long.valueOf("701")) {
//					continue;
//				}
				ResponseEntity<ArrayList> data = restTemplate
						.getForEntity("http://localhost:8075/commonservice/serviceAPI/getempinfosevarthbyorg/"
								+ org.getId() + "?q=" + StringUtils.upperCase(sevarthid), ArrayList.class);
				if(data.getBody() != null && !data.getBody().isEmpty()) {
					obj = data.getBody();
					break;
				}

			}
			if(obj != null && !obj.isEmpty()) {
				ObjectNode obb = objectMapper.createObjectNode();
				obb.put("employeeName", obj.get(4)+" "+obj.get(5)+" "+obj.get(6));
				obb.put("sevarthid", obj.get(3).toString());
				obb.put("caseno", "");
				obb.put("caseid", "");
				if(obj.get(2) != null) {
					obb.put("designationName", obj.get(2).toString());
				} else {
					obb.put("designationName", "");
				}
				
				obb.put("finaldecision","");
				obb.put("orgid",orgid);
				obb.put("birthdate",dateofbirth);
				
				obb.put("department", orgRepo.findById(Long.valueOf(obj.get(10).toString())).orElse(new GlobalOrg()).getGlobalOrgName());
				obb.put("subdepartment", "");
				obb.put("vigilancestatus", "nodatafound");
				obbList.add(obb);
			}
		}
		
		if(obbList.isEmpty()) {
			ObjectNode obb = objectMapper.createObjectNode();
			obb.put("employeeName", employeeName);
			obb.put("sevarthid", "");
			obb.put("caseno", "");
			obb.put("caseid", "");				
			obb.put("designationName", "");
			obb.put("orgid",orgid);
			obb.put("birthdate",dateofbirth);
			obb.put("finaldecision","");
			obb.put("department", orgRepo.findById(Long.valueOf(orgid)).orElse(new GlobalOrg()).getGlobalOrgName());
			obb.put("subdepartment", "");
			obb.put("vigilancestatus", "nodatafound");
			obbList.add(obb);
		}
		return ResponseEntity.ok(obbList);
	}
	
	
	
	/*
	 * search by emp sevarth id and department name , written by vijay on 20/10/2023
	 */
	
	
	@GetMapping(value = "getemployeeSevarthIdAndDeptFilter")
	public ResponseEntity<?> getemployeeSevarthIdAndDeptFilter(@RequestParam("q") final String input) {
		
		List<NoDECertificate> empDetail = nodecertificaterepository.findByEmployeeNameContainingIgnoreCase(input);
		List<String> idList = new ArrayList<>();
		empDetail.stream().forEach(s -> {
			idList.add(s.getEmployeeName());
		});
		return new ResponseEntity<>(idList, HttpStatus.OK);
	}
	
	
	
	
	
	
	
	
	
	


}
