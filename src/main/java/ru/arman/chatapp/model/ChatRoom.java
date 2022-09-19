package ru.arman.chatapp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

/*    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(
            name = "sender_id",
            referencedColumnName = "id")
    @JsonIgnoreProperties("sender")
    private User sender;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(
            name = "recipient_id",
            referencedColumnName = "id")
    @JsonIgnoreProperties("recipient")
    private User recipient;*/

    private String senderName;
    private String recipientName;

    @ManyToMany(mappedBy = "chatRooms", fetch = FetchType.EAGER)
    private List<ChatMessage> chatMessages;

    private Date lastUpdate;

    public void addMessage(ChatMessage chatMessage) {
        this.chatMessages.add(chatMessage);
    }
}
