package com.sdi.hostedin.grpc;

import android.util.Log;

import com.google.protobuf.ByteString;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.examples.DownloadMultimediaRequest;
import io.grpc.examples.MultimediaServiceGrpc;
import io.grpc.examples.UploadMultimediaRequest;
import io.grpc.examples.UploadMultimediaResponse;
import io.grpc.stub.StreamObserver;

public class GrpcAccommodationMultimedia {
    private static final String TAG = "PRUEBA";
    private final ManagedChannel channel;
    private final MultimediaServiceGrpc.MultimediaServiceStub asyncStub;
    private final ExecutorService executorService;

    public GrpcAccommodationMultimedia() {
        channel = ManagedChannelBuilder
                .forAddress(GrpcServerData.HOST, GrpcServerData.PORT)
                .usePlaintext()
                .build();
        asyncStub = MultimediaServiceGrpc.newStub(channel);
        executorService = Executors.newSingleThreadExecutor();
    }

    public void uploadAccommodationMultimedia(String accommodationId, byte[] data) {
        executorService.submit(new UpdateAccommodationMultimediaTask(asyncStub, accommodationId, data));
    }

    public void shutdown() throws InterruptedException {
        try {
            channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Log.e(TAG, "Channel shutdown interrupted: " + e.getMessage());
        } finally {
            executorService.shutdown();
        }
    }

    public static class UpdateAccommodationMultimediaTask implements Runnable {
        private final MultimediaServiceGrpc.MultimediaServiceStub asyncStub;
        private final String accommodationId;
        private final byte[] data;

        UpdateAccommodationMultimediaTask(MultimediaServiceGrpc.MultimediaServiceStub asyncStub, String accommodationId, byte[] data) {
            this.asyncStub = asyncStub;
            this.accommodationId = accommodationId;
            this.data = data;
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

            StreamObserver<UploadMultimediaRequest> requestObserver = asyncStub.uploadAccommodationMultimedia(responseObserver);
            try {
                int bufferSize = 1024;
                byte[] buffer = new byte[bufferSize];
                int offset = 0;

                while (offset < data.length) {
                    int length = Math.min(bufferSize, data.length - offset);
                    System.arraycopy(data, offset, buffer, 0, length);

                    UploadMultimediaRequest request = UploadMultimediaRequest.newBuilder()
                            .setModelId(accommodationId)
                            .setData(ByteString.copyFrom(buffer, 0, length))
                            .build();

                    requestObserver.onNext(request);
                    offset += length;
                }

                requestObserver.onCompleted();
            } catch (NullPointerException e) {
                Log.e(TAG, "Accommodation data is null: " + e.getMessage());
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

    public static byte[] downloadAccommodationMultimedia(ManagedChannel ch, String accommodationId, int multimediaIndex) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        MultimediaServiceGrpc.MultimediaServiceBlockingStub stub = MultimediaServiceGrpc.newBlockingStub(ch);

        DownloadMultimediaRequest request = DownloadMultimediaRequest.newBuilder()
                .setModelId(accommodationId)
                .setMultimediaIndex(multimediaIndex)
                .build();

        stub.downloadAccommodationMultimedia(request).forEachRemaining(response -> {
            try {
                byte[] data = response.getData().toByteArray();
                stream.write(data);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        System.out.println("\nRecepcion de datos correcta.\n\n");

        try {
            stream.close();
        } catch (IOException e) {
            System.out.println("error on closing stream: ");
            e.printStackTrace();
        }
        return (stream.toByteArray());
    }

//    public Future<byte[]> downloadAccommodationMultimedia(String accommodationId, int multimediaIndex) {
//        return executorService.submit(new RecoverAccommodationMultimediaTask(asyncStub, accommodationId, multimediaIndex));
//    }
//
//    public static class RecoverAccommodationMultimediaTask implements Callable {
//        private final MultimediaServiceGrpc.MultimediaServiceStub asyncStub;
//        private final String accommodationId;
//        private int multimediaIndex;
//
//        RecoverAccommodationMultimediaTask(MultimediaServiceGrpc.MultimediaServiceStub asyncStub, String accommodationId, int multimediaIndex) {
//            this.asyncStub = asyncStub;
//            this.accommodationId = accommodationId;
//            this.multimediaIndex = multimediaIndex;
//        }
//
//        @Override
//        public byte[] call() throws Exception {
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//
//            DownloadMultimediaRequest request = DownloadMultimediaRequest.newBuilder()
//                    .setModelId(accommodationId)
//                    .setMultimediaIndex(multimediaIndex)
//                    .build();
//
//            StreamObserver<DownloadMultimediaResponse> responseObserver = new StreamObserver<DownloadMultimediaResponse>() {
//                @Override
//                public void onNext(DownloadMultimediaResponse response) {
//                    try {
//                        byte[] data = response.getData().toByteArray();
//                        stream.write(data);
//                    } catch (Exception ex) {
//                        Log.e(TAG, "Error processing download response: " + ex.getMessage());
//                    }
//                }
//
//                @Override
//                public void onError(Throwable t) {
//                    Log.e(TAG, "Download failed: " + t.getMessage());
//                }
//
//                @Override
//                public void onCompleted() {
//                    Log.d(TAG, "Download completed, data size: " + stream.size());
//                }
//            };
//
//            try {
//                asyncStub.downloadAccommodationMultimedia(request, responseObserver);
//                while (stream.size() == 0) {
//                    Thread.sleep(100); // wait for data to be received
//                }
//            } catch (Exception e) {
//                Log.e(TAG, "Download failed: " + e.getMessage());
//            }
//
//            return stream.toByteArray();
//        }
//    }
}

