package com.bon.customview.edittext.validators;

/**
 * Alphabet validator extend;
 * @author Dang
 * @year 2016
 */

public class AlphaValidator extends RegExpValidator {
	public AlphaValidator(String error) {
		super("^[a-zA-Z \\.,:;/-]*$", error);
	}
}
