package com.ikeyit.account.domain.model;

import com.ikeyit.common.data.domain.BaseAggregateRoot;
import com.ikeyit.common.exception.BizAssert;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class UserConnection extends BaseAggregateRoot<Long> {
	private Long id;
	private Long localUserId;
	private String provider;
	private String sub;
	private String name;
	private String preferredUsername;
	private String nickname;
	private String picture;
	private String email;
	private String phoneNumber;
	private Instant createdAt;
	private Instant updatedAt;
	public UserConnection() {
	}
	private UserConnection(Builder builder) {
		BizAssert.notNull(builder.localUserId, "localUserId is required");
		BizAssert.notNull(builder.provider, "provider is required");
		BizAssert.notNull(builder.sub, "sub is required");
		localUserId = builder.localUserId;
		provider = builder.provider;
		sub = builder.sub;
		name = builder.name;
		nickname = builder.nickname;
		picture = builder.picture;
		email = builder.email;
		phoneNumber = builder.phoneNumber;
		preferredUsername = builder.preferredUsername;
		createdAt = Instant.now().truncatedTo(ChronoUnit.MICROS);
		updatedAt = this.createdAt;
	}

	public static Builder newBuilder() {
		return new Builder();
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void assignId(Long id) {
		this.id = id;
	}

	public Long getLocalUserId() {
		return localUserId;
	}

	public String getProvider() {
		return provider;
	}

	public String getSub() {
		return sub;
	}

	public String getName() {
		return name;
	}

	public String getPreferredUsername() {
		return preferredUsername;
	}

	public String getNickname() {
		return nickname;
	}

	public String getPicture() {
		return picture;
	}

	public String getEmail() {
		return email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}

	public void assignLocalUserId(Long localUserId) {
		this.localUserId = localUserId;
	}

	public static final class Builder {
		private Long localUserId;
		private String provider;
		private String sub;
		private String name = "";
		private String nickname = "";
		private String picture = "";
		private String email = "";
		private String phoneNumber = "";
		private String preferredUsername = "";
		private Builder() {
		}

		public Builder localUserId(Long val) {
			localUserId = val;
			return this;
		}

		public Builder provider(String val) {
			provider = val;
			return this;
		}

		public Builder sub(String val) {
			sub = val;
			return this;
		}

		public Builder name(String val) {
			name = val;
			return this;
		}

		public Builder nickname(String val) {
			nickname = val;
			return this;
		}

		public Builder picture(String val) {
			picture = val;
			return this;
		}

		public Builder email(String val) {
			email = val;
			return this;
		}

		public Builder phoneNumber(String val) {
			phoneNumber = val;
			return this;
		}

		public Builder preferredUsername(String val) {
			preferredUsername = val;
			return this;
		}

		public UserConnection build() {
			return new UserConnection(this);
		}


	}
}
