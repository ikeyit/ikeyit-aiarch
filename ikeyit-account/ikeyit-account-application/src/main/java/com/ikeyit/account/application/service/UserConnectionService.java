package com.ikeyit.account.application.service;

import com.ikeyit.account.application.assembler.UserAssembler;
import com.ikeyit.account.application.model.ConnectUserCMD;
import com.ikeyit.account.application.model.UserAuthDTO;
import com.ikeyit.account.domain.model.User;
import com.ikeyit.account.domain.model.UserConnection;
import com.ikeyit.account.domain.repository.UserConnectionRepository;
import com.ikeyit.account.domain.repository.UserRepository;
import com.ikeyit.common.data.IdUtils;
import com.ikeyit.common.exception.BizAssert;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class UserConnectionService {

    private final UserRepository userRepository;

    private final UserConnectionRepository userConnectionRepository;

    private final UserAssembler userAssembler;

    public UserConnectionService(UserRepository userRepository,
                                 UserConnectionRepository userConnectionRepository,
                                 UserAssembler userAssembler) {
        this.userRepository = userRepository;
        this.userConnectionRepository = userConnectionRepository;
        this.userAssembler = userAssembler;
    }

    public User loadUserByConnection(String providerId, String providerUserId) {
       return userConnectionRepository.findByProviderSub(providerId, providerUserId)
           .flatMap(userConnection -> userRepository.findById(userConnection.getLocalUserId()))
           .orElseThrow(BizAssert.failSupplier("User is not found!"));
    }

    @Transactional(transactionManager = "accountTransactionManager")
    public UserAuthDTO loadOrCreateUserByConnection(ConnectUserCMD connectUserCMD) {
        BizAssert.notNull(connectUserCMD, "connect user command is null!");
        BizAssert.notNull(connectUserCMD.getProvider(), "provider is null!");
        BizAssert.notNull(connectUserCMD.getSub(), "sub is null!");
        var userConnectionOpt = userConnectionRepository
            .findByProviderSub(connectUserCMD.getProvider(), connectUserCMD.getSub());
        if (userConnectionOpt.isPresent()) {
            return userRepository.findById(userConnectionOpt.get().getId())
                .map(userAssembler::toUserAuthDTO)
                .orElseThrow(BizAssert.failSupplier("User is not found!"));
        }

        // Create user
        var userBuilder = User.newBuilder()
            .username(IdUtils.uuid())
            .locale(getCurrentLocale());
        var displayName = connectUserCMD.getPreferredUsername();
        if (!StringUtils.hasLength(displayName)) {
            displayName = connectUserCMD.getPreferredUsername();
        }
        if (!StringUtils.hasLength(displayName)) {
            displayName = connectUserCMD.getNickname();
        }
        if (!StringUtils.hasLength(displayName)) {
            displayName = connectUserCMD.getName();
        } else {
            displayName = IdUtils.uuid();
        }
        userBuilder.displayName(displayName);
        if (StringUtils.hasLength(connectUserCMD.getPicture())) {
            userBuilder.avatar(connectUserCMD.getPicture());
        }
        User user = userBuilder.build();
        userRepository.create(user);
        var userConnectionBuilder = UserConnection.newBuilder()
            .localUserId(user.getId())
            .provider(connectUserCMD.getProvider())
            .sub(connectUserCMD.getSub());
        if (StringUtils.hasLength(connectUserCMD.getName())) {
            userConnectionBuilder.name(connectUserCMD.getName());
        }
        if (StringUtils.hasLength(connectUserCMD.getNickname())) {
            userConnectionBuilder.nickname(connectUserCMD.getNickname());
        }
        if (StringUtils.hasLength(connectUserCMD.getPhoneNumber())) {
            userConnectionBuilder.phoneNumber(connectUserCMD.getPhoneNumber());
        }
        if (StringUtils.hasLength(connectUserCMD.getPreferredUsername())) {
            userConnectionBuilder.preferredUsername(connectUserCMD.getPreferredUsername());
        }
        if (StringUtils.hasLength(connectUserCMD.getPicture())) {
            userConnectionBuilder.picture(connectUserCMD.getPicture());
        }
        userConnectionRepository.create(userConnectionBuilder.build());
        return userAssembler.toUserAuthDTO(user);
    }

    private String getCurrentLocale() {
        return LocaleContextHolder.getLocale().toLanguageTag();
    }
}
