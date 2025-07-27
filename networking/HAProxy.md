# HAProxy
* http://www.haproxy.org/

> HAProxy is a free, very fast and reliable reverse-proxy offering high availability, load balancing, and proxying for TCP and HTTP-based applications. It is particularly suited for very high traffic web sites and powers a significant portion of the world's most visited ones. Over the years it has become the de-facto standard opensource load balancer, is now shipped with most mainstream Linux distributions, and is often deployed by default in cloud platforms. Since it does not advertise itself, we only know it's used when the admins report it :-)

> 2024-09-18: LTS 3.0, 2.8, 2.6, 2.4, 2.2; DEV 3.1.

[HAProxy Documentation](https://docs.haproxy.org/)
- Starter Guide
- Configuration Manual
- Management Guide

[ebooks](https://www.haproxy.com/content-library/ebooks)
- HAProxy as an API Gateway
- HAProxy in Kubernetes
- The HAProxy Guide to Multi-Layered Security

actions:
- https://github.com/zhoujiagen/application-store/tree/main/ops/haproxy

## haproxy.cfg

sections:
- global
- defaults
- frontend
- backend
- listen: combine frontend and backend into one.

## timeout

- [Haproxy : tcp-request connection Close the connexion in 60 seconds](https://discourse.haproxy.org/t/haproxy-tcp-request-connection-close-the-connexion-in-60-seconds/7320)

```
timeout client 1m
timeout server 1m
```

- [Connections closed by client are left unclosed by HAProxy](https://github.com/haproxy/haproxy/issues/136)
