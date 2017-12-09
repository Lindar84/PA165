package cz.fi.muni.pa165.rest;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: create  javadoc
 *
 * @author Ludmila Fialova
 */
public class ApiError {

    private List<String> errors;

    public ApiError() {
    }

    public ApiError(List<String> errors) {
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
