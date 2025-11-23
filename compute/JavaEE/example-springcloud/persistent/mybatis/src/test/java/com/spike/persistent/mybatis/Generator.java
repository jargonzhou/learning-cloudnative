package com.spike.persistent.mybatis;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

// see https://mybatis.org/generator/running/runningWithJava.html
public class Generator {
    public static void main(String[] args) throws Exception {
        List<String> warnings = new ArrayList<>();
        boolean overwrite = true;

        try (InputStream is = Generator.class.getResourceAsStream("/generatorConfig.xml")) {
            ConfigurationParser cp = new ConfigurationParser(warnings);
            Configuration config = cp.parseConfiguration(is);
            DefaultShellCallback callback = new DefaultShellCallback(overwrite);

            MyBatisGenerator mbg = new MyBatisGenerator(config, callback, warnings);
            mbg.generate(null);

            for (String warning : warnings) {
                System.out.println(warning);
            }
        }
    }
}