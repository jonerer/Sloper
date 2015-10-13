package se.lolcalhost.services;

import java.util.*;

/**
 * Daniel Lemire
 * A simple implementation of the weighted slope one
 * algorithm in Java for item-based collaborative
 * filtering.
 * Assumes Java 1.5.
 * <p/>
 * See main function for example.
 * <p/>
 * June 1st 2006.
 * Revised by Marco Ponzi on March 29th 2007
 */

public class SlopeOne {

    public static void main(String args[]) {
        // this is my data base
        Map<UserId, Map<ItemId, Float>> data = new HashMap<UserId, Map<ItemId, Float>>();
        // items
        ItemId BlåKängor = new ItemId("         Blå Kängor");
        ItemId RödaKängor = new ItemId("        Röda kängor");
        ItemId GrönaKängor = new ItemId("       Gröna kängor");
        ItemId BlåSneakers = new ItemId("       Blå sneakers");
        ItemId SneakersMedFlames = new ItemId("Sneakers med flames");
        ItemId RödaSneakers = new ItemId("      Röda sneakers");
        ItemId RödaKlackskor = new ItemId("     Röda klackskor");
        ItemId BlåKlackskor = new ItemId("      Blå klackskor");
        ItemId GrönaKlackskor = new ItemId("    Gröna klackskor");

        mAllItems = new ItemId[]{BlåKängor, RödaKängor, GrönaKängor,
                BlåSneakers, RödaSneakers, RödaKlackskor, BlåKlackskor,
                GrönaKlackskor, SneakersMedFlames};

        //I'm going to fill it in
        HashMap<ItemId, Float> VanligAnvändare = new HashMap<ItemId, Float>();
        HashMap<ItemId, Float> RödaSkor = new HashMap<ItemId, Float>();
        HashMap<ItemId, Float> KlackGillarn = new HashMap<ItemId, Float>();
        HashMap<ItemId, Float> Skogsvandrarn = new HashMap<ItemId, Float>();

        VanligAnvändare.put(BlåKängor, 2.0f);
        VanligAnvändare.put(RödaSneakers, 4.0f);
        VanligAnvändare.put(BlåSneakers, 4.5f);
        VanligAnvändare.put(SneakersMedFlames, 5.0f);
        VanligAnvändare.put(RödaKlackskor, 0.5f);
        VanligAnvändare.put(GrönaKlackskor, 1.0f);

        data.put(new UserId("VanligAnvändare"), VanligAnvändare);

        RödaSkor.put(BlåKängor, 1.0f);
        RödaSkor.put(RödaKängor, 4.5f);
        RödaSkor.put(GrönaKängor, 0.2f);
        RödaSkor.put(BlåSneakers, 2.0f);
        RödaSkor.put(SneakersMedFlames, 4.5f);
        RödaSkor.put(RödaSneakers, 4.2f);
        RödaSkor.put(RödaKlackskor, 5.0f);
        RödaSkor.put(GrönaKlackskor, 1.0f);
        data.put(new UserId("RödaSkor"), RödaSkor);

        KlackGillarn.put(RödaSneakers, 0.9f);
        KlackGillarn.put(BlåKängor, 0.9f);
        KlackGillarn.put(GrönaKängor, 0.9f);
        KlackGillarn.put(RödaKängor, 0.3f);
        KlackGillarn.put(RödaKlackskor, 4.4f);
        KlackGillarn.put(BlåKlackskor, 4.5f);
        KlackGillarn.put(GrönaKlackskor, 4.1f);
        data.put(new UserId("KlackGillarn"), KlackGillarn);

        Skogsvandrarn.put(BlåKängor, 5.0f);
        Skogsvandrarn.put(GrönaKängor, 4.1f);
        Skogsvandrarn.put(RödaKängor, 4.3f);
        Skogsvandrarn.put(RödaSneakers, 1.1f);
        Skogsvandrarn.put(GrönaKlackskor, 1.0f);
        Skogsvandrarn.put(SneakersMedFlames, 0.4f);
        data.put(new UserId("Skogsvandrarn"), Skogsvandrarn);


        // next, I create my predictor engine
        SlopeOne so = new SlopeOne(data);
        System.out.println("Here's the data I have accumulated...");
        so.printData();

        HashMap<ItemId, Float> user = new HashMap<ItemId, Float>();
        System.out.println("Ok, now we predict...");
        // RÖSTER
        user.put(RödaSneakers, 5.0f);
        user.put(BlåKlackskor, 1f);

        System.out.println("Inputting...");
        SlopeOne.print(user);
        System.out.println("Getting...");
        Map<ItemId, Float> predictions = so.predict(user);
        SlopeOne.print(predictions);


        // förberett:
//        user.put(BlåKlackskor, 1f);
//        user.put(BlåSneakers, 2.1f);

    }

