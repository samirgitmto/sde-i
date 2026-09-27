package com.factory;

public class HtmlReportFormatFactory implements ReportFormatFactory {

	@Override
	public Formatter getReportFormatter() {
		return new HtmlFormatter();
	}

	@Override
	public Validator getReportValidator() {
		// TODO Auto-generated method stub
		return new HtmlValidator();
	}
	
}
