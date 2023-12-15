package com.ehrms.deptenq.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ehrms.deptenq.models.CaseDetails;
import com.ehrms.deptenq.models.EmployeeDetails;
import com.ehrms.deptenq.models.GlobalOrg;
import com.ehrms.deptenq.models.Role;
import com.ehrms.deptenq.models.SubDepartment;
import com.ehrms.deptenq.models.TypeOfPenaltyInflictedPojo;
import com.ehrms.deptenq.models.User;
import com.ehrms.deptenq.repository.CaseDetailsRepository;
import com.ehrms.deptenq.repository.IEmployeeDetailsRepository;
import com.ehrms.deptenq.repository.IGlobalOrgRepository;
import com.ehrms.deptenq.repository.SubDepartmentRepository;
import com.ehrms.deptenq.repository.TypeOfPenaltyRepository;
import com.ehrms.deptenq.repository.UserRepository;
import com.ehrms.deptenq.service.UserService;

@Controller
@RequestMapping("departmentalEnquiry")
public class FinaloutcomewiseController {

	@Autowired
	private IEmployeeDetailsRepository empRepo;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private UserService userService;

	@Autowired
	private TypeOfPenaltyRepository penaltyRepo;

	@Autowired
	private CaseDetailsRepository caseDetailsRepo;

	@Autowired
	private SubDepartmentRepository subDepartmentRepo;

	@Autowired
	private IGlobalOrgRepository globalRepo;

//	private static final Logger LOG = LoggerFactory.getLogger(RetiredEmployeesReportsController.class);

	/* Added on 17-11-2022 case details report page */

	@GetMapping("/finaloutcomeReportsController/{page}")
	public String ruleApplicableReportsController(Model model, @PathVariable("page") int page,
			@RequestParam(name = "decision", required = false) String decision,
			@RequestParam(name = "penalty", required = false) String penalty,
			@RequestParam(name = "globalorg", required = false) String globalorg,
			@RequestParam(name = "sort-field", required = false) String sortField,
			@RequestParam(name = "sort-dir", required = false) String sortDirection) {
		
		if(sortField == null) {
			sortField = "caseNo";
		}
		if(sortDirection == null) {
			sortDirection = "asc";
		}

		final Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
				: Sort.by(sortField).descending();
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDirection);
		model.addAttribute("reverseSortDir", sortDirection.equals("asc") ? "desc" : "asc");

		model.addAttribute("globalList", globalRepo.findByActive(true));

		// pagination Writting coding
		int index = 1;

		if (page != 0) {
			index = (page * 10) - (10 - 1);
			model.addAttribute("pageIndex", index);
		}

		if (page < 0) {
			page = 1;
		}

		Pageable pageable = PageRequest.of(page - 1, 10, sort);
		Pageable allPage = PageRequest.of(0, Integer.MAX_VALUE, sort);

		User currentUser = userService.getCurrentUserPojo();
		Page<CaseDetails> noticePages = null;
//		List<CaseDetails> caseList = null;
		Page<CaseDetails> allCases = null;
		model.addAttribute("localdatenow", LocalDate.now());
		if (decision == null) {
			decision = "";
		}
		if (penalty == null) {
			penalty = "";
		}
		if (globalorg == null) {
			globalorg = "";
		}
		model.addAttribute("decision", decision);
		model.addAttribute("penalty", penalty);
		model.addAttribute("globalorg", globalorg);
		List<Long> caseIdx = new ArrayList<>();
		caseIdx.add(0L);
		List<CaseDetails> casesTransferList = caseDetailsRepo.findByActiveForTransfer(true);
		if (!casesTransferList.isEmpty()) {
			caseIdx.addAll(casesTransferList.parallelStream().map(CaseDetails::getId).collect(Collectors.toList()));
		}
		if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SUPERADMIN")) {