    Map<UserId, Map<ItemId, Float>> mData;
    Map<ItemId, Map<ItemId, Float>> mDiffMatrix;
    Map<ItemId, Map<ItemId, Integer>> mFreqMatrix;

    static ItemId[] mAllItems;

    public SlopeOne(Map<UserId, Map<ItemId, Float>> data) {
        mData = data;
        buildDiffMatrix();
    }

    /**
     * Based on existing data, and using weights,
     * try to predict all missing ratings.
     * The trick to make this more scalable is to consider
     * only mDiffMatrix entries having a large  (>1) mFreqMatrix
     * entry.
     * <p/>
     * It will output the prediction 0 when no prediction is possible.
     */
    public Map<ItemId, Float> predict(Map<ItemId, Float> user) {
        HashMap<ItemId, Float> predictions = new HashMap<ItemId, Float>();
        HashMap<ItemId, Integer> frequencies = new HashMap<ItemId, Integer>();
        for (ItemId j : mDiffMatrix.keySet()) {
            frequencies.put(j, 0);
            predictions.put(j, 0.0f);
        }
        for (ItemId j : user.keySet()) {
            for (ItemId k : mDiffMatrix.keySet()) {
                try {
                    float newval = (mDiffMatrix.get(k).get(j).floatValue() + user.get(j).floatValue()) * mFreqMatrix.get(k).get(j).intValue();
                    predictions.put(k, predictions.get(k) + newval);
                    frequencies.put(k, frequencies.get(k) + mFreqMatrix.get(k).get(j).intValue());
                } catch (NullPointerException e) {
                }
            }
        }
        HashMap<ItemId, Float> cleanpredictions = new HashMap<ItemId, Float>();
        for (ItemId j : predictions.keySet()) {
            if (frequencies.get(j) > 0) {
                cleanpredictions.put(j, predictions.get(j).floatValue() / frequencies.get(j).intValue());
            }
        }
        for (ItemId j : user.keySet()) {
            cleanpredictions.put(j, user.get(j));
        }
        return cleanpredictions;
    }

    /**
     * Based on existing data, and not using weights,
     * try to predict all missing ratings.
     * The trick to make this more scalable is to consider
     * only mDiffMatrix entries having a large  (>1) mFreqMatrix
     * entry.
     */
    public Map<ItemId, Float> weightlesspredict(Map<ItemId, Float> user) {
        HashMap<ItemId, Float> predictions = new HashMap<ItemId, Float>();
        HashMap<ItemId, Integer> frequencies = new HashMap<ItemId, Integer>();
        for (ItemId j : mDiffMatrix.keySet()) {
            predictions.put(j, 0.0f);
            frequencies.put(j, 0);
        }
        for (ItemId j : user.keySet()) {
            for (ItemId k : mDiffMatrix.keySet()) {
                //System.out.println("Average diff between "+j+" and "+ k + " is "+mDiffMatrix.get(k).get(j).floatValue()+" with n = "+mFreqMatrix.get(k).get(j).floatValue());
                float newval = (mDiffMatrix.get(k).get(j).floatValue() + user.get(j).floatValue());
                predictions.put(k, predictions.get(k) + newval);
            }
        }
        for (ItemId j : predictions.keySet()) {
            predictions.put(j, predictions.get(j).floatValue() / user.size());
        }
        for (ItemId j : user.keySet()) {
            predictions.put(j, user.get(j));
        }
        return predictions;
    }


