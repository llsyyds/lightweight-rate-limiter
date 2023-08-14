package io.llsfish.ratelimiter.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Redick01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response<T> implements Serializable {

    private String code;

    private String msg;

    private T data;
}
