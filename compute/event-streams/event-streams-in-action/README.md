# Examples in 'Event Streams in Action'

```shell
gradle init --type java-application --dsl kotlin
```

Component versions: 

> samza\samza-hello-samza\bin\grid

- zookeeper-3.4.14
- hadoop-2.9.2
- kafka_2.11-2.1.1

Applications:

- `app`
  - `com.spike.eventstreams.App`: example Nile
    - gen events: `com.spike.eventstreams.GenEventsTest.testGenEvents`

- `app-samza`
  - `com.spike.eventstreams.samza.WikipediaZkLocalApplication`: example samza-hello-samza Wikipedia for Samza Run as embedded library
    - change to consume `wikipedia-raw` events using gen: `com.spike.eventstreams.GenEventsTest.testGenEvents`
