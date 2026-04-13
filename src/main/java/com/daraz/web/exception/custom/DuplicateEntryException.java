package com.daraz.web.exception.custom;

/**
 * @author : yashen
 * @created : 4/13/26
 * @project : web
 * @email : yashensavindu@gmail.com
 * @since : 0.1.0
 **/
public class DuplicateEntryException extends RuntimeException{
    public DuplicateEntryException(String message){
        super(message);
    }
}