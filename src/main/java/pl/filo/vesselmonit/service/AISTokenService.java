package pl.filo.vesselmonit.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import pl.filo.vesselmonit.entity.AISToken;
import pl.filo.vesselmonit.model.AISTokenData;
import pl.filo.vesselmonit.repository.AISTokenRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AISTokenService {
    @Value("${ais.token.client_id}")
    private String clientId;
    @Value("${ais.token.client_secret}")
    private String clientSecret;

    private final RestTemplate restTemplate;
    private final AISTokenRepository aisTokenRepository;

    public AISTokenService(
            final RestTemplateBuilder restTemplateBuilder,
            final AISTokenRepository aisTokenRepository)
    {
        this.restTemplate = restTemplateBuilder.build();
        this.aisTokenRepository = aisTokenRepository;
    }

    public String getAISToken() {
        final AISToken aisToken = aisTokenRepository.findById(1).orElse(null);
        final List<AISToken> all = aisTokenRepository.findAll();
        if(aisToken == null || LocalDateTime.now().isAfter(aisToken.getExpiresAt())) {
            final AISTokenData tokenFromAPI = getAisTokenFromAPI();
            saveAisToken(tokenFromAPI);
            return tokenFromAPI.getAccessToken();
        }
        return aisToken.getAccessToken();
    }

    private AISTokenData getAisTokenFromAPI() {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        final MultiValueMap<String, String> bodyMap = new LinkedMultiValueMap<>();
        bodyMap.add("grant_type", "client_credentials");
        bodyMap.add("client_id", clientId);
        bodyMap.add("client_secret", clientSecret);
        bodyMap.add("scope", "api");
        final HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(bodyMap, httpHeaders);

        final ResponseEntity<AISTokenData> token = restTemplate.exchange("https://id.barentswatch.no/connect/token",
                HttpMethod.POST,
                httpEntity,
                AISTokenData.class);
        return token.getBody();
    }

    private void saveAisToken(final AISTokenData aisTokenData) {
        final AISToken aisToken = new AISToken(
                1,
                aisTokenData.getAccessToken(),
                LocalDateTime.now().plusSeconds(aisTokenData.getExpiresIn()),
                aisTokenData.getTokenType(),
                aisTokenData.getScope());
        aisTokenRepository.save(aisToken);
    }

}