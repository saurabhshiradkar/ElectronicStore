package com.developedbysaurabh.electronic.store.exceptions;

import com.developedbysaurabh.electronic.store.dtos.ApiResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    //resource not found exception handler
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseMessage> resourceNotFoundExceptionHandler(ResourceNotFoundException ex)
    {
        logger.info("Global Exception Handler : ResourceNotFoundException Handler Invoked");
        ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
                .message(ex.getMessage())
                .status(HttpStatus.NOT_FOUND)
                .success(true)
                .build();

        return new ResponseEntity<>(apiResponseMessage,HttpStatus.NOT_FOUND);
    }


    //MethodArgumentNotValidException

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){

        List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
        Map<String,Object> response = new HashMap<>();

        allErrors.stream().forEach(objectError -> {
            String message = objectError.getDefaultMessage();
            String field = ((FieldError) objectError).getField();
            response.put(field,message);
        });

        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(BadApiRequestException.class)
    public ResponseEntity<ApiResponseMessage> handleBadApiRequestException(BadApiRequestException ex)
    {
        logger.info("Global Exception Handler : BadApiRequestException Handler Invoked");
        ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
                .message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .success(false)
                .build();

        return new ResponseEntity<>(apiResponseMessage,HttpStatus.BAD_REQUEST);
    }



}
