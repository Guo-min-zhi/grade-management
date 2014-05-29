package org.bjtuse.egms.util;

import org.apache.commons.lang3.exception.ExceptionUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExceptionLog {
	public static void log(Exception e){
		log.error("\n" + ExceptionUtils.getStackTrace(e));
	}
}
