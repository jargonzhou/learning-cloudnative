# Testing

# JUnit
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
JUnit 5 = JUnit Platform + JUnit Jupiter + JUnit Vintage
- The JUnit Platform serves as a foundation for launching testing frameworks on the JVM.
- JUnit Jupiter is the combination of the programming model and extension model for writing tests and extensions in JUnit 5.
- JUnit Vintage provides a TestEngine for running JUnit 3 and JUnit 4 based tests on the platform.

<!--
books:
- Tudose, Cătălin  **JUnit in Action**. 2020, 3. edition. Manning.

  - Part 1. JUnit: 1-5
  - Part 2. Different testing strategies: 6-9
  - Part 3. Working with JUnit 5 and other tools: 10-13
  - Part 4. Working with modern frameworks and JUnit 5: 14-19
  - Part 5. Developing applicationswith JUnit 5: 20-22

| #    | Title                                                       |
| :--- | :---------------------------------------------------------- |
| 1    | [[#5.1 JUnit jump-start]]                                   |
| 2    | [[#5.2 Exploring core JUnit]]                               |
| 3    | [[#5.3 JUnit architecture]]                                 |
| 4    | [[#5.4 Migrating from JUnit 4 to JUnit 5]]                  |
| 5    | [[#5.5 Software testing principles]]                        |
| 6    | [[#5.6 Test quality]]                                       |
| 7    | [[#5.7 Coarse-grained testing with stubs]]                  |
| 8    | [[#5.8 Testing with mock objects]]                          |
| 9    | [[#5.9 In-container testing]]                               |
| 10   | [[#5.10 Running JUnit tests from Maven 3]]                  |
| 11   | [[#5.11 Running JUnit tests from Gradle 6]]                 |
| 12   | [[#5.12 JUnit 5 IDE support]]                               |
| 13   | [[#5.13 Continuous integration with JUnit 5]]               |
| 14   | [[#5.14 JUnit 5 extension model]]                           |
| 15   | [[#5.15 Presentation-layer testing]]                        |
| 16   | [[#5.16 Testing Spring applications]]                       |
| 17   | [[#5.17 Testing Spring Boot applications]]                  |
| 18   | [[#5.18 Testing a REST API]]                                |
| 19   | [[#5.19 Testing database applications]]                     |
| 20   | [[#5.20 Test-driven development with JUnit 5]]              |
| 21   | [[#5.21 Behavior-driven development with JUnit 5]]          |
| 22   | [[#5.22 Implementing a test pyramid strategy with JUnit 5]] |
-->

# REST-assured
- https://github.com/rest-assured/rest-assured

> Testing and validation of REST services in Java is harder than in dynamic languages such as Ruby and Groovy. REST Assured brings the simplicity of using these languages into the Java domain.

# Fuzz Testing

- [Fuzzing - wikipedia](https://en.wikipedia.org/wiki/Fuzzing)

> Fuzz Testing (模糊测试)
>
> In programming and software development, fuzzing or fuzz testing is an automated software testing technique that involves providing invalid, unexpected, or random data as inputs to a computer program. The program is then monitored for exceptions such as crashes, failing built-in code assertions, or potential memory leaks. 
> Typically, fuzzers are used to test programs that take structured inputs. This structure is specified, e.g., in a file format or protocol and distinguishes valid from invalid input. An effective fuzzer generates semi-valid inputs that are "valid enough" in that they are not directly rejected by the parser, but do create unexpected behaviors deeper in the program and are "invalid enough" to expose corner cases that have not been properly dealt with.

- [Google oss-fuzz](https://github.com/google/oss-fuzz): OSS-Fuzz - continuous fuzzing for open source software.
- [The Fuzzing Book: Tools and Techniques for Generating Software Tests](https://www.fuzzingbook.org/)
- [Awesome-Grammar-Fuzzing](https://github.com/Microsvuln/Awesome-Grammar-Fuzzing)

# Load Testing
## Fortio
- https://github.com/fortio/fortio
> [!info] Fortio
> Fortio load testing library, command line tool, advanced echo server and web UI in go (golang). Allows to specify a set query-per-second load and record latency histograms and other useful stats.
>
> Fortio (Φορτίο) started as, and is, [Istio](https://istio.io/)'s load testing tool and later (2018) graduated to be its own project.

# TestContainers
- https://testcontainers.com/

> Testcontainers: Unit tests with real dependencies
>
> Testcontainers is an open source framework for providing throwaway, lightweight instances of databases, message brokers, web browsers, or just about anything that can run in a Docker container.

Test dependencies as code
- [Java](https://testcontainers.com/guides/getting-started-with-testcontainers-for-java/): examples in 'Cloud Native Spring'
- [Python](https://testcontainers-python.readthedocs.io/en/latest/): examples in 'Data Pipelines with Apache Airflow'

# Misc
- [curl](https://curl.se/docs/manpage.html)
- [nc - arbitrary TCP and UDP connections and listens](https://linux.die.net/man/1/nc)
	- [How to Use Netcat Commands: Examples and Cheat Sheets](https://www.varonis.com/blog/netcat-commands)
	- [The netcat Command in Linux](https://www.baeldung.com/linux/netcat-command)
