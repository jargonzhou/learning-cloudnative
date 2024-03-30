package com.spike.nio2.walk;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Directory Walker <br>
 *
 * @author zhoujiagen
 */
public class MyDirWalker extends SimpleFileVisitor<Path> {
    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        System.out.println("\t\\--" + file.getFileName().toString());

        // do something here

        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        System.out.println("\n" + dir.getParent() + System.getProperty("file.separator")
                + dir.getFileName());

        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        if (exc != null) exc.printStackTrace();
        return FileVisitResult.CONTINUE;
    }
}