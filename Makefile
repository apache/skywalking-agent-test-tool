# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

SHELL := /bin/bash -o pipefail

export TOOLS_ROOT := $(shell dirname $(realpath $(lastword $(MAKEFILE_LIST))))

export TOOLS_OUT:=${TOOLS_ROOT}/dist

SKIP_TEST?=false

HUB?=skywalking

TAG?=latest

DOCKER_BUILD_TOP:=${TOOLS_OUT}/docker_build

DOCKER_TARGETS:=docker.mock-collector

.PHONY: build docker docker.mock docker.push

build:
	cd $(TOOLS_ROOT) && ./mvnw -B -Dmaven.test.skip=$(SKIP_TEST) clean package

docker: build docker.mock-collector

docker.mock-collector: $(TOOLS_OUT)/skywalking-mock-collector.tar.gz
docker.mock-collector: $(TOOLS_ROOT)/docker/Dockerfile.mock-collector
		$(DOCKER_RULE)

# $@ is the name of the target
# $^ the name of the dependencies for the target
# Rule Steps #
##############
# 1. Make a directory $(DOCKER_BUILD_TOP)/%@
# 2. This rule uses cp to copy all dependency filenames into into $(DOCKER_BUILD_TOP/$@
# 3. This rule then changes directories to $(DOCKER_BUID_TOP)/$@
# 4. This rule runs $(BUILD_PRE) prior to any docker build and only if specified as a dependency variable

DOCKER_RULE=time (mkdir -p $(DOCKER_BUILD_TOP)/$@ && cp -r $^ $(DOCKER_BUILD_TOP)/$@ && cd $(DOCKER_BUILD_TOP)/$@ && $(BUILD_PRE) docker build --no-cache -t $(HUB)/$(subst docker.,,$@):$(TAG) -f Dockerfile$(suffix $@) .)

# for each docker.XXX target create a push.docker.XXX target that pushes
# the local docker image to another hub
# a possible optimization is to use tag.$(TGT) as a dependency to do the tag for us
$(foreach TGT,$(DOCKER_TARGETS),$(eval push.$(TGT): | $(TGT) ; \
	time (docker push $(HUB)/$(subst docker.,,$(TGT)):$(TAG))))

# create a DOCKER_PUSH_TARGETS that's each of DOCKER_TARGETS with a push. prefix
DOCKER_PUSH_TARGETS:=
$(foreach TGT,$(DOCKER_TARGETS),$(eval DOCKER_PUSH_TARGETS+=push.$(TGT)))

# Will build and push docker images.
docker.push: $(DOCKER_PUSH_TARGETS)