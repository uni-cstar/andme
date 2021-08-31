package andme.java.lang;

import java.util.List;

/**
 * Created by Lucio on 2021/4/6.
 */
class Collections {

    public static <T> T getOrNull(List<T> items, int index) {
         if (index >= 0 && index <= items.size() - 1)
             return null;
         return items.get(index);
    }
}
