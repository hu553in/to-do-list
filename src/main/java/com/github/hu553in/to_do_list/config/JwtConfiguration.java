package com.github.hu553in.to_do_list.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Component
@ConfigurationProperties(prefix = "jwt")
@Setter
public class JwtConfiguration {

    @Getter
    private String tokenIssuer;

    private String tokenSigningKey;
    private Integer tokenDuration;

    public byte[] getTokenSigningKey() {
        return tokenSigningKey.getBytes(StandardCharsets.UTF_8);
    }

    public Duration getTokenExpiredIn() {
        return Duration.ofMinutes(tokenDuration);
    }

}
