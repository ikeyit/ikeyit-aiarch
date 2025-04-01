package com.ikeyit.account.interfaces.api.controller;

import com.ikeyit.common.data.IdUtils;
import com.ikeyit.common.storage.ObjectStorageService;
import com.ikeyit.common.storage.PresignResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UploadController {


    private final ObjectStorageService objectStorageService;

    public UploadController(ObjectStorageService objectStorageService) {
        this.objectStorageService = objectStorageService;
    }


    @PostMapping("/api/presign-upload")
    public PresignResult presignUpload() {
        return objectStorageService.presign("misc/" + IdUtils.uuid());
    }
}