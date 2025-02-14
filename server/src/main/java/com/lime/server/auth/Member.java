package com.lime.server.auth;

import com.lime.server.subscribe.Subscription;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    private String username;
    private String password;
    private String nickname;
    private String email;

    @OneToMany(mappedBy = "member")
    List<Subscription> subscriptions;

    public static Member of(String username, String password, String nickname, String email) {
        return new Member(null, username, password, nickname, email, new ArrayList<>());
    }
}
