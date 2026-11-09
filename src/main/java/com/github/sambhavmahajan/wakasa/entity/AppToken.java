package com.github.sambhavmahajan.wakasa.entity;

import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.io.Serializable;
import java.util.Random;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(indexes = @Index(columnList = "token_value"))
@AllArgsConstructor
public class AppToken implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false, name="token_value")
    private String value = "";
    @OneToOne(fetch=FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    private AppUser owner;
    @Column(nullable = false)
    private String smtpUsername="";
    @Column(nullable = false)
    private String host="";
    @Column(nullable = false)
    private Integer port = 25;
    @Column(nullable = false)
    private String password = "";
    @Column(nullable = false)
    private String fromEmail = "";
    @Version
    @Column(columnDefinition="integer")
    private Integer version = null;
    public void setNewValue() {
        Random random = new Random();
        this.value = DigestUtils.sha256Hex(owner.getUsername() + random.nextLong());
    }
}
