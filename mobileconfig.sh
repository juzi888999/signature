#!/bin/bash

openssl smime -sign -in $1 -out $2 -signer server.crt -inkey server.key -certfile cert-chain.crt -outform der -nodetach