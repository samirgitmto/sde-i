package com.driver;

import com.factory.Formatter;
import com.factory.HtmlReportFormatFactory;
import com.factory.PdfReportFormatFactory;
import com.factory.ReportFormatFactory;
import com.factory.Validator;
import com.report.PasswordExpiredReport;
import com.report.Report;

public class Client {

	public static void main(String[] args) {
		
		Report pwdExpiryReport = new PasswordExpiredReport();
//		ReportFormatFactory reportFormatFactory = new HtmlReportFormatFactory();
		ReportFormatFactory reportFormatFactory = new PdfReportFormatFactory();
		Formatter formatter = reportFormatFactory.getReportFormatter();
		String result = formatter.generateFormattedReport(pwdExpiryReport);
		System.out.println(result);

		
		Validator validator = reportFormatFactory.getReportValidator();

		Report report = new PasswordExpiredReport();
		if (validator.isValidated(report)) {
		    String output = formatter.generateFormattedReport(report);
		    System.out.println(output);
		} else {
		    System.out.println("Report is invalid!");
		} 
	}
	
}