package df.util.type;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: liudongfeng
 * Date: 2005-5-29
 * Time: 10:47:59
 * To change this template use File | Settings | File Templates.
 */
public class CollectionUtil {

    public static void addListElementIntoMap(HashMap map, String key, String value) {
        List list = (List) map.get(key);
        if (list == null) {
            list = new ArrayList();
            map.put(key, list);
        }
        list.add(value);
    }

//    /**
//     * 把qr中的对象的顺序混淆打乱
//     * @param qr
//     */
//    public static void shuffle(QueryResult qr){
//        if((qr==null)||(qr.size()==0))
//            return;
//        else
//          Collections.shuffle(qr);
//    }

}
