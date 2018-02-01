package util;

import java.awt.*;

public class GUITestUtil {

    public static Component getChildNamed(Component parent, String childName) {

        if (childName.equals(parent.getName())) {
            return parent;
        }

        if (parent instanceof Container) {
            Component[] children = ((Container)parent).getComponents();
            for (Component aChildren : children) {
                Component child = getChildNamed(aChildren, childName);
                if (child != null) {
                    return child;
                }
            }
        }

        return null;
    }
}
