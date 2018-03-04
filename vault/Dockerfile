FROM vault

ENV VAULT_ADDR "http://0.0.0.0:8200"

RUN apk update && apk add bash bash-doc bash-completion screen && mkdir /root/vault

ADD ./initialize.sh /root/vault/initialize.sh
ADD ./secrets.file /root/vault/secrets.file

EXPOSE 8200

ENTRYPOINT screen -s bash -dmS init /root/vault/initialize.sh $VAULT_ADDR && \
            vault server -dev -dev-listen-address="0.0.0.0:8200"