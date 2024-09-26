#!/bin/bash

set -e
set -u

function create_databases() {
    database=$1
    user=$2
    password=$3
    echo "Creating database '$database' and user '$user' with password '$password'"
    psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
      CREATE USER $user WITH ENCRYPTED PASSWORD '$password';
      GRANT ALL ON SCHEMA public TO $user;
      CREATE DATABASE $database;
      GRANT ALL PRIVILEGES ON DATABASE $database TO $user;
      ALTER DATABASE $database OWNER TO $user
EOSQL
}

if [ -n "$POSTGRES_MULTIPLE_DATABASES" ]; then
  echo "Multiple database creation requested: $POSTGRES_MULTIPLE_DATABASES"
  for db in $(echo "$POSTGRES_MULTIPLE_DATABASES" | tr ',' ' '); do
    database=$(echo "$db" | awk -F":" '{print $1}')
    user=$(echo "$db" | awk -F":" '{print $2}')
    password=$(echo "$db" | awk -F":" '{print $3}')
    if [[ -z "$password" ]]
    then
      password=$user
    fi

    echo "database is $database user is $user and password is $password"
    create_databases "$database" "$user" "$password"
  done
  echo "Multiple databases created!"
fi