#!/bin/bash

echo "$1 $2 $3"

git clone $2 $1

cd $1

git checkout $3