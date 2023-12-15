package com.ehrms.deptenq.constants;

/**
 * The Enum CPTPCONSTANTS.
 */
public enum CPTPCONSTANTS {
	
	
	BASE64STRING("data:application/pdf),base64,"),	
	RESERVEDCATE("reservedcate"),
	NOTICE("notice"),
	MESSAGE("message"),	
	REDIRECTINBOXGADCPTP("redirect:/recruitmentcptp/inboxgad/ptype/CPTP/page/1"),
	
	REDIRECTLOGINCPTP("redirect:/recruitmentcptpcandidate/authApplicationForm"),
	
	FILENOTFOUND("filenotfound"),
	CPTPINDEXGAD("1.2CPTP/indexgad"),  /*1.2CPTP/fragments/FormDepartment */
		
	/** The Constant REDIRECTCPTPNOTICEINBOXGAD. */
	 REDIRECTCPTPNOTICEINBOXGAD ("redirect:/recruitmentcptp/noticeinboxgad/page/1"),
	
	 REDIRECTCPTPMASTERNOTICEINBOXGAD ("redirect:/recruitmentcptp/masternoticeinboxgad/page/1"),
		
	 
	/** The Constant NOTICEGAD. */
	 NOTICEGAD ("noticegad"),
	
	/** The Constant PROCESSTYPE. */
	 PROCESSTYPE ("CPTP"),
	
	/** The Constant PREVIOUSPROPOSAL. */
	 PREVIOUSPROPOSAL ("previousproposal"),
	
	/** The Constant INBOXREDIRECTSUCCESSSAVE. */
	 INBOXREDIRECTSUCCESSSAVE ("inbox.redirect.successsave"),
	
	/** The Constant REDIRECTMPSCLETTER. */
	 REDIRECTMPSCLETTER ("redirect:/recruitmentcptp/mpscletter/"),
	
	/** The Constant GETMPSCFILE. */
	 GETMPSCFILE ("redirect:/recruitmentcptp/getmpscfilegad/"),
	 
	 /** The Constant GETMPSCFILE. */
	 GETMPSCFILEALL ("redirect:/recruitmentcptp/importallcandidates/"),
	
	
	/** The Constant CURRENTPAGE. */
	 CURRENTPAGE ("currentPage"),
	
	/** The Constant BEGIN. */
	 BEGIN ("begin"),
	 
	 END("end"),
	
	/** The Constant TOTALPAGES. */
	 TOTALPAGES ("totalPages"),
	
	/** The Constant TOTALITEMS. */
	 TOTALITEMS ("totalItems"),
	
	/** The Constant NOTICELIST. */
	 NOTICELIST ("noticelist"),
	
	/** The Constant CPTPINDEX. */
	 CPTPINDEX ("1.2CPTP/indexcptp"),
	
	/** The Constant PROPOSAL. */
	 PROPOSAL ("proposal"),
	
	/** The Constant NOTICEID. */
	 NOTICEID ("proposalId"),
		
	/** The Constant CANDIDATELIST. */
	 CANDIDATELIST ("candidatelist"),
	
	/** The Constant QUERYPROCESSTYPE. */
	 QUERYPROCESSTYPE ("?processType="),
	
	REQUISITION("requisition"),
	

	CONTENTDEPOSITION("Content-Disposition"),
	
	CANDIDATEMAP("candidatemap"),
	
	CANDIDATEDTO("candidatedto"),
	
	BASEURL("baseURL"),
	CANDIDATELISTTEMP("1.2CPTP/templateformat/candidatelisttemp"),
	INLINEFILENAME("inline; filename="),
	
	OPTION1("option1"),
	
	PROPOSALLIST("proposalList");
	
	public final String label;

    private CPTPCONSTANTS(String label) {
        this.label = label;
    }
	
}
