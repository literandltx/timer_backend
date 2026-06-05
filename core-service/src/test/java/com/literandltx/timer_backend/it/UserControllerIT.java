package com.literandltx.timer_backend.it;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import com.literandltx.timer_backend.dto.user.ChangeEmailRequestDto;
import com.literandltx.timer_backend.dto.user.ChangePasswordRequestDto;
import com.literandltx.timer_backend.dto.user.UserUpdateRequestDto;
import com.literandltx.timer_backend.dto.user.auth.ForgotPasswordRequestDto;
import com.literandltx.timer_backend.dto.user.auth.ResetPasswordRequestDto;
import com.literandltx.timer_backend.model.PasswordResetToken;
import com.literandltx.timer_backend.model.User;
import com.literandltx.timer_backend.repository.LabelRepository;
import com.literandltx.timer_backend.repository.PasswordResetTokenRepository;
import com.literandltx.timer_backend.repository.UserRepository;
import io.restassured.http.ContentType;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserControllerIT extends BaseIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final String userEmail = "testuser@example.com";
    private final String userPlainPassword = "password1234";
    private User currentUser;

    @BeforeEach
    void setUp() {
        super.setUp();
        currentUser = User.builder()
                .email(userEmail)
                .password(passwordEncoder.encode(userPlainPassword))
                .build();
        userRepository.save(currentUser);
    }

    @AfterEach
    void tearDown() {
        passwordResetTokenRepository.deleteAll();
        labelRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldReturnCurrentUser_WhenAuthenticated() {
        given()
                .contentType(ContentType.JSON)
                .auth().basic(userEmail, userPlainPassword)
                .when()
                .get("/api/v1/users/me")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("email", equalTo(userEmail))
                .body("id", equalTo(currentUser.getId().intValue()));
    }

    @Test
    void shouldReturnUnauthorized_WhenGettingUserWithoutAuth() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/users/me")
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void shouldUpdateAccount_WhenRequestIsValid() {
        UserUpdateRequestDto request = new UserUpdateRequestDto();
        request.setEmail("updated@example.com");
        request.setCurrentPassword(userPlainPassword);
        request.setNewPassword("newPassword123");
        request.setConfirmationPassword("newPassword123");

        given()
                .contentType(ContentType.JSON)
                .auth().basic(userEmail, userPlainPassword)
                .body(request)
                .when()
                .put("/api/v1/users/me")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("email", equalTo("updated@example.com"));

        User updatedUser = userRepository.findById(currentUser.getId()).orElseThrow();
        Assertions.assertEquals("updated@example.com", updatedUser.getEmail());
    }

    @Test
    void shouldReturnBadRequest_WhenUpdateAccountHasPasswordMismatch() {
        UserUpdateRequestDto request = new UserUpdateRequestDto();
        request.setEmail("updated@example.com");
        request.setCurrentPassword(userPlainPassword);
        request.setNewPassword("newPassword123");
        request.setConfirmationPassword("mismatchPassword123");

        given()
                .contentType(ContentType.JSON)
                .auth().basic(userEmail, userPlainPassword)
                .body(request)
                .when()
                .put("/api/v1/users/me")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void shouldChangePassword_WhenCurrentPasswordIsCorrect() {
        String newPassword = "newSecretPassword123";
        ChangePasswordRequestDto request = new ChangePasswordRequestDto();
        request.setCurrentPassword(userPlainPassword);
        request.setNewPassword(newPassword);
        request.setConfirmationPassword(newPassword);

        given()
                .contentType(ContentType.JSON)
                .auth().basic(userEmail, userPlainPassword)
                .body(request)
                .when()
                .patch("/api/v1/users/me/password")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        given()
                .contentType(ContentType.JSON)
                .auth().basic(userEmail, newPassword)
                .when()
                .get("/api/v1/users/me")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void shouldReturnUnauthorized_WhenChangingPasswordWithWrongCurrent() {
        ChangePasswordRequestDto request = new ChangePasswordRequestDto();
        request.setCurrentPassword("wrongPassword");
        request.setNewPassword("newSecretPassword123");
        request.setConfirmationPassword("newSecretPassword123");

        given()
                .contentType(ContentType.JSON)
                .auth().basic(userEmail, userPlainPassword)
                .body(request)
                .when()
                .patch("/api/v1/users/me/password")
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void shouldChangeEmail_WhenEmailIsAvailable() {
        ChangeEmailRequestDto request = new ChangeEmailRequestDto();
        request.setNewEmail("newemail@example.com");

        given()
                .contentType(ContentType.JSON)
                .auth().basic(userEmail, userPlainPassword)
                .body(request)
                .when()
                .patch("/api/v1/users/me/email")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("email", equalTo("newemail@example.com"));

        User updatedUser = userRepository.findById(currentUser.getId()).orElseThrow();
        Assertions.assertEquals("newemail@example.com", updatedUser.getEmail());
    }

    @Test
    void shouldReturnConflict_WhenChangingEmailToExistingOne() {
        User otherUser = User.builder()
                .email("other@example.com")
                .password(passwordEncoder.encode("password"))
                .build();
        userRepository.save(otherUser);

        ChangeEmailRequestDto request = new ChangeEmailRequestDto();
        request.setNewEmail("other@example.com");

        given()
                .contentType(ContentType.JSON)
                .auth().basic(userEmail, userPlainPassword)
                .body(request)
                .when()
                .patch("/api/v1/users/me/email")
                .then()
                .statusCode(HttpStatus.CONFLICT.value());
    }

    @Test
    void shouldDeleteAccount_WhenAuthenticated() {
        given()
                .contentType(ContentType.JSON)
                .auth().basic(userEmail, userPlainPassword)
                .when()
                .delete("/api/v1/users/me")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        boolean exists = userRepository.existsById(currentUser.getId());
        Assertions.assertFalse(exists);
    }

    @Test
    void shouldReturnAccepted_AndCreateToken_WhenForgotPasswordRequestedForExistingUser() {
        ForgotPasswordRequestDto request = new ForgotPasswordRequestDto();
        request.setEmail(userEmail);

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/auth/forgot")
                .then()
                .statusCode(HttpStatus.ACCEPTED.value());

        var tokens = passwordResetTokenRepository.findAll();
        Assertions.assertFalse(tokens.isEmpty(), "A password reset token should have been created");
        Assertions.assertEquals(currentUser.getId(), tokens.get(0).getUser().getId());
        Assertions.assertNotNull(tokens.get(0).getToken());
    }

    @Test
    void shouldReturnAccepted_ButNotCreateToken_WhenForgotPasswordRequestedForNonExistentUser() {
        ForgotPasswordRequestDto request = new ForgotPasswordRequestDto();
        request.setEmail("doesnotexist@example.com");

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/auth/forgot")
                .then()
                .statusCode(HttpStatus.ACCEPTED.value());

        var tokens = passwordResetTokenRepository.findAll();
        Assertions.assertTrue(tokens.isEmpty(), "No token should be created for a non-existent user");
    }

    @Test
    void shouldReturnBadRequest_WhenForgotPasswordRequestedWithInvalidEmail() {
        ForgotPasswordRequestDto request = new ForgotPasswordRequestDto();
        request.setEmail("invalid-email-format");

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/auth/forgot")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void shouldReturnBadRequest_WhenForgotPasswordRequestedWithMissingEmail() {
        ForgotPasswordRequestDto request = new ForgotPasswordRequestDto();
        request.setEmail(null);

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/auth/forgot")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void shouldResetPassword_WhenTokenAndRequestAreValid() {
        PasswordResetToken resetToken = createActiveResetToken(currentUser);
        ResetPasswordRequestDto request = new ResetPasswordRequestDto();
        request.setPassword("newSuperSecret123");
        request.setRepeatPassword("newSuperSecret123");

        given()
                .contentType(ContentType.JSON)
                .queryParam("token", resetToken.getToken())
                .body(request)
                .when()
                .post("/api/v1/auth/reset")
                .then()
                .statusCode(HttpStatus.OK.value());

        User updatedUser = userRepository.findById(currentUser.getId()).orElseThrow();
        Assertions.assertTrue(passwordEncoder.matches("newSuperSecret123", updatedUser.getPassword()),
                "Password should be encoded and match the new password");

        PasswordResetToken updatedToken = passwordResetTokenRepository.findById(resetToken.getId()).orElseThrow();
        Assertions.assertFalse(updatedToken.isActive(), "Token should be deactivated after successful use");
    }

    @Test
    void shouldReturnError_WhenResetPasswordWithExpiredToken() {
        PasswordResetToken expiredToken = PasswordResetToken.builder()
                .user(currentUser)
                .token(UUID.randomUUID().toString())
                .active(true)
                .expiresAt(LocalDateTime.now().minusMinutes(5))
                .build();
        passwordResetTokenRepository.save(expiredToken);

        ResetPasswordRequestDto request = new ResetPasswordRequestDto();
        request.setPassword("newSuperSecret123");
        request.setRepeatPassword("newSuperSecret123");

        given()
                .contentType(ContentType.JSON)
                .queryParam("token", expiredToken.getToken())
                .body(request)
                .when()
                .post("/api/v1/auth/reset")
                .then()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());

        PasswordResetToken updatedToken = passwordResetTokenRepository.findById(expiredToken.getId()).orElseThrow();
        Assertions.assertFalse(updatedToken.isActive(), "Expired token should be set to inactive");
    }

    @Test
    void shouldReturnError_WhenResetPasswordWithInactiveToken() {
        PasswordResetToken inactiveToken = PasswordResetToken.builder()
                .user(currentUser)
                .token(UUID.randomUUID().toString())
                .active(false)
                .expiresAt(LocalDateTime.now().minusHours(1))
                .build();
        passwordResetTokenRepository.save(inactiveToken);

        ResetPasswordRequestDto request = new ResetPasswordRequestDto();
        request.setPassword("newSuperSecret123");
        request.setRepeatPassword("newSuperSecret123");

        given()
                .contentType(ContentType.JSON)
                .queryParam("token", inactiveToken.getToken())
                .body(request)
                .when()
                .post("/api/v1/auth/reset")
                .then()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @Test
    void shouldReturnBadRequest_WhenResetPasswordWithMismatchedPasswords() {
        PasswordResetToken resetToken = createActiveResetToken(currentUser);
        ResetPasswordRequestDto request = new ResetPasswordRequestDto();
        request.setPassword("newSuperSecret123");
        request.setRepeatPassword("differentPassword456");

        given()
                .contentType(ContentType.JSON)
                .queryParam("token", resetToken.getToken())
                .body(request)
                .when()
                .post("/api/v1/auth/reset")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void shouldReturnBadRequest_WhenResetPasswordWithShortPassword() {
        PasswordResetToken resetToken = createActiveResetToken(currentUser);
        ResetPasswordRequestDto request = new ResetPasswordRequestDto();
        request.setPassword("short");
        request.setRepeatPassword("short");

        given()
                .contentType(ContentType.JSON)
                .queryParam("token", resetToken.getToken())
                .body(request)
                .when()
                .post("/api/v1/auth/reset")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    private PasswordResetToken createActiveResetToken(User user) {
        PasswordResetToken token = PasswordResetToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .active(true)
                .expiresAt(LocalDateTime.now().plusHours(1))
                .build();
        return passwordResetTokenRepository.save(token);
    }
}
