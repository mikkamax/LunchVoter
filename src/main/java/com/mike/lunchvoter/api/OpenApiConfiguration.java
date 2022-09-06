package com.mike.lunchvoter.api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "LunchVoter",
                description = """
                        A simple REST demo application (with user management) that provides a backend for voting for lunch/restaurants
                                                
                        Authentication and authorization details for trying out secure endpoints are as follows
                                                
                        Admin:
                        * mike@mail.com / adminpass
                        * has all permissions
                                                
                        User:
                        * peter@mail.com / userpass
                        * has permissions profile:read, profile:update, profile:delete, restaurant:vote
                        """,
                contact = @Contact(
                        name = "Mike Pashkov",
                        url = "https://github.com/mikkamax",
                        email = "mikkamax@yandex.ru"
                )
        ),
        servers = @Server(url = "http://localhost:8080")
)
@SecuritySchemes(value = {
        @SecurityScheme(
                name = "admin",
                scheme = "basic",
                type = SecuritySchemeType.HTTP,
                in = SecuritySchemeIn.HEADER
        ),
        @SecurityScheme(
                name = "user",
                scheme = "basic",
                type = SecuritySchemeType.HTTP,
                in = SecuritySchemeIn.HEADER
        )
})
public class OpenApiConfiguration {
}
