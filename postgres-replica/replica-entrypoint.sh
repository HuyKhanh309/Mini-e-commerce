#!/bin/sh
set -e

echo "Checking if replica needs base backup..."

if [ ! -s "$PGDATA/PG_VERSION" ]; then
  echo "Running base backup..."

  rm -rf ${PGDATA}/*

  pg_basebackup -h postgres \
    -D ${PGDATA} \
    -U replicator \
    -P -v -R

  echo "Base backup completed."
fi

exec docker-entrypoint.sh postgres
