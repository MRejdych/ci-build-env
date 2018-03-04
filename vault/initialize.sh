#!/bin/bash

IP_ADDR=$1

if [ -z "$IP_ADDR" ];
    then IP_ADDR="http://0.0.0.0:8200"
fi

. /root/vault/secrets.file

sleep 5

touch /root/.bashrc
echo export VAULT_ADDR=$IP_ADDR >> /root/.bashrc
source /root/.bashrc

vault secrets enable -path=concourse/main kv
vault write concourse/main/example_var value=$EXAMPLE_VAR


rm /root/vault/secrets.file