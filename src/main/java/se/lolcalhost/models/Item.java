package se.lolcalhost.models;

import javax.persistence.*;
import java.util.List;

/**
 * Created by jonmar on 2015-10-12.
 */
@Entity(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;
    public String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "item")
    public List<Vote> votes;
}
