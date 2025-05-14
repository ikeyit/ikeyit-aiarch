package com.ikeyit.access.application.service;

import com.ikeyit.access.application.model.OrgDTO;
import com.ikeyit.access.application.model.UpdateOrgCMD;
import com.ikeyit.access.domain.model.Org;
import com.ikeyit.access.domain.repository.OrgRepository;
import com.ikeyit.common.exception.BizAssert;
import com.ikeyit.common.exception.CommonErrorCode;
import com.ikeyit.common.storage.ObjectStorageService;
import org.springframework.stereotype.Service;

@Service
public class OrgService {
    private final OrgRepository orgRepository;
    private final ObjectStorageService objectStorageService;

    public OrgService(OrgRepository orgRepository, ObjectStorageService objectStorageService) {
        this.orgRepository = orgRepository;
        this.objectStorageService = objectStorageService;
    }

    public OrgDTO getOrg() {
        Org org = orgRepository.find().orElseThrow(BizAssert.failSupplier(CommonErrorCode.NOT_FOUND, "No org found"));
        return toOrgDTO(org);
    }

    public OrgDTO updateOrg(UpdateOrgCMD updateOrgCMD) {
        Org org = orgRepository.find().orElseThrow(BizAssert.failSupplier(CommonErrorCode.NOT_FOUND, "No org found"));
        org.update(updateOrgCMD.getName(), objectStorageService.getObjectKey(updateOrgCMD.getPicture()));
        orgRepository.update(org);
        return toOrgDTO(org);
    }

    private OrgDTO toOrgDTO(Org org) {
        OrgDTO orgDTO = new OrgDTO();
        orgDTO.setName(org.getName());
        orgDTO.setPicture(objectStorageService.getCdnUrl(org.getPicture()));
        orgDTO.setCreatedAt(org.getCreatedAt());
        orgDTO.setUpdatedAt(org.getUpdatedAt());
        return orgDTO;
    }
}
