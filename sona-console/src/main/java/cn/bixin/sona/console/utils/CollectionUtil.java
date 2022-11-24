package cn.bixin.sona.console.utils;


import org.apache.commons.lang.math.NumberUtils;
import org.apache.dubbo.common.utils.StringUtils;
import org.eclipse.collections.impl.factory.Lists;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CollectionUtil {
    public static List<String> spilt(String str, char ch) {
        if (StringUtils.isBlank(str)) {
            return Lists.mutable.empty();
        }
        String[] split = StringUtils.split(str, ch);
        assert split != null;
        return Arrays.stream(split).collect(Collectors.toList());
    }

    public static List<Long> spiltToLong(String str, char ch) {
        if (StringUtils.isBlank(str)) {
            return Lists.mutable.empty();
        }
        String[] split = StringUtils.split(str, ch);
        assert split != null;
        return Arrays.stream(split).map(NumberUtils::toLong).collect(Collectors.toList());
    }
}
