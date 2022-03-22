package com.yatoufang.action;

import com.yatoufang.entity.Field;
import org.apache.commons.compress.utils.Lists;

import java.util.*;

class Test {

    public static void main(String[] args) {
        ArrayList<Field> list = Lists.newArrayList();
        Field a1 = new Field("a1");
        Field a2 = new Field("a2");
        Field a3 = new Field("a3");
        Field a4 = new Field("a4");
        Field aa = new Field("aa");
        a4.parent = a3;
        a3.parent = a2;
        a2.parent = a1;
        a1.parent = aa;

        list.add(a1);
        list.add(a2);
        list.add(a3);
        list.add(a4);


        Field field = list.get(2);
        field.setName("ax");

        Field parent = a2.parent;
        parent.setName("dd");
        System.out.println(a2.parent);

        System.out.println(list);

    }


}
