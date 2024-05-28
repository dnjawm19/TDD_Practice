package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@TestPropertySource("classpath:test-application.properties")
@SqlGroup({
        @Sql(value = "/sql/user-service-test-data.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
@Sql("/sql/user-service-test-data.sql")
public class UserServiceTest {

    @Autowired
    private UserService userService;

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
}
