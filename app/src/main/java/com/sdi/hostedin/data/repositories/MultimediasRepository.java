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
        void onSuccess(byte[] multimedia);
        void onError(String message);
    }

    public interface UploadAccommodationMultimediaCallback {
        void onSuccess(String message);
        void onError(String message);
    }

    public interface LoadMainImageAccommodationCallback {
        void onSuccess(byte[] image);
        void onError(String message);
    }

    public MultimediasRepository(Executor executor) {
        this.executor = executor;
    }

    public void loadAccommodationMultimedia(String _id, LoadAccommodationMultimediaCallback callback, final Handler handler) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(GrpcServerData.HOST, GrpcServerData.PORT)
                .usePlaintext()
                .build();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //List<byte[]> multimedias = new ArrayList<>();
                    for (int i = 0; i < 4; i++) {
                        byte[] bytes = GrpcAccommodationMultimedia.downloadAccommodationMultimedia(channel, _id ,i );
                        notifySuccessResult(bytes, callback, handler);
                        //multimedias.add(bytes);
                    }
                    channel.shutdown();
                    //callback.onSuccess(multimedias);
                } catch (Exception e) {
                    notifyErrorResult(e.getMessage(), callback, handler);
                }
            }
        });
    }

    public void loadAllAccommodationMultimedia(String _id, LoadAccommodationMultimediaCallback callback, final Handler handler) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(GrpcServerData.HOST, GrpcServerData.PORT)
                .usePlaintext()
                .build();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    List<byte[]> multimedias = new ArrayList<>();
                    for (int i = 0; i < 4; i++) {
                        byte[] bytes = GrpcAccommodationMultimedia.downloadAccommodationMultimedia(channel, _id , i );
                        multimedias.add(bytes);
                    }
                    channel.shutdown();
                    notifySuccessAllMultimediaResult(multimedias, callback, handler);
                } catch (Exception e) {
                    notifyErrorResult(e.getMessage(), callback, handler);
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

    public void uploadAccommodationMultimedia(String accommodationId, byte[][] data, UploadAccommodationMultimediaCallback callback, final Handler handler) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(GrpcServerData.HOST, GrpcServerData.PORT)
                .usePlaintext()
                .build();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    GrpcAccommodationMultimedia grpcClient = new GrpcAccommodationMultimedia();
                    for (byte[] multimediaItem : data) {
                        grpcClient.uploadAccommodationMultimedia(accommodationId, multimediaItem);
                    }
                    channel.shutdown();
                    notifySuccessUpload(callback, handler);
                } catch (Exception e) {
                    notifyErrorUpload(e.getMessage(), callback, handler);
                }
            }
        });
    }

    public void updateAccommodationMultimedia(String accommodationId, byte[][] data, UploadAccommodationMultimediaCallback callback, final Handler handler) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(GrpcServerData.HOST, GrpcServerData.PORT)
                .usePlaintext()
                .build();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    GrpcAccommodationMultimedia grpcClient = new GrpcAccommodationMultimedia();
                    int indexMultimedia = 0;
                    for (byte[] multimediaItem : data) {
                        grpcClient.UpdateAccommodationMultimediaEditionTask(accommodationId, multimediaItem, indexMultimedia);
                        indexMultimedia++;
                    }
                    channel.shutdown();
                    notifySuccessUpload(callback, handler);
                } catch (Exception e) {
                    notifyErrorUpload(e.getMessage(), callback, handler);
                }
            }
        });
    }

    //UploadAccommodationMultimedia
    private void notifySuccessUpload(UploadAccommodationMultimediaCallback callback, Handler handler) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess("uploaded");
            }
        });
    }

    private void notifyErrorUpload(String message, UploadAccommodationMultimediaCallback callback, Handler handler) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                callback.onError("error uploading");
            }
        });
    }

    // LoadMainImageAccommodation
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

    // LoadAccommodationMultimedia
    private void notifySuccessResult(byte[] image, LoadAccommodationMultimediaCallback callback, Handler handler) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(image);
            }
        });
    }

    private void notifySuccessAllMultimediaResult(List<byte[]> image, LoadAccommodationMultimediaCallback callback, Handler handler) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(image);
            }
        });
    }

    private void notifyErrorResult(String message, LoadAccommodationMultimediaCallback callback, Handler handler) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(message);
            }
        });
    }
}
