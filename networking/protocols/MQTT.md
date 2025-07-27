# MQTT
* https://mqtt.org/
* https://github.com/mqtt/mqtt.github.io/wiki

> MQTT: The Standard for IoT Messaging
>
> MQTT is an OASIS standard messaging protocol for the Internet of Things (IoT). It is designed as an extremely lightweight publish/subscribe messaging transport that is ideal for connecting remote devices with a small code footprint and minimal network bandwidth. MQTT today is used in a wide variety of industries, such as automotive, manufacturing, telecommunications, oil and gas, etc.

abbrev: Message Queuing Telemetry Transport

version
- 3.1.1
- 5

# Implementation
- [Which mqtt message broker has the fastest throughput?](https://www.quora.com/Which-mqtt-message-broker-has-the-fastest-throughput)
- software: https://github.com/mqtt/mqtt.github.io/wiki/software
## Brokers
- Eclipse Mosquitto

**Eclipse Mosquitto** is an open source (EPL/EDL licensed) message broker that implements the MQTT protocol versions 5.0, 3.1.1 and 3.1. Mosquitto is lightweight and is suitable for use on all devices from low power single board computers to full servers.

https://mosquitto.org/

- Moquette

Moquette is a Java MQTT broker based on an eventing model with Netty.

http://andsel.github.io/moquette/

- EMQ: Erlang MQTT Broker

http://www.emqtt.io

- HiveMQ

http://hivemq.com

- CloudMQTT

http://www.cloudmqtt.com

## Client
- [Paho](https://www.eclipse.org/paho/)

The Eclipse Paho project provides open source, mainly client side, implementations of MQTT and MQTT-SN in a variety of programming languages.

- [MQTT.fx](http://www.mqttfx.org)

MQTT.fx is a GUI utility implemented with JavaFX that is available for Windows, Linux and macOS. This tool allows us to connect with an MQTT server, subscribe to topic filters, see the received messages and publish messages to topics. 

- [mqtt-spy](http://kamilfb.github.io/mqtt-spy/)

An open source utility intended to help you with monitoring activity on MQTT topics

# Key Feature
- MQTT Clients
- MQTT Broker
- MQTT QoS(Quality of Service) Levels
  - at most once: 0. PUBLISH
  - at least once: 1. PUBLISH, PUBACK
  - exactly once: 2. PUBLISH, PUBREC, PUBREL, PUBCOMP
- Persitent Sessions
- Retained Messages
- Last Will and Testament(LWT)

# Messages
* [MQTT Packets: A Comprehensive Guide](https://www.hivemq.com/blog/mqtt-packets-comprehensive-guide/)
## PUBLISH
<img src="https://www.hivemq.com/sb-assets/f/243938/490x291/a8acf37894/publish_packet.webp"/>
PacketId:
- assigned by client, included in PUBLISH/PUBREL/PUBREC/PUBCOMP
- broker receive PUBLISH message, assign PacketId to the message, send PUBACK to client
publish/ack messages:
- PUBLISH: client -msg-> broker, message contains a topic and payload
- PUBREC: broker -PUBREC-> client, ack received the message
- PUBREL: client -PUBREL -> broker, release the broker from the resposibility of keep the message in memory
- PUBCOMP: broker -PUBCOMP -> client, successfully received and processed the message
broker handle messages published from clients:
- message reception
- acknowledgement: broker -receipt message ack msg-> client
- processing: send msg to subscriber, retain message
- feedback: broker -publish successfully confirmation message-> client
QoS
- 0: fire and forget
- 1: sender keeps a copy of the message until receives a PUBACK from receiver, need re-transmit
- 2: PUBLISH/PUBREC/PUBREL/PUBCOMP
<img src="https://www.hivemq.com/sb-assets/f/243938/1024x360/41d4e98134/qos-levels_qos0.webp" width="400"/>

<br/>
<img src="https://www.hivemq.com/sb-assets/f/243938/1024x360/529ab80be0/qos-levels_qos1.webp" width="400"/>

<br/>
<img src="https://www.hivemq.com/sb-assets/f/243938/1024x360/3b314a5496/qos-levels_qos2.webp" width="400"/>
### PUBACK
<img src="https://www.hivemq.com/sb-assets/f/243938/490x291/335995b671/puback_packet.webp"/>
### PUBREC
<img src="https://www.hivemq.com/sb-assets/f/243938/490x291/dcb1eb7d0e/pubrec_packet.webp"/>
### PUBREL
<img src="https://www.hivemq.com/sb-assets/f/243938/490x291/e9cf421ae9/pubrel_packet.webp"/>
### PUBCOMP
<img src="https://www.hivemq.com/sb-assets/f/243938/490x291/04dcb1c8d9/pubcomp_packet.webp"/>
## SUBSCRIBE
<img src="https://www.hivemq.com/sb-assets/f/243938/490x291/683f163f34/subscribe_packet.webp"/>
qos 1:
- broker -msg-> client
- client -PUBACK-> broker

qos 2:
- broker -msg-> client
- client -PUBREC-> broker
- broker -PUBREL-> client
- client -PUBCOMP-> broker
### SUBACK
<img src="https://www.hivemq.com/sb-assets/f/243938/490x291/be336d46a5/suback_packet.webp"/>
### UNSUBSCRIBE
<img src="https://www.hivemq.com/sb-assets/f/243938/490x291/5878803e52/unsubscribe_packet.webp"/>
### UNSUBACK
<img src="https://www.hivemq.com/sb-assets/f/243938/490x291/39093abdcc/unsuback_packet.webp"/>
## CONNECT
<img src="https://www.hivemq.com/sb-assets/f/243938/490x308/1f8afc7b3e/connect.webp"/>
### DISCONNECT
<img src="https://www.hivemq.com/sb-assets/f/243938/490x291/67c4bc137d/disconnect.webp"/>
## PINGREQ
<img src="https://www.hivemq.com/sb-assets/f/243938/490x291/45491be72f/pingreq.webp"/>
### PINGRESP
<img src="https://www.hivemq.com/sb-assets/f/243938/490x291/0e07825f98/pingresp.webp"/>


# QoS
message delivery:
- from the publishing client to the broker: QoS level for the message during transmission
- from the broker to the subscribing client: QoS level defined by each subscribing client during the subscription process

# Session
- persistent session: broker stores the client's subscription information and any undelivered messages for the client
- clean session

# MQTT 5
TODO: from Chapter 14 in hivemq-ebook-mqtt-essentials

new features:
- session expiry and message expiry intervals: clean session -> clean start
- improved client feedback and negative ACKs
- user properties: 用户属性
- shared subscription: 共享订阅
- payload format description and content type: 负载格式描述
- request-response pattern: 请求-响应模式
- topic alias: 主题别名
- enhanced authentication mechanism: 增强的身份验证机制, AUTH
- flow control: 流控
- topic tree and topic matching
- header properties
- reason codes: nagative ACK