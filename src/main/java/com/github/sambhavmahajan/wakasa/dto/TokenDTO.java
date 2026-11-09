package com.github.sambhavmahajan.wakasa.dto;

public record TokenDTO(String value, String smtpUsername, String host, Integer port, String password, String fromEmail) { }
