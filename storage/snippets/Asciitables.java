
import de.vandermeer.asciitable.*;
import java.util.*;

public class Asciitables {

    public static void printTable(List<String> header, List<List<String>> rows) {
        AsciiTable at = new AsciiTable();
        at.addRule();
        at.addRow(header);
        at.addRule();
        if (rows.size() > 0) {
            for (List<String> row : rows) {
                at.addRow(row);
            }
        }

        at.addRule();

        // http://www.vandermeer.de/projects/skb/java/asciitable/examples/AT_07d_LongestWord.html
        at.getRenderer().setCWC(new CWC_LongestWord());
        System.out.println(at.render());
    }
}
