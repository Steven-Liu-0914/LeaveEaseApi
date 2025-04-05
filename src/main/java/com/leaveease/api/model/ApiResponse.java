package com.leaveease.api.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.leaveease.api.util.CamelCaseMapSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse<T> {

    @JsonSerialize(using = CamelCaseMapSerializer.class)
    private T data;

    public static <T> ApiResponse<T> of(T data) {
        return new ApiResponse<>(data);
    }


}
