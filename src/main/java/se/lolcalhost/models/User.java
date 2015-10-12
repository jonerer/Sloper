package se.lolcalhost.models;

import javax.persistence.*;
import java.util.List;

/**
 * Created by jonmar on 2015-10-12.
 */
@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;
    public String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Vote> votes;

    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }
}
