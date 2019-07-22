package ru.stonlex.api.java.interfaces;

public interface ResponseHandler<R, O, T extends Throwable> {

    R handleResponse(O o) throws T;
}
