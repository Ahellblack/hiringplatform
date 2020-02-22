package com.siti.common;


public class ReturnResult {
    private int result;
    private String message;
    private Object data;

    public ReturnResult(int result, String message, Object object) {
        this.result = result;
        this.message = message;
        this.data = object;
    }

    public ReturnResult(int result, String message) {
        this.result = result;
        this.message = message;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object object) {
        this.data = object;
    }
}
