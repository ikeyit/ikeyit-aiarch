plugins {
    // NOTE: use this convention plugin for grpc project
    id("com.ikeyit.build-grpc")
}

dependencies {
    api("com.ikeyit:ikeyit-common-grpc-client")
}