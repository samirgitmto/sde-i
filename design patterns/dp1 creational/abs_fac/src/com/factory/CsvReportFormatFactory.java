package com.factory;

public class CsvReportFormatFactory implements ReportFormatFactory {

	@Override
	public Formatter getReportFormatter() {
		return new CsvFormatter();
	}
	
	
	@Override
	public Validator getReportValidator() {
		return new CsvValidator();
	}
	
}