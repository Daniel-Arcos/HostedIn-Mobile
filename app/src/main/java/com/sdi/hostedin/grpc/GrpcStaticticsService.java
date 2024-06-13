package com.sdi.hostedin.grpc;

import com.sdi.hostedin.data.datasource.local.DataStoreAccess;
import com.sdi.hostedin.data.model.AccommodationGrpc;
import com.sdi.hostedin.data.model.EarningGrpc;

import java.util.ArrayList;
import java.util.List;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.examples.BestRatedAccommodation;
import io.grpc.examples.BestRatedAccommodationsResponse;
import io.grpc.examples.Earning;
import io.grpc.examples.EarningsResponse;
import io.grpc.examples.GuestRequest;
import io.grpc.examples.HostRequest;
import io.grpc.examples.MostBookedAccommodation;
import io.grpc.examples.MostBookedAccommodationsResponse;
import io.grpc.examples.MultimediaServiceGrpc;
import io.grpc.examples.StaticticsServiceGrpc;

public class GrpcStaticticsService {

    private final ManagedChannel channel;
    private final StaticticsServiceGrpc.StaticticsServiceBlockingStub stub;

    public GrpcStaticticsService() {
        channel = ManagedChannelBuilder
                .forAddress(GrpcServerData.HOST, GrpcServerData.PORT)
                .usePlaintext()
                .build();
        stub = StaticticsServiceGrpc.newBlockingStub(channel);
    }

    public List<AccommodationGrpc> getMostBookedAccommodations(String token) {
        try {
            GuestRequest guestRequest = GuestRequest.newBuilder().setToken(token).build();
            MostBookedAccommodationsResponse response = stub.getMostBookedAccommodations(guestRequest);

            List<AccommodationGrpc> accommodations = new ArrayList<>();
            for (MostBookedAccommodation accommodation: response.getAccommodationsList()) {
                AccommodationGrpc accommodationGrpc = new AccommodationGrpc();
                accommodationGrpc.setTitle(accommodation.getTitle());
                accommodationGrpc.setBookingsNumber(accommodation.getBookingsNumber());
                accommodations.add(accommodationGrpc);
            }
            channel.shutdown();
            return accommodations;
        } catch (Exception e) {
            channel.shutdown();
            throw e;
        }
    }

    public List<AccommodationGrpc> getBestRatedAccommodations(String token) {
        try {
            GuestRequest guestRequest = GuestRequest.newBuilder().setToken(token).build();
            BestRatedAccommodationsResponse response = stub.getBestRatedAccommodations(guestRequest);
            List<AccommodationGrpc> accommodations = new ArrayList<>();
            for (BestRatedAccommodation accommodation: response.getAccommodationsList()) {
                AccommodationGrpc accommodationGrpc = new AccommodationGrpc();
                accommodationGrpc.setTitle(accommodation.getName());
                accommodationGrpc.setRate(accommodation.getRate());
                accommodations.add(accommodationGrpc);
            }
            channel.shutdown();
            return accommodations;
        } catch (Exception e) {
            channel.shutdown();
            throw e;
        }
    }

    public List<EarningGrpc> getHostEarnings(String token, String idHost) {
        try {
            HostRequest hostRequest = HostRequest.newBuilder()
                    .setToken(token)
                    .setIdHost(idHost)
                    .build();

            EarningsResponse response = stub.getEarnings(hostRequest);
            List<EarningGrpc> earnings = new ArrayList<>();
            for (Earning earning: response.getEarningsList()) {
                EarningGrpc earningGrpc = new EarningGrpc(earning.getMonth(), earning.getEarning());
                earnings.add(earningGrpc);
            }
            channel.shutdown();
            return earnings;
        } catch (Exception e) {
            channel.shutdown();
            throw e;
        }
    }

    public List<AccommodationGrpc> getMostBookedAccommodationsHost(String token, String idHost) {
        try {
            HostRequest hostRequest = HostRequest.newBuilder()
                    .setToken(token)
                    .setIdHost(idHost)
                    .build();

            MostBookedAccommodationsResponse response = stub.getMostBookedAccommodationsOfHost(hostRequest);
            List<AccommodationGrpc> accommodations = new ArrayList<>();
            for (MostBookedAccommodation accommodation: response.getAccommodationsList()) {
                AccommodationGrpc accommodationGrpc = new AccommodationGrpc();
                accommodationGrpc.setTitle(accommodation.getTitle());
                accommodationGrpc.setBookingsNumber(accommodation.getBookingsNumber());
                accommodations.add(accommodationGrpc);
            }
            channel.shutdown();
            return accommodations;
        } catch (Exception e) {
            channel.shutdown();
            throw e;
        }
    }
}