    public void printData() {
        for (UserId user : mData.keySet()) {
            System.out.println(user);
            print(mData.get(user));
        }
        for (int i = 0; i < mAllItems.length; i++) {
            System.out.print("\n" + mAllItems[i] + ":");
            printMatrixes(mDiffMatrix.get(mAllItems[i]), mFreqMatrix.get(mAllItems[i]));
        }
    }

    private void printMatrixes(Map<ItemId, Float> ratings,
                               Map<ItemId, Integer> frequencies) {
        for (int j = 0; j < mAllItems.length; j++) {
            System.out.format("%10.3f", ratings.get(mAllItems[j]));
            System.out.print(" ");
            System.out.format("%10d", frequencies.get(mAllItems[j]));
        }
        System.out.println();
    }

    public static void print(Map<ItemId, Float> user) {
        Map<ItemId, Float> itemIdFloatMap = MapUtil.sortByValue(user);
        for (ItemId j : itemIdFloatMap.keySet()) {
            System.out.println(" " + j + " --> " + user.get(j).floatValue());
        }
    }

    public void buildDiffMatrix() {
        mDiffMatrix = new HashMap<ItemId, Map<ItemId, Float>>();
        mFreqMatrix = new HashMap<ItemId, Map<ItemId, Integer>>();
        // first iterate through users
        for (Map<ItemId, Float> user : mData.values()) {
            // then iterate through user data
            for (Map.Entry<ItemId, Float> entry : user.entrySet()) {
                if (!mDiffMatrix.containsKey(entry.getKey())) {
                    mDiffMatrix.put(entry.getKey(), new HashMap<ItemId, Float>());
                    mFreqMatrix.put(entry.getKey(), new HashMap<ItemId, Integer>());
                }
                for (Map.Entry<ItemId, Float> entry2 : user.entrySet()) {
                    int oldcount = 0;
                    if (mFreqMatrix.get(entry.getKey()).containsKey(entry2.getKey()))
                        oldcount = mFreqMatrix.get(entry.getKey()).get(entry2.getKey()).intValue();
                    float olddiff = 0.0f;
                    if (mDiffMatrix.get(entry.getKey()).containsKey(entry2.getKey()))
                        olddiff = mDiffMatrix.get(entry.getKey()).get(entry2.getKey()).floatValue();
                    float observeddiff = entry.getValue() - entry2.getValue();
                    mFreqMatrix.get(entry.getKey()).put(entry2.getKey(), oldcount + 1);
                    mDiffMatrix.get(entry.getKey()).put(entry2.getKey(), olddiff + observeddiff);
                }
            }
        }
        for (ItemId j : mDiffMatrix.keySet()) {
            for (ItemId i : mDiffMatrix.get(j).keySet()) {
                float oldvalue = mDiffMatrix.get(j).get(i).floatValue();
                int count = mFreqMatrix.get(j).get(i).intValue();
                mDiffMatrix.get(j).put(i, oldvalue / count);
            }
        }
    }
}

class UserId {
    String content;

    public UserId(String s) {
        content = s;
    }

    public int hashCode() {
        return content.hashCode();
    }

    public String toString() {
        return content;
    }
}

class MapUtil {
    public static <K, V extends Comparable<? super V>> Map<K, V>
    sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list =
                new LinkedList<Map.Entry<K, V>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return -(o1.getValue()).compareTo(o2.getValue());
            }
        });

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
}

class ItemId {
    String content;

    public ItemId(String s) {
        content = s;
    }

    public int hashCode() {
        return content.hashCode();
    }

    public String toString() {
        return content;
    }
}


