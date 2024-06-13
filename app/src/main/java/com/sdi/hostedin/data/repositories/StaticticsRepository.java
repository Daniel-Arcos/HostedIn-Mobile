package com.sdi.hostedin.data.repositories;

import com.sdi.hostedin.data.model.AccommodationGrpc;
import com.sdi.hostedin.data.model.EarningGrpc;
import com.sdi.hostedin.grpc.GrpcStaticticsService;

import java.util.List;

public class StaticticsRepository {
    GrpcStaticticsService staticticsService;

    public interface GetAccommodations {
        void onSuccess(List<AccommodationGrpc> accommodations);
        void onError(String message);
    }

    public interface GetEarnings {
        void onSuccess(List<EarningGrpc> earnings);
        void onError(String message);
    }

    public StaticticsRepository() {
        staticticsService = new GrpcStaticticsService();
    }

    public void getMostBookedAccommodations(String token, GetAccommodations callback) {
        try {
            List<AccommodationGrpc> accommodations = staticticsService.getMostBookedAccommodations(token);
            callback.onSuccess(accommodations);
        } catch (Exception e) {
            callback.onError("Ocurrió un problema al obtener las estadísticas de alojamientos más reservados.");
        }
    }

    public void getBestRatedAccommodations(String token, GetAccommodations callback) {
        try {
            List<AccommodationGrpc> accommodations = staticticsService.getBestRatedAccommodations(token);
            callback.onSuccess(accommodations);
        } catch (Exception e) {
            callback.onError("Ocurrió un problema al obtener las estadísticas de alojamientos mejores calificados");
        }
    }

    public void getMostBookedAccommodationsHost(String token, String idHost, GetAccommodations callback) {
        try {
            List<AccommodationGrpc> accommodations = staticticsService.getMostBookedAccommodationsHost(token, idHost);
            callback.onSuccess(accommodations);
        } catch (Exception e) {
            callback.onError("Ocurrio un problema al obtener las estadisticas de alojamientos mas reservados.");
        }
    }

    public void getEarnings(String token, String idHost, GetEarnings callback) {
        try {
            List<EarningGrpc> earnings = staticticsService.getHostEarnings(token, idHost);
            callback.onSuccess(earnings);
        } catch (Exception e) {
            callback.onError("Ocurrio un problema al obtener las estadisticas de ganancias");
        }
    }
}
