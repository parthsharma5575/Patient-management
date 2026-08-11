package com.pm.apigateway.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
//filter class to intercept the request and validate the JWT token
public class JwtValidationGatewayFilterFactory extends
        AbstractGatewayFilterFactory<Object> {
    private final WebClient webClient;

    public JwtValidationGatewayFilterFactory(WebClient.Builder webClientBuilder,
                                             @Value("${auth-service.url}") String authServiceUrl) {

        this.webClient = webClientBuilder.baseUrl(authServiceUrl).build();
    }


    @Override
    public GatewayFilter apply(Object config){
        //exchange -> variable that contains the request and response(current request and response)
        //chain -> variable that contains the next filter in the chain
        return (exchange,chain) -> {
            String token=exchange.getRequest().getHeaders().getFirst("Authorization");
            if(token==null || !token.startsWith("Bearer ")){
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            return webClient.get()
                    .uri("/validate")
                    .header(HttpHeaders.AUTHORIZATION,token)
                    .retrieve()
                    .toBodilessEntity()
                    .then(chain.filter(exchange));
        };
    }

}
