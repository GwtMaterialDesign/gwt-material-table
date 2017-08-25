package gwt.material.design.client;

import gwt.material.design.client.data.component.RowComponent;
import gwt.material.design.client.model.Person;

import java.util.List;
import java.util.logging.Logger;

public class SortHelper {

    private static final Logger logger = Logger.getLogger(SortHelper.class.getName());

    public static boolean isReversed(List<RowComponent<Person>> targetList, List<RowComponent<Person>> checkList) {
        if(checkList.size() != targetList.size()) {
            return false;
        }
        for(int i = 0; i < targetList.size(); i++) {
            Person target = targetList.get(i).getData();
            Person check = checkList.get((targetList.size() - 1) - i).getData();

            if(!target.equals(check)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotSame(List<RowComponent<Person>> targetList, List<RowComponent<Person>> checkList) {
        if(checkList.size() != targetList.size()) {
            return false;
        }
        int same = 0;
        for(int i = 0; i < targetList.size(); i++) {
            Person target = targetList.get(i).getData();
            Person check = checkList.get(i).getData();

            if(target.getId() == check.getId()) {
                same++;
            }
        }
        return same != checkList.size();
    }
}
