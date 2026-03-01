package com.schools.logesco.mail;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Service
public class HtmlTemplateLoader {

    public String loadTemplate(String path, Map<String, String> variables) {
        try {
            // Lire le fichier
            String content = new String(
                    Objects.requireNonNull(
                            getClass().getClassLoader().getResourceAsStream(path)
                    ).readAllBytes()
            );

            // Remplacer les variables ${var}
            for (Map.Entry<String, String> entry : variables.entrySet()) {
                content = content.replace("${" + entry.getKey() + "}", entry.getValue());
            }

            return content;

        } catch (Exception e) {
            throw new RuntimeException("Impossible de charger le template : " + path, e);
        }
    }
}

