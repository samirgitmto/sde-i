package com.factory;

public class PdfReportFormatFactory implements ReportFormatFactory {

	@Override
	public Formatter getReportFormatter() {
		return new PdfFormatter();
	}
	
	@Override
	public Validator getReportValidator() {
		// TODO Auto-generated method stub
		return new PdfValidator();
	}
	
}