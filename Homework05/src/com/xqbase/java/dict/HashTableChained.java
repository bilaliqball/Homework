/* HashTableChained.java */

package com.xqbase.java.dict;

import com.xqbase.java.list.DList;
import com.xqbase.java.list.DListNode;

import java.util.Random;

/**
 * HashTableChained implements a Dictionary as a hash table with chaining.
 * All objects used as keys must have a valid hashCode() method, which is
 * used to determine which bucket of the hash table an entry is stored in.
 * Each object's hashCode() is presumed to return an int between
 * Integer.MIN_VALUE and Integer.MAX_VALUE.  The HashTableChained class
 * implements only the compression function, which maps the hash code to
 * a bucket in the table's range.
 * <p/>
 * DO NOT CHANGE ANY PROTOTYPES IN THIS FILE.
 */

public class HashTableChained implements Dictionary {

    /** The has table data .*/
    private DList[] table;
    /** The total number of entries in the hash table. */
    private int count;
    /** The load factor for the hash table. */
    private float loadFactor;

    /**
     * Construct a new empty hash table intended to hold roughly sizeEstimate
     * entries.  (The precise number of buckets is up to you, but we recommend
     * you use a prime number, and shoot for a load factor between 0.5 and 1.)
     */

    public HashTableChained(int sizeEstimate) {
        loadFactor = 0.75f;
        table = new DList[getNearestPrime((int)Math.ceil(sizeEstimate/loadFactor))];
    }

    /**
     * Construct a new empty hash table with a default size.  Say, a prime in
     * the neighborhood of 100.
     */

    public HashTableChained() {
        loadFactor = 0.75f;
        table = new DList[101];
    }

    private Entry newEntry(Object key, Object value) {
        Entry entry = new Entry();
        entry.key = key;
        entry.value = value;
        return entry;
    }

    /**
     * Converts a hash code in the range Integer.MIN_VALUE...Integer.MAX_VALUE
     * to a value in the range 0...(size of hash table) - 1.
     * <p/>
     * This function should have package protection (so we can test it), and
     * should be used by insert, find, and remove.
     */

    int compFunction(int code) {
        int p = getNearestPrime(table.length);
        Random r = new Random();
        int a = r.nextInt(p - 1) + 1;
        int b = r.nextInt(p);
        return ((a * code + b) % p) % table.length;
    }

    /**
     * Returns the number of entries stored in the dictionary.  Entries with
     * the same key (or even the same key and value) each still count as
     * a separate entry.
     *
     * @return number of entries in the dictionary.
     */

    public int size() {
        return count;
    }

    /**
     * Tests if the dictionary is empty.
     *
     * @return true if the dictionary has no entries; false otherwise.
     */

    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Create a new Entry object referencing the input key and associated value,
     * and insert the entry into the dictionary.  Return a reference to the new
     * entry.  Multiple entries with the same key (or even the same key and
     * value) can coexist in the dictionary.
     * <p/>
     * This method should run in O(1) time if the number of collisions is small.
     *
     * @param key   the key by which the entry can be retrieved.
     * @param value an arbitrary object.
     * @return an entry containing the key and value.
     */

    public Entry insert(Object key, Object value) {
        Entry entry = newEntry(key, value);
        int index = compFunction(key.hashCode());
        table[index].insertBack(entry);
        count ++;
        return entry;
    }

    /**
     * Search for an entry with the specified key.  If such an entry is found,
     * return it; otherwise return null.  If several entries have the specified
     * key, choose one arbitrarily and return it.
     * <p/>
     * This method should run in O(1) time if the number of collisions is small.
     *
     * @param key the search key.
     * @return an entry containing the key and an associated value, or null if
     * no entry contains the specified key.
     */

    public Entry find(Object key) {
        int index = compFunction(key.hashCode());
        // delegate this to the list find method
        DListNode node = table[index].find(newEntry(key, null));
        return node == null ? null : node.entry;
    }

    /**
     * Remove an entry with the specified key.  If such an entry is found,
     * remove it from the table and return it; otherwise return null.
     * If several entries have the specified key, choose one arbitrarily, then
     * remove and return it.
     * <p/>
     * This method should run in O(1) time if the number of collisions is small.
     *
     * @param key the search key.
     * @return an entry containing the key and an associated value, or null if
     * no entry contains the specified key.
     */

    public Entry remove(Object key) {
        int index = compFunction(key.hashCode());
        DListNode node = table[index].find(newEntry(key, null));
        if (node != null) {
            table[index].remove(node);
            count --;
            return node.entry;
        }
        return null;
    }

    /**
     * Remove all entries from the dictionary.
     */
    public void makeEmpty() {
        if (isEmpty())
            return;
        for (int i = 0; i < table.length; i++) {
            table[i].clear();
        }
        count = 0;
    }

    /**
     * Whether a num is prime or not.
     */
    private static boolean isPrime(int num) {
        if (num < 2)
            return false;
        for (int i = 2; i <= Math.sqrt(num); i++) {
            if (num % i == 0)
                return false;
        }

        return true;
    }

    /**
     * Get the nearest prime bigger than num.
     */
    private static int getNearestPrime(int num) {
        for (int i = num; i < 2 * num; i++) {
            if (isPrime(i))
                return i;
        }

        // This should not happen in real case.
        return 2;
    }

    public static void main(String[] args) {
        float loadF = 0.75f;
        System.out.println(10/loadF);
        System.out.println(Math.ceil(10/loadF));
        System.out.println(getNearestPrime((int) Math.ceil(10 / loadF)));
    }
}
