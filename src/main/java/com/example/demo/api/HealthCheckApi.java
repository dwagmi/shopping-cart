package com.example.demo.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckApi {
    @RequestMapping(method = RequestMethod.GET, value="/health")
    public HttpStatus checkHealth() {
        return HttpStatus.OK;
    }
}
