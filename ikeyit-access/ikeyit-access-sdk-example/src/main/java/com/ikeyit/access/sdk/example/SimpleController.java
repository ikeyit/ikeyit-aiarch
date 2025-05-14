package com.ikeyit.access.sdk.example;

import com.ikeyit.access.core.AccessRequest;
import com.ikeyit.access.remote.RemoteAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpleController {

    @Autowired
    private RemoteAccessService remoteAccessService;

    public void run(String... args) throws Exception {
        AccessRequest accessRequest = new AccessRequest();
        accessRequest.setUserId(1L);
        accessRequest.setPermission("read-order");
        accessRequest.setRealmId(1L);
        accessRequest.setRealmType("store");
        System.out.println(remoteAccessService.check(accessRequest));
    }
}
