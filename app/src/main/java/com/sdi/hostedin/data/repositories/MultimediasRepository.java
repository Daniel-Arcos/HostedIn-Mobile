package com.sdi.hostedin.data.repositories;

import android.os.Handler;

import com.sdi.hostedin.grpc.GrpcAccommodationMultimedia;
import com.sdi.hostedin.grpc.GrpcServerData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class MultimediasRepository {

    private final Executor executor;

    public interface LoadAccommodationMultimediaCallback {
        void onSuccess(List<byte[]> multimedias);
        void onError(String message);
    }

    public interface LoadMainImageAccommodationCallback {
        void onSuccess(byte[] image);
        void onError(String message);
    }

    public MultimediasRepository(Executor executor) {
        this.executor = executor;
    }

    public void loadAccommodationMultimedia(String _id, LoadAccommodationMultimediaCallback callback) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(GrpcServerData.HOST, GrpcServerData.PORT)
                .usePlaintext()
                .build();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    List<byte[]> multimedias = new ArrayList<>();
                    for (int i = 0; i < 4; i++) {
                        byte[] bytes = GrpcAccommodationMultimedia.downloadAccommodationMultimedia(channel, _id ,i );
                        multimedias.add(bytes);
                    }
                    channel.shutdown();
                    callback.onSuccess(multimedias);
                } catch (Exception e) {
                    callback.onError(e.getMessage());
                }
            }
        });
    }

    public void loadMainImageAccommodation(String _id, LoadMainImageAccommodationCallback callback, final Handler handler) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(GrpcServerData.HOST, GrpcServerData.PORT)
                .usePlaintext()
                .build();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    byte[] image = GrpcAccommodationMultimedia.downloadAccommodationMultimedia(channel, _id, 0);
                    channel.shutdown();
                    notifySuccessResult(image, callback, handler);
                } catch (Exception e) {
                    notifyErrorResult(e.getMessage(), callback, handler);
                }
            }
        });
    }

    private void notifySuccessResult(byte[] image, LoadMainImageAccommodationCallback callback, Handler handler) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(image);
            }
        });
    }

    private void notifyErrorResult(String message, LoadMainImageAccommodationCallback callback, Handler handler) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(message);
            }
        });
    }
}
