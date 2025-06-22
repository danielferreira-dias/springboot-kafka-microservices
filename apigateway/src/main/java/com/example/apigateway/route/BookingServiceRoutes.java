package com.example.apigateway.route;

import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.HandlerFunction;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;


/**
 * Configuration class for routing HTTP requests to the Booking Service.
 * <p>
 * This class defines custom API Gateway routes using Spring's functional routing DSL.
 * It maps external client requests made to <code>/api/v1/booking</code> to the internal
 * booking service hosted at <code>http://localhost:8081/api/v1/booking</code>.
 * </p>
 * <p>
 * The routing is limited to HTTP POST requests, typically used for creating bookings.
 * </p>
 */

@Configuration
public class BookingServiceRoutes {

    /**
     * Defines a route function that intercepts POST requests to "/api/v1/booking" and
     * forwards them to the actual Booking Service URL.
     *
     * @return a {@link RouterFunction} mapping the client-facing booking endpoint to the internal booking service
     */

    @Bean
    public RouterFunction<ServerResponse> bookingRoutes(){
        return GatewayRouterFunctions.route("booking-service")
                .route(RequestPredicates.POST("/api/v1/booking"),
                        HandlerFunctions.http("http://localhost:8081/api/v1/booking"))
                .build();
    }
}
