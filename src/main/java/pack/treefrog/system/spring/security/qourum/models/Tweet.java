package pack.treefrog.system.spring.security.qourum.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity(name= "tweet")
@Table(name= "tweets")
@Getter
@Setter
public class Tweet {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "tweet_id")
    private Long tweetId;

     // Relacionamento de muitos pra 1, um usuario pode ter varios tweets
    @ManyToOne
     // Essa anotação adiciona um "relacão" entre "user" e "user_Id "
    @JoinColumn(name = "user_id")
    private User user;

    private String content;

    @CreationTimestamp
    private Instant creationTimestar;
}
