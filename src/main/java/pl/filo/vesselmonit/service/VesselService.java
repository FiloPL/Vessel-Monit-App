package pl.filo.vesselmonit.service;

import org.springframework.stereotype.Service;
import pl.filo.vesselmonit.entity.Vessel;
import pl.filo.vesselmonit.repository.VesselRepository;


import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

@Service
public class VesselService
{
    public static final Map<Integer, String> SHIP_TYPE_MAP = getShipTypeMap();

    private final VesselRepository vesselRepository;

    public VesselService(final VesselRepository vesselRepository)
    {
        this.vesselRepository = vesselRepository;
    }

    public List<Vessel> getVessels()
    {
        return vesselRepository.findAll();
    }

    public Vessel getVesselById(final Integer id)
    {
        return vesselRepository.findById(id).orElse(null);
    }

    private static Map<Integer, String> getShipTypeMap()
    {
        return Map.ofEntries(
                new AbstractMap.SimpleEntry<>(20, "Wing in ground (WIG)"),
                new AbstractMap.SimpleEntry<>(21, "Wing in ground (WIG), Hazardous category A"),
                new AbstractMap.SimpleEntry<>(22, "Wing in ground (WIG), Hazardous category B"),
                new AbstractMap.SimpleEntry<>(23, "Wing in ground (WIG), Hazardous category C"),
                new AbstractMap.SimpleEntry<>(24, "Wing in ground (WIG), Hazardous category D"),
                new AbstractMap.SimpleEntry<>(25, "Wing in ground (WIG)"),
                new AbstractMap.SimpleEntry<>(26, "Wing in ground (WIG)"),
                new AbstractMap.SimpleEntry<>(27, "Wing in ground (WIG)"),
                new AbstractMap.SimpleEntry<>(28, "Wing in ground (WIG)"),
                new AbstractMap.SimpleEntry<>(29, "Wing in ground (WIG)"),
                new AbstractMap.SimpleEntry<>(30, "Fishing"),
                new AbstractMap.SimpleEntry<>(31, "Towing"),
                new AbstractMap.SimpleEntry<>(32, "Towing: length exceeds 200m or breadth exceeds 25m"),
                new AbstractMap.SimpleEntry<>(33, "Dredging or underwater ops"),
                new AbstractMap.SimpleEntry<>(34, "Diving ops"),
                new AbstractMap.SimpleEntry<>(35, "Military ops"),
                new AbstractMap.SimpleEntry<>(36, "Sailing"),
                new AbstractMap.SimpleEntry<>(37, "Pleasure Craft"),
                new AbstractMap.SimpleEntry<>(38, ""),
                new AbstractMap.SimpleEntry<>(39, ""),
                new AbstractMap.SimpleEntry<>(40, "High speed craft (HSC)"),
                new AbstractMap.SimpleEntry<>(41, "High speed craft (HSC), Hazardous category A"),
                new AbstractMap.SimpleEntry<>(42, "High speed craft (HSC), Hazardous category B"),
                new AbstractMap.SimpleEntry<>(43, "High speed craft (HSC), Hazardous category C"),
                new AbstractMap.SimpleEntry<>(44, "High speed craft (HSC), Hazardous category D"),
                new AbstractMap.SimpleEntry<>(45, "High speed craft (HSC)"),
                new AbstractMap.SimpleEntry<>(46, "High speed craft (HSC)"),
                new AbstractMap.SimpleEntry<>(47, "High speed craft (HSC)"),
                new AbstractMap.SimpleEntry<>(48, "High speed craft (HSC)"),
                new AbstractMap.SimpleEntry<>(49, "High speed craft (HSC)"),
                new AbstractMap.SimpleEntry<>(50, "Pilot Vessel"),
                new AbstractMap.SimpleEntry<>(51, "Search and Rescue vessel"),
                new AbstractMap.SimpleEntry<>(52, "Tug"),
                new AbstractMap.SimpleEntry<>(53, "Port Tender"),
                new AbstractMap.SimpleEntry<>(54, "Anti-pollution equipment"),
                new AbstractMap.SimpleEntry<>(55, "Law Enforcement"),
                new AbstractMap.SimpleEntry<>(56, "Spare - Local Vessel"),
                new AbstractMap.SimpleEntry<>(57, "Spare - Local Vessel"),
                new AbstractMap.SimpleEntry<>(58, "Medical Transport"),
                new AbstractMap.SimpleEntry<>(59, "Noncombatant ship according to RR Resolution No. 18"),
                new AbstractMap.SimpleEntry<>(60, "Passenger"),
                new AbstractMap.SimpleEntry<>(61, "Passenger, Hazardous category A"),
                new AbstractMap.SimpleEntry<>(62, "Passenger, Hazardous category B"),
                new AbstractMap.SimpleEntry<>(63, "Passenger, Hazardous category C"),
                new AbstractMap.SimpleEntry<>(64, "Passenger, Hazardous category D"),
                new AbstractMap.SimpleEntry<>(65, "Passenger"),
                new AbstractMap.SimpleEntry<>(66, "Passenger"),
                new AbstractMap.SimpleEntry<>(67, "Passenger"),
                new AbstractMap.SimpleEntry<>(68, "Passenger"),
                new AbstractMap.SimpleEntry<>(69, "Passenger"),
                new AbstractMap.SimpleEntry<>(70, "Cargo"),
                new AbstractMap.SimpleEntry<>(71, "Cargo, Hazardous category A"),
                new AbstractMap.SimpleEntry<>(72, "Cargo, Hazardous category B"),
                new AbstractMap.SimpleEntry<>(73, "Cargo, Hazardous category C"),
                new AbstractMap.SimpleEntry<>(74, "Cargo, Hazardous category D"),
                new AbstractMap.SimpleEntry<>(75, "Cargo"),
                new AbstractMap.SimpleEntry<>(76, "Cargo"),
                new AbstractMap.SimpleEntry<>(77, "Cargo"),
                new AbstractMap.SimpleEntry<>(78, "Cargo"),
                new AbstractMap.SimpleEntry<>(79, "Cargo"),
                new AbstractMap.SimpleEntry<>(80, "Tanker"),
                new AbstractMap.SimpleEntry<>(81, "Tanker, Hazardous category A"),
                new AbstractMap.SimpleEntry<>(82, "Tanker, Hazardous category B"),
                new AbstractMap.SimpleEntry<>(83, "Tanker, Hazardous category C"),
                new AbstractMap.SimpleEntry<>(84, "Tanker, Hazardous category D"),
                new AbstractMap.SimpleEntry<>(85, "Tanker"),
                new AbstractMap.SimpleEntry<>(86, "Tanker"),
                new AbstractMap.SimpleEntry<>(87, "Tanker"),
                new AbstractMap.SimpleEntry<>(88, "Tanker"),
                new AbstractMap.SimpleEntry<>(89, "Tanker"),
                new AbstractMap.SimpleEntry<>(91, "Other Type"),
                new AbstractMap.SimpleEntry<>(92, "Other Type, Hazardous category A"),
                new AbstractMap.SimpleEntry<>(93, "Other Type, Hazardous category B"),
                new AbstractMap.SimpleEntry<>(94, "Other Type, Hazardous category C"),
                new AbstractMap.SimpleEntry<>(95, "Other Type, Hazardous category D"),
                new AbstractMap.SimpleEntry<>(96, "Other Type"),
                new AbstractMap.SimpleEntry<>(97, "Other Type"),
                new AbstractMap.SimpleEntry<>(98, "Other Type"),
                new AbstractMap.SimpleEntry<>(99, "Other Type")
        );
    }

}