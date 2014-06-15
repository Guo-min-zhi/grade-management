package org.bjtuse.egms.util;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ImportResult {

	int successCount;
	
	int failCount;
	
	List<String> messages;
	
	public ImportResult(){
		successCount = 0;
		failCount = 0;
		messages = new ArrayList<String>();
	}
}
