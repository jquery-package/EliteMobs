package com.magmaguy.elitemobs.utils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import com.magmaguy.elitemobs.EliteMobs;

public final class ClassFinder {

    private static final ClassLoader classLoader;

    static {
        classLoader = EliteMobs.class.getClassLoader();
    }

    public static List<Class<?>> find(String pkgName) throws IOException {
        String pkgPath = pkgName.replace('.', '/');
        URL url = classLoader.getResource(pkgPath);
        if (url == null) {
            throw new IOException("No resource found");
        }
        URI pkg;
        try {
            pkg = url.toURI();
        } catch (URISyntaxException e) {
            throw new IOException(e);
        }
        List<Class<?>> allClasses = new ArrayList<>();

        Path root;
        if (pkg.toString().startsWith("jar:")) {
            try {
                root = FileSystems.getFileSystem(pkg).getPath(pkgPath);
            } catch (FileSystemNotFoundException e) {
                root = FileSystems.newFileSystem(pkg, Collections.emptyMap()).getPath(pkgPath);
            }
        } else {
            root = Paths.get(pkg);
        }

        String extension = ".class";
        try (Stream<Path> allPaths = Files.walk(root)) {
            allPaths.filter(Files::isRegularFile).forEach(file -> {
                try {
                    String path = file.toString().replace('/', '.');
                    String name = path.substring(path.indexOf(pkgName), path.length() - extension.length());
                    allClasses.add(Class.forName(name));
                } catch (ClassNotFoundException | StringIndexOutOfBoundsException ignored) {
                }
            });
        }
        return allClasses;
    }

}
