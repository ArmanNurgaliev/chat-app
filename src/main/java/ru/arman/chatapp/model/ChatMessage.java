package ru.arman.chatapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(name = "chat_messages_rooms",
            joinColumns = @JoinColumn(name = "message_id",
                    referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "chat_id",
                    referencedColumnName = "chat_id"))
    @JsonIgnore
    private List<ChatRoom> chatRooms;

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


    public void addRooms(List<ChatRoom> rooms) {
        if (chatRooms == null)
            chatRooms = new ArrayList<>();
        chatRooms.addAll(rooms);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ChatMessage that = (ChatMessage) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
