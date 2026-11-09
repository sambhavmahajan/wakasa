package com.github.sambhavmahajan.wakasa.payload;

public record EmailPayload(String key, String to, String subject, String message) {
}
