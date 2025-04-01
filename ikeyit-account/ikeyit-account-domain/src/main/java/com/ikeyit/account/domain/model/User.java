package com.ikeyit.account.domain.model;

import com.ikeyit.account.domain.event.*;
import com.ikeyit.common.data.JsonField;
import com.ikeyit.common.data.domain.BaseAggregateRoot;
import com.ikeyit.common.data.domain.ForRepo;
import com.ikeyit.common.exception.BizAssert;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Set;

/**
 * User domain model representing a user in the system. It's an aggregate root.
 */
public class User extends BaseAggregateRoot<Long> {
    private Long id;
    private String username;
    private String password;
    private String mobile;
    private String email;
    private String displayName;
    private String avatar;
    private Gender gender;
    private String location;
    private String locale;
    private boolean enabled;
    private boolean verified;
    @JsonField
    private Set<String> roles;
    private Instant createdAt;
    private Instant updatedAt;

    /**
     * This constructor is used for the persistence
     * DO NOT use it in your business logic
     */
    @ForRepo
    protected User() {
    }

    private User(Builder builder) {
        username = builder.username;
        password = builder.password;
        mobile = builder.mobile;
        email = builder.email;
        displayName = builder.displayName;
        avatar = builder.avatar;
        gender = builder.gender;
        location = builder.location;
        enabled = builder.enabled;
        verified = builder.verified;
        roles = builder.roles;
        locale = builder.locale;
        createdAt = Instant.now().truncatedTo(ChronoUnit.MICROS);
        updatedAt = createdAt;
        addDomainEvent(() -> new UserCreatedEvent(this));
    }

    /**
     * Creates a new builder instance for constructing User objects
     * @return A new Builder instance
     */
    public static Builder newBuilder() {
        return new Builder();
    }

    /**
     * Gets the unique identifier of the user
     * @return The user's ID
     */
    @Override
    public Long getId() {
        return id;
    }

    /**
     * Assign an id for a new entity. It is used for the persistence
     * DO NOT use it in your business logic
     * @param id The identifier to assign
     */
    public void assignId(Long id) {
        this.id = id;
    }

    /**
     * Update the profile of the user
     * @param avatar The user's avatar URL
     * @param displayName The user's display name
     * @param gender The user's gender
     * @param location The user's location
     */
    public void updateProfile(String avatar, String displayName, Gender gender, String location) {
        boolean changed = false;
        if (avatar != null && !avatar.equals(this.avatar)) {
            this.avatar = avatar;
            changed = true;
        }
        if (displayName != null && !displayName.equals(this.displayName)) {
            BizAssert.isTrue(displayName.length() > 2, "Display name should have at least two characters");
            this.displayName = displayName;
            changed = true;
        }
        if (gender != null && gender != this.gender) {
            this.gender = gender;
            changed = true;
        }
        if (location != null && !location.equals(this.location)) {
            this.location = location;
            changed = true;
        }
        if (changed) {
            this.updatedAt = Instant.now().truncatedTo(ChronoUnit.MICROS);
            addDomainEvent(new UserProfileUpdatedEvent(this));
        }
    }

    /**
     * Updates the user's password
     * @param password The new password to set
     * @throws IllegalArgumentException if password is null
     */
    public void updatePassword(String password) {
        BizAssert.notNull(password, "Password cannot be null");
        this.password = password;
        this.updatedAt = Instant.now().truncatedTo(ChronoUnit.MICROS);
        addDomainEvent(new UserPasswordUpdatedEvent(this));
    }

    /**
     * Updates the user's mobile number
     * @param mobile The new mobile number
     * @throws IllegalArgumentException if mobile is null
     */
    public void updateMobile(String mobile) {
        BizAssert.notNull(mobile, "Mobile cannot be null");
        if (Objects.equals(mobile, this.mobile)) {
            return;
        }
        var previousMobile = this.mobile;
        this.mobile = mobile;
        this.updatedAt = Instant.now().truncatedTo(ChronoUnit.MICROS);
        addDomainEvent(new UserMobileUpdatedEvent(this, previousMobile));
    }

    /**
     * Updates the user's email address
     * @param email The new email address
     * @throws IllegalArgumentException if email is null or invalid
     */
    public void updateEmail(String email) {
        BizAssert.notNull(email, "Email cannot be null");
        BizAssert.isTrue(email.contains("@"), "Invalid email format");
        if (Objects.equals(email, this.email)) {
            return;
        }
        var previousEmail = this.email;
        this.email = email;
        this.updatedAt = Instant.now().truncatedTo(ChronoUnit.MICROS);
        addDomainEvent(new UserEmailUpdatedEvent(this, previousEmail));
    }

