#!/usr/bin/env bash
set -euo pipefail

mvn -Prelease -DskipTests deploy \
  -Dnexus.releases.url="${NEXUS_RELEASES_URL:-http://localhost:8089/repository/maven-releases/}" \
  -Dnexus.snapshots.url="${NEXUS_SNAPSHOTS_URL:-http://localhost:8089/repository/maven-snapshots/}"
