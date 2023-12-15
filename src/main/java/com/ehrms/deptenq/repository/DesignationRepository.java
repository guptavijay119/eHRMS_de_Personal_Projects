package com.ehrms.deptenq.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ehrms.deptenq.models.Designation;
import com.ehrms.deptenq.models.Service_Group;

public interface DesignationRepository extends JpaRepository<Designation, Long> {

	List<Designation> findByServiceGroupId(Service_Group group);

	Object findByServiceGroupIdNotIn(List<Long> longs);

	 List<Designation> findByServiceGroupId_IdNotIn(List<Long> longs); 

	/* List<Designation> findByOrderById(); */
	
//	@Query("select d from Designation d where d.designation_name_en=?1")
	/*List<Designation> findByDesignationName(String name);

	List<Designation> findAllByServiceGroupId(Service_Group serviceGroup);

	//List<Designation> findByRrulesListIn(List<RRules> rrRules);

	List<Designation> findByDesignationCodeIn(List<String> designationCodeList);

	Designation findByDesignationCode(String designationCode);


	List<Designation> findByServiceGroupIdAndDesignationCodeIn(Service_Group orElse, List<String> designationCodeList);

	List<Designation> findByPayBandFixedLessThanEqual(int fixed);

	List<Designation> findByDesignationCodeNotNull();

	List<Designation> findByServiceGroupIdAndActiveAndPayBandLowLimitLessThan(Service_Group serviceGroup, boolean b,
			int i);

	List<Designation> findByServiceGroupIdAndActiveAndPayBandLowLimitGreaterThanEqual(Service_Group serviceGroup,
			boolean b, int i);

	
	 * List<Designation>
	 * findByServiceGroupIdInAndActiveAndPayBandLowLimitLessThan(List<Service_Group>
	 * groupList, boolean b, int i);
	 

//	List<Designation> findByServiceGroupIdAndActive(Service_Group serviceGroup, boolean b);

*/
}
