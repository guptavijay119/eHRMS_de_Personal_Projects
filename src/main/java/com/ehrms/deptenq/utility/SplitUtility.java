package com.ehrms.deptenq.utility;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class SplitUtility {
	
	
	public List<Integer> getSplitdata(Integer total) {
		List<Integer> largeList = new ArrayList<>();
		while(true) {
			if(total>10) {
				largeList.add(10);
			} else {
				if(total>0 && total<=10) {
					largeList.add(total);
				} else {
					break;
				}
			}
			total=total-10;
		}
		
		return largeList;
	}

}
