#!/bin/bash

for port in {9020..9025}
do
  for i in {1..100}
  do
    payload='{"key": "value"}'  # Substitua pelo seu payload real
    url="http://localhost:${port}/test-gateway"  # Substitua pela sua URL real

    echo "Port: ${port} - Request ${i} - Payload: ${payload}"
  done
done
