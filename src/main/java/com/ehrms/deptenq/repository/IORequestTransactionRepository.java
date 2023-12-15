package com.ehrms.deptenq.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ehrms.deptenq.models.IORequestTransaction;
import com.ehrms.deptenq.models.InquiryOfficerList;

@Repository
public interface IORequestTransactionRepository extends JpaRepository<IORequestTransaction, Long>{

	boolean existsByRequestStatusAndCasedetailsId(String string, Long caseid);

	List<IORequestTransaction> findByIo(InquiryOfficerList io);

	List<IORequestTransaction> findByIoAndRequestStatus(InquiryOfficerList io, String string);

	int countByIoAndRejectedDateIsNotNull(InquiryOfficerList io);

	int countByIoAndApprovalDateIsNotNull(InquiryOfficerList io);

	int countByIo(InquiryOfficerList io);

}
