package com.ikeyit

import com.google.protobuf.gradle.*

plugins {
    id("com.ikeyit.build-java-library")
    id("com.google.protobuf")
}

val grpcVersion = "1.68.2"
val protobufVersion = "3.25.3"

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:${protobufVersion}"
    }

    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:${grpcVersion}"
        }
    }

    generateProtoTasks {
        all().forEach { task ->
            task.plugins {
                id("grpc") {
                    option("lite")
                }
            }
        }
    }
}

dependencies {
    implementation("io.grpc:grpc-protobuf")
    implementation("io.grpc:grpc-stub")
    implementation("io.grpc:grpc-protobuf-lite")
    compileOnly("javax.annotation:javax.annotation-api")
}
