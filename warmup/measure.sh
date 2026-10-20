#!/usr/bin/env bash
# usage: ./measure.sh            (웜업 적용)
#        ./measure.sh no-warmup  (웜업 미적용)
set -euo pipefail

PROFILE=${1:-default}
JAR=$(ls build/libs/warmup-*-SNAPSHOT.jar | grep -v plain)
URL=http://localhost:8080

java -jar "$JAR" --spring.profiles.active="$PROFILE" > "build/$PROFILE.log" 2>&1 &
PID=$!
trap 'kill $PID' EXIT

until curl -sf "$URL/actuator/health/readiness" > /dev/null; do sleep 0.1; done
echo "[$PROFILE] ready"

for i in $(seq 1 10); do
  curl -s -o /dev/null -w "request #$i %{time_total}s\n" "$URL/products?category=BOOK"
done
