package uz.script.wincrm.sms;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "eskiz_settings")
@Getter
@Setter
public class EskizSettings {

    /** Yagona qator (singleton sozlama), id doim 1L. */
    @Id
    private Long id = 1L;

    private String email;

    @Column(name = "encrypted_password", length = 1000)
    private String encryptedPassword;

    @Column(length = 1000)
    private String token;

    private LocalDateTime tokenExpiresAt;

    private LocalDateTime updatedAt;

    private Long updatedBy;
}