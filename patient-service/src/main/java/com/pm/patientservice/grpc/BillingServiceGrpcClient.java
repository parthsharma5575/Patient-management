package com.pm.patientservice.grpc;

import com.pm.billing.BillingRequest;
import com.pm.billing.BillingResponse;
import com.pm.billing.BillingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BillingServiceGrpcClient {
    private static final Logger log = LoggerFactory.getLogger(BillingServiceGrpcClient.class);
    private final BillingServiceGrpc.BillingServiceBlockingStub billingServiceBlockingStub;

    public BillingServiceGrpcClient(
            @Value("${billing.service.address:localhost}")String serverAddress,
            @Value("${billing.service.grpc.port:9001}")int serverPort
    ){
        log.info("Billing Service Address: {}:{}",serverAddress,serverPort);
        ManagedChannel channel= ManagedChannelBuilder.forAddress(serverAddress,serverPort).usePlaintext().build();
        billingServiceBlockingStub=BillingServiceGrpc.newBlockingStub(channel);
    }

    public void createBillingAccount(String patientId, String name, String email) {

        log.info("Calling Billing Service");

        BillingRequest billingRequest =
                BillingRequest.newBuilder()
                        .setPatientId(patientId)
                        .setName(name)
                        .setEmail(email)
                        .build();

        BillingResponse response =
                billingServiceBlockingStub.createBillingAccount(billingRequest);

        log.info("Received response {}", response);
    }


}
