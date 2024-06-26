package com.example.demo.user.infrastructure;

import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest()
@TestPropertySource("classpath:test-application.properties")
@Sql("/sql/user-repository-test-data.sql")
public class UserJpaRepositoryTest {

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Test
    void TestFindByIdAndStatus() {
        Optional<UserEntity> result = userJpaRepository.findByIdAndStatus(1, UserStatus.ACTIVE);

        assertThat(result.isPresent()).isTrue();
    }

    @Test
    void TestFindByIdAndStatusEmpty() {
        Optional<UserEntity> result = userJpaRepository.findByIdAndStatus(1, UserStatus.PENDING);

        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    void TestFindByEmailAndStatus() {
        Optional<UserEntity> result = userJpaRepository.findByEmailAndStatus("dnjawm19@naver.com", UserStatus.ACTIVE);

        assertThat(result.isPresent()).isTrue();
    }

    @Test
    void TestFindByEmailAndStatusEmpty() {
        Optional<UserEntity> result = userJpaRepository.findByEmailAndStatus("dnjawm19@naver.com", UserStatus.PENDING);

        assertThat(result.isEmpty()).isTrue();
    }
}
