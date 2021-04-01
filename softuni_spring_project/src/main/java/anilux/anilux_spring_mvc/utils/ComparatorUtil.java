package anilux.anilux_spring_mvc.utils;

import java.util.Comparator;
import java.util.Map;

public class ComparatorUtil implements Comparator<String> {
    private Map<String, Integer> map;

    public ComparatorUtil(Map<String, Integer> map) {
        this.map = map;
    }

    @Override
    public int compare(String first, String second) {
        if (this.map.get(first) >= this.map.get(second)) {
            return -1;
        }

        return 1;
    }
}
