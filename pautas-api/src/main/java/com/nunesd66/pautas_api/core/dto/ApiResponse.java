package com.nunesd66.pautas_api.core.dto;

public record ApiResponse<T> (
    T data,
    String error
){}
