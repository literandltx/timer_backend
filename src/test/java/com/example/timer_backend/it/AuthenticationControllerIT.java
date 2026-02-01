package com.example.timer_backend.it;

import com.example.timer_backend.dto.user.UserRegistrationRequestDto;
import com.example.timer_backend.model.User;
import com.example.timer_backend.repository.LabelRepository;
import com.example.timer_backend.repository.UserRepository;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class AuthenticationControllerIT extends BaseIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final String userEmail = "testuser@example.com";
    private final String userPlainPassword = "password";

    @AfterEach
    void tearDown() {
        labelRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldCreateLabel_WhenUserIsAuthenticated() {
        UserRegistrationRequestDto request = UserRegistrationRequestDto.builder()
                .email(userEmail)
                .password(userPlainPassword)
                .repeatPassword(userPlainPassword)
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/auth/register")
                .then()
                .log().ifValidationFails()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", notNullValue())
                .body("email", equalTo(userEmail));
    }

    @Test
    void shouldReturnBadRequest_WhenPasswordsDoNotMatch() {
        UserRegistrationRequestDto request = UserRegistrationRequestDto.builder()
                .email(userEmail)
                .password("password123")
                .repeatPassword("password999")
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/auth/register")
                .then()
                .log().ifValidationFails()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", containsString("Password and repeat password shouldn't be empty and should be equal"));
    }

    @Test
    void shouldReturnConflict_WhenUserAlreadyExists() {
        User existingUser = new User();
        existingUser.setEmail(userEmail);
        existingUser.setPassword(passwordEncoder.encode(userPlainPassword));
        userRepository.save(existingUser);

        UserRegistrationRequestDto request = UserRegistrationRequestDto.builder()
                .email(userEmail)
                .password("newpassword")
                .repeatPassword("newpassword")
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/auth/register")
                .then()
                .log().ifValidationFails()
                .statusCode(HttpStatus.CONFLICT.value())
                .body("message", equalTo("Unable to complete registration. User already exists."));
    }

    @Test
    void shouldReturnBadRequest_WhenEmailIsInvalid() {
        UserRegistrationRequestDto request = UserRegistrationRequestDto.builder()
                .email("not-a-valid-email-format")
                .password(userPlainPassword)
                .repeatPassword(userPlainPassword)
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/auth/register")
                .then()
                .log().ifValidationFails()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", containsString("email"));
    }

    @Test
    void shouldReturnBadRequest_WhenPasswordIsTooShort() {
        UserRegistrationRequestDto request = UserRegistrationRequestDto.builder()
                .email(userEmail)
                .password("pass")
                .repeatPassword("pass")
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/auth/register")
                .then()
                .log().ifValidationFails()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", containsString("password"))
                .body("message", containsString("size"));
    }
}
