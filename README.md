Apache SkyWalking Agent Test Tool Suite
==========

<img src="http://skywalking.apache.org/assets/logo.svg" alt="Sky Walking logo" height="90px" align="right" />

[![Twitter Follow](https://img.shields.io/twitter/follow/asfskywalking.svg?style=for-the-badge&label=Follow&logo=twitter)](https://twitter.com/AsfSkyWalking)

![CI](https://github.com/apache/skywalking-nginx-lua/workflows/CI/badge.svg?branch=master)


[**SkyWalking**](https://github.com/apache/skywalking) Agent Test Tool is a tremendously useful test tools suite in a wide variety of languages of `Agent`. 
Includes `mock collector` and `validator`. The `mock collector` is a SkyWalking receiver, like OAP server. 

The `mock collector` is responsible for mocking the SkyWalking OAP receiver to collect data from the agent. It will collect data from Agent reported by `GRPC`. 
And then it can be downloaded by Http API that the mock collector collected(service registered, instance registered, and segments). 
Eventually, validate the file downloaded from `mock collector` by using `SkyWalking Validator`. 

## Apache SkyWalking Mock Collector

- requirement:
1. JDK 1.8+
2. Maven 


- How to install

The `mock collector` is written by pure-`Java`. It compiles and packages through `Maven`.


```bash
mvn package -DskipTests
unzip ./dist/mock-collector.tar.gz -d ./mock-collector
cd ./mock-collector
bash ./bin/collector-startup.sh
```

after above steps, we can check whether the `mock collector` is available throgh HTTP API. To visit `http://localhost:12800/status` and get the reponse with `success` in the body of content.

Finally, To visit follow URL to download the data as `yaml` file, which can be used by `SkyWalking Validator Tool`.

http://localhost:12800/receiveData

- How to config on agent

We can modify directly the conguration of agent, `./config/agent.conf`, likes following.

```yaml
# Backend service addresses.
collector.backend_service=${SW_AGENT_COLLECTOR_BACKEND_SERVICES:127.0.0.1:19876}
```

or specify the jvm arguments likes:

```bash
# Backend service addresses.
-DSW_AGENT_COLLECTOR_BACKEND_SERVICES=127.0.0.1:19876
```

## Apache SkyWalking Validator Tool

`Validator Tool` is a data validating tool. It is responsible for validating the `expected data` with `actual data`.

```bash
java -jar \
    -Xmx256m -Xms256m \
    -DcaseName="${SCENARIO_NAME}-${SCENARIO_VERSION}" \
    -DtestCasePath=${SCENARIO_HOME}/data/ \
    ${TOOLS_HOME}/skywalking-validator-tools.jar
```

NOTICE: the `expected data` have to call `expectedData.yaml`, and the `actual data` must name as `actualData.yaml`. 


# Contact Us
* Mail list: **dev@skywalking.apache.org**. Mail to `dev-subscribe@skywalking.apache.org`, follow the reply to subscribe the mail list.
* Join `skywalking` channel at [Apache Slack](https://join.slack.com/t/the-asf/shared_invite/enQtNzc2ODE3MjI1MDk1LTAyZGJmNTg1NWZhNmVmOWZjMjA2MGUyOGY4MjE5ZGUwOTQxY2Q3MDBmNTM5YTllNGU4M2QyMzQ4M2U4ZjQ5YmY). If the link is not working, find the latest one at [Apache INFRA WIKI](https://cwiki.apache.org/confluence/display/INFRA/Slack+Guest+Invites).
* QQ Group: 392443393(2000/2000, not available), 901167865(available)

# License
Apache 2.0
