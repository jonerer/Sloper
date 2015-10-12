package se.lolcalhost.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by jonmar on 2015-10-12.
 */
@Entity(name = "votes")
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    public float value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotNull
    public User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    @NotNull
    public Item item;

    public Vote() {}

    public Vote(User user, Item item, float voteValue) {
        this.user = user;
        this.item = item;
        this.value = voteValue;
    }
}
