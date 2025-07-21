package org.alkemy.alkywallet.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        type = SecuritySchemeType.HTTP,
        name = "bearerAuth",
        bearerFormat = "JWT",
        scheme = "bearer")
@OpenAPIDefinition(
        info = @Info(title = "TurbowWallet",
                version = "1.0.0",
                description = "Billetera TurboWallet",
                contact = @Contact(
                        name = "Facundo Martinez",
                        email = "martinez.facundo85@gmail.com",
                        url = "https://www.google.com"
                ),
                license = @License(
                        name = "MIT License",
                        url = "https://opensource.org/licenses/MIT"
                )),
        security = @SecurityRequirement(name = "bearerAuth")
)
public class OpenApiConfig {
}
