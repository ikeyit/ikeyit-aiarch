package com.ikeyit.foo.interfaces.api.controller;

import com.ikeyit.foo.application.model.CreateFooCMD;
import com.ikeyit.foo.application.model.FooDTO;
import com.ikeyit.foo.application.service.FooService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FooController {
    private final FooService fooService;

    public FooController(FooService fooService) {
        this.fooService = fooService;
    }

    @RequestMapping("/foos")
    public List<FooDTO> getFoos() {
        return fooService.findAll();
    }

    @PostMapping("/foo")
    public FooDTO createStore(@RequestBody CreateFooCMD createFooCMD) {
        return fooService.create(createFooCMD);
    }
}
