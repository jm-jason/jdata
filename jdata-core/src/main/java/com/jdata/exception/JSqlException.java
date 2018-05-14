package com.jdata.exception;

import java.io.Serializable;

public class JSqlException extends RuntimeException implements Serializable{

    private static final long serialVersionUID = -4731087826856458367L;

    public JSqlException(String s) {
        super(s);
    }
}
