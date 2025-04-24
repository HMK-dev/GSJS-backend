package com.gsjs.gsjs.exception.payload.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.gsjs.gsjs.exception.payload.code.BaseCode;
import com.gsjs.gsjs.exception.payload.code.SuccessStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonPropertyOrder
public class ApiResponseDto<T> {

    private final Boolean isSuccess;
    private final Integer code;
    private final String message;
    private T result;

    public static <T> ApiResponseDto onSuccess(T result){
        return new ApiResponseDto(true, SuccessStatus._SUCCESS.getCode(), SuccessStatus._SUCCESS.getMessage(), result);
    }

    public static <T> ApiResponseDto<T> of(BaseCode code, T result){
        return new ApiResponseDto<>(true, code.getReasonHttpStatus().getCode(), code.getReason().getMessage(), result);
    }

    public static <T> ApiResponseDto<T> onFailure(Integer code, String message, T data){
        return new ApiResponseDto<>(false, code, message, data);
    }

}