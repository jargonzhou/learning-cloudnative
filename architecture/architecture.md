# Architecture

# The Twelve-Factor App
* https://12factor.net/

| #    | Factor              | Description                                                      |
| ---- | ------------------- | ---------------------------------------------------------------- |
| I    | Codebase            | One codebase tracked in revision control, many deploys           |
| II   | Dependencies        | Explicitly declare and isolate dependencies                      |
| III  | Config              | Store config in the environment                                  |
| IV   | Backing services    | Treat backing services as attached resources                     |
| V    | Build, release, run | Strictly separate build and run stages                           |
| VI   | Processes           | Execute the app as one or more stateless processes               |
| VII  | Port binding        | Export services via port binding                                 |
| VIII | Concurrency         | Scale out via the process model                                  |
| IX   | Disposability       | Maximize robustness with fast startup and graceful shutdown      |
| X    | Dev/prod parity     | Keep development, staging, and production as similar as possible |
| XI   | Logs                | Treat logs as event streams                                      |
| XII  | Admin processes     | Run admin/management tasks as one-off processes                  |


> [The Fifteen-Factor App](https://domenicoluciani.com/2021/10/30/15-factor-app.html)
>
>- [Beyond the Twelve-Factor App](https://www.oreilly.com/library/view/beyond-the-twelve-factor/9781492042631/) by Kevin Hoffman
>
>13. API First 
    - We should define the service contract first to help our consumers understand what to send and receive, defining a common contract.  
    - This approach enables consumers and service developers to work in parallel. 
    - It helps avoid bottlenecks and facilitates the virtualization of the APIs so the consumers can run the tests against the mocks.
>14. Telemetry 
    - Monitoring our microservices, containers, and env help us to scale, self-heal and manage alerts for end-users and platform operators. 
    - We can use machine learning towards those metrics to derive future business strategies. 
    - We can observe the application’s performance, stream of events and data, health checks, when the application starts, scales, and so on.
>15. Authentication 
    - Make sure all security policies are in place. Developers tend to underestimate the importance that security has. 
    - APIs should be secured using OAuth, RBAC, etc. 
    - Web content should be exposed externally on HTTPS 
    - MFA 
    - Database protection

Mentioned Resources
- Tornado: [https://www.tornadoweb.org/en/stable/](https://www.tornadoweb.org/en/stable/)

[Tornado](https://www.tornadoweb.org/) is a Python web framework and asynchronous networking library, originally developed at [FriendFeed](https://en.wikipedia.org/wiki/FriendFeed). By using non-blocking network I/O, Tornado can scale to tens of thousands of open connections, making it ideal for [long polling](https://en.wikipedia.org/wiki/Push_technology#Long_polling), [WebSockets](https://en.wikipedia.org/wiki/WebSocket), and other applications that require a long-lived connection to each user.

- Jetty: [https://eclipse.dev/jetty/](https://eclipse.dev/jetty/)

Jetty provides a web server and servlet container, additionally providing support for HTTP/2, WebSocket, OSGi, JMX, JNDI, JAAS and many other integrations. These components are open source and are freely available for commercial use and distribution.

Jetty is used in a wide variety of projects and products, both in development and production. Jetty has long been loved by developers due to its long history of being easily embedded in devices, tools, frameworks, application servers, and modern cloud services.

# Real World applications

* [Bitly](https://en.wikipedia.org/wiki/Bitly): Bitly is a URL shortening service and a link management platform.
* [Amazon S3](https://en.wikipedia.org/wiki/Amazon_S3): Amazon Simple Storage Service (S3) is a service offered by Amazon Web Services (AWS) that provides object storage through a web service interface.
* [How YouTube Supported 2.49 Billion Users With MySQL (Scaling with Vitess)](https://medium.com/system-design-mastery-series/how-youtube-supported-2-49-billion-users-with-mysql-scaling-with-vitess-1bb7438892d7)
* [How Meta Serverless Handles 11.5 Million Function Calls per Second](https://newsletter.systemdesign.one/p/serverless-architecture)
* [Apache Kafka](https://en.wikipedia.org/wiki/Apache_Kafka): Apache Kafka is a distributed event store and stream-processing platform.
* [Slack Architecture](https://systemdesign.one/slack-architecture/)
* [How Stripe Prevents Double Payment Using Idempotent API](https://newsletter.systemdesign.one/p/idempotent-api)
* [Design A Stock Exchange System](https://www.systemdesignhandbook.com/guides/design-a-stock-exchange-system/)
* [Twitter Timeline Architecture](https://medium.com/@tnale/twitter-timeline-architecture-e1c299646dc7)
* [Reddit vote system implementation](https://share.google/aimode/KwZAYgZgAJj8zoqi2)
* [The Architecture of a Match: How Tinder Finds Users Nearby](https://medium.com/@rajanjoshi68/the-architecture-of-a-match-how-tinder-finds-users-nearby-00f054eb7961)
* [How Uber Finds Nearby Drivers at 1 Million Requests per Second](https://newsletter.systemdesign.one/p/how-does-uber-find-nearby-drivers)
* [Design a Collaborative Editing System like Google Docs](https://systemdesignschool.io/problems/google-doc/solution)
* [Spotify Design System and Backend Architecture: Building a Scalable Music Platform](https://www.systemdesignhandbook.com/guides/spotify-design-system/)
* [How WhatsApp Handles 40 Billion Messages Per Day](https://blog.bytebytego.com/p/how-whatsapp-handles-40-billion-messages)
* [Elasticity, Scalability & High Availability in AWS Cloud Architecture](https://medium.com/@ismailkovvuru/elasticity-scalability-high-availability-in-aws-cloud-architecture-22dce7301271)
* [How ChatGPT System Design Works: A Complete Guide](https://www.systemdesignhandbook.com/guides/how-chatgpt-system-design/)

## See Also
* [System Design Handbook](https://www.systemdesignhandbook.com/)
* [The System Design Newsletter](https://systemdesign.one/)
* [ByteByteGo Newsletter](https://blog.bytebytego.com/)
