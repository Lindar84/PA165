package cz.fi.muni.pa165.rest.controllers;

import cz.fi.muni.pa165.rest.ApiError;
import cz.fi.muni.pa165.rest.exceptions.ResourceAlreadyExistingException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * TODO: create  javadoc
 *
 * @author Ludmila Fialova
 */
@ControllerAdvice
public class GlobalExceptionController {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    private ApiError handleException(ResourceAlreadyExistingException ex){
        ApiError apiError = new ApiError();
        apiError.setErrors(Arrays.asList("the requested resource already exists"));
        return apiError;
    }
}
