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

TOOL_ROOT := $(shell dirname $(realpath $(lastword $(MAKEFILE_LIST))))
CONTEXT ?= ${TOOL_ROOT}/dist
SKIP_TEST ?= false
DIST ?= skywalking-mock-collector.tar.gz

.PHONY: build.mock-collector
build.mock-collector:
	cd $(TOOL_ROOT) && ./mvnw -B -Dmaven.test.skip=$(SKIP_TEST) clean package

DOCKER_BUILD_TOP:=${CONTEXT}/docker_build

HUB ?= skywalking
MOCK_COLLECTOR_NAME ?= mock-collector
TAG ?= latest

.PHONY: docker build.mock-collector

docker: build.mock-collector docker.all

DOCKER_TARGETS:=docker.mock-collector

%.mock-collector: NAME = $(MOCK_COLLECTOR_NAME)

docker.%: PLATFORMS =
docker.%: LOAD_OR_PUSH = --load
push.%: PLATFORMS = --platform linux/amd64,linux/arm64,linux/arm/v7
push.%: LOAD_OR_PUSH = --push

docker.% push.docker.%: $(CONTEXT)/$(DIST) $(TOOL_ROOT)/docker/%/*
	$(DOCKER_RULE)

docker.all: $(DOCKER_TARGETS)
docker.push: $(DOCKER_TARGETS:%=push.%)

# $^ the name of the dependencies for the target
# Rule Steps #
##############
# 1. Make a directory $(DOCKER_BUILD_TOP)/$(NAME)
# 2. This rule uses cp to copy all dependency filenames into into $(DOCKER_BUILD_TOP/$(NAME)
# 3. This rule finally runs docker build passing $(BUILD_ARGS) to docker if they are specified as a dependency variable

define DOCKER_RULE
	mkdir -p $(DOCKER_BUILD_TOP)/$(NAME)
	cp -r $^ $(DOCKER_BUILD_TOP)/$(NAME)
	docker buildx create --use --driver docker-container --name skywalking_main > /dev/null 2>&1 || true
	docker buildx build $(PLATFORMS) $(LOAD_OR_PUSH) \
		--no-cache $(BUILD_ARGS) \
		-t $(HUB)/$(NAME):$(TAG) \
		-t $(HUB)/$(NAME):latest \
		$(DOCKER_BUILD_TOP)/$(NAME)
	docker buildx rm skywalking_main || true
endef
