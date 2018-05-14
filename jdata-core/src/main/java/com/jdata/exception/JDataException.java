package com.jdata.exception;

import java.io.Serializable;

public class JDataException extends RuntimeException implements Serializable{
    private static final long serialVersionUID = 8415613189871699677L;

    public JDataException(String message) {
        super(message);
    }
}
