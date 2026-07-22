package com.pm.billingservice.grpc;

import com.pm.billing.BillingRequest;
import com.pm.billing.BillingResponse;
import com.pm.billing.BillingServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class BillingGrpcService extends BillingServiceGrpc.BillingServiceImplBase {
    @Override
    public void createBillingAccount (BillingRequest request, StreamObserver<BillingResponse> responseObserver) {
        responseObserver.onNext(com.pm.billing.BillingResponse.newBuilder().build());
        responseObserver.onCompleted();
    }
}
