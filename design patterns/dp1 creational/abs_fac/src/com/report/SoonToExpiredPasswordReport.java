package com.report;

public class SoonToExpiredPasswordReport implements Report {

	@Override
	public String getReport() {
		return getClass().getSimpleName();
	}
	
}
