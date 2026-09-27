package com.factory;

import com.report.Report;

public class PdfFormatter implements Formatter {

	@Override
	public String generateFormattedReport(Report report) {
		return report.getReport() + " by " + getClass().getSimpleName();
	}
	
}
