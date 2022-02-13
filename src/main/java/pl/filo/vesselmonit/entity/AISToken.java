package pl.filo.vesselmonit.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "ais_token")
public class AISToken
{
    @Id
    private Integer id;
    @Column(name = "access_token", length = 2048)
    private String accessToken;
    @Column(name = "expires_at")
    private LocalDateTime expiresAt;
    @Column(name = "token_type")
    private String tokenType;
    private String scope;

}