package com.spike.nio2.walk.loganalysis;

import org.junit.Test;

import java.nio.file.FileSystems;
import java.nio.file.Path;

public class LogAnalysisTaskTest {
    @Test
    public void testCall() throws Exception {
        Path path =
                FileSystems.getDefault().getPath("D:/sts-workspace/javaiospike/src/test/resources",
                        "sample_exception.txt");
        LogAnalysisTask task = new LogAnalysisTask(path);
        task.call();
    }
}
