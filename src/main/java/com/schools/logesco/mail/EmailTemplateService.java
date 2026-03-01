package com.schools.logesco.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailTemplateService {

    private final HtmlTemplateLoader loader;

    public String userCreated(String username, String password, String appUrl) {
        return loader.loadTemplate(
                "templates/email/user-created.html",
                Map.of(
                        "username", username,
                        "password", password,
                        "appUrl", appUrl
                )
        );
    }

    public String resetPassword(String username, String resetLink) {
        return loader.loadTemplate(
                "templates/email/reset-password.html",
                Map.of(
                        "username", username,
                        "resetLink", resetLink
                )
        );
    }
}