			if (StringUtils.isBlank(globalorg)) {
				List<EmployeeDetails> empList = empRepo
						.findByCasedetailsIsNotNullAndFinalOutcomeDetailsWhetherCaseFinallyDecidedAndCasedetailsIdNotIn(
								true, caseIdx);
				List<CaseDetails> caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotIn(empList, caseIdx);

				model.addAttribute("casesDecided", caseList.size());
				model.addAttribute("empDecided", empList.size());

				empList = empRepo
						.findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsTypeOfPenaltyInflictedIsNotNullAndCasedetailsIdNotIn(
								true, caseIdx);
				caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotIn(empList, caseIdx);
				model.addAttribute("casesPenalty", caseList.size());
				model.addAttribute("empPenalty", empList.size());

				empList = empRepo
						.findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsTypeOfPenaltyInflictedIdInAndCasedetailsIdNotIn(
								true, Arrays.asList(2L, 3L), caseIdx);
				caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotIn(empList, caseIdx);
				model.addAttribute("casesPenaltyMajor", caseList.size());
				model.addAttribute("empPenaltyMajor", empList.size());

				empList = empRepo
						.findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsTypeOfPenaltyInflictedIdInAndCasedetailsIdNotIn(
								true, Arrays.asList(1L), caseIdx);
				caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotIn(empList, caseIdx);
				model.addAttribute("casesPenaltyMinor", caseList.size());
				model.addAttribute("empPenaltyMinor", empList.size());

				empList = empRepo
						.findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsDecisionTakenIdInAndCasedetailsIdNotIn(
								true, Arrays.asList(2L), caseIdx);
				caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotIn(empList, caseIdx);
				model.addAttribute("casesAq", caseList.size());
				model.addAttribute("empAq", empList.size());

				empList = empRepo
						.findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsDecisionTakenIdInAndCasedetailsIdNotIn(
								true, Arrays.asList(3L), caseIdx);
				caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotIn(empList, caseIdx);
				model.addAttribute("casesdeath", caseList.size());
				model.addAttribute("empdeath", empList.size());

				empList = empRepo
						.findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsDecisionTakenIdInAndCasedetailsIdNotIn(
								true, Arrays.asList(4L), caseIdx);
				caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotIn(empList, caseIdx);
				model.addAttribute("casesdropped", caseList.size());
				model.addAttribute("empdropped", empList.size());

				empList = empRepo
						.findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsDecisionTakenIdInAndCasedetailsIdNotIn(
								true, Arrays.asList(5L), caseIdx);
				caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotIn(empList, caseIdx);
				model.addAttribute("casescourtorder", caseList.size());
				model.addAttribute("empcourtorder", empList.size());

				if (StringUtils.isNotBlank(decision) && StringUtils.isBlank(penalty)) {

					List<EmployeeDetails> empListx = empRepo
							.findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsDecisionTakenIdInAndCasedetailsIdNotIn(
									true, Arrays.asList(Long.valueOf(decision)), caseIdx);
					noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotIn(empListx, pageable, caseIdx);
					noticePages.getContent().stream().forEach(s -> {
						List<EmployeeDetails> empp = new ArrayList<>();
						empListx.stream().forEach(ss -> {
							if (ss.getCasedetails().getId() == s.getId()) {
								empp.add(ss);
							}
						});
						s.setEmployeeList(empp);
					});
					allCases = caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotIn(empListx, allPage, caseIdx);
					model.addAttribute("empCount", empListx.size());
				} else if (StringUtils.isBlank(decision) && StringUtils.isNotBlank(penalty)) {
					if (penalty.equalsIgnoreCase("all")) {
						List<TypeOfPenaltyInflictedPojo> pen = penaltyRepo.findByActive(true);

						List<EmployeeDetails> empListx = empRepo
								.findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsTypeOfPenaltyInflictedInAndCasedetailsIdNotIn(
										true, pen, caseIdx);
						noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotIn(empListx, pageable, caseIdx);
						noticePages.getContent().stream().forEach(s -> {
							List<EmployeeDetails> empp = new ArrayList<>();
							empListx.stream().forEach(ss -> {
								if (ss.getCasedetails().getId() == s.getId()) {
									empp.add(ss);
								}
							});
							s.setEmployeeList(empp);
						});
						allCases = caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotIn(empListx, allPage, caseIdx);
						model.addAttribute("empCount", empListx.size());
					} else if (penalty.equalsIgnoreCase("major")) {

						List<EmployeeDetails> empListx = empRepo
								.findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsTypeOfPenaltyInflictedIdInAndCasedetailsIdNotIn(
										true, Arrays.asList(2L, 3L), caseIdx);
						noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotIn(empListx, pageable, caseIdx);
						noticePages.getContent().stream().forEach(s -> {
							List<EmployeeDetails> empp = new ArrayList<>();
							empListx.stream().forEach(ss -> {
								if (ss.getCasedetails().getId() == s.getId()) {
									empp.add(ss);
								}
							});
							s.setEmployeeList(empp);
						});
						allCases = caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotIn(empListx, allPage, caseIdx);
						model.addAttribute("empCount", empListx.size());
					} else {
						TypeOfPenaltyInflictedPojo pen = penaltyRepo.findById(Long.valueOf(penalty))
								.orElse(new TypeOfPenaltyInflictedPojo());
						List<TypeOfPenaltyInflictedPojo> listpen = new ArrayList<>();
						listpen.add(pen);

						List<EmployeeDetails> empListx = empRepo
								.findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsTypeOfPenaltyInflictedInAndCasedetailsIdNotIn(
										true, listpen, caseIdx);
						noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotIn(empListx, pageable, caseIdx);
						noticePages.getContent().stream().forEach(s -> {
							List<EmployeeDetails> empp = new ArrayList<>();
							empListx.stream().forEach(ss -> {
								if (ss.getCasedetails().getId() == s.getId()) {
									empp.add(ss);
								}
							});
							s.setEmployeeList(empp);
						});
						allCases = caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotIn(empListx, allPage, caseIdx);
						model.addAttribute("empCount", empListx.size());
					}
				} else {

					List<EmployeeDetails> empListx = empRepo
							.findByCasedetailsIsNotNullAndFinalOutcomeDetailsWhetherCaseFinallyDecidedAndCasedetailsIdNotIn(
									true, caseIdx);
					noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotIn(empListx, pageable, caseIdx);
					noticePages.getContent().stream().forEach(s -> {
						List<EmployeeDetails> empp = new ArrayList<>();
						empListx.stream().forEach(ss -> {
							if (ss.getCasedetails().getId() == s.getId()) {
								empp.add(ss);
							}
						});
						s.setEmployeeList(empp);
					});
					allCases = caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotIn(empListx, allPage, caseIdx);
					model.addAttribute("empCount", empListx.size());
				}
			} else {
				model.addAttribute("globalorg", Long.valueOf(globalorg));
				List<EmployeeDetails> empList = empRepo
						.findByCasedetailsIsNotNullAndFinalOutcomeDetailsWhetherCaseFinallyDecidedAndCurrentUserPimsEmployeeGlobalOrgIdIdAndCasedetailsIdNotIn(
								true, Long.valueOf(globalorg), caseIdx);
				List<CaseDetails> caseList = caseDetailsRepo
						.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndIdNotIn(empList,
								Long.valueOf(globalorg), caseIdx);

				model.addAttribute("casesDecided", caseList.size());
				model.addAttribute("empDecided", empList.size());

				empList = empRepo
						.findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsTypeOfPenaltyInflictedIsNotNullAndCurrentUserPimsEmployeeGlobalOrgIdIdAndCasedetailsIdNotIn(
								true, Long.valueOf(globalorg), caseIdx);
				caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndIdNotIn(
						empList, Long.valueOf(globalorg), caseIdx);
				model.addAttribute("casesPenalty", caseList.size());
				model.addAttribute("empPenalty", empList.size());

				empList = empRepo
						.findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsTypeOfPenaltyInflictedIdInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndCasedetailsIdNotIn(
								true, Arrays.asList(2L, 3L), Long.valueOf(globalorg), caseIdx);
				caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndIdNotIn(
						empList, Long.valueOf(globalorg), caseIdx);
				model.addAttribute("casesPenaltyMajor", caseList.size());
				model.addAttribute("empPenaltyMajor", empList.size());

				empList = empRepo
						.findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsTypeOfPenaltyInflictedIdInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndCasedetailsIdNotIn(
								true, Arrays.asList(1L), Long.valueOf(globalorg), caseIdx);
				caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndIdNotIn(
						empList, Long.valueOf(globalorg), caseIdx);
				model.addAttribute("casesPenaltyMinor", caseList.size());
				model.addAttribute("empPenaltyMinor", empList.size());

				empList = empRepo
						.findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsDecisionTakenIdInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndCasedetailsIdNotIn(
								true, Arrays.asList(2L), Long.valueOf(globalorg), caseIdx);
				caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndIdNotIn(
						empList, Long.valueOf(globalorg), caseIdx);
				model.addAttribute("casesAq", caseList.size());
				model.addAttribute("empAq", empList.size());

				empList = empRepo
						.findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsDecisionTakenIdInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndCasedetailsIdNotIn(
								true, Arrays.asList(3L), Long.valueOf(globalorg), caseIdx);
				caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndIdNotIn(
						empList, Long.valueOf(globalorg), caseIdx);
				model.addAttribute("casesdeath", caseList.size());
				model.addAttribute("empdeath", empList.size());

				empList = empRepo
						.findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsDecisionTakenIdInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndCasedetailsIdNotIn(
								true, Arrays.asList(4L), Long.valueOf(globalorg), caseIdx);
				caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndIdNotIn(
						empList, Long.valueOf(globalorg), caseIdx);
				model.addAttribute("casesdropped", caseList.size());
				model.addAttribute("empdropped", empList.size());

				empList = empRepo
						.findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsDecisionTakenIdInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndCasedetailsIdNotIn(
								true, Arrays.asList(5L), Long.valueOf(globalorg), caseIdx);
				caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndIdNotIn(
						empList, Long.valueOf(globalorg), caseIdx);
				model.addAttribute("casescourtorder", caseList.size());
				model.addAttribute("empcourtorder", empList.size());

				if (StringUtils.isNotBlank(decision) && StringUtils.isBlank(penalty)) {

					List<EmployeeDetails> empListx = empRepo
							.findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsDecisionTakenIdInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndCasedetailsIdNotIn(
									true, Arrays.asList(Long.valueOf(decision)), Long.valueOf(globalorg), caseIdx);
					noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndIdNotIn(
							empListx, pageable, Long.valueOf(globalorg), caseIdx);
					noticePages.getContent().stream().forEach(s -> {
						List<EmployeeDetails> empp = new ArrayList<>();
						empListx.stream().forEach(ss -> {
							if (ss.getCasedetails().getId() == s.getId()) {
								empp.add(ss);
							}
						});
						s.setEmployeeList(empp);
					});
					allCases = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdId(
							empListx, allPage, Long.valueOf(globalorg));
					model.addAttribute("empCount", empListx.size());
				} else if (StringUtils.isBlank(decision) && StringUtils.isNotBlank(penalty)) {
					if (penalty.equalsIgnoreCase("all")) {
						List<TypeOfPenaltyInflictedPojo> pen = penaltyRepo.findByActive(true);

						List<EmployeeDetails> empListx = empRepo
								.findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsTypeOfPenaltyInflictedInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndCasedetailsIdNotIn(
										true, pen, Long.valueOf(globalorg), caseIdx);
						noticePages = caseDetailsRepo
								.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndIdNotIn(empListx,
										pageable, Long.valueOf(globalorg), caseIdx);
						noticePages.getContent().stream().forEach(s -> {
							List<EmployeeDetails> empp = new ArrayList<>();
							empListx.stream().forEach(ss -> {
								if (ss.getCasedetails().getId() == s.getId()) {
									empp.add(ss);
								}
							});
							s.setEmployeeList(empp);
						});
						allCases = caseDetailsRepo
								.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndIdNotIn(empListx,
										allPage, Long.valueOf(globalorg), caseIdx);
						model.addAttribute("empCount", empListx.size());
					} else if (penalty.equalsIgnoreCase("major")) {

						List<EmployeeDetails> empListx = empRepo
								.findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsTypeOfPenaltyInflictedIdInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndCasedetailsIdNotIn(
										true, Arrays.asList(2L, 3L), Long.valueOf(globalorg), caseIdx);
						noticePages = caseDetailsRepo
								.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndIdNotIn(empListx,
										pageable, Long.valueOf(globalorg), caseIdx);
						noticePages.getContent().stream().forEach(s -> {
							List<EmployeeDetails> empp = new ArrayList<>();
							empListx.stream().forEach(ss -> {
								if (ss.getCasedetails().getId() == s.getId()) {
									empp.add(ss);
								}
							});
							s.setEmployeeList(empp);
						});
						allCases = caseDetailsRepo
								.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndIdNotIn(empListx,
										allPage, Long.valueOf(globalorg), caseIdx);
						model.addAttribute("empCount", empListx.size());
					} else {
						TypeOfPenaltyInflictedPojo pen = penaltyRepo.findById(Long.valueOf(penalty))
								.orElse(new TypeOfPenaltyInflictedPojo());
						List<TypeOfPenaltyInflictedPojo> listpen = new ArrayList<>();
						listpen.add(pen);

						List<EmployeeDetails> empListx = empRepo
								.findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsTypeOfPenaltyInflictedInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndCasedetailsIdNotIn(
										true, listpen, Long.valueOf(globalorg), caseIdx);
						noticePages = caseDetailsRepo
								.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndIdNotIn(empListx,
										pageable, Long.valueOf(globalorg), caseIdx);
						noticePages.getContent().stream().forEach(s -> {
							List<EmployeeDetails> empp = new ArrayList<>();
							empListx.stream().forEach(ss -> {
								if (ss.getCasedetails().getId() == s.getId()) {
									empp.add(ss);
								}
							});
							s.setEmployeeList(empp);
						});
						allCases = caseDetailsRepo
								.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndIdNotIn(empListx,
										allPage, Long.valueOf(globalorg), caseIdx);
						model.addAttribute("empCount", empListx.size());
					}
				} else {

					List<EmployeeDetails> empListx = empRepo
							.findByCasedetailsIsNotNullAndFinalOutcomeDetailsWhetherCaseFinallyDecidedAndCurrentUserPimsEmployeeGlobalOrgIdIdAndCasedetailsIdNotIn(
									true, Long.valueOf(globalorg), caseIdx);
					noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndIdNotIn(
							empListx, pageable, Long.valueOf(globalorg), caseIdx);
					noticePages.getContent().stream().forEach(s -> {
						List<EmployeeDetails> empp = new ArrayList<>();
						empListx.stream().forEach(ss -> {
							if (ss.getCasedetails().getId() == s.getId()) {
								empp.add(ss);
							}
						});
						s.setEmployeeList(empp);
					});
					allCases = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndIdNotIn(
							empListx, allPage, Long.valueOf(globalorg), caseIdx);
					model.addAttribute("empCount", empListx.size());
				}
			}

		} else if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SECRETARYOLD")) {

			GlobalOrg org = userService.getCurrentUserPojo().getPimsEmployee().getGlobalOrgId();
			globalorg = String.valueOf(org.getId());
			model.addAttribute("globalorg", Long.valueOf(globalorg));
			List<EmployeeDetails> empList = empRepo
					.findByCasedetailsIsNotNullAndFinalOutcomeDetailsWhetherCaseFinallyDecidedAndCurrentUserPimsEmployeeGlobalOrgIdIdAndCasedetailsIdNotIn(
							true, Long.valueOf(globalorg), caseIdx);
			List<CaseDetails> caseList = caseDetailsRepo
					.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndIdNotIn(empList,
							Long.valueOf(globalorg), caseIdx);

			model.addAttribute("casesDecided", caseList.size());
			model.addAttribute("empDecided", empList.size());

			empList = empRepo
					.findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsTypeOfPenaltyInflictedIsNotNullAndCurrentUserPimsEmployeeGlobalOrgIdIdAndCasedetailsIdNotIn(
							true, Long.valueOf(globalorg), caseIdx);
			caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndIdNotIn(empList,
					Long.valueOf(globalorg), caseIdx);
			model.addAttribute("casesPenalty", caseList.size());
			model.addAttribute("empPenalty", empList.size());

			empList = empRepo
					.findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsTypeOfPenaltyInflictedIdInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndCasedetailsIdNotIn(
							true, Arrays.asList(2L, 3L), Long.valueOf(globalorg), caseIdx);
			caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndIdNotIn(empList,
					Long.valueOf(globalorg), caseIdx);
			model.addAttribute("casesPenaltyMajor", caseList.size());
			model.addAttribute("empPenaltyMajor", empList.size());

			empList = empRepo
					.findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsTypeOfPenaltyInflictedIdInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndCasedetailsIdNotIn(
							true, Arrays.asList(1L), Long.valueOf(globalorg), caseIdx);
			caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndIdNotIn(empList,
					Long.valueOf(globalorg), caseIdx);
			model.addAttribute("casesPenaltyMinor", caseList.size());
			model.addAttribute("empPenaltyMinor", empList.size());

			empList = empRepo
					.findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsDecisionTakenIdInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndCasedetailsIdNotIn(
							true, Arrays.asList(2L), Long.valueOf(globalorg), caseIdx);
			caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndIdNotIn(empList,
					Long.valueOf(globalorg), caseIdx);
			model.addAttribute("casesAq", caseList.size());
			model.addAttribute("empAq", empList.size());

			empList = empRepo
					.findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsDecisionTakenIdInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndCasedetailsIdNotIn(
							true, Arrays.asList(3L), Long.valueOf(globalorg), caseIdx);
			caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndIdNotIn(empList,
					Long.valueOf(globalorg), caseIdx);
			model.addAttribute("casesdeath", caseList.size());
			model.addAttribute("empdeath", empList.size());

			empList = empRepo
					.findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsDecisionTakenIdInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndCasedetailsIdNotIn(
							true, Arrays.asList(4L), Long.valueOf(globalorg), caseIdx);
			caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndIdNotIn(empList,
					Long.valueOf(globalorg), caseIdx);
			model.addAttribute("casesdropped", caseList.size());
			model.addAttribute("empdropped", empList.size());

			empList = empRepo
					.findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsDecisionTakenIdInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndCasedetailsIdNotIn(
							true, Arrays.asList(5L), Long.valueOf(globalorg), caseIdx);
			caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndIdNotIn(empList,
					Long.valueOf(globalorg), caseIdx);
			model.addAttribute("casescourtorder", caseList.size());
			model.addAttribute("empcourtorder", empList.size());

			if (StringUtils.isNotBlank(decision) && StringUtils.isBlank(penalty)) {

				List<EmployeeDetails> empListx = empRepo
						.findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsDecisionTakenIdInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndCasedetailsIdNotIn(
								true, Arrays.asList(Long.valueOf(decision)), Long.valueOf(globalorg), caseIdx);
				noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndIdNotIn(
						empListx, pageable, Long.valueOf(globalorg), caseIdx);
				noticePages.getContent().stream().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empListx.stream().forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
				allCases = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndIdNotIn(
						empListx, allPage, Long.valueOf(globalorg), caseIdx);
				model.addAttribute("empCount", empListx.size());
			} else if (StringUtils.isBlank(decision) && StringUtils.isNotBlank(penalty)) {
				if (penalty.equalsIgnoreCase("all")) {
					List<TypeOfPenaltyInflictedPojo> pen = penaltyRepo.findByActive(true);

					List<EmployeeDetails> empListx = empRepo
							.findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsTypeOfPenaltyInflictedInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndCasedetailsIdNotIn(
									true, pen, Long.valueOf(globalorg), caseIdx);
					noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndIdNotIn(
							empListx, pageable, Long.valueOf(globalorg), caseIdx);
					noticePages.getContent().stream().forEach(s -> {
						List<EmployeeDetails> empp = new ArrayList<>();
						empListx.stream().forEach(ss -> {
							if (ss.getCasedetails().getId() == s.getId()) {
								empp.add(ss);
							}
						});
						s.setEmployeeList(empp);
					});
					allCases = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndIdNotIn(
							empListx, allPage, Long.valueOf(globalorg), caseIdx);
					model.addAttribute("empCount", empListx.size());
				} else if (penalty.equalsIgnoreCase("major")) {

					List<EmployeeDetails> empListx = empRepo
							.findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsTypeOfPenaltyInflictedIdInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndCasedetailsIdNotIn(
									true, Arrays.asList(2L, 3L), Long.valueOf(globalorg), caseIdx);
					noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndIdNotIn(
							empListx, pageable, Long.valueOf(globalorg), caseIdx);
					noticePages.getContent().stream().forEach(s -> {
						List<EmployeeDetails> empp = new ArrayList<>();
						empListx.stream().forEach(ss -> {
							if (ss.getCasedetails().getId() == s.getId()) {
								empp.add(ss);
							}
						});
						s.setEmployeeList(empp);
					});
					allCases = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndIdNotIn(
							empListx, allPage, Long.valueOf(globalorg), caseIdx);
					model.addAttribute("empCount", empListx.size());
				} else {
					TypeOfPenaltyInflictedPojo pen = penaltyRepo.findById(Long.valueOf(penalty))
							.orElse(new TypeOfPenaltyInflictedPojo());
					List<TypeOfPenaltyInflictedPojo> listpen = new ArrayList<>();
					listpen.add(pen);

					List<EmployeeDetails> empListx = empRepo
							.findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsTypeOfPenaltyInflictedInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndCasedetailsIdNotIn(
									true, listpen, Long.valueOf(globalorg), caseIdx);
					noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndIdNotIn(
							empListx, pageable, Long.valueOf(globalorg), caseIdx);
					noticePages.getContent().stream().forEach(s -> {
						List<EmployeeDetails> empp = new ArrayList<>();
						empListx.stream().forEach(ss -> {
							if (ss.getCasedetails().getId() == s.getId()) {
								empp.add(ss);
							}
						});
						s.setEmployeeList(empp);
					});
					allCases = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndIdNotIn(
							empListx, allPage, Long.valueOf(globalorg), caseIdx);
					model.addAttribute("empCount", empListx.size());
				}
			} else {

				List<EmployeeDetails> empListx = empRepo
						.findByCasedetailsIsNotNullAndFinalOutcomeDetailsWhetherCaseFinallyDecidedAndCurrentUserPimsEmployeeGlobalOrgIdIdAndCasedetailsIdNotIn(
								true, Long.valueOf(globalorg), caseIdx);
				noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndIdNotIn(
						empListx, pageable, Long.valueOf(globalorg), caseIdx);
				noticePages.getContent().stream().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empListx.stream().forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
				allCases = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndIdNotIn(
						empListx, allPage, Long.valueOf(globalorg), caseIdx);
				model.addAttribute("empCount", empListx.size());
			}

		}

		else if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_DEPARTMENTADMIN")|| currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SECRETARY")) {
			List<SubDepartment> subList = currentUser.getSecsubList().stream().filter(s -> s.isActive())
					.map(s -> s.getSubDepartment()).collect(Collectors.toList());
			List<User> userList = new ArrayList<>();
			if(subList.isEmpty()) {
				userList = userRepo.findBySubDepartmentIn(
						subDepartmentRepo.findByGlobalorg(currentUser.getPimsEmployee().getGlobalOrgId()));
				userList.add(currentUser);
			} else {
				userList = userRepo.findBySubDepartmentIn(subList);
			}

			List<EmployeeDetails> empList = empRepo
					.findByCasedetailsIsNotNullAndFinalOutcomeDetailsWhetherCaseFinallyDecidedAndCurrentUserInAndCasedetailsIdNotIn(true,
							userList, caseIdx);
			List<CaseDetails> caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserInAndIdNotIn(empList,
					userList, caseIdx);

			model.addAttribute("casesDecided", caseList.size());
			model.addAttribute("empDecided", empList.size());

			empList = empRepo
					.findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsTypeOfPenaltyInflictedIsNotNullAndCurrentUserInAndCasedetailsIdNotIn(
							true, userList, caseIdx);
			caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserInAndIdNotIn(empList, userList, caseIdx);
			model.addAttribute("casesPenalty", caseList.size());
			model.addAttribute("empPenalty", empList.size());

			empList = empRepo
					.findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsTypeOfPenaltyInflictedIdInAndCurrentUserInAndCasedetailsIdNotIn(
							true, Arrays.asList(2L, 3L), userList, caseIdx);
			caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserInAndIdNotIn(empList, userList, caseIdx);
			model.addAttribute("casesPenaltyMajor", caseList.size());
			model.addAttribute("empPenaltyMajor", empList.size());

			empList = empRepo
					.findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsTypeOfPenaltyInflictedIdInAndCurrentUserInAndCasedetailsIdNotIn(
							true, Arrays.asList(1L), userList, caseIdx);
			caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserInAndIdNotIn(empList, userList, caseIdx);
			model.addAttribute("casesPenaltyMinor", caseList.size());
			model.addAttribute("empPenaltyMinor", empList.size());

			empList = empRepo
					.findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsDecisionTakenIdInAndCurrentUserInAndCasedetailsIdNotIn(
							true, Arrays.asList(2L), userList, caseIdx);
			caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserInAndIdNotIn(empList, userList, caseIdx);
			model.addAttribute("casesAq", caseList.size());
			model.addAttribute("empAq", empList.size());

			empList = empRepo
					.findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsDecisionTakenIdInAndCurrentUserInAndCasedetailsIdNotIn(
							true, Arrays.asList(3L), userList, caseIdx);
			caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserInAndIdNotIn(empList, userList, caseIdx);
			model.addAttribute("casesdeath", caseList.size());
			model.addAttribute("empdeath", empList.size());

			empList = empRepo
					.findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsDecisionTakenIdInAndCurrentUserInAndCasedetailsIdNotIn(
							true, Arrays.asList(4L), userList, caseIdx);
			caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserInAndIdNotIn(empList, userList, caseIdx);
			model.addAttribute("casesdropped", caseList.size());
			model.addAttribute("empdropped", empList.size());

			empList = empRepo
					.findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsDecisionTakenIdInAndCurrentUserInAndCasedetailsIdNotIn(
							true, Arrays.asList(5L), userList, caseIdx);
			caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserInAndIdNotIn(empList, userList, caseIdx);
			model.addAttribute("casescourtorder", caseList.size());
			model.addAttribute("empcourtorder", empList.size());

			if (StringUtils.isNotBlank(decision) && StringUtils.isBlank(penalty)) {

				List<EmployeeDetails> empListx = empRepo
						.findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsDecisionTakenIdInAndCurrentUserInAndCasedetailsIdNotIn(
								true, Arrays.asList(Long.valueOf(decision)), userList, caseIdx);
				noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserInAndIdNotIn(empListx, pageable,
						userList, caseIdx);
				noticePages.getContent().stream().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empListx.stream().forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
				allCases = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserInAndIdNotIn(empListx, allPage, userList, caseIdx);
				model.addAttribute("empCount", empListx.size());
			} else if (StringUtils.isBlank(decision) && StringUtils.isNotBlank(penalty)) {
				if (penalty.equalsIgnoreCase("all")) {
					List<TypeOfPenaltyInflictedPojo> pen = penaltyRepo.findByActive(true);

					List<EmployeeDetails> empListx = empRepo
							.findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsTypeOfPenaltyInflictedInAndCurrentUserInAndCasedetailsIdNotIn(
									true, pen, userList, caseIdx);
					noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserInAndIdNotIn(empListx, pageable,
							userList, caseIdx);
					noticePages.getContent().stream().forEach(s -> {
						List<EmployeeDetails> empp = new ArrayList<>();
						empListx.stream().forEach(ss -> {
							if (ss.getCasedetails().getId() == s.getId()) {
								empp.add(ss);
							}
						});
						s.setEmployeeList(empp);
					});
					allCases = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserInAndIdNotIn(empListx, allPage,
							userList, caseIdx);
					model.addAttribute("empCount", empListx.size());
				} else if (penalty.equalsIgnoreCase("major")) {

					List<EmployeeDetails> empListx = empRepo
							.findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsTypeOfPenaltyInflictedIdInAndCurrentUserInAndCasedetailsIdNotIn(
									true, Arrays.asList(2L, 3L), userList, caseIdx);
					noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserInAndIdNotIn(empListx, pageable,
							userList, caseIdx);
					noticePages.getContent().stream().forEach(s -> {
						List<EmployeeDetails> empp = new ArrayList<>();
						empListx.stream().forEach(ss -> {
							if (ss.getCasedetails().getId() == s.getId()) {
								empp.add(ss);
							}
						});
						s.setEmployeeList(empp);
					});
					allCases = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserInAndIdNotIn(empListx, allPage,
							userList, caseIdx);
					model.addAttribute("empCount", empListx.size());
				} else {
					TypeOfPenaltyInflictedPojo pen = penaltyRepo.findById(Long.valueOf(penalty))
							.orElse(new TypeOfPenaltyInflictedPojo());
					List<TypeOfPenaltyInflictedPojo> listpen = new ArrayList<>();
					listpen.add(pen);

					List<EmployeeDetails> empListx = empRepo
							.findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsTypeOfPenaltyInflictedInAndCurrentUserInAndCasedetailsIdNotIn(
									true, listpen, userList, caseIdx);
					noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserInAndIdNotIn(empListx, pageable,
							userList, caseIdx);
					noticePages.getContent().stream().forEach(s -> {
						List<EmployeeDetails> empp = new ArrayList<>();
						empListx.stream().forEach(ss -> {
							if (ss.getCasedetails().getId() == s.getId()) {
								empp.add(ss);
							}
						});
						s.setEmployeeList(empp);
					});
					allCases = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserInAndIdNotIn(empListx, allPage,
							userList, caseIdx);
					model.addAttribute("empCount", empListx.size());
				}
			} else {

				List<EmployeeDetails> empListx = empRepo
						.findByCasedetailsIsNotNullAndFinalOutcomeDetailsWhetherCaseFinallyDecidedAndCurrentUserInAndCasedetailsIdNotIn(true,
								userList, caseIdx);
				noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserInAndIdNotIn(empListx, pageable,
						userList, caseIdx);
				noticePages.getContent().stream().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empListx.stream().forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
				allCases = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserInAndIdNotIn(empListx, allPage, userList, caseIdx);
				model.addAttribute("empCount", empListx.size());
			}

		}

		else {
			List<EmployeeDetails> empList = empRepo
					.findByCasedetailsIsNotNullAndFinalOutcomeDetailsWhetherCaseFinallyDecidedAndCurrentUserAndCasedetailsIdNotIn(true,
							currentUser, caseIdx);
			List<CaseDetails> caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserAndIdNotIn(empList,
					currentUser, caseIdx);

			model.addAttribute("casesDecided", caseList.size());
			model.addAttribute("empDecided", empList.size());

			empList = empRepo
					.findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsTypeOfPenaltyInflictedIsNotNullAndCurrentUserAndCasedetailsIdNotIn(
							true, currentUser, caseIdx);
			caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserAndIdNotIn(empList, currentUser, caseIdx);
			model.addAttribute("casesPenalty", caseList.size());
			model.addAttribute("empPenalty", empList.size());

			empList = empRepo
					.findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsTypeOfPenaltyInflictedIdInAndCurrentUserAndCasedetailsIdNotIn(
							true, Arrays.asList(2L, 3L), currentUser, caseIdx);
			caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserAndIdNotIn(empList, currentUser, caseIdx);
			model.addAttribute("casesPenaltyMajor", caseList.size());
			model.addAttribute("empPenaltyMajor", empList.size());

			empList = empRepo
					.findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsTypeOfPenaltyInflictedIdInAndCurrentUserAndCasedetailsIdNotIn(
							true, Arrays.asList(1L), currentUser, caseIdx);
			caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserAndIdNotIn(empList, currentUser, caseIdx);
			model.addAttribute("casesPenaltyMinor", caseList.size());
			model.addAttribute("empPenaltyMinor", empList.size());

			empList = empRepo
					.findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsDecisionTakenIdInAndCurrentUserAndCasedetailsIdNotIn(
							true, Arrays.asList(2L), currentUser, caseIdx);
			caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserAndIdNotIn(empList, currentUser, caseIdx);
			model.addAttribute("casesAq", caseList.size());
			model.addAttribute("empAq", empList.size());

			empList = empRepo
					.findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsDecisionTakenIdInAndCurrentUserAndCasedetailsIdNotIn(
							true, Arrays.asList(3L), currentUser, caseIdx);
			caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserAndIdNotIn(empList, currentUser, caseIdx);
			model.addAttribute("casesdeath", caseList.size());
			model.addAttribute("empdeath", empList.size());

			empList = empRepo
					.findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsDecisionTakenIdInAndCurrentUserAndCasedetailsIdNotIn(
							true, Arrays.asList(4L), currentUser, caseIdx);
			caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserAndIdNotIn(empList, currentUser, caseIdx);
			model.addAttribute("casesdropped", caseList.size());
			model.addAttribute("empdropped", empList.size());

			empList = empRepo
					.findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsDecisionTakenIdInAndCurrentUserAndCasedetailsIdNotIn(
							true, Arrays.asList(5L), currentUser, caseIdx);
			caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserAndIdNotIn(empList, currentUser, caseIdx);
			model.addAttribute("casescourtorder", caseList.size());
			model.addAttribute("empcourtorder", empList.size());

			if (StringUtils.isNotBlank(decision) && StringUtils.isBlank(penalty)) {

				List<EmployeeDetails> empListx = empRepo
						.findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsDecisionTakenIdInAndCurrentUserAndCasedetailsIdNotIn(
								true, Arrays.asList(Long.valueOf(decision)), currentUser, caseIdx);
				noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserAndIdNotIn(empListx, pageable,
						currentUser, caseIdx);
				noticePages.getContent().stream().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empListx.stream().forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
				allCases = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserAndIdNotIn(empListx, allPage, currentUser, caseIdx);
				model.addAttribute("empCount", empListx.size());
			} else if (StringUtils.isBlank(decision) && StringUtils.isNotBlank(penalty)) {
				if (penalty.equalsIgnoreCase("all")) {
					List<TypeOfPenaltyInflictedPojo> pen = penaltyRepo.findByActive(true);

					List<EmployeeDetails> empListx = empRepo
							.findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsTypeOfPenaltyInflictedInAndCurrentUserAndCasedetailsIdNotIn(
									true, pen, currentUser, caseIdx);
					noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserAndIdNotIn(empListx, pageable,
							currentUser, caseIdx);
					noticePages.getContent().stream().forEach(s -> {
						List<EmployeeDetails> empp = new ArrayList<>();
						empListx.stream().forEach(ss -> {
							if (ss.getCasedetails().getId() == s.getId()) {
								empp.add(ss);
							}
						});
						s.setEmployeeList(empp);
					});
					allCases = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserAndIdNotIn(empListx, allPage,
							currentUser, caseIdx);
					model.addAttribute("empCount", empListx.size());
				} else if (penalty.equalsIgnoreCase("major")) {

					List<EmployeeDetails> empListx = empRepo
							.findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsTypeOfPenaltyInflictedIdInAndCurrentUserAndCasedetailsIdNotIn(
									true, Arrays.asList(2L, 3L), currentUser, caseIdx);
					noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserAndIdNotIn(empListx, pageable,
							currentUser, caseIdx);
					noticePages.getContent().stream().forEach(s -> {
						List<EmployeeDetails> empp = new ArrayList<>();
						empListx.stream().forEach(ss -> {
							if (ss.getCasedetails().getId() == s.getId()) {
								empp.add(ss);
							}
						});
						s.setEmployeeList(empp);
					});
					allCases = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserAndIdNotIn(empListx, allPage,
							currentUser, caseIdx);
					model.addAttribute("empCount", empListx.size());
				} else {
					TypeOfPenaltyInflictedPojo pen = penaltyRepo.findById(Long.valueOf(penalty))
							.orElse(new TypeOfPenaltyInflictedPojo());
					List<TypeOfPenaltyInflictedPojo> listpen = new ArrayList<>();
					listpen.add(pen);

					List<EmployeeDetails> empListx = empRepo
							.findByFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsTypeOfPenaltyInflictedInAndCurrentUserAndCasedetailsIdNotIn(
									true, listpen, currentUser, caseIdx);
					noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserAndIdNotIn(empListx, pageable,
							currentUser, caseIdx);
					noticePages.getContent().stream().forEach(s -> {
						List<EmployeeDetails> empp = new ArrayList<>();
						empListx.stream().forEach(ss -> {
							if (ss.getCasedetails().getId() == s.getId()) {
								empp.add(ss);
							}
						});
						s.setEmployeeList(empp);
					});
					allCases = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserAndIdNotIn(empListx, allPage,
							currentUser, caseIdx);
					model.addAttribute("empCount", empListx.size());
				}
			} else {

				List<EmployeeDetails> empListx = empRepo
						.findByCasedetailsIsNotNullAndFinalOutcomeDetailsWhetherCaseFinallyDecidedAndCurrentUserAndCasedetailsIdNotIn(true,
								currentUser, caseIdx);
				noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserAndIdNotIn(empListx, pageable,
						currentUser, caseIdx);
				noticePages.getContent().stream().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empListx.stream().forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
				allCases = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserAndIdNotIn(empListx, allPage, currentUser, caseIdx);
				model.addAttribute("empCount", empListx.size());
			}

		}

		model.addAttribute("caseDetailsAllRecordsreport", allCases);
		model.addAttribute("caseDetailsAllRecords", noticePages);
		model.addAttribute("currentPage", page);
		int begin = Math.max(1, page - 5);
		if (page >= 5) {
			begin = page;
		}
		int end = Math.min(begin + 4, noticePages.getTotalPages());
		model.addAttribute("begin", begin);
		model.addAttribute("end", end);
		model.addAttribute("totalPages", noticePages.getTotalPages());
		model.addAttribute("totalItems", noticePages.getTotalElements());

		return "DeptInquiryViews/Reportsforfinaloutcomeall.html";

	}

