package com.example.demo.repository;


import com.example.demo.repository.model.UserStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest()
@TestPropertySource("classpath:test-application.properties")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void TestConnectUserRepository() {
        UserEntity userEntity = new UserEntity();

        userEntity.setEmail("dnjawm19@naver.com");
        userEntity.setAddress("경기도");
        userEntity.setNickname("무관심");
        userEntity.setStatus(UserStatus.ACTIVE);
        userEntity.setCertificationCode("aaaa-aa");

        UserEntity result = userRepository.save(userEntity);

        assertThat(result.getId()).isNotNull();
    }

    @Test
    void TestFindByIdAndStatus() {
        UserEntity userEntity = new UserEntity();

        userEntity.setEmail("dnjawm19@naver.com");
        userEntity.setAddress("경기도");
        userEntity.setNickname("무관심");
        userEntity.setStatus(UserStatus.ACTIVE);
        userEntity.setCertificationCode("aaaa-aa");
        userEntity.setId(1L);

        userRepository.save(userEntity);
        Optional<UserEntity> result = userRepository.findByIdAndStatus(1,UserStatus.ACTIVE);

        assertThat(result.isPresent()).isTrue();
    }

    @Test
    void TestFindByIdAndStatusEmpty() {
        UserEntity userEntity = new UserEntity();

        userEntity.setEmail("dnjawm19@naver.com");
        userEntity.setAddress("경기도");
        userEntity.setNickname("무관심");
        userEntity.setStatus(UserStatus.ACTIVE);
        userEntity.setCertificationCode("aaaa-aa");
        userEntity.setId(1L);

        userRepository.save(userEntity);
        Optional<UserEntity> result = userRepository.findByIdAndStatus(1,UserStatus.PENDING);

        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    void TestFindByEmailAndStatus() {
        UserEntity userEntity = new UserEntity();

        userEntity.setEmail("dnjawm19@naver.com");
        userEntity.setAddress("경기도");
        userEntity.setNickname("무관심");
        userEntity.setStatus(UserStatus.ACTIVE);
        userEntity.setCertificationCode("aaaa-aa");
        userEntity.setId(1L);

        userRepository.save(userEntity);
        Optional<UserEntity> result = userRepository.findByEmailAndStatus("dnjawm19@naver.com",UserStatus.ACTIVE);

        assertThat(result.isPresent()).isTrue();
    }

    @Test
    void TestFindByEmailAndStatusEmpty() {
        UserEntity userEntity = new UserEntity();

        userEntity.setEmail("dnjawm19@naver.com");
        userEntity.setAddress("경기도");
        userEntity.setNickname("무관심");
        userEntity.setStatus(UserStatus.ACTIVE);
        userEntity.setCertificationCode("aaaa-aa");
        userEntity.setId(1L);

        userRepository.save(userEntity);
        Optional<UserEntity> result = userRepository.findByEmailAndStatus("dnjawm19@naver.com",UserStatus.PENDING);

        assertThat(result.isEmpty()).isTrue();
    }
}
