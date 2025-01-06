package com.lime.server.bus.dto.general;

import com.lime.server.bus.dto.cityApi.CityCodeDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ApiItems {
    private List<CityCodeDto> item;
}
