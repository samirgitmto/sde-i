package com.factory;

import com.report.Report;

public class PdfValidator implements Validator {

	@Override
	public Boolean isValidated(Report report) {
		return false;
	}
	
}
