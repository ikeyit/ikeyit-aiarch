package com.ikeyit.foo.application.service;

import com.ikeyit.common.data.Page;
import com.ikeyit.foo.application.assembler.FooAssembler;
import com.ikeyit.foo.application.model.CreateFooCMD;
import com.ikeyit.foo.application.model.FooDTO;
import com.ikeyit.foo.application.model.FooQRY;
import com.ikeyit.foo.domain.model.Foo;
import com.ikeyit.foo.domain.model.FooSource;
import com.ikeyit.foo.domain.repository.FooRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <pre>
 * === AI-NOTE ===
 * - Name the application layer service as FooService without the word "Application".
 * === AI-NOTE-END ===
 * </pre>
 * Foo application service orchestrates domain objects and other services to perform business cases
 */
@Service
public class FooService {

    private final FooRepository fooRepository;

    private final FooAssembler fooAssembler;

    public FooService(FooRepository fooRepository, FooAssembler fooAssembler) {
        this.fooRepository = fooRepository;
        this.fooAssembler = fooAssembler;
    }

    // AI-NOTE: Ensure the code uses the foo dedicated transactionManager
    @Transactional(transactionManager = "fooTransactionManager")
    public FooDTO create(CreateFooCMD createFooCMD) {
        Foo foo = new Foo(createFooCMD.getMessage(), new FooSource("device1", "port1"));
        fooRepository.create(foo);
        return fooAssembler.toFooDTO(foo);
    }

    public List<FooDTO> findAll() {
        return fooRepository.findAll().stream().map(fooAssembler::toFooDTO).toList();
    }

    /**
     * <pre>
     * === AI-NOTE ===
     * - If pagination is required, return the result with the container class com.ikeyit.common.data.Page
     * === AI-NOTE-END ===
     * @param fooQRY
     * @return
     */
    public Page<FooDTO> findAll(FooQRY fooQRY) {
        return Page.emptyPage();
    }

}
