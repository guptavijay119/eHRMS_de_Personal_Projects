package com.ehrms.deptenq.utility;

import java.io.File;

public class DirectoryUtil {
	
	private DirectoryUtil() {
		
	}
	

	
	public static final String NOTICEGADCPTP = "recruitmentcptp/notice";
	
	public static final String PROPOSALRECRUITMENT1_2MPSC = "recruitment1_2/proposal";
	
	public static final String PROPOSALRECRUITMENTSPORTS = "recruitmentsports/proposal";
	
	public static final String PROPOSALRECRUITMENT1_3MPSC = "recruitment1_3/proposal";
	
	public static final String PROPOSALRECRUITMENTBC = "recruitmentbc/proposal";
		
	public static final String COMPASSIONATEEMPLOYEEDIRECTORY = "compassionate/employee";
	
	public static final String APPLICANTSDIRECTORY = "/applicants/";
	
	public static final String MPSCCANDIDATEFILES = "/candidate/";
	
	public static final String PROBATION = "probation";
	
	public static final String EMPLOYEE = "employee";
	
	public static final String PERMANENCY = "permanency";
	
	public static final String GAD = "GENERAL ADMINISTRATION DEPARTMENT(GAD),MAHARASHTRA";
	
	private static final String FILEREPOSITORY = "filerepository";
	
	private static final String MASTERNOTICECPTP = "masternoticeCPTP";
	
	public static String getprobationfile(String department,long fileid) {
		return FILEREPOSITORY+File.separator+department+File.separator+PROBATION+File.separator+fileid;
	}
	
	public static String getpermanencyfile(String department,long fileid) {
		return FILEREPOSITORY+File.separator+department+File.separator+PERMANENCY+File.separator+fileid;
	}
	
	public static String getprombationemployeefile(String department,long probationemployeeid) {
		return FILEREPOSITORY+File.separator+department+File.separator+PROBATION+File.separator+EMPLOYEE+File.separator+probationemployeeid;
	}
	
	
	public static String getpermanencyemployeefile(String department,long permanencyemployeeid) {
		return FILEREPOSITORY+File.separator+department+File.separator+PERMANENCY+File.separator+EMPLOYEE+File.separator+permanencyemployeeid;
	}
	
	public static String getnoticegadcptp(String department,long noticeid) {
		return FILEREPOSITORY+File.separator+department+File.separator+NOTICEGADCPTP+File.separator+noticeid;
	}
	
	public static String getMpscCandidateFiles(String department,long proposalId,long mpscncandidateid) {
		return FILEREPOSITORY+File.separator+department+File.separator+PROPOSALRECRUITMENT1_2MPSC+File.separator+proposalId+MPSCCANDIDATEFILES+mpscncandidateid;
	}
	
	public static String getProposalRecruitment13MPSC(String department,long proposalId) {
		return FILEREPOSITORY+File.separator+department+File.separator+PROPOSALRECRUITMENT1_3MPSC+File.separator+proposalId;
	}
	
	public static String getProposalRecruitment12MPSC(String department,long proposalId) {
		return FILEREPOSITORY+File.separator+department+File.separator+PROPOSALRECRUITMENT1_2MPSC+File.separator+proposalId;
	}
	
	public static String getProposalSports(String department,long proposalId) {
		return FILEREPOSITORY+File.separator+department+File.separator+PROPOSALRECRUITMENTSPORTS+File.separator+proposalId;
	}
	
	public static String getProposalRecruitmentBC(String department,long proposalId) {
		return FILEREPOSITORY+File.separator+department+File.separator+PROPOSALRECRUITMENTBC+File.separator+proposalId;
	}
	
	public static String getCompassionateApplicants(String department,long employeeid) {
		return FILEREPOSITORY+File.separator+department+File.separator+COMPASSIONATEEMPLOYEEDIRECTORY+File.separator+employeeid+APPLICANTSDIRECTORY;
	}
	
	public static String getCompassionateEmployeeFile(String department,long employeeid) {
		return FILEREPOSITORY+File.separator+department+File.separator+COMPASSIONATEEMPLOYEEDIRECTORY+File.separator+employeeid+"/";
	}
	
	public static String getDirectSportsApplicantsFile(String department,long applicantsId) {
		return FILEREPOSITORY+File.separator+department+File.separator+applicantsId+File.separator;
	}
	
