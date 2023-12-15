package com.ehrms.deptenq.modelsDto;

import java.util.ArrayList;
import java.util.List;

public class DashboardData {
	
	private String departmentName;
	
	private Long orgid;
	
	private long countCaseNo;
	
	private long totalemployee;
	
	private long numberSuspended;
	
	private long caseSuspended;
	
	private long withoutdeSuspended;
	
	private long withoutdeProsecuted;
	
	private long numberReinstated;
	
	private long major;
	
	private long employeeCountMajor;
	
	private long minor;
	
	private long employeeCountMinor;
	
	private long greaterThan5years;
	
	private long employeeCountGreaterThan5years;
	
	private long lessThan5years;
	
	private long employeeCountLessThan5years;
	
	private long numberFinalCases;
	
	
	private long numberOfCasesWithAbeyanceCase;
	
	private long numberOfCasesWithProsecution;
	
	private long employeeCountProsecutionCases;
	
	private long numberOfCasesWithRetiredEmp;
	
	private long employeeCountRetiredCases;
	
	private GroupAData dataA;
	
	private GroupBData dataB;
	
	private GroupCData dataC;
	
	private GroupDData dataD;
	
	private CommonABData dataAB;
	
	
	private CommonCDData dataCD;
	
	private List<SubDepartmentData> subDepartmentList = new ArrayList<>();

	/**
	 * @return the dataA
	 */
	public GroupAData getDataA() {
		return dataA;
	}

	/**
	 * @param dataA the dataA to set
	 */
	public void setDataA(GroupAData dataA) {
		this.dataA = dataA;
	}

	/**
	 * @return the dataB
	 */
	public GroupBData getDataB() {
		return dataB;
	}

	/**
	 * @param dataB the dataB to set
	 */
	public void setDataB(GroupBData dataB) {
		this.dataB = dataB;
	}

	/**
	 * @return the dataAB
	 */
	public CommonABData getDataAB() {
		return dataAB;
	}

	/**
	 * @param dataAB the dataAB to set
	 */
	public void setDataAB(CommonABData dataAB) {
		this.dataAB = dataAB;
	}

	/**
	 * @return the numberOfCasesWithProsecution
	 */
	public long getNumberOfCasesWithProsecution() {
		return numberOfCasesWithProsecution;
	}

	/**
	 * @param numberOfCasesWithProsecution the numberOfCasesWithProsecution to set
	 */
	public void setNumberOfCasesWithProsecution(long numberOfCasesWithProsecution) {
		this.numberOfCasesWithProsecution = numberOfCasesWithProsecution;
	}

	/**
	 * @return the departmentName
	 */
	public String getDepartmentName() {
		return departmentName;
	}

	/**
	 * @param departmentName the departmentName to set
	 */
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	/**
	 * @return the countCaseNo
	 */
	public long getCountCaseNo() {
		return countCaseNo;
	}

	/**
	 * @param countCaseNo the countCaseNo to set
	 */
	public void setCountCaseNo(long countCaseNo) {
		this.countCaseNo = countCaseNo;
	}

	/**
	 * @return the totalemployee
	 */
	public long getTotalemployee() {
		return totalemployee;
	}

	/**
	 * @param totalemployee the totalemployee to set
	 */
	public void setTotalemployee(long totalemployee) {
		this.totalemployee = totalemployee;
	}

	/**
	 * @return the numberSuspended
	 */
	public long getNumberSuspended() {
		return numberSuspended;
	}

	/**
	 * @param numberSuspended the numberSuspended to set
	 */
	public void setNumberSuspended(long numberSuspended) {
		this.numberSuspended = numberSuspended;
	}

	/**
	 * @return the numberReinstated
	 */
	public long getNumberReinstated() {
		return numberReinstated;
	}

	/**
	 * @param numberReinstated the numberReinstated to set
	 */
	public void setNumberReinstated(long numberReinstated) {
		this.numberReinstated = numberReinstated;
	}

	/**
	 * @return the major
	 */
	public long getMajor() {
		return major;
	}

	/**
	 * @param major the major to set
	 */
	public void setMajor(long major) {
		this.major = major;
	}

	/**
	 * @return the minor
	 */
	public long getMinor() {
		return minor;
	}

	/**
	 * @param minor the minor to set
	 */
	public void setMinor(long minor) {
		this.minor = minor;
	}

	/**
	 * @return the greaterThan5years
	 */
	public long getGreaterThan5years() {
		return greaterThan5years;
	}

	/**
	 * @param greaterThan5years the greaterThan5years to set
	 */
	public void setGreaterThan5years(long greaterThan5years) {
		this.greaterThan5years = greaterThan5years;
	}

	/**
	 * @return the lessThan5years
	 */
	public long getLessThan5years() {
		return lessThan5years;
	}

	/**
	 * @param lessThan5years the lessThan5years to set
	 */
	public void setLessThan5years(long lessThan5years) {
		this.lessThan5years = lessThan5years;
	}

	/**
	 * @return the numberFinalCases
	 */
	public long getNumberFinalCases() {
		return numberFinalCases;
	}

	/**
	 * @param numberFinalCases the numberFinalCases to set
	 */
	public void setNumberFinalCases(long numberFinalCases) {
		this.numberFinalCases = numberFinalCases;
	}

	/**
	 * @return the numberOfCasesWithAbeyanceCase
	 */
	public long getNumberOfCasesWithAbeyanceCase() {
		return numberOfCasesWithAbeyanceCase;
	}

