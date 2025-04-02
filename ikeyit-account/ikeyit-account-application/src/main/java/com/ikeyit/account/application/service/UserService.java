package com.ikeyit.account.application.service;


import com.ikeyit.account.application.model.*;
import com.ikeyit.account.domain.model.User;
import com.ikeyit.account.domain.repository.UserRepository;
import com.ikeyit.common.data.IdUtils;
import com.ikeyit.common.data.PrivacyUtils;
import com.ikeyit.common.exception.BizAssert;
import com.ikeyit.common.exception.CommonErrorCode;
import com.ikeyit.common.storage.ObjectStorageService;
import com.ikeyit.security.code.core.VerificationCodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Manage user account!
 */
@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final ObjectStorageService objectStorageService;

    private final PasswordEncoder passwordEncoder;

    private final PasswordValidator passwordValidator;

    private final VerificationCodeService signupEmailVerificationCodeService;

    private final VerificationCodeService signupMobileVerificationCodeService;

    private final VerificationCodeService updateEmailVerificationCodeService;

    private final VerificationCodeService updateMobileVerificationCodeService;

    public UserService(UserRepository userRepository,
                       ObjectStorageService objectStorageService,
                       PasswordEncoder passwordEncoder,
                       PasswordValidator passwordValidator,
                       @Qualifier("signupEmailVerificationCodeService")
                       VerificationCodeService signupEmailVerificationCodeService,
                       @Qualifier("signupMobileVerificationCodeService")
                       VerificationCodeService signupMobileVerificationCodeService,
                       @Qualifier("updateEmailVerificationCodeService")
                       VerificationCodeService updateEmailVerificationCodeService,
                       @Qualifier("updateMobileVerificationCodeService")
                       VerificationCodeService updateMobileVerificationCodeService) {
        this.userRepository = userRepository;
        this.objectStorageService = objectStorageService;
        this.passwordEncoder = passwordEncoder;
        this.passwordValidator = passwordValidator;
        this.signupEmailVerificationCodeService = signupEmailVerificationCodeService;
        this.signupMobileVerificationCodeService = signupMobileVerificationCodeService;
        this.updateEmailVerificationCodeService = updateEmailVerificationCodeService;
        this.updateMobileVerificationCodeService = updateMobileVerificationCodeService;
    }

    public UserDTO getUser(Long userId) {
        User user = getExistingUser(userId);
        return buildUserDTO(user);
    }

    /**
     * verify the email or mobile for signup
     */
    public VerifySignupResultDTO verifySignup(String username) {
        BizAssert.notNull(username, "username must not be null");
        if (username.contains("@")) {
            BizAssert.isTrue(userRepository.findByEmail(username).isEmpty(), "email already exists");
            signupEmailVerificationCodeService.sendCode(username);
            return new VerifySignupResultDTO("A code has been sent to your email");
        } else {
            BizAssert.isTrue(userRepository.findByMobile(username).isEmpty(), "mobile already exists");
            signupMobileVerificationCodeService.sendCode(username);
            return new VerifySignupResultDTO("A code has been sent to your mobile");
        }
    }

    @Transactional(transactionManager = "accountTransactionManager")
    public UserDTO signup(SignupCMD signupCMD) {
        BizAssert.notNull(signupCMD, "signupCMD must not be null");
        BizAssert.isTrue(signupCMD.getEmail() != null || signupCMD.getMobile() != null,
                "Please specify either email or mobile");
        var userBuilder = User.newBuilder()
                .username(generateUsername())
                .locale(getCurrentLocale())
                .displayName(signupCMD.getDisplayName());
        // validate the password strength
        passwordValidator.validate(signupCMD.getPassword());
        userBuilder.password(passwordEncoder.encode(signupCMD.getPassword()));
        if (signupCMD.getEmail() != null) {
            // validate the email
            signupEmailVerificationCodeService.validate(signupCMD.getEmail(), signupCMD.getCode());
            userRepository.findByEmail(signupCMD.getEmail())
                    .ifPresent(BizAssert.failAction(CommonErrorCode.OCCUPIED, "The email is occupied!"));
            userBuilder.email(signupCMD.getEmail());
        } else {
            // validate the mobile
            signupMobileVerificationCodeService.validate(signupCMD.getMobile(), signupCMD.getCode());
            userRepository.findByMobile(signupCMD.getMobile())
                    .ifPresent(BizAssert.failAction(CommonErrorCode.OCCUPIED, "The mobile is occupied!"));
            userBuilder.mobile(signupCMD.getMobile());
        }
        User user = userBuilder.build();
        userRepository.create(user);
        return buildUserDTO(user);
    }

    @Transactional(transactionManager = "accountTransactionManager")
    public UserDTO signupWithMobileInstantly(String mobile) {
        BizAssert.notNull(mobile, "mobile must not be null");
        userRepository.findByMobile(mobile)
                .ifPresent(BizAssert.failAction(CommonErrorCode.OCCUPIED, "The mobile is occupied!"));
        User user = User.newBuilder()
                .username(generateUsername())
                .locale(getCurrentLocale())
                .mobile(mobile)
                .build();
        userRepository.create(user);
        return buildUserDTO(user);
    }

    @Transactional(transactionManager = "accountTransactionManager")
    public UserDTO signupWithEmailInstantly(String email) {
        BizAssert.notNull(email, "email must not be null");
        userRepository.findByEmail(email)
                .ifPresent(BizAssert.failAction(CommonErrorCode.OCCUPIED, "The email is occupied!"));
        User user = User.newBuilder()
                .username(generateUsername())
                .locale(getCurrentLocale())
                .email(email)
                .build();
        userRepository.create(user);
        return buildUserDTO(user);
    }

    @Transactional(transactionManager = "accountTransactionManager")
    public UserDTO updateUserProfile(UpdateUserProfileCMD updateUserProfileCMD) {
        BizAssert.notNull(updateUserProfileCMD, "updateUserProfileCMD must not be null");
        User user = getExistingUser(updateUserProfileCMD.getUserId());
        user.updateProfile(
                updateUserProfileCMD.getAvatar(),
                updateUserProfileCMD.getDisplayName(),
                updateUserProfileCMD.getGender(),
                updateUserProfileCMD.getLocation());
        userRepository.update(user);
        return buildUserDTO(user);
    }

    @Transactional(transactionManager = "accountTransactionManager")
    public UserDTO updateUserLocale(UpdateUserLocaleCMD updateUserLocaleCMD) {
        BizAssert.notNull(updateUserLocaleCMD, "updateUserLocaleCMD must not be null");
        User user = getExistingUser(updateUserLocaleCMD.getUserId());
        user.updateLocale(updateUserLocaleCMD.getLocale());
        userRepository.update(user);
        return buildUserDTO(user);
    }

    @Transactional(transactionManager = "accountTransactionManager")
    public void updatePassword(UpdateUserPasswordCMD updateUserPasswordCMD) {
        BizAssert.notNull(updateUserPasswordCMD, "updateUserPasswordCMD must not be null");
        User user = getExistingUser(updateUserPasswordCMD.getUserId());
        BizAssert.isTrue(passwordEncoder.matches(updateUserPasswordCMD.getPassword(), user.getPassword()),
                "The password is wrong");
        BizAssert.isFalse(passwordEncoder.matches(updateUserPasswordCMD.getNewPassword(), user.getPassword()),
                "The new password should not be same with the previous one");
        passwordValidator.validate(updateUserPasswordCMD.getNewPassword());
        user.updatePassword(passwordEncoder.encode(updateUserPasswordCMD.getNewPassword()));
        userRepository.update(user);
    }

    @Transactional(transactionManager = "accountTransactionManager")
    public UserDTO updateUserMobile(UpdateUserMobileCMD updateUserMobileCMD) {
        BizAssert.notNull(updateUserMobileCMD, "updateUserMobileCMD must not be null");
        String code = updateUserMobileCMD.getCode();
        String mobile = updateUserMobileCMD.getMobile();
        updateMobileVerificationCodeService.validate(mobile, code);
        User user = getExistingUser(updateUserMobileCMD.getUserId());
        BizAssert.notEquals(user.getMobile(), mobile, "The new mobile number is the same with the previous one");
        userRepository.findByMobile(mobile)
                .ifPresent(BizAssert.failAction(CommonErrorCode.OCCUPIED, "The mobile is occupied!"));
        user.updateMobile(mobile);
        userRepository.update(user);
        return buildUserDTO(user);
    }

    @Transactional(transactionManager = "accountTransactionManager")
    public UserDTO updateUserEmail(UpdateUserEmailCMD updateUserEmailCMD) {
        BizAssert.notNull(updateUserEmailCMD, "updateUserEmailCMD must not be null");
        String email = updateUserEmailCMD.getEmail();
        String code = updateUserEmailCMD.getCode();
        updateEmailVerificationCodeService.validate(email, code);
        User user = getExistingUser(updateUserEmailCMD.getUserId());
        BizAssert.notEquals(user.getEmail(), email, "The new email number is the same with the previous one");
        userRepository.findByEmail(email)
                .ifPresent(BizAssert.failAction(CommonErrorCode.OCCUPIED, "The email address is occupied!"));
        user.updateEmail(email);
        userRepository.update(user);
        return buildUserDTO(user);
    }

    private User getExistingUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(BizAssert.failSupplier(CommonErrorCode.INVALID_ARGUMENT, "Not found"));
    }

    private UserDTO buildUserDTO(User user) {
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
        userDTO.setMobile(PrivacyUtils.hidePrivacy(user.getMobile(), 4));
        userDTO.setEmail(PrivacyUtils.hideEmail(user.getEmail(), 4));
        return userDTO;
    }

    private String getCurrentLocale() {
        return LocaleContextHolder.getLocale().toString();
    }

    private String generateUsername() {
        return IdUtils.uuid();
    }
}
