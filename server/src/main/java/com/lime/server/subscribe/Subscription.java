package com.lime.server.subscribe;

import com.lime.server.auth.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;

    private int cityCode;

    private String nodeId;
    private String nodeNo;
    private String nodeName;
    private String routeId;

    public static Subscription of(Member member, int cityCode, String nodeId, String nodeNo, String nodeName, String routeId) {
        Subscription subscription = new Subscription(null, member, cityCode, nodeId, nodeNo, nodeName, routeId);

        return subscription;
    }
}
