package ru.arman.chatapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long chat_id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(
            name = "sender_id",
            referencedColumnName = "id")
    private User sender;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(
            name = "recipient_id",
            referencedColumnName = "id")
    private User recipient;

  /*  @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessage> chatMessages;*/

    private Date lastUpdate;
}
