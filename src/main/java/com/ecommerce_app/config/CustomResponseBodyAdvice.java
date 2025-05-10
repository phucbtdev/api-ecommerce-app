package com.ecommerce_app.config;

import com.ecommerce_app.dto.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;


@RestControllerAdvice
@RequiredArgsConstructor
public class CustomResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    private final HttpServletRequest servletRequest;

    @Override
    public boolean supports(MethodParameter returnType,
                            Class<? extends HttpMessageConverter<?>> converterType
    ) {
        return true; // Xử lý tất cả response
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {

        String path = servletRequest.getRequestURI();

        if (path.startsWith("/api-docs") || path.startsWith("/swagger") || path.contains("api-docs")) {
            return body;
        }

        if (body instanceof ApiResponse) {
            return body;
        }

        return ApiResponse.success(body);
    }
}