	public static String getCandidateFileProposalMPSC(String department,long proposalId,long candidateId) {
		return FILEREPOSITORY+File.separator+department+File.separator+"recruitment1_2MPSC"+File.separator+proposalId+MPSCCANDIDATEFILES+candidateId;
	}
	
	public static String getCandidateFileProposalBC(String department,long proposalId,long candidateId) {
		return FILEREPOSITORY+File.separator+department+File.separator+"recruitmentbc"+File.separator+proposalId+MPSCCANDIDATEFILES+candidateId;
	}
	
	public static String getMasterNoticeFile(String department,String masterNoticeId) {
		return FILEREPOSITORY+File.separator+department+File.separator+MASTERNOTICECPTP+File.separator+masterNoticeId;
	}
	
	public static String getCPTPproposalFilePath(String department,String masterNoticeId,long proposalId) {
		return FILEREPOSITORY+File.separator+GAD+File.separator+MASTERNOTICECPTP+File.separator+masterNoticeId+File.separator+department+File.separator+"proposalCPTP"
				+File.separator+proposalId;
	}

	public static String getCPTPRequistionFilePath(String masterNoticeId,long requistionid) {
		return FILEREPOSITORY+File.separator+GAD
				+File.separator+MASTERNOTICECPTP
				+File.separator+masterNoticeId
				+File.separator+"requistionCPTP"
				+File.separator+requistionid;
//				+File.separator+department
//				+File.separator+"proposalCPTP";
	}
	
	public static String getCPTPRequistionFilePathMailToCandidate(String masterNoticeId,long requistionid) {
		return FILEREPOSITORY+File.separator+GAD
				+File.separator+MASTERNOTICECPTP
				+File.separator+masterNoticeId
				+File.separator+"requistionCPTP"
				+File.separator+requistionid
				+File.separator+"mailtocandidates";
//				+File.separator+department
//				+File.separator+"proposalCPTP";
	}
	
	public static String getCPTPRequistionFilePathMailToCandidateExtension(String masterNoticeId,long requistionid) {
		return FILEREPOSITORY+File.separator+GAD
				+File.separator+MASTERNOTICECPTP
				+File.separator+masterNoticeId
				+File.separator+"requistionCPTP"
				+File.separator+requistionid
				+File.separator+"mailtocandidates"
				+File.separator+"extensionorder";
//				+File.separator+department
//				+File.separator+"proposalCPTP";
	}
	
	public static String getCPTPRequistionFilePathMPSC(String masterNoticeId,long requistionid) {
		return FILEREPOSITORY+File.separator+GAD
				+File.separator+MASTERNOTICECPTP
				+File.separator+masterNoticeId
				+File.separator+"requistionCPTP"
				+File.separator+requistionid
//				+File.separator+department
				+File.separator+"importedcandidatelist";
	}
	
	public static String getCPTPrequisitionPoliceVerificationFile(String department,long requisitionId) {
		return FILEREPOSITORY+File.separator+department+File.separator+"requisitionCPTP"+File.separator+"policeverification"+File.separator+requisitionId;
	}
	
	public static String getCPTPrequisitionMedicalVerificationFile(String department,long requisitionId) {
		return FILEREPOSITORY+File.separator+department+File.separator+"requisitionCPTP"+File.separator+"medicalverification"+File.separator+requisitionId;
	}
	
	public static String getProposalRequisitionBC(String department,long proposalId,long requisitionId) {
		return FILEREPOSITORY+File.separator+department+File.separator+PROPOSALRECRUITMENT1_2MPSC+File.separator+proposalId+File.separator+requisitionId;
		
	}	
	public static String getProposalRequisitionCandidatefileBC(String department,long proposalId,long requisitionId) {
		return FILEREPOSITORY+File.separator+department+File.separator+PROPOSALRECRUITMENT1_2MPSC+File.separator+proposalId+File.separator+requisitionId+File.separator+"mpsccandidatefile";
		
	}
	
	public static String getProposalRequisitionCandidatefilewithoutRequistionMPSC(String department,long proposalId) {
		return FILEREPOSITORY+File.separator+department+File.separator+PROPOSALRECRUITMENT1_2MPSC+File.separator+proposalId+File.separator+"mpsccandidatefile";
		
	}
	
	public static String getProposalRequisitionCandidatefilewithoutRequistionD(String department,long proposalId) {
		return FILEREPOSITORY+File.separator+department+File.separator+PROPOSALRECRUITMENT1_2MPSC+File.separator+proposalId+File.separator+"mpsccandidatefile";
		
	}
	


}
