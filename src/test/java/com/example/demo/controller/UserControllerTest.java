package com.example.demo.controller;

import com.example.demo.model.UserStatus;
import com.example.demo.model.dto.UserUpdateDto;
import com.example.demo.repository.UserEntity;
import com.example.demo.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@SqlGroup({
    @Sql(value = "/sql/user-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
    @Sql(value = "/sql/delete-all.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
public class UserControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;

    @Test
    void TestGetUserById() throws Exception {
        mockMvc.perform(get("/api/users/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.email").value("dnjawm19@naver.com"))
            .andExpect(jsonPath("$.nickname").value("무관심"))
            .andExpect(jsonPath("$.status").value("ACTIVE"))
            .andExpect(jsonPath("$.address").doesNotExist());
    }

    @Test
    void TestGetUserByIdNotExist() throws Exception {
        mockMvc.perform(get("/api/users/3"))
            .andExpect(status().isNotFound())
            .andExpect(content().string("Users에서 ID 3를 찾을 수 없습니다."));
    }

    @Test
    void TestVerifyEmail() throws Exception {
        mockMvc.perform(get("/api/users/2/verify")
                .queryParam("certificationCode", "aaab"))
            .andExpect(status().isFound());

        UserEntity userEntity = userRepository.findById(2L).get();

        assertThat(userEntity.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void TestGetMyInfo() throws Exception {
        mockMvc.perform(get("/api/users/me")
                .header("EMAIL", "dnjawm19@naver.com"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.email").value("dnjawm19@naver.com"))
            .andExpect(jsonPath("$.nickname").value("무관심"))
            .andExpect(jsonPath("$.status").value("ACTIVE"))
            .andExpect(jsonPath("$.address").value("경기도"));
    }

    @Test
    void TestUpdateMyInfo() throws Exception {
        UserUpdateDto userUpdateDto = UserUpdateDto.builder()
            .address("주소 수정 테스트")
            .nickname("닉네임 수정 테스트")
            .build();

        mockMvc.perform(put("/api/users/me")
                .header("EMAIL", "dnjawm19@naver.com")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userUpdateDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.email").value("dnjawm19@naver.com"))
            .andExpect(jsonPath("$.nickname").value("닉네임 수정 테스트"))
            .andExpect(jsonPath("$.status").value("ACTIVE"))
            .andExpect(jsonPath("$.address").value("주소 수정 테스트"));

    }
}
