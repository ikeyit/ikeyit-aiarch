package com.ikeyit.account.application.service;


import com.ikeyit.account.application.model.*;
import com.ikeyit.account.domain.model.User;
import com.ikeyit.account.domain.repository.UserRepository;
import com.ikeyit.common.data.IdUtils;
import com.ikeyit.common.exception.BizAssert;
import com.ikeyit.common.exception.BizException;
import com.ikeyit.common.exception.CommonErrorCode;
import com.ikeyit.security.code.core.VerificationCodeException;
import com.ikeyit.security.code.core.VerificationCodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * Manage user account!
 */
@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final PasswordValidator passwordValidator;
    
    private final ContactInfoValidator contactInfoValidator;

    private final VerificationCodeService signupEmailVerificationCodeService;

    private final VerificationCodeService signupPhoneVerificationCodeService;

    private final VerificationCodeService updateEmailVerificationCodeService;

    private final VerificationCodeService updatePhoneVerificationCodeService;

    private final UserDtoBuilder userDtoBuilder;

    public UserService(UserRepository userRepository,
                       UserDtoBuilder userDtoBuilder,
                       PasswordEncoder passwordEncoder,
                       PasswordValidator passwordValidator,
                       ContactInfoValidator contactInfoValidator,
                       @Qualifier("signupEmailVerificationCodeService")
                       VerificationCodeService signupEmailVerificationCodeService,
                       @Qualifier("signupPhoneVerificationCodeService")
                       VerificationCodeService signupPhoneVerificationCodeService,
                       @Qualifier("updateEmailVerificationCodeService")
                       VerificationCodeService updateEmailVerificationCodeService,
                       @Qualifier("updatePhoneVerificationCodeService")
                       VerificationCodeService updatePhoneVerificationCodeService) {
        this.userRepository = userRepository;
        this.userDtoBuilder = userDtoBuilder;
        this.passwordEncoder = passwordEncoder;
        this.passwordValidator = passwordValidator;
        this.contactInfoValidator = contactInfoValidator;
        this.signupEmailVerificationCodeService = signupEmailVerificationCodeService;
        this.signupPhoneVerificationCodeService = signupPhoneVerificationCodeService;
        this.updateEmailVerificationCodeService = updateEmailVerificationCodeService;
        this.updatePhoneVerificationCodeService = updatePhoneVerificationCodeService;
    }

    public UserDTO getUser(Long userId) {
        User user = getExistingUser(userId);
        return userDtoBuilder.buildUserDTO(user);
    }

    public UserAuthDTO loadUserAuth(Long userId) {
        BizAssert.notNull(userId, "userId must not be null");
        return userRepository.findById(userId)
            .map(userDtoBuilder::buildUserAuthDTO)
            .orElseThrow(BizAssert.failSupplier("User is not found!"));
    }

    public UserAuthDTO loadUserAuth(String username) {
        BizAssert.hasLength(username, "username is required");
        // if username is an email address
        if (username.contains("@")) {
            return userRepository.findByEmail(username)
                .map(userDtoBuilder::buildUserAuthDTO)
                .orElseThrow(BizAssert.failSupplier("User is not found!"));
        }
        // If username is a phone number
        if (username.startsWith("+")) {
            return userRepository.findByPhone(username)
                .map(userDtoBuilder::buildUserAuthDTO)
                .orElseThrow(BizAssert.failSupplier("User is not found!"));
        }
        return userRepository.findByUsername(username)
            .map(userDtoBuilder::buildUserAuthDTO)
            .orElseThrow(BizAssert.failSupplier("User is not found!"));
    }

    /**
     * verify the email or phone for signup
     */
    public VerifySignupResultDTO verifySignup(VerifySignupCMD verifySignupCMD) {
        BizAssert.notNull(verifySignupCMD, "verifySignupCMD must not be null");
        String username = verifySignupCMD.getUsername();
        BizAssert.hasLength(username, "username is required");
        if (username.contains("@")) {
            contactInfoValidator.validateEmail(username);
            BizAssert.isTrue(userRepository.findByEmail(username).isEmpty(), "email already exists");
            signupEmailVerificationCodeService.sendCode(username);
            return new VerifySignupResultDTO("A code has been sent to your email");
        } else if (username.startsWith("+")) {
            contactInfoValidator.validatePhone(username);
            BizAssert.isTrue(userRepository.findByPhone(username).isEmpty(), "phone already exists");
            signupPhoneVerificationCodeService.sendCode(username);
            return new VerifySignupResultDTO("A code has been sent to your phone");
        } else {
            throw new BizException(CommonErrorCode.INVALID_ARGUMENT, "username must be an email address or a phone number");
        }
    }

    @Transactional(transactionManager = "accountTransactionManager")
    public UserAuthDTO signup(SignupCMD signupCMD) {
        BizAssert.notNull(signupCMD, "signupCMD must not be null");
        String username = signupCMD.getUsername();
        BizAssert.hasLength(username, "username must not be empty");
        String displayName = signupCMD.getDisplayName();
        String password = signupCMD.getPassword();
        String code = signupCMD.getCode();
        var userBuilder = User.newBuilder()
                .username(generateUsername()) // This username is a general username which is not enabled now
                .locale(getCurrentLocale())
                .displayName(displayName);
        // validate the password strength
        passwordValidator.validate(password);
        userBuilder.password(passwordEncoder.encode(password));
        if (username.contains("@")) {
            // validate the email
            contactInfoValidator.validateEmail(username);
            userRepository.findByEmail(username)
                    .ifPresent(BizAssert.failAction(CommonErrorCode.OCCUPIED, "The email is occupied!"));
            signupEmailVerificationCodeService.validate(username, code);
            userBuilder.email(username);
        } else if (username.startsWith("+")) {
            // validate the phone
            contactInfoValidator.validatePhone(username);
            userRepository.findByPhone(username)
                    .ifPresent(BizAssert.failAction(CommonErrorCode.OCCUPIED, "The phone is occupied!"));
            signupPhoneVerificationCodeService.validate(username, code);
            userBuilder.phone(username);
        } else {
            throw new BizException(CommonErrorCode.INVALID_ARGUMENT, "username must be an email address or a phone number");
        }
        User user = userBuilder.build();
        userRepository.create(user);
        return userDtoBuilder.buildUserAuthDTO(user);
    }

    @Transactional(transactionManager = "accountTransactionManager")
    public UserAuthDTO loadOrCreateUserByEmailOrPhone(String target) {
        if (target.contains("@")) {
            contactInfoValidator.validateEmail(target);
            return loadOrCreateUserByEmail(target);
        } else if (target.startsWith("+")) {
            contactInfoValidator.validatePhone(target);
            return loadOrCreateUserByPhone(target);
        } else {
            throw new BizException(CommonErrorCode.INVALID_ARGUMENT, "target must be an email address or a phone number");
        }
    }

    @Transactional(transactionManager = "accountTransactionManager")
    public UserAuthDTO loadOrCreateUserByPhone(String phone) {
        BizAssert.notNull(phone, "phone must not be null");
        contactInfoValidator.validatePhone(phone);
        var user = userRepository.findByPhone(phone)
            .orElseGet(() -> {
                var newUser = User.newBuilder()
                    .username(generateUsername())
                    .displayName(generateDisplayName())
                    .locale(getCurrentLocale())
                    .phone(phone)
                    .build();
                userRepository.create(newUser);
                return newUser;
            });
       return userDtoBuilder.buildUserAuthDTO(user);
    }

    @Transactional(transactionManager = "accountTransactionManager")
    public UserAuthDTO loadOrCreateUserByEmail(String email) {
        BizAssert.notNull(email, "email must not be null");
        contactInfoValidator.validateEmail(email);
        var user = userRepository.findByEmail(email)
            .orElseGet(() -> {
                var newUser = User.newBuilder()
                    .username(generateUsername())
                    .displayName(generateDisplayName())
                    .locale(getCurrentLocale())
                    .email(email)
                    .build();
                userRepository.create(newUser);
                return newUser;
            });
        return userDtoBuilder.buildUserAuthDTO(user);
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
        return userDtoBuilder.buildUserDTO(user);
    }

    @Transactional(transactionManager = "accountTransactionManager")
    public UserDTO updateUserLocale(UpdateUserLocaleCMD updateUserLocaleCMD) {
        BizAssert.notNull(updateUserLocaleCMD, "updateUserLocaleCMD must not be null");
        User user = getExistingUser(updateUserLocaleCMD.getUserId());
        user.updateLocale(updateUserLocaleCMD.getLocale());
        userRepository.update(user);
        return userDtoBuilder.buildUserDTO(user);
    }

    @Transactional(transactionManager = "accountTransactionManager")
    public void updatePassword(UpdateUserPasswordCMD updateUserPasswordCMD) {
        BizAssert.notNull(updateUserPasswordCMD, "updateUserPasswordCMD must not be null");
        User user = getExistingUser(updateUserPasswordCMD.getUserId());
        if (StringUtils.hasLength(user.getPassword())) {
            BizAssert.isTrue(passwordEncoder.matches(updateUserPasswordCMD.getPassword(), user.getPassword()),
                "The password is wrong");
            BizAssert.isFalse(passwordEncoder.matches(updateUserPasswordCMD.getNewPassword(), user.getPassword()),
                "The new password should not be same with the previous one");
        }
        passwordValidator.validate(updateUserPasswordCMD.getNewPassword());
        user.updatePassword(passwordEncoder.encode(updateUserPasswordCMD.getNewPassword()));
        userRepository.update(user);
    }

    /**
     * Send verification code
     */
    public void sendVerificationCodeForUpdatePhone(SendPhoneVerificationCodeCMD sendPhoneVerificationCodeCMD) {
        BizAssert.notNull(sendPhoneVerificationCodeCMD, "sendPhoneVerificationCMD must not be null");
        String phone = sendPhoneVerificationCodeCMD.getPhone();
        contactInfoValidator.validatePhone(phone);
        userRepository.findByPhone(phone)
            .ifPresent(BizAssert.failAction(CommonErrorCode.OCCUPIED, "The phone is occupied by the other one!"));
        try {
            updatePhoneVerificationCodeService.sendCode(phone);
        } catch (VerificationCodeException e) {
            throw new BizException(CommonErrorCode.INVALID_ARGUMENT, e.getMessage());
        }
    }

    @Transactional(transactionManager = "accountTransactionManager")
    public UserDTO updateUserPhone(UpdateUserPhoneCMD updateUserPhoneCMD) {
        BizAssert.notNull(updateUserPhoneCMD, "updateUserPhoneCMD must not be null");
        String code = updateUserPhoneCMD.getCode();
        String phone = updateUserPhoneCMD.getPhone();
        contactInfoValidator.validatePhone(phone);
        updatePhoneVerificationCodeService.validate(phone, code);
        User user = getExistingUser(updateUserPhoneCMD.getUserId());
        BizAssert.notEquals(user.getPhone(), phone, "The new phone number is the same with the previous one");
        userRepository.findByPhone(phone)
                .ifPresent(BizAssert.failAction(CommonErrorCode.OCCUPIED, "The phone is occupied!"));
        user.updatePhone(phone);
        userRepository.update(user);
        return userDtoBuilder.buildUserDTO(user);
    }

    public void sendVerificationCodeForUpdateEmail(SendEmailVerificationCodeCMD sendEmailVerificationCodeCMD) {
        BizAssert.notNull(sendEmailVerificationCodeCMD, "sendEmailVerificationCMD must not be null");
        String email = sendEmailVerificationCodeCMD.getEmail();
        contactInfoValidator.validateEmail(email);
        userRepository.findByEmail(email)
            .ifPresent(BizAssert.failAction(CommonErrorCode.OCCUPIED, "The email address is occupied by the other one!"));
        try {
            updateEmailVerificationCodeService.sendCode(email);
        } catch (VerificationCodeException e) {
            throw new BizException(CommonErrorCode.INVALID_ARGUMENT, e.getMessage());
        }
    }

    @Transactional(transactionManager = "accountTransactionManager")
    public UserDTO updateUserEmail(UpdateUserEmailCMD updateUserEmailCMD) {
        BizAssert.notNull(updateUserEmailCMD, "updateUserEmailCMD must not be null");
        String email = updateUserEmailCMD.getEmail();
        String code = updateUserEmailCMD.getCode();
        contactInfoValidator.validateEmail(email);
        updateEmailVerificationCodeService.validate(email, code);
        User user = getExistingUser(updateUserEmailCMD.getUserId());
        BizAssert.notEquals(user.getEmail(), email, "The new email number is the same with the previous one");
        userRepository.findByEmail(email)
                .ifPresent(BizAssert.failAction(CommonErrorCode.OCCUPIED, "The email address is occupied!"));
        user.updateEmail(email);
        userRepository.update(user);
        return userDtoBuilder.buildUserDTO(user);
    }

    private User getExistingUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(BizAssert.failSupplier(CommonErrorCode.INVALID_ARGUMENT, "Not found"));
    }

    private String getCurrentLocale() {
        return LocaleContextHolder.getLocale().toLanguageTag();
    }

    private String generateUsername() {
        return IdUtils.uuid();
    }

    private String generateDisplayName() {
        return IdUtils.uuid();
    }

}
