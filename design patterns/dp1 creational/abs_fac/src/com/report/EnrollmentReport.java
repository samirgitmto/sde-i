package com.report;

public class EnrollmentReport implements Report {

	@Override
	public String getReport() {
		return getClass().getSimpleName();
	}
	
}