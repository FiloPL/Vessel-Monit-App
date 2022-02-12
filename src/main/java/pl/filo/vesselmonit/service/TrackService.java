package pl.filo.vesselmonit.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.filo.vesselmonit.model.Datum;
import pl.filo.vesselmonit.model.Point;
import pl.filo.vesselmonit.model.Track;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TrackService {
    RestTemplate restTemplate = new RestTemplate();

    public List<Point> getTracks() {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6IjBCM0I1NEUyRkQ5OUZCQkY5NzVERDMxNDBDREQ4OEI1QzA5RkFDRjMiLCJ0eXAiOiJhdCtqd3QiLCJ4NXQiOiJDenRVNHYyWi03LVhYZE1VRE4ySXRjQ2ZyUE0ifQ.eyJuYmYiOjE2NDM5Mjg1NTAsImV4cCI6MTY0MzkzMjE1MCwiaXNzIjoiaHR0cHM6Ly9pZC5iYXJlbnRzd2F0Y2gubm8iLCJhdWQiOiJhaXMiLCJjbGllbnRfaWQiOiJkb2dtYWpvYWNoaW1AZ21haWwuY29tOmRvZ21ham9hY2hpbUBnbWFpbC5jb20iLCJzY29wZSI6WyJhaXMiXX0.rrNItUT8dzmwN2gkvolpkikHsJBJZxnM2upsGjL_9Iz0pEVyFRVjwoUfIZeU3hfpDySOZbSEzuZSZVH8aZ_G5BGaqNL78Y12RyMw3gMiGbHw4eHE7MY15EBmvCozSEWlZcPsKg2g3vIi3TeaaCylbJ4Zt9X4EB6foV0cSYg3QNNa6D_fw6CCokpTJ11XWSX2bwC84OYaE_gxdM6y_V3x1Hp5BpEy4Nrrr4Q55boKoZo51u09Ki-u2utezo-vMGCsQFFQnv6k0yVOGgBzNH1Idy5LzW-qBpOlOX3NZfSB8RdcpJbajThGaMcHYjM2Jdi_l_CuGSnLTOcRdGbwP4vYZSdfqtZjQNqG_fxu7dDjT3pUysGazF052EO0s1SrJRDg34FlgTlBVApz1WYhgnqXRqoSBKPjv5WQuKwSZbWBATzfc3RU_SV-sypB34jW5EW0AwKIWEQtBa8fLicNz60l2pnvZdYtFFY_9gwioNHiU4GvdcoxOmBVgNgwUhK3l0t7YBlzNJOuOuZwrxNOeZMsX9_gqNhewAUFmGN-v2GV4tBgqbCCpVs1DxSHBI-IGWuJeLDdujGsdmxo4o7d5GVw2Wx43HjWUX7HPYXHbjxeFRYn58UX5pBbpzjtTycJNmQuZgY8WDYwULcRsAsKGjY5YXIWRcL3zjnEywqg64aiwuQ");
        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        ResponseEntity<Track[]> exchange = restTemplate.exchange("https://www.barentswatch.no/bwapi/v2/geodata/ais/openpositions?Xmin=10.09094&Xmax=10.67047&Ymin=63.3989&Ymax=63.58645",
                HttpMethod.GET,
                httpEntity,
                Track[].class);

        List<Point> collect = Stream.of(exchange.getBody()).map(
                track -> new Point(
                        track.getGeometry().getCoordinates().get(0),
                        track.getGeometry().getCoordinates().get(1),
                        track.getName(),
                        getDestination(track.getDestination(), track.getGeometry().getCoordinates()).getLongitude(),
                        getDestination(track.getDestination(), track.getGeometry().getCoordinates()).getLatitude()
                )
        ).collect(Collectors.toList());
        return collect;
    }

    public Datum getDestination(String destinationName, List<Double> coordinates) {
        try {
            String url = "http://api.positionstack.com/v1/forward?access_key=326a25862db8fb72c2c3ed000277efe6&query=" + destinationName;
            // sp[rytne obejście mapowania do medelu, tylko do JasonNode gdzie wyciąga sie to co potrzeba
            JsonNode data = restTemplate.getForObject(url, JsonNode.class).get("data").get(0);
            double latitude = data.get("latitude").asDouble();
            double longitude = data.get("longitude").asDouble();
            return new Datum(latitude, longitude);

        } catch (Exception ex) {
            return new Datum(coordinates.get(1), coordinates.get(0));
        }
    }
}
