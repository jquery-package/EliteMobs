package com.magmaguy.elitemobs.utils;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.util.stream.Collectors;

public final class ClassFinder {

    public final static List<Class<?>> find(String packageName) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream stream = classLoader.getResourceAsStream(packageName.replaceAll("[.]", "/"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        return reader.lines()
          .filter(line -> line.endsWith(".class"))
          .map(line -> getClass(line, packageName))
          .collect(Collectors.toSet());
    }
 
    private final static Class getClass(String className, String packageName) {
        try {
            return Class.forName(packageName + "." + className.substring(0, className.lastIndexOf('.')));
        } catch (ClassNotFoundException ignored) {
        }
        return null;
    }

}
