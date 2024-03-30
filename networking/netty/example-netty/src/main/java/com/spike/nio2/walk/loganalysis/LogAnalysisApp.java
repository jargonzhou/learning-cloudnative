package com.spike.nio2.walk.loganalysis;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Log Analysis App
 *
 * @author zhoujiagen
 */
public class LogAnalysisApp {

    public static void main(String[] args) throws IOException {
        Path start = FileSystems.getDefault().getPath("C:/Users/logs");

        FileVisitor<? super Path> visitor = new LogAnalysisWalker();
        // LogAnalysisWalker visitor = new LogAnalysisWalker();
        Files.walkFileTree(start, visitor);

    }
}
