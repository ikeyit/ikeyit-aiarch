package com.ikeyit.allinone;

import com.ikeyit.account.interfaces.allinone.AccountAllinoneConfig;
import com.ikeyit.classroom.interfaces.allinone.ClassroomAllinoneConfig;
import com.ikeyit.staff.interfaces.allinone.AccessAllinoneConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({
    AccountAllinoneConfig.class,
    ClassroomAllinoneConfig.class,
    AccessAllinoneConfig.class
})
public class AllinoneApp {

    public static void main(String[] args) {
        new SpringApplication(AllinoneApp.class).run(args);
    }
}
