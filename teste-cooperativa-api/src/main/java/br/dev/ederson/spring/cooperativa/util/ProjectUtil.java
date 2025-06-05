package br.dev.ederson.spring.cooperativa.util;

import java.util.List;

public class ProjectUtil {

    public static boolean isEmpty(List list) {
        if (list == null)
            return true;

        return list.isEmpty();
    }
}
