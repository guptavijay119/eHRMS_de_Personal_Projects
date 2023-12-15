package com.ehrms.deptenq.constants;

public enum PERMANENCYCONSTANTS {
	FILEPERMANENCYLIST("filepermanencylist"),FILEPERMANENCY("filepermanency"),DESIGNATION("designation"),PROMOTIONDESIGN("promotiondesign"),MESSAGE("message"),CPTAUTH("CPTAUTH"),
	REDIRECTINBOX("redirect:/permanency/inboxpermanencyfile/page/1"),
	REDIRECTSUCCESS("inbox.redirect.successsave"),
	REDIRECTINBOXORDER("redirect:/permanency/inboxgenerateorderperm/page/1?auth=DEP"),
	SUCCESSSAVED("listUnderprobation.successsaved");
	
	public final String label;
	
	private PERMANENCYCONSTANTS(String label) {
		this.label = label;
	}
	
}
