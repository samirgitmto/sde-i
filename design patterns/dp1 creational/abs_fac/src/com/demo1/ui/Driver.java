package com.demo1.ui;

public class Driver {

	public static void main(String[] args) {
		
		UIFactory uiFactory = new DarkThemeFactory();
		Button button = uiFactory.createButton();
		button.describe();
		TextBox textBox = uiFactory.createTextBox();
		textBox.describe();
		
	}
	
}