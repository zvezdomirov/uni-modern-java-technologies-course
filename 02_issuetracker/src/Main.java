import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>(Arrays.asList("a", "b", "c", "d"));

//        for (Iterator<String> iter = list.iterator(); iter.hasNext(); ) {
//            if (iter.next().equals("b")) {
//                 iter.remove();    // #1
//                // list.remove("b"); // #2
//            }
//        }

        Iterator it1 = list.iterator();
        Iterator it2 = list.iterator();

        it1.next();
        it1.remove();

        // it2 = list.iterator();
        it2.next();

        System.out.println(list);

    }
}