    /**
     * Enables the user account if it's not already enabled
     */
    public void enable() {
        if (enabled) {
            return;
        }
        this.enabled = true;
        this.updatedAt = Instant.now().truncatedTo(ChronoUnit.MICROS);
        addDomainEvent(new UserEnabledEvent(this));
    }

    /**
     * Marks the user account as verified if it's not already verified
     */
    public void markAsVerified() {
        if (verified) {
            return;
        }
        this.verified = true;
        this.updatedAt = Instant.now().truncatedTo(ChronoUnit.MICROS);
        addDomainEvent(new UserVerifiedEvent(this));
    }

    /**
     * Updates the user's locale setting
     * @param locale The new locale to set
     * @throws IllegalArgumentException if locale is null
     */
    public void updateLocale(String locale) {
        BizAssert.notNull(locale, "Locale cannot be null");
        if (Objects.equals(this.locale, locale)) {
            return;
        }
        this.locale = locale;
        this.updatedAt = Instant.now().truncatedTo(ChronoUnit.MICROS);
        addDomainEvent(new UserLocaleUpdatedEvent(this));
    }

    /**
     * Updates the user's roles
     * @param roles The new set of roles
     * @throws IllegalArgumentException if roles is null or empty
     */
    public void updateRoles(Set<String> roles) {
        BizAssert.notEmpty(roles, "Roles can not be empty");
        if (Objects.equals(this.roles, roles)) {
            return;
        }
        this.roles = roles;
        addDomainEvent(new UserRolesUpdatedEvent(this));
    }

    /**
     * Gets the username
     * @return The username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the password
     * @return The password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets the mobile number
     * @return The mobile number
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * Gets the email address
     * @return The email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets the display name
     * @return The display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Gets the avatar URL
     * @return The avatar URL
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     * Gets the gender
     * @return The gender
     */
    public Gender getGender() {
        return gender;
    }

    /**
     * Gets the location
     * @return The location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Checks if the user account is enabled
     * @return true if enabled, false otherwise
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Checks if the user account is verified
     * @return true if verified, false otherwise
     */
    public boolean isVerified() {
        return verified;
    }

    /**
     * Gets the user's roles
     * @return Set of roles
     */
    public Set<String> getRoles() {
        return roles;
    }

    /**
     * Gets the creation timestamp
     * @return The creation timestamp
     */
    public Instant getCreatedAt() {
        return createdAt;
    }

    /**
     * Gets the last update timestamp
     * @return The last update timestamp
     */
    public Instant getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Gets the locale setting
     * @return The locale
     */
    public String getLocale() {
        return locale;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                ", displayName='" + displayName + '\'' +
                ", enabled=" + enabled +
                ", verified=" + verified +
                ", roles='" + roles + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    public static final class Builder {
        private String username;
        private String password;
        private String mobile;
        private String email;
        private String displayName;
        private String avatar = "";
        private Gender gender = Gender.UNKNOWN;;
        private String location = "";
        private boolean enabled = true;
        private boolean verified = true;
        private Set<String> roles = Set.of("user");
        private String locale = "en";

        private Builder() {
        }

        public Builder username(String val) {
            BizAssert.notNull(val, "username is required");
            BizAssert.isTrue(val.length() > 2, "username should have at least three characters");
            username = val;
            return this;
        }

        public Builder password(String val) {
            password = val;
            return this;
        }

        public Builder mobile(String val) {
            mobile = val;
            return this;
        }

        public Builder email(String val) {
            email = val;
            return this;
        }

        public Builder displayName(String val) {
            BizAssert.notNull(val, "displayName must not be null");
            BizAssert.isTrue(val.length() > 2, "displayName should have at least three characters");
            displayName = val;
            return this;
        }

        public Builder avatar(String val) {
            avatar = val;
            return this;
        }
        public Builder gender(Gender val) {
            BizAssert.notNull(val, "gender is required");
            gender = val;
            return this;
        }
        public Builder location(String val) {
            location = val;
            return this;
        }
        public Builder enabled(boolean val) {
            enabled = val;
            return this;
        }
        public Builder verified(boolean val) {
            verified = val;
            return this;
        }
        public Builder roles(Set<String> val) {
            BizAssert.notEmpty(val, "roles is required");
            roles = val;
            return this;
        }
        public Builder locale(String val) {
            BizAssert.hasLength(val, "locale is required");
            locale = val;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
