Apache SkyWalking Agent Test Tool Suite
==========

<img src="http://skywalking.apache.org/assets/logo.svg" alt="Sky Walking logo" height="90px" align="right" />

[![Twitter Follow](https://img.shields.io/twitter/follow/asfskywalking.svg?style=for-the-badge&label=Follow&logo=twitter)](https://twitter.com/AsfSkyWalking)

![CI](https://github.com/apache/skywalking-agent-test-tool/workflows/CI/badge.svg?branch=master)


[**SkyWalking**](https://github.com/apache/skywalking) Agent Test Tool is a tremendously useful test tools suite in a wide variety of languages of `Agent`. 
Includes `mock collector` and `validator`. The `mock collector` is a SkyWalking receiver, like OAP server. 

The `mock collector` is responsible for mocking the SkyWalking OAP receiver to collect data from the agent. It receives data from agent through `GRPC` and `HTTP REST API`(in plan).
And then it can be downloaded by Http API that the mock collector collected(service registered, instance registered, and segments). 
Eventually, validate the file downloaded from `mock collector` by using `SkyWalking Validator`. 

## Apache SkyWalking Mock Collector

### Requirement
1. JDK 1.8+
2. Maven 


### How to install

The `mock collector` is written by pure-`Java`. It compiles and packages through `Maven`.


```bash
mvn package -DskipTests
unzip ./dist/mock-collector.tar.gz -d ./mock-collector
cd ./mock-collector
bash ./bin/collector-startup.sh
```

after above steps, we can check whether the `mock collector` is available through HTTP API. To visit `http://localhost:12800/healthCheck` and get the response with `success` in the body of content.

Finally, To visit follow URL to download the data as `yaml` file, which can be used by `SkyWalking Validator Tool`.

http://localhost:12800/receiveData

### How to config on agent

We can modify the configurations of agent in `./config/agent.conf`, as follows

```properties
# Backend service addresses.
collector.backend_service=${SW_AGENT_COLLECTOR_BACKEND_SERVICES:127.0.0.1:19876}
```

or specify the jvm arguments like:

```bash
# Backend service addresses.
-DSW_AGENT_COLLECTOR_BACKEND_SERVICES=127.0.0.1:19876
```

### Data validation in Mock Collector

Currently, we integration SkyWalking Validator Tool in Mock Collector. We can post the `expectedData.yaml` to `/dataValidate` after agent reported.

## Apache SkyWalking Validator Tool

`Validator Tool` is a data validating tool. It is responsible for validating the `expected data` with `actual data`.

```bash
java -jar \
    -Xmx256m -Xms256m \
    -DcaseName="case_name" \
    -DtestCasePath=/path/to/download-folder \
    ${TOOLS_HOME}/skywalking-validator-tools.jar
```

NOTICE: the `expected data` have to call `expectedData.yaml`, and the `actual data` must name as `actualData.yaml`. And these are in the same directory, `/path/to/download-folder`.
JVM argument `caseName` just for show in the log.

The format and documentation of `expectedData.yaml` could be found in [SkyWalking plugin test doc](https://github.com/apache/skywalking/blob/master/docs/en/guides/Plugin-test.md#expecteddatayaml)

# Contact Us
* Mail list: **dev@skywalking.apache.org**. Mail to `dev-subscribe@skywalking.apache.org`, follow the reply to subscribe the mail list.
* Join `skywalking` channel at [Apache Slack](https://join.slack.com/t/the-asf/shared_invite/enQtNzc2ODE3MjI1MDk1LTAyZGJmNTg1NWZhNmVmOWZjMjA2MGUyOGY4MjE5ZGUwOTQxY2Q3MDBmNTM5YTllNGU4M2QyMzQ4M2U4ZjQ5YmY). If the link is not working, find the latest one at [Apache INFRA WIKI](https://cwiki.apache.org/confluence/display/INFRA/Slack+Guest+Invites).
* QQ Group: 392443393(2000/2000, not available), 901167865(available)

# License
Apache 2.0
