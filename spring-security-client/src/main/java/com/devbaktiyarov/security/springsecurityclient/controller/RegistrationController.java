package com.devbaktiyarov.security.springsecurityclient.controller;

import com.devbaktiyarov.security.springsecurityclient.entity.User;
import com.devbaktiyarov.security.springsecurityclient.entity.VerificationToken;
import com.devbaktiyarov.security.springsecurityclient.event.RegistrationCompetedEvent;
import com.devbaktiyarov.security.springsecurityclient.model.PasswordModel;
import com.devbaktiyarov.security.springsecurityclient.model.UserModel;
import com.devbaktiyarov.security.springsecurityclient.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
public class RegistrationController {

    private final UserService userService;
    private final ApplicationEventPublisher publisher;

    public RegistrationController(UserService userService, ApplicationEventPublisher publisher) {
        this.userService = userService;
        this.publisher = publisher;
    }

    @PostMapping("/register")
    public String registerUser(@RequestBody UserModel userModel, final HttpServletRequest request) {
        User user = userService.registerUser(userModel);
        publisher.publishEvent(new RegistrationCompetedEvent(user, applicationUrl(request)));
        return "User registered successfully";
    }

    private String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() +
                ":" + request.getServerPort() +
                request.getContextPath();
    }

    @GetMapping("/verifyRegistration")
    public String verifyRegistration(@RequestParam("token") String token) {
        String result = userService.validateVerificationToken(token);

        if (result.equalsIgnoreCase("valid")) {
            return "User verifies successfully";
        }

        return "User verifies unsuccessfully";
    }

    @GetMapping("/resendVerifyToken")
    public String resendVerificationToken(@RequestParam("token") String oldToken,
                                          final HttpServletRequest request) {
        VerificationToken verificationToken =
                userService.generateNewVerificationToken(oldToken);
        User user = verificationToken.getUser();
        resendVerificationToken(user, applicationUrl(request), verificationToken);
        return "Verification Link Sent";
    }

    private void resendVerificationToken(User user, String applicationUrl, VerificationToken token) {
        String url = applicationUrl + "/verifyRegistration?token=" + token.getToken();

        log.info("/resendVerifyToken: Click the link to verify your account: {}", url);
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestBody PasswordModel passwordModel, final HttpServletRequest request) {
        User user = userService.findUserByEmail(passwordModel.getEmail());
        String url = "";
        if (user != null) {
            String token = UUID.randomUUID().toString();
            userService.createPasswordResetTokenForUser(user, token);
            url = passwordResetTokenMail(user, applicationUrl(request), token);
        }
        return url;
    }

    private String passwordResetTokenMail(User user, String applicationUrl, String token) {
        String url = applicationUrl + "/savePassword?token=" + token;
        log.info("//resetPassword: Click the link to reset your account: {}", url);
        return url;
    }

    @PostMapping("/savePassword")
    public String savePassword(@RequestParam("token") String token,
                               @RequestBody PasswordModel passwordModel) {
        String result = userService.validatePasswordResetToken(token);
        if (!result.equalsIgnoreCase("valid")) {
            return "invalid token";
        }

        Optional<User> user = userService.getUserByPasswordResetToken(token);
        if (user.isPresent()) {
            userService.changePassword(user.get(), passwordModel.getNewPassword());
            return "Password reset successfully";
        } else {
            return "Invalid token";
        }

    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestBody PasswordModel passwordModel) {
        User user = userService.findUserByEmail(passwordModel.getEmail());
        if (!userService.checkIfValidOldPassword(user, passwordModel.getOldPassword())) {
            return "Invalid old password";
        }

        userService.changePassword(user, passwordModel.getNewPassword());
        return "Password changed successfully";
    }


}
