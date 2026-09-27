package com.demo1.ui;

public class DarkThemeFactory implements UIFactory {

	@Override
	public Button createButton() {
		return new DarkButton();
	}
	
	@Override
	public TextBox createTextBox() {
		return new DarkTextBox();
	}
}
