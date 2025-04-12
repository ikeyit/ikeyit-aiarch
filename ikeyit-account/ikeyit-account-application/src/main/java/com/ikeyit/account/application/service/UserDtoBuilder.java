package com.ikeyit.account.application.service;

import com.ikeyit.account.application.model.UserAuthDTO;
import com.ikeyit.account.application.model.UserDTO;
import com.ikeyit.account.domain.model.User;
import com.ikeyit.common.storage.ObjectStorageService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UserDtoBuilder {
    private final ObjectStorageService objectStorageService;

    public UserDtoBuilder(ObjectStorageService objectStorageService) {
        this.objectStorageService = objectStorageService;
    }

    public UserDTO buildUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setLocale(user.getLocale());
        userDTO.setLocation(userDTO.getLocation());
        userDTO.setEnabled(user.isEnabled());
        userDTO.setVerified(user.isVerified());
        userDTO.setGender(user.getGender());
        userDTO.setDisplayName(user.getDisplayName());
        userDTO.setAvatar(objectStorageService.getCdnUrl(user.getAvatar()));
        userDTO.setPhone(user.getPhone());
        userDTO.setEmail(user.getEmail());
        userDTO.setHasPassword(StringUtils.hasLength(user.getPassword()));
        return userDTO;
    }

    public UserAuthDTO buildUserAuthDTO(User user) {
        UserAuthDTO userAuthDTO = new UserAuthDTO();
        userAuthDTO.setId(user.getId());
        userAuthDTO.setUsername(user.getUsername());
        userAuthDTO.setLocale(user.getLocale());
        userAuthDTO.setDisplayName(user.getDisplayName());
        userAuthDTO.setAvatar(objectStorageService.getCdnUrl(user.getAvatar()));
        userAuthDTO.setPhone(user.getPhone());
        userAuthDTO.setEmail(user.getEmail());
        userAuthDTO.setPassword(user.getPassword());
        userAuthDTO.setRoles(user.getRoles());
        userAuthDTO.setEnabled(user.isEnabled());
        userAuthDTO.setVerified(user.isVerified());
        return userAuthDTO;
    }
}
