package com.dbserver.desafiovotacaofullstack.utils;

import java.util.Arrays;

public class FormatStackTracerException {
	
	public static String getFormatStackTracer(StackTraceElement[] stackTrace) {
		 return Arrays.stream(stackTrace).map(StackTraceElement::toString).reduce("", (partialResult, currentString) -> partialResult + currentString + "\n");
	}
}
