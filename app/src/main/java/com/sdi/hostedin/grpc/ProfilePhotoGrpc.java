package com.sdi.hostedin.grpc;

import android.util.Log;

import com.google.protobuf.ByteString;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.examples.MultimediaServiceGrpc;
import io.grpc.examples.UploadMultimediaResponse;
import io.grpc.examples.UploadProfilePhotoRequest;
import io.grpc.stub.StreamObserver;

public class ProfilePhotoGrpc {
    private static final String TAG = "PRUEBA";
    private final ManagedChannel channel;
    private final MultimediaServiceGrpc.MultimediaServiceStub asyncStub;
    private final ExecutorService executorService;

    public ProfilePhotoGrpc(String host, int port) {
        channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        asyncStub = MultimediaServiceGrpc.newStub(channel);
        executorService = Executors.newSingleThreadExecutor();
    }

    public void uploadProfilePhoto(String userId, byte[] photoData) {
        executorService.submit(new UploadPhotoTask(asyncStub, userId, photoData));
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        executorService.shutdown();
    }

    private static class UploadPhotoTask implements Runnable {
        private final MultimediaServiceGrpc.MultimediaServiceStub asyncStub;
        private final String userId;
        private final byte[] photoData;

        UploadPhotoTask(MultimediaServiceGrpc.MultimediaServiceStub asyncStub, String userId, byte[] photoData) {
            this.asyncStub = asyncStub;
            this.userId = userId;
            this.photoData = photoData;
        }

        @Override
        public void run() {
            StreamObserver<UploadMultimediaResponse> responseObserver = new StreamObserver<UploadMultimediaResponse>() {
                @Override
                public void onNext(UploadMultimediaResponse response) {
                    Log.d(TAG, "Upload successful: " + response.getDescription());
                }

                @Override
                public void onError(Throwable t) {
                    Log.e(TAG, "Upload failed: " + t.getMessage());
                }

                @Override
                public void onCompleted() {
                    Log.d(TAG, "Upload completed");
                }
            };

            StreamObserver<UploadProfilePhotoRequest> requestObserver = asyncStub.uploadPhotoProfile(responseObserver);
            try {
                int bufferSize = 1024;
                byte[] buffer = new byte[bufferSize];
                int offset = 0;

                while (offset < photoData.length) {
                    int length = Math.min(bufferSize, photoData.length - offset);
                    System.arraycopy(photoData, offset, buffer, 0, length);

                    UploadProfilePhotoRequest request = UploadProfilePhotoRequest.newBuilder()
                            .setUserId(userId)
                            .setData(ByteString.copyFrom(buffer, 0, length))
                            .build();

                    requestObserver.onNext(request);
                    offset += length;
                }

                requestObserver.onCompleted();
            } catch (NullPointerException e) {
                Log.e(TAG, "Photo data is null: " + e.getMessage());
                requestObserver.onError(e);
            } catch (ArrayIndexOutOfBoundsException e) {
                Log.e(TAG, "Array index out of bounds: " + e.getMessage());
                requestObserver.onError(e);
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "Illegal argument: " + e.getMessage());
                requestObserver.onError(e);
            } catch (StatusRuntimeException e) {
                Log.e(TAG, "gRPC error: " + e.getStatus().getDescription());
                requestObserver.onError(e);
            } catch (Exception e) {
                Log.e(TAG, "Upload failed: " + e.getMessage());
                requestObserver.onError(e);
            }
        }
    }
}