	/**
	 * @param numberOfCasesWithAbeyanceCase the numberOfCasesWithAbeyanceCase to set
	 */
	public void setNumberOfCasesWithAbeyanceCase(long numberOfCasesWithAbeyanceCase) {
		this.numberOfCasesWithAbeyanceCase = numberOfCasesWithAbeyanceCase;
	}

	/**
	 * @return the numberOfCasesWithRetiredEmp
	 */
	public long getNumberOfCasesWithRetiredEmp() {
		return numberOfCasesWithRetiredEmp;
	}

	/**
	 * @param numberOfCasesWithRetiredEmp the numberOfCasesWithRetiredEmp to set
	 */
	public void setNumberOfCasesWithRetiredEmp(long numberOfCasesWithRetiredEmp) {
		this.numberOfCasesWithRetiredEmp = numberOfCasesWithRetiredEmp;
	}

	/**
	 * @return the subDepartmentList
	 */
	public List<SubDepartmentData> getSubDepartmentList() {
		return subDepartmentList;
	}

	/**
	 * @param subDepartmentList the subDepartmentList to set
	 */
	public void setSubDepartmentList(List<SubDepartmentData> subDepartmentList) {
		this.subDepartmentList = subDepartmentList;
	}

	/**
	 * @return the employeeCountMajor
	 */
	public long getEmployeeCountMajor() {
		return employeeCountMajor;
	}

	/**
	 * @param employeeCountMajor the employeeCountMajor to set
	 */
	public void setEmployeeCountMajor(long employeeCountMajor) {
		this.employeeCountMajor = employeeCountMajor;
	}

	/**
	 * @return the employeeCountMinor
	 */
	public long getEmployeeCountMinor() {
		return employeeCountMinor;
	}

	/**
	 * @param employeeCountMinor the employeeCountMinor to set
	 */
	public void setEmployeeCountMinor(long employeeCountMinor) {
		this.employeeCountMinor = employeeCountMinor;
	}

	/**
	 * @return the employeeCountGreaterThan5years
	 */
	public long getEmployeeCountGreaterThan5years() {
		return employeeCountGreaterThan5years;
	}

	/**
	 * @param employeeCountGreaterThan5years the employeeCountGreaterThan5years to set
	 */
	public void setEmployeeCountGreaterThan5years(long employeeCountGreaterThan5years) {
		this.employeeCountGreaterThan5years = employeeCountGreaterThan5years;
	}

	/**
	 * @return the employeeCountLessThan5years
	 */
	public long getEmployeeCountLessThan5years() {
		return employeeCountLessThan5years;
	}

	/**
	 * @param employeeCountLessThan5years the employeeCountLessThan5years to set
	 */
	public void setEmployeeCountLessThan5years(long employeeCountLessThan5years) {
		this.employeeCountLessThan5years = employeeCountLessThan5years;
	}

	/**
	 * @return the employeeCountProsecutionCases
	 */
	public long getEmployeeCountProsecutionCases() {
		return employeeCountProsecutionCases;
	}

	/**
	 * @param employeeCountProsecutionCases the employeeCountProsecutionCases to set
	 */
	public void setEmployeeCountProsecutionCases(long employeeCountProsecutionCases) {
		this.employeeCountProsecutionCases = employeeCountProsecutionCases;
	}

	/**
	 * @return the employeeCountRetiredCases
	 */
	public long getEmployeeCountRetiredCases() {
		return employeeCountRetiredCases;
	}

	/**
	 * @param employeeCountRetiredCases the employeeCountRetiredCases to set
	 */
	public void setEmployeeCountRetiredCases(long employeeCountRetiredCases) {
		this.employeeCountRetiredCases = employeeCountRetiredCases;
	}

	/**
	 * @return the caseSuspended
	 */
	public long getCaseSuspended() {
		return caseSuspended;
	}

	/**
	 * @param caseSuspended the caseSuspended to set
	 */
	public void setCaseSuspended(long caseSuspended) {
		this.caseSuspended = caseSuspended;
	}

	/**
	 * @return the dataC
	 */
	public GroupCData getDataC() {
		return dataC;
	}

	/**
	 * @param dataC the dataC to set
	 */
	public void setDataC(GroupCData dataC) {
		this.dataC = dataC;
	}

	/**
	 * @return the dataD
	 */
	public GroupDData getDataD() {
		return dataD;
	}

	/**
	 * @param dataD the dataD to set
	 */
	public void setDataD(GroupDData dataD) {
		this.dataD = dataD;
	}

	public long getWithoutdeSuspended() {
		return withoutdeSuspended;
	}

	public void setWithoutdeSuspended(long withoutdeSuspended) {
		this.withoutdeSuspended = withoutdeSuspended;
	}

	public long getWithoutdeProsecuted() {
		return withoutdeProsecuted;
	}

	public void setWithoutdeProsecuted(long withoutdeProsecuted) {
		this.withoutdeProsecuted = withoutdeProsecuted;
	}

	public Long getOrgid() {
		return orgid;
	}

	public void setOrgid(Long orgid) {
		this.orgid = orgid;
	}

	public CommonCDData getDataCD() {
		return dataCD;
	}

	public void setDataCD(CommonCDData dataCD) {
		this.dataCD = dataCD;
	}

	
	
	
}
