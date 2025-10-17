package com.luizbarros.dscatalog.controllers.exceptions;

import java.time.Instant;

public record StandardError(
	Instant timestamp,
	Integer status,
	String error,
	String message,
	String path) {
}
