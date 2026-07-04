package uz.script.wincrm.security.refreshToken;

import jakarta.persistence.*;
import lombok.*;
import uz.script.wincrm.utils.TableName;


import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = TableName.REFRESH_TOKENS)
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private String username;

    private LocalDateTime expiryDate;
}
