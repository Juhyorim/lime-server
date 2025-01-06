package com.lime.server.bus.dto.general;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ApiHeader {
    private String resultCode;
    private String resultMsg;
}
