import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
public class cacheMemory {
    public Map<String, String> cache;
    private final int SIZE;
    private final Deque<String> QUEUE;
    // Initializing all parameters for the cache
    public cacheMemory(int SIZE) {
        this.SIZE = SIZE;
        this.cache = new ConcurrentHashMap<>(SIZE);
        this.QUEUE = new LinkedList<>();
    }
    // Updates the Queue every time a new element is added here
    private void updateQueue(String key) {
        QUEUE.addLast(key);
        if (QUEUE.size() > SIZE) {
            String k = QUEUE.removeFirst();
            cache.remove(k);
        }
    }
    // Returns the list
    public String getQueue() {
        return String.join(",", QUEUE);
    }
    // Add or update
    public void keyUpdate(String key, String value) {
        updateQueue(key);
        cache.put(key, value);
    }
    // Delete element
    public void deleteKey(String key) {
        cache.remove(key);
        QUEUE.remove(key);
    }
    public String getKey(String key) {
        return cache.get(key);
    }
}
