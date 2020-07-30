package com.example.orderorder.data;

/**
 * A generic class that holds a result success w/ data or an error exception.
 */
public class Result<T> {
    private T data = null;
    private Exception e = null;

    // hide the private constructor to limit subclass types (Success, Error)
    Result(Exception e) {
        this.e = e;
    }
    Result(T data) {
        this.data = data;
    }
    Result() {
    }

    public Exception getError() {
        return this.e;
    }
    public void setError(Exception e) {
        this.e = e;
    }

    public T getData() {
        return this.data;
    }
    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        if (this.e == null) {
            //Result success = this;
            return "Success[data=" + this.getData().toString() + "]";
        } else if (this.e != null) {
            //Result.Error error = (Result.Error) this;
            return "Error[exception=" + this.getError().toString() + "]";
        }
        return "";
    }
/*
    // Success sub-class
    public final static class Success<T> extends Result {
        private T data;

        public Success(T data) {
            this.data = data;
        }

        public T getData() {
            return this.data;
        }
    }

    // Error sub-class
    public final static class Error extends Result {
        private Exception error;

        public Error(Exception error) {
            this.error = error;
        }

        public Exception getError() {
            return this.error;
        }
    }


    @Override
    public String toString() {
        if (this instanceof Result.Success) {
            Result.Success success = (Result.Success) this;
            return "Success[data=" + success.getData().toString() + "]";
        } else if (this instanceof Result.Error) {
            Result.Error error = (Result.Error) this;
            return "Error[exception=" + error.getError().toString() + "]";
        }
        return "";
    }

 */
}