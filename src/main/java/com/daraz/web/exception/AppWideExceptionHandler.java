package com.daraz.web.exception;

import com.daraz.web.exception.custom.DuplicateEntryException;
import com.daraz.web.exception.custom.EntryNotFoundException;
import com.daraz.web.util.StandardResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author : yashen
 * @created : 4/13/26
 * @project : web
 * @email : yashensavindu@gmail.com
 * @since : 0.1.0
 **/
@CrossOrigin
@RestControllerAdvice
public class AppWideExceptionHandler {

    @ExceptionHandler(EntryNotFoundException.class)
    public ResponseEntity<StandardResponse>  handleNotFound(EntryNotFoundException e){
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(
                        404,
                        e.getMessage(),
                        null
                ),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(DuplicateEntryException.class)
    public ResponseEntity<StandardResponse> handleDuplicates(DuplicateEntryException e){
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(
                        409,
                        e.getMessage(),
                        null
                ),
                HttpStatus.CONFLICT
        );
    }
}