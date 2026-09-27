package com.factory;

import com.report.Report;

public class HtmlValidator implements Validator {

	@Override
	public Boolean isValidated(Report report) {
		return true;
	}

}
