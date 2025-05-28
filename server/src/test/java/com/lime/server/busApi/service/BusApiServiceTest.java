package com.lime.server.busApi.service;

import com.lime.server.busApi.dto.BusRouteResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BusApiServiceTest {
    @InjectMocks
    private BusApiService busApiService;

    @Mock
    private BusApiClient busApiClient;

    @Test
    @DisplayName("nodeID가 잘못된 예시")
    void UnknownNodeIdTest() throws URISyntaxException, IOException {
        //given
        String jsonContent = Files.readString(
                Paths.get(getClass().getResource("/busRouteInfo/unknownNodeId.json").toURI())
        );

        Mockito.when(busApiClient.getBusRouteInfoResponse(any(), any())).thenReturn(new StringBuilder(jsonContent));

        //when
        BusRouteResponse busRouteInfo = busApiService.getBusRouteInfo("cityCode", "nodeID");

        //then
        assertEquals(busRouteInfo.getBusStations().size(), 0);
    }
}