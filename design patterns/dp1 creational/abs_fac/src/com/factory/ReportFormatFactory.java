package com.factory;

public interface ReportFormatFactory {

	Formatter getReportFormatter();
	
	Validator getReportValidator();
	
}