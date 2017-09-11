package com.certified.verityscanningOne.helper;

import com.quickblox.core.helper.StringifyArrayList;

public class Utils {

    public static String ListToString(StringifyArrayList tags) {
        return tags.isEmpty() ? "" : tags.toString().replace("[", "").replace("]", "");
    }
}
