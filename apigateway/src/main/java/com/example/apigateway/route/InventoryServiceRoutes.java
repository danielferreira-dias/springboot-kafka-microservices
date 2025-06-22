package com.example.apigateway.route;

import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;


/**
 * Configuration class for routing HTTP requests to the Inventory Service.
 *
 * <p>This class defines custom API Gateway routes using Spring's functional routing style.
 * It maps specific inventory-related endpoints to their corresponding internal service URLs.
 * Routing is based on path variables such as {@code venueId} and {@code eventId}.</p>
 *
 * <p>Each route is dynamically resolved by extracting the path variable from the request URL
 * and appending it to the internal base URL for the inventory service.</p>
 *
 * <p>Example:
 * <ul>
 *   <li>GET {@code /api/v1/inventory/venue/10} → {@code http://localhost:8080/api/v1/inventory/venue/10}</li>
 *   <li>GET {@code /api/v1/inventory/event/5} → {@code http://localhost:8080/api/v1/inventory/event/5}</li>
 * </ul>
 * </p>
 */

@Configuration
public class InventoryServiceRoutes {

    /**
     * Defines routing rules for the Inventory Service.
     *
     * <p>Routes:
     * <ul>
     *   <li>{@code /api/v1/inventory/venue/{venueId}} → forwards to internal venue endpoint</li>
     *   <li>{@code /api/v1/inventory/event/{eventId}} → forwards to internal event endpoint</li>
     * </ul>
     * </p>
     *
     * @return a {@link RouterFunction} that handles forwarding of inventory-related requests.
     */

    @Bean
    public RouterFunction<ServerResponse> inventoryRoutes() {
        return GatewayRouterFunctions.route("inventory-service")
                .route(RequestPredicates.path("/api/v1/inventory/venue/{venueId}"),
                        request -> forwardWithPathVariable(request, "venueId",
                                "http://localhost:8080/api/v1/inventory/venue/"))

                .route(RequestPredicates.path("/api/v1/inventory/event/{eventId}"),
                        request -> forwardWithPathVariable(request, "eventId",
                                "http://localhost:8080/api/v1/inventory/event/"))
                .build();

    }

    /**
     * Utility method that extracts a path variable from the request and forwards the request
     * to the target URL with the path variable value appended.
     *
     * @param request      the incoming {@link ServerRequest}
     * @param pathVariable the name of the path variable to extract
     * @param baseUrl      the base URL to forward the request to
     * @return a {@link ServerResponse} containing the forwarded response
     * @throws Exception if forwarding fails or path variable is missing
     */
    private static ServerResponse forwardWithPathVariable(ServerRequest request,
                                                          String pathVariable,
                                                          String baseUrl) throws Exception {
        String value = request.pathVariable(pathVariable);
        return HandlerFunctions.http(baseUrl + value).handle(request);
    }
}
