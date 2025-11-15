package workwell.WorkWell.controller.advice;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import workwell.WorkWell.exception.ApiErrorResponse;
import workwell.WorkWell.exception.BusinessException;
import workwell.WorkWell.exception.ResourceNotFoundException;

@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex,
		HttpServletRequest request) {
		List<String> details = ex.getBindingResult()
			.getFieldErrors()
			.stream()
			.map(this::formatFieldError)
			.collect(Collectors.toList());

		return buildError(HttpStatus.BAD_REQUEST, "Erro de validação", details, request.getRequestURI());
	}

	private String formatFieldError(FieldError error) {
		return error.getField() + ": " + error.getDefaultMessage();
	}

	@ExceptionHandler(BusinessException.class)
	ResponseEntity<ApiErrorResponse> handleBusiness(BusinessException ex, HttpServletRequest request) {
		return buildError(HttpStatus.UNPROCESSABLE_ENTITY, "Regra de negócio violada",
			List.of(ex.getMessage()), request.getRequestURI());
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	ResponseEntity<ApiErrorResponse> handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
		return buildError(HttpStatus.NOT_FOUND, "Recurso não encontrado",
			List.of(ex.getMessage()), request.getRequestURI());
	}

	@ExceptionHandler(AccessDeniedException.class)
	ResponseEntity<ApiErrorResponse> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
		return buildError(HttpStatus.FORBIDDEN, "Acesso negado",
			List.of(ex.getMessage()), request.getRequestURI());
	}

	@ExceptionHandler(Exception.class)
	ResponseEntity<ApiErrorResponse> handleGeneric(Exception ex, HttpServletRequest request) {
		return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno",
			List.of(ex.getMessage()), request.getRequestURI());
	}

	private ResponseEntity<ApiErrorResponse> buildError(HttpStatus status, String error,
		List<String> details, String path) {
		ApiErrorResponse body = new ApiErrorResponse(
			Instant.now(),
			status.value(),
			error,
			details.isEmpty() ? error : details.get(0),
			details,
			path
		);
		return ResponseEntity.status(status).body(body);
	}
}

