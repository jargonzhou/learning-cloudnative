/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.spike.grpc;

import com.spike.grpc.hello.HelloWorldServer;

import java.io.IOException;

public class App {

    public static void main(String[] args) throws IOException, InterruptedException {
        HelloWorldServer.main(args);
    }

    public Object getGreeting() {
        return "";
    }
}
