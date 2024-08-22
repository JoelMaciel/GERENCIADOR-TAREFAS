package com.joelmaciel.api_gerenciador.api.exceptionhandler;

import com.fasterxml.jackson.databind.JsonMappingException;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    public static final String MSG_ERRO_GENERICO_USUARIO_FINAL = "Ocorreu um erro interno inesperado do sistema. "
            + "Tente novamente e se o problema persistir, entre em contato com o administrador do sistema.";

    private final MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUncaught(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ProblemType problemType = ProblemType.ERROR_SISTEMA;

        String detail = MSG_ERRO_GENERICO_USUARIO_FINAL;
        ex.printStackTrace();

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(detail)
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<?> PropertyReference(PropertyReferenceException ex, WebRequest webRequest) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ProblemType problemType = ProblemType.DADOS_INVALIDOS;
        String detail = ex.getMessage();

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(detail)
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, webRequest);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolation(ConstraintViolationException ex, WebRequest webRequest) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ProblemType problemType = ProblemType.DADOS_INVALIDOS;
        String detail = ex.getMessage();

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(detail)
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, webRequest);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request
    ) {
        ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSÍVEL;
        String detail = "O corpo da solicitação é inválido. Verifique se preencheu os campos corretamente" +
                " ou se há erros de sintaxe";

        Problem problem = createProblemBuilder(statusCode, problemType, detail)
                .userMessage(detail)
                .build();

        return handleExceptionInternal(ex, problem, headers, statusCode, request);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request
    ) {

        ProblemType problemType = ProblemType.RECURSO_NAO_EXISTE;
        String detail = String.format("O recurso %s, que você tentou acessar, não existe.", ex.getRequestURL());

        Problem problem = createProblemBuilder(statusCode, problemType, detail).build();

        return handleExceptionInternal(ex, problem, headers, statusCode, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request
    ) {
        ProblemType problemType = ProblemType.DADOS_INVALIDOS;
        String detail = "Um ou mais campos estão inválidos. Preencha corretamente e tente novamente.";

        BindingResult bindingResult = ex.getBindingResult();

        List<Problem.Object> problemObjects = bindingResult.getFieldErrors().stream()
                .map(fieldError -> {

                    String message = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());

                    String fieldName = fieldError.getField();
                    String simpleFieldName = fieldName.contains("[") ?
                            fieldName.substring(fieldName.lastIndexOf("]") + 2) : fieldName;

                    return Problem.Object.builder()
                            .name(simpleFieldName)
                            .userMessage(message)
                            .build();
                })
                .toList();

        Problem problem = createProblemBuilder(statusCode, problemType, detail)
                .userMessage(detail)
                .objects(problemObjects)
                .build();

        return handleExceptionInternal(ex, problem, headers, statusCode, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request
    ) {
        if (body == null) {
            body = Problem.builder()
                    .timestamp(LocalDateTime.now())
                    .title(statusCode.toString())
                    .status(statusCode.value())
                    .userMessage(MSG_ERRO_GENERICO_USUARIO_FINAL)
                    .build();
        } else if (body instanceof String) {
            body = Problem.builder()
                    .timestamp(LocalDateTime.now())
                    .title((String) body)
                    .status(statusCode.value())
                    .userMessage(MSG_ERRO_GENERICO_USUARIO_FINAL)
                    .build();
        }

        return super.handleExceptionInternal(ex, body, headers, statusCode, request);
    }


    private String joinPath(List<JsonMappingException.Reference> references) {
        return references.stream()
                .map(JsonMappingException.Reference::getFieldName)
                .collect(Collectors.joining("."));
    }

    private Problem.ProblemBuilder createProblemBuilder(
            HttpStatusCode statusCode, ProblemType problemType, String detail
    ) {
        return Problem.builder()
                .timestamp(LocalDateTime.now())
                .status(statusCode.value())
                .type(problemType.getUri())
                .title(problemType.getTitle())
                .detail(detail);
    }
}