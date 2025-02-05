package org.example.feedback.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inbox {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String topic;
    private String signature;
    private LocalDateTime expirationDate;
    private boolean allowAnonymous;

    @OneToMany(mappedBy = "inbox", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Message> messages = new ArrayList<>();
}
