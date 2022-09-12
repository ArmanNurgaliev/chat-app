package ru.arman.chatapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(
            name = "chat_id",
            referencedColumnName = "chat_id")
    @ToString.Exclude
    @JsonIgnore
    private ChatRoom chatRoom;
    private String senderName;
    private String recipientName;

/*    @ManyToOne(
            cascade = CascadeType.MERGE
    )
    @JoinColumn(
            name = "sender_id",
            referencedColumnName = "id"
    )
    @ToString.Exclude
    private User sender;

    @ManyToOne(
            cascade = CascadeType.MERGE
    )
    @JoinColumn(
            name = "recipient_id",
            referencedColumnName = "id"
    )
    @ToString.Exclude
    private User recipient;*/

    private String content;
    private Date timestamp;
    private MessageStatus status;

}
