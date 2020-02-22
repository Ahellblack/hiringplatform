package com.siti.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by szy on 2019/8/24 8:27.
 */
public class PageUtil {

    /**
     * 自定义分页
     * @param page     页码，大于0，从1开始
     * @param pageSize 行数，大于0
     */
    public static List<?> subPageList(List<?> list, int page, int pageSize) {
        if (list.size() == 0)
            return list;
        if (page <= 0 || pageSize <= 0)
            throw new RuntimeException("参数异常");

        int total = list.size();
        int index = page - 1;
        if (index * pageSize > total)
            return new ArrayList<>();
        List<?> retList = list.subList(index * pageSize, (index + 1) * pageSize > total ? total : ((index + 1) * pageSize));
        return retList;
    }

}
