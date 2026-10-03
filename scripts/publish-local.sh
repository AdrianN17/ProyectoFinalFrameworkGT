#!/usr/bin/env bash
set -euo pipefail

WITH_TESTS="${1:-}"
if [ "$WITH_TESTS" = "--with-tests" ]; then
  mvn clean install
else
  mvn -DskipTests clean install
fi
