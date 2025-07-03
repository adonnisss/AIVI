package com.evplatform.web;

import com.evplatform.service.interfaces.ChargingStationServiceInterface;
import com.evplatform.service.interfaces.ProviderServiceInterface;
import com.evplatform.vao.ChargingStation;
import com.evplatform.vao.Provider;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/stations") // Pot do vira bo /api/stations
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ChargingStationResource {

    @EJB
    private ChargingStationServiceInterface stationService;

    @EJB
    private ProviderServiceInterface providerService; // Potrebujemo za povezavo postaje s ponudnikom


    @GET
    public List<ChargingStation> getAllStations() {
        return stationService.getAllChargingStations();
    }


    @POST
    public Response addStation(ChargingStation station) {
        try {

            if (station.getProviderId() > 0) {
                Provider provider = providerService.getProviderById(station.getProviderId());
                if (provider == null) {
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity("Provider with ID " + station.getProviderId() + " not found.")
                            .build();
                }
                station.setProvider(provider);
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Field 'providerId' is required.")
                        .build();
            }

            stationService.addChargingStation(station);
            return Response.status(Response.Status.CREATED).entity(station).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}