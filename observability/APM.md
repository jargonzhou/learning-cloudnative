# APM(Application Performance Management)
* [Application performance management - wikipedia](https://en.wikipedia.org/wiki/Application_performance_management)

![APM Conceptual Framework](https://upload.wikimedia.org/wikipedia/commons/6/6a/APM_Conceptual_Framework.jpg)
- End User Experience
- Runtime Application Architecture
- Business Transactions
- Deeep Dive Component Monitoring
- Analystics & Reporting
# Example: Prometheus + ELK
## docker-compose.yaml

```yaml
    version: "3.8"
    services:
      grafana:
        image: grafana/grafana-oss
        container_name: grafana
        restart: unless-stopped
        ports:
         - '3333:3000'
        volumes:
          - ./grafana_data:/var/lib/grafana
        networks:
          - apm-network
      
      prometheus:
        image: prom/prometheus:latest
        container_name: prometheus
        ports:
          - 9090:9090
        command:
          - --config.file=/etc/prometheus/prometheus.yml
        volumes:
          - ./prometheus.yml:/etc/prometheus/prometheus.yml:ro
          - ./prometheus-data:/prometheus
        networks:
          - apm-network
        depends_on:
          - cadvisor
      
      cadvisor:
        image: gcr.io/cadvisor/cadvisor:latest
        container_name: cadvisor
        ports:
        - 8888:8080
        volumes:
          - /:/rootfs:ro
          - /var/run:/var/run:rw
          - /sys:/sys:ro
          - /var/lib/docker/:/var/lib/docker:ro
        networks:
          - apm-network
          - redis-network
    #    depends_on:
    #      - redis
    
      skywalking-oap-server:
        image: apache/skywalking-oap-server:9.0.0
        container_name: skywalking-oap-server
        restart: always
        ports:
          - 11800:11800
          - 12800:12800
        networks:
          - apm-network 
          - app-network
        environment:
          - "SW_JDBC_URL=jdbc:mysql://localhost:3306/skywalking?rewriteBatchedStatements=true&allowMultiQueries=true"
          - "SW_DATA_SOURCE_USER=root"
          - "SW_DATA_SOURCE_PASSWORD=app+mysql"
    
      skywalking-ui:
        image: apache/skywalking-ui:9.0.0
        container_name: skywalking-ui
        restart: always
        ports:
          - 18080:8080
        networks:
          - apm-network 
          - app-network
        environment:
          - "SW_OAP_ADDRESS=http://skywalking-oap-server:12800"
        depends_on:
          - skywalking-oap-server
    
      apm-es:
        image: docker.elastic.co/elasticsearch/elasticsearch:7.10.1
        container_name: apm-es
        restart: always
        volumes:
          - ./es_data:/usr/share/elasticsearch/data
        ports:
          - 19200:9200
        networks:
          - apm-network
        environment:
          - "ES_JAVA_OPTS=-Xms256m -Xmx256m"
          - discovery.type=single-node
    
      kibana:
        image: docker.elastic.co/kibana/kibana:7.10.1
        container_name: kibana
        restart: always
        volumes:
          - ./kibana.yml:/usr/share/kibana/config/kibana.yml
        ports:
          - 15601:5601
        networks:
          - apm-network
        depends_on:
          - apm-es
    
      filebeat:
        image: docker.elastic.co/beats/filebeat:7.10.1
        container_name: filebeat
        restart: always
        user: root
        volumes:
          - ./filebeat.yml:/usr/share/filebeat/filebeat.yml:ro
          - /var/lib/docker/containers:/var/lib/docker/containers:ro
          - /var/run/docker.sock:/var/run/docker.sock:ro
        networks:
          - apm-network
        environment: # command?
          - "strict.perms=false"
        depends_on:
          - apm-es
    
    #volumes:
    #  gradana_data: {}
    
    networks:
      apm-network:
        external: true
      es-network:
        external: true
      redis-network:
        external: true
      app-network:
        external: true
```
## prometheus.yml
```yaml
    global:
      scrape_interval: 15s
    
    scrape_configs:
      - job_name: cadvisor
        scrape_interval: 10s
        static_configs:
          - targets:
            - cadvisor:8080
      - job_name: node
        static_configs:
          - targets: ['<node_exporter ip:port>'] 
```
## kibana.yml
```yaml
    server.name: apm-kibana
    server.host: kibana
    
    elasticsearch.hosts: ["<http://apm-es:9200>"]
    elasticsearch.username: "app"
    elasticsearch.password: "app+es1"
    elasticsearch.ssl.verificationMode: none
```
## filebeat.yml
```yaml
    filebeat.config:
      modules:
        path: ${path.config}/modules.d/*.yml
        reload.enabled: false
    
    # filebeat.inputs:
    #  - type: container
    #    paths:
    #      - /var/lib/docker/containers/*/*.log
    #    processors:
    #      - drop_event:
    #          when:
    #            equals: 
    #              docker.container.name: filebeat
    
    filebeat.autodiscover:
      providers:
        - type: docker
          hints.enabled: true
          
          templates:
    				- condition: # 过滤不收集的容器日志
                or:
                - contains:
                    docker.container.name: filebeat
                - contains:
                    docker.container.name: kibana
                - contains:
                    docker.container.name: "apm-es"
                - contains:
                    docker.container.name: cadvisor
                - contains:
                    docker.container.name: grafana
              config:
                - type: container
                  processors:
                    - drop_event
            - condition:
                equals:
                  docker.container.labels.com.service.language: Java
              config:
                - type: container
                  containers.paths:
                    - /var/lib/docker/containers/${data.docker.container.id}/*.log
                  multiline.type: pattern
                  multiline.pattern: '^[0-9]{4}-[0-9]{2}-[0-9]{2}'
                  multiline.negate: true
                  multiline.match: after
            - condition:
                contains:
                  #docker.container.name: rocketmq
                  docker.container.labels.com.docker.compose.service: rocketmq
              config:
                - type: container
                  paths:
                    - /var/lib/docker/containers/${data.docker.container.id}/*.log
                  multiline:
                    type: pattern
                    pattern: '^\\[[0-9]{4}-[0-9]{2}-[0-9]{2}'
                    negate: true
                    match: after    
          
    processors:
    - add_cloud_metadata: ~
    
    setup.kibana.host: kibana:5601
    
    output.elasticsearch:
      hosts: '${ELASTICSEARCH_HOSTS:apm-es:9200}'
      username: '${ELASTICSEARCH_USERNAME:xxx}'
      password: '${ELASTICSEARCH_PASSWORD:app+es1}'
```