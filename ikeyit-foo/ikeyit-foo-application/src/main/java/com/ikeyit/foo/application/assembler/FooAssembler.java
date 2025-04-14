package com.ikeyit.foo.application.assembler;

import com.ikeyit.foo.application.model.FooDTO;
import com.ikeyit.foo.domain.model.Foo;
import org.springframework.stereotype.Component;

/**
 * <pre>
 * === AI-NOTE ===
 * - Name a DTO converter as FooAssembler
 * - Do not use static methods to implement the functionalities. Use an object instance as a bean.
 * === AI-NOTE-END ===
 * </pre>
 * Convert Foo domain objects to DTOs
 */
@Component
public class FooAssembler {
    public FooDTO toFooDTO(Foo foo) {
        FooDTO fooDTO = new FooDTO();
        fooDTO.setId(foo.getId());
        fooDTO.setMessage(foo.getMessage());
        return fooDTO;
    };
}
