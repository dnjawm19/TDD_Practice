package com.example.demo.user.service;

import com.example.demo.mock.FakeMailSender;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CertificationServiceTest {

    FakeMailSender fakeMailSender = new FakeMailSender();

    @Test
    public void TestSend() {
        CertificationService certificationService = new CertificationService(fakeMailSender);

        certificationService.send("dnjawm19@naver.com", 1L, "aaaa");

        assertThat(fakeMailSender.email).isEqualTo("dnjawm19@naver.com");
        assertThat(fakeMailSender.title).isEqualTo("Please certify your email address");
        assertThat(fakeMailSender.content).isEqualTo("Please click the following link to certify your email address: http://localhost:8080/api/users/1/verify?certificationCode=aaaa");
    }
}
