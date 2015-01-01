package com.lelann.json.utils;

public enum JData {
	NEXT_DATA(), // , }
	NEW_DATA(), // "
	DATA_OPEN(), // :
	DATA_VALUE(), // {, [, ", 1~9
	VAR_NBR(); // 1~9, .
}