//	private DashboardData getdashBoradData(GlobalOrg s) {
//		DashboardData data = new DashboardData();
//		
//		List<ReInstatedDetails> reinList = reintRepo.findByEmployeeReInstatedAndCasedetailsIsNotNullAndCurrentUserPimsEmployeeGlobalOrgId(true,s);
//		List<String> sevarthList = new ArrayList<>();
//		reinList.parallelStream().filter(ss->StringUtils.isNotBlank(ss.getSevarthId())).forEach(ss->sevarthList.add(ss.getSevarthId()));
//		List<SuspensionDetails> susList = susRepo.findByEmployeeSuspendedAndCasedetailsIsNotNullAndSevarthIdNotInAndCurrentUserPimsEmployeeGlobalOrgId(true,sevarthList,s);
//		List<String> sevarthList2 = new ArrayList<>();
//		susList.parallelStream().filter(ss->StringUtils.isNotBlank(ss.getSevarthId())).forEach(ss->sevarthList2.add(ss.getSevarthId()));
//		
//		List<EmployeeDetails> empList = empRepo.findBySevarthIdInAndCurrentUserPimsEmployeeGlobalOrgId(sevarthList2,s);
//		List<CaseDetails> caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgId(empList,s);
//		
//		data.setDepartmentName(s.getGlobalOrgName());
//		data.setCountCaseNo(caseList.size());
//		data.setTotalemployee(empList.size());
//		
//		
//		return data;
//	}

}
