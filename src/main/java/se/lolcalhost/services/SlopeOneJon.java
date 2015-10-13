package se.lolcalhost.services;

import se.lolcalhost.models.Item;
import se.lolcalhost.models.Vote;

import java.util.HashMap;
import java.util.List;

/**
 * Created by jonm on 13/10/15.
 */
public class SlopeOneJon {

    HashMap<Item, HashMap<Item, Float>> diffs = new HashMap<>();
    HashMap<Item, HashMap<Item, Integer>> freqs = new HashMap<>();

    public void update(List<Vote> votes) {
        for (Vote vote1: votes) {
            Item from = vote1.item;
            if (!diffs.containsKey(from)) {
                diffs.put(from, new HashMap<>());
            }
            if (!freqs.containsKey(from)) {
                freqs.put(from, new HashMap<>());
            }
            HashMap<Item, Float> fromdiff = diffs.get(from);
            HashMap<Item, Integer> fromfreq = freqs.get(from);
            for (Vote vote2: votes) {
                Item to = vote2.item;
                if (!fromdiff.containsKey(to)) {
                    fromdiff.put(to, new Float(0.0f));
                }
                if (!fromfreq.containsKey(to)) {
                    fromfreq.put(to, new Integer(0));
                }
                fromdiff.put(to, fromdiff.get(to) + (vote1.value - vote2.value));
                fromfreq.put(to, fromfreq.get(to) + 1);
            }
        }
        // stämmer det här verkligen? blir många divisioner...
        for (Item item : diffs.keySet()) {
            HashMap<Item, Float> itemFloatHashMap = diffs.get(item);
            HashMap<Item, Integer> itemIntegerHashMap = freqs.get(item);
//            it
        }


    }
}
