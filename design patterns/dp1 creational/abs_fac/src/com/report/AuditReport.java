package com.report;

public class AuditReport implements Report {

	@Override
	public String getReport() {
		return getClass().getSimpleName();
	}
	
}
