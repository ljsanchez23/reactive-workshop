#!/bin/bash

export AWS_ACCESS_KEY_ID=fakeAccessKey
export AWS_SECRET_ACCESS_KEY=fakeSecretKey
export AWS_DEFAULT_REGION=us-east-1

aws --endpoint-url=http://localhost:4566 sqs create-queue --queue-name user-created-events

aws --endpoint-url=http://localhost:4566 dynamodb create-table --table-name users --attribute-definitions AttributeName=id,AttributeType=N --key-schema AttributeName=id,KeyType=HASH --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5

