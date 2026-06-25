#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
    CREATE DATABASE db_catg;
    CREATE DATABASE db_prm;
    CREATE DATABASE db_mim;
EOSQL

echo "Bases de datos creadas: db_catg, db_prm, db_mim"
