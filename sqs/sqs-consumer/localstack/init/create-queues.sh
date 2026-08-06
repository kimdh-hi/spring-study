#!/bin/bash
awslocal sqs create-queue --queue-name order-queue
awslocal sqs create-queue --queue-name order-queue.fifo \
  --attributes FifoQueue=true,ContentBasedDeduplication=false
