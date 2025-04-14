package com.ikeyit.foo.interfaces.api.controller;

import com.ikeyit.foo.application.model.CreateFooCMD;
import com.ikeyit.foo.application.model.FooDTO;
import com.ikeyit.foo.application.service.FooService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <pre>
 * === AI-NOTE ===
 * Name all controllers with a project name prefix as "FooXxxController" to avoid bean name confliction in the all-in-one package.
 * === AI-NOTE-END ===
 * </pre>
 */
@RestController
public class FooGeneralController {
    private final FooService fooService;

    public FooGeneralController(FooService fooService) {
        this.fooService = fooService;
    }

    @GetMapping("/foos")
    public List<FooDTO> getFoos() {
        return fooService.findAll();
    }

    @PostMapping
    public FooDTO createFoo(@RequestBody CreateFooCMD createFooCMD) {
        return fooService.create(createFooCMD);
    }
}
