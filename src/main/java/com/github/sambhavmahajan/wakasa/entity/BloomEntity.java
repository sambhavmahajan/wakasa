package com.github.sambhavmahajan.wakasa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Setter@Getter
public class BloomEntity {
    @Id
    private Integer id;
    @Version
    private Integer bloomVersion;
    @Lob
    private byte[] data;
}
