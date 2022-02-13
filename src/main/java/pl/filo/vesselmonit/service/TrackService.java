package pl.filo.vesselmonit.service;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestTemplate;

import pl.filo.vesselmonit.entity.Position;
import pl.filo.vesselmonit.entity.Vessel;
import pl.filo.vesselmonit.model.Point;
import pl.filo.vesselmonit.model.TrackData;
import pl.filo.vesselmonit.model.VesselModel;
import pl.filo.vesselmonit.repository.PositionRepository;
import pl.filo.vesselmonit.repository.VesselRepository;
import pl.filo.vesselmonit.service.AISTokenService;
import pl.filo.vesselmonit.service.VesselService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;


@Service
public class TrackService
{
    private static final String VESSEL_LOCATIONS_URL = "https://www.barentswatch.no/bwapi/v2/geodata/ais/openpositions?" +
            "Xmin=10.09094&Xmax=10.67047&Ymin=63.3989&Ymax=63.58645";

    private final RestTemplate restTemplate;
    private final AISTokenService aisTokenService;
    private final VesselRepository vesselRepository;
    private final PositionRepository positionRepository;

    public TrackService(
            final RestTemplateBuilder restTemplateBuilder,
            final AISTokenService aisTokenService,
            final VesselRepository vesselRepository,
            final PositionRepository positionRepository)
    {
        this.restTemplate = restTemplateBuilder.build();
        this.aisTokenService = aisTokenService;
        this.vesselRepository = vesselRepository;
        this.positionRepository = positionRepository;
    }

    public List<Point> getTracks()
    {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + aisTokenService.getAISToken());
        final HttpEntity httpEntity = new HttpEntity(httpHeaders);

        final ResponseEntity<TrackData[]> trackDataExchange = restTemplate.exchange(
                VESSEL_LOCATIONS_URL,
                HttpMethod.GET,
                httpEntity,
                TrackData[].class);
        final List<TrackData> trackData = Arrays.asList(trackDataExchange.getBody());
        saveVessels(trackData);
        savePositions(trackData);
        return getPoints(trackData);
    }

    private void saveVessels(final List<TrackData> trackData)
    {
        final List<Integer> trackDataMmsi = trackData.stream()
                .map(TrackData::getMmsi)
                .collect(toList());
        final List<Integer> savedMmsi = vesselRepository.findAllById(trackDataMmsi)
                .stream()
                .map(Vessel::getMmsi)
                .collect(toList());
        trackDataMmsi.removeAll(savedMmsi);
        final List<Vessel> vessels = trackData.stream()
                .filter(track -> trackDataMmsi.contains(track.getMmsi()))
                .map(track -> new Vessel(
                        track.getMmsi(),
                        track.getName(),
                        track.getImo(),
                        track.getCallsign(),
                        track.getCountry(),
                        track.getShipType(),
                        track.getA(),
                        track.getB(),
                        track.getC(),
                        track.getD()))
                .collect(toList());
        vesselRepository.saveAll(vessels);
    }

    private void savePositions(final List<TrackData> trackData)
    {
        final List<Position> positions = trackData.stream()
                .map(track -> new Position(
                        null,
                        parseToDate(track.getTimeStamp()),
                        track.getGeometry().getCoordinates().get(0),
                        track.getGeometry().getCoordinates().get(1),
                        track.getCog(),
                        track.getSog(),
                        track.getRot(),
                        track.getNavstat(),
                        parseToDate(track.getEta()),
                        track.getDestination(),
                        track.getHeading(),
                        track.getDraught(),
                        track.getIsSurvey(),
                        vesselRepository.getById(track.getMmsi())))
                .collect(toList());
        positionRepository.saveAll(positions);
    }

    private LocalDateTime parseToDate(final String date)
    {
        if(date == null)
        {
            return null;
        }
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(date.replace('T', ' ').replace('Z', ' ').trim(), formatter);
    }

    private List<Point> getPoints(final List<TrackData> trackData)
    {
        return trackData.stream()
                .map(track -> new Point(
                        track.getGeometry().getCoordinates().get(1),
                        track.getGeometry().getCoordinates().get(0),
                        new VesselModel(
                                track.getMmsi(),
                                track.getName(),
                                track.getImo(),
                                track.getCallsign(),
                                track.getCountry(),
                                track.getShipType(),
                                VesselService.SHIP_TYPE_MAP.getOrDefault(track.getShipType(), ""),
                                track.getA(),
                                track.getB(),
                                track.getC(),
                                track.getD())))
                .collect(toList());
    }

}
