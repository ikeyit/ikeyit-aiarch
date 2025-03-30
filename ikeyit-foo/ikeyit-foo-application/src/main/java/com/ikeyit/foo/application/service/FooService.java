package com.ikeyit.foo.application.service;

import com.ikeyit.foo.application.model.CreateFooCMD;
import com.ikeyit.foo.application.model.FooDTO;
import com.ikeyit.foo.domain.model.Foo;
import com.ikeyit.foo.domain.repository.FooRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FooService {

    private final FooRepository fooRepository;

    public FooService(FooRepository fooRepository) {
        this.fooRepository = fooRepository;
    }

    public FooDTO create(CreateFooCMD createFooCMD) {
        Foo foo = new Foo(createFooCMD.getMessage());
        fooRepository.create(foo);
        return buildFooDTO(foo);
    }

    public List<FooDTO> findAll() {
        return fooRepository.findAll().stream().map(this::buildFooDTO).toList();
    }

    private FooDTO buildFooDTO(Foo foo) {
        FooDTO fooDTO = new FooDTO();
        fooDTO.setId(foo.getId());
        fooDTO.setMessage(foo.getMessage());
        return fooDTO;
    }
}
