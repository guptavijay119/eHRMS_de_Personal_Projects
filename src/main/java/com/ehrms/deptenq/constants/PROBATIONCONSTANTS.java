package com.ehrms.deptenq.constants;

public enum PROBATIONCONSTANTS {
	FILEPROBATION("fileprobations"),DESIGNATION("designation"),PROMOTIONDESIGN("promotiondesign"),MESSAGE("message"),PROBATIONEXTENSION("Probation Extension"),TERMINATION("Termination"),
	FILENAMENOTFOUND("fileNameNotFound"),EMPLOYEEFILENAMENOTFOUND("EmployeeDetail.redirect.fileNameNotFound"),SELECTDESIGCLEARPROBATIONS("selectDesigClearProbations"),
	REDIRECTINBOXPROBATIONS("redirect:/probation/inboxprobationsfile/page/1"),CPTAUTH("CPTAUTH"),FILEPROBATIONSLIST("fileprobationslist"),
	INBOXREDIRECTSUCCESS("inbox.redirect.successsave"),
	REDIRECTINDEXGENERATEORDER("redirect:/probation/inboxgenerateorder/page/1?auth=DEP"),
	CONTENTDEPOSITION("Content-Disposition"),	
	INLINEFILENAME("inline; filename="),
	SHOWLIST("showlist"),
	CANDIDATELIST("candidatelist"),
	GROUPLIST("groupList"),
	BASE64STRING("data:application/pdf;base64,");
	
	public final String label;

    private PROBATIONCONSTANTS(String label) {
        this.label = label;
    }
}
