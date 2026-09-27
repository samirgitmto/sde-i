package com.factory;

import com.report.Report;

public interface Formatter {

	String generateFormattedReport(Report report);


	//	Encapsulate Validation Inside the Formatter
//	Validator getValidator(); // Optionally expose the validator
//	default String safeFormat(Report report) {
//		if (getValidator().isValidated(report)) {
//			return generateFormattedReport(report);
//		} else {
//			return "Report is invalid!";
//		}
//	}

}
