package com.example.orderorder.data;

public interface RepositoryCallback<T> {
    void onComplete(Result<T> result);
}
