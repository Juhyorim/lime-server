package com.lime.server.mongoTest.TestArriveInfo;

import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "test_arrive_info")
public class TestArriveInfo {
    @Id
    private String id;

    private LocalDateTime time;
    private String timeString;

    public static TestArriveInfo of(LocalDateTime current) {
        TestArriveInfo info = new TestArriveInfo();
        info.time = current;
        info.timeString = current.toString();

        return info;
    }
}
