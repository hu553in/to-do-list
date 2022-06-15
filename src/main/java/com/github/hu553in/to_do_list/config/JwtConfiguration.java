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
    private String issuer;

    private String signingKey;
    private Integer durationMinutes;

    public byte[] getSigningKey() {
        return signingKey.getBytes(StandardCharsets.UTF_8);
    }

    public Duration getExpiresIn() {
        return Duration.ofMinutes(durationMinutes);
    }

}
