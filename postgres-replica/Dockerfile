FROM postgres:16-alpine

COPY replica-entrypoint.sh /usr/local/bin/replica-entrypoint.sh

ENTRYPOINT ["sh", "/usr/local/bin/replica-entrypoint.sh"]
