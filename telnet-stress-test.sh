#!/bin/bash
# Create a loop to send requests to the ports.
for port in {8000..8012}; do
  # Send a request to the port using telnet.
  telnet localhost "$port" << EOF
Mensagem que você quer enviar
Outra linha da mensagem
EOF
done

