package com.lime.server.mongoTest.controller;

import com.lime.server.mongoTest.TestArriveInfo.TestArriveInfo;
import com.lime.server.mongoTest.dto.GetTimeResponse;
import com.lime.server.mongoTest.repository.TestArriveRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("${api_prefix}/mongo-test")
@RestController
public class TimeTestController {
    private final TestArriveRepository testArriveInfoRepository;

    @GetMapping("/serverTime")
    public ResponseEntity<GetTimeResponse> getServerTIme() {
        LocalDateTime now = LocalDateTime.now();
        return ResponseEntity.ok(new GetTimeResponse(null, now, now.toString()));
    }

    @PostMapping("/data")
    public ResponseEntity<GetTimeResponse> addData() {
        LocalDateTime now = LocalDateTime.now();
        TestArriveInfo entity = testArriveInfoRepository.save(TestArriveInfo.of(now));

        return ResponseEntity.ok(new GetTimeResponse(entity.getId(), now, now.toString()));
    }

    @GetMapping("/data")
    public ResponseEntity<GetTimeResponse> getData(@RequestParam(name = "id") String id) {
        TestArriveInfo testArriveInfo = testArriveInfoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("찾을 수  아이디"));

        return ResponseEntity.ok(new GetTimeResponse(testArriveInfo.getId(), testArriveInfo.getTime(),
                testArriveInfo.getTimeString()));
    }
}
