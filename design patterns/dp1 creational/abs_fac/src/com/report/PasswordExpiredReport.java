package com.report;

public class PasswordExpiredReport implements Report {

	@Override
	public String getReport() {
		return getClass().getSimpleName();
	}
	
}
