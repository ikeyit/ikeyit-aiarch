# How to integrate other apps with account using OAuth2 and OIDC
## IOS App / Android App / Windows / MacOS / Linux Native App
1. Store access token and refresh token in OS Credential Manager. 
- IOS/MacOS: Keychain
- Windows: Credential Manager / Windows Data Protection API (DPAPI)
- Android: EncryptedSharedPreferences /Android Keystore System

2. Set these properties
```yaml
          mobileClient:
            registration:
              client-id: mobileClientId
              client-authentication-methods:
                - none # It's public client which can not own a client secret. Set the property to none
              authorization-grant-types:
                - authorization_code
                - refresh_token # Allow to refresh token
              redirect-uris:
                - com.ikeyit.Classroom://oidc_callback 
              post-logout-redirect-uris:
                - com.ikeyit.Classroom://oidc_logout_callback
              scopes:
                - openid
                - profile
                - email
                - roles
                - offline_access # Allow to get a refresh token
            token:
              access-token-time-to-live: 1m
              refresh-token-time-to-live: 1d
              reuse-refresh-tokens: false # Do not reuse the refresh token
            require-authorization-consent: false
            require-proof-key: true # Must be true
```
## SPA with less security requirement
1. Store access token in memory or session storage with a short live time. DO NOT store it in the local storage.
2. Use silent login / renew + iframe to keep the authentication status. DO NOT use refresh token.
3. Set these properties
```yaml
          spa-client:
            registration:
              client-id: spa-client
              client-authentication-methods:
                - none # It's public client which can not own a client secret. Set the property to none
              authorization-grant-types: # DO NOT grant refresh_token to allow refresh token
                - authorization_code
              redirect-uris:
                - https://classroom.dev.test:5174/oidc-callback
                - https://classroom.dev.test:5174/silent-renew.html # the call back is for silent login / renew
              post-logout-redirect-uris:
                - https://classroom.dev.test:5174/oidc-logout-callback
              scopes: # DO NOT grant the scope offline_access to issue refresh token
                - openid
                - profile
                - email
                - roles
            token:
              access-token-time-to-live: 5m
              refresh-token-time-to-live: 12h
            require-authorization-consent: false
            require-proof-key: true # Must be true
```
## Web apps with high security requirement
1. Must have a dedicated backend to store client secret and refresh token. BFF is your best choice
2. Do not expose the refresh token to the frontend.
3. Use backend to refresh token
4. Set the access token in the cookie with strict constraint. httponly=true, samesite=Strict,secure=true
5. Enable csrf
```yaml
          webClientId:
            registration:
              client-id: webClientId
              client-secret: "{noop}library-system" # Must have a secret
              client-authentication-methods:
                - client_secret_post # It's confidential client using post secret to authenticate the client
              authorization-grant-types:
                - authorization_code
                - refresh_token # Allow to refresh token
              redirect-uris:
                - https://your-bff/callback
              post-logout-redirect-uris:
                - https://your-bff/logout_callback
              scopes:
                - openid
                - profile
                - email
                - roles
                - offline_access # Allow to get a refresh token
            token:
              access-token-time-to-live: 1m
              refresh-token-time-to-live: 1d
              reuse-refresh-tokens: false # Do not reuse the refresh token
            require-authorization-consent: false
            require-proof-key: false # False is ok, but true is safer
```