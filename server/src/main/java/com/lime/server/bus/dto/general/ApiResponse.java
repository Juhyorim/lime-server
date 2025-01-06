package com.lime.server.bus.dto.general;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ApiResponse {
    private ApiHeader header;
    private ApiBody body;
}
