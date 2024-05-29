package com.example.demo.user.service;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import com.example.demo.user.infrastructure.UserEntity;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@TestPropertySource("classpath:test-application.properties")
@SqlGroup({
    @Sql(value = "/sql/user-service-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
    @Sql(value = "/sql/delete-all.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
@Sql("/sql/user-service-test-data.sql")
public class UserServiceTest {

    @Autowired
    private UserService userService;
    @MockBean
    private JavaMailSender mailSender;

    @Test
    void TestGetByEmail() {
        String email = "dnjawm19@naver.com";

        UserEntity result = userService.getByEmail(email);

        assertThat(result.getNickname()).isEqualTo("무관심");
    }

    @Test
    void TestGetByEmailStatusPending() {
        String email = "dnjawm1995@naver.com";

        assertThatThrownBy(() -> userService.getByEmail(email)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void TestGetById() {
        UserEntity result = userService.getById(1);

        assertThat(result.getNickname()).isEqualTo("무관심");
    }

    @Test
    void TestGetByIdStatusPending() {
        assertThatThrownBy(() -> userService.getById(2)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void TestCreateDto() {
        UserCreate userCreate = UserCreate.builder()
            .email("dnjawm19@naver.com")
            .address("경기도")
            .nickname("무관심")
            .build();
        BDDMockito.doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        UserEntity result = userService.create(userCreate);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
    }

    @Test
    void TestUpdateDto() {
        UserUpdate userUpdate = UserUpdate.builder()
            .address("서울")
            .nickname("무관심하자")
            .build();

        userService.update(1, userUpdate);

        UserEntity userEntity = userService.getById(1);
        assertThat(userEntity.getId()).isNotNull();
        assertThat(userEntity.getAddress()).isEqualTo("서울");
        assertThat(userEntity.getNickname()).isEqualTo("무관심하자");
    }

    @Test
    void TestLogin() {
        userService.login(1);

        UserEntity userEntity = userService.getById(1);

        assertThat(userEntity.getLastLoginAt()).isGreaterThan(0);
    }

    @Test
    void TestUsingCodeStatusPendingToActive() {
        userService.verifyEmail(2, "aaab");

        UserEntity userEntity = userService.getById(2);

        assertThat(userEntity.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void TestUsingWrongCodeStatusPendingToActive() {
        assertThatThrownBy(() -> {
            userService.verifyEmail(2, "aaaa");
        }).isInstanceOf(CertificationCodeNotMatchedException.class);
    }
}
