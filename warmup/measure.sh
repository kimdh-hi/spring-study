#!/usr/bin/env bash
# usage: ./measure.sh            (웜업 적용)
#        ./measure.sh no-warmup  (웜업 미적용)
set -euo pipefail

PROFILE=${1:-default}
JAR=$(ls build/libs/warmup-*-SNAPSHOT.jar | grep -v plain)
URL=http://localhost:8080

metric() {
  curl -s "$URL/actuator/metrics/$1" | sed -E 's/.*"value":([0-9.E]+).*/\1/'
}

java -jar "$JAR" --spring.profiles.active="$PROFILE" > "build/$PROFILE.log" 2>&1 &
PID=$!
trap 'kill $PID' EXIT

until curl -sf "$URL/actuator/health/readiness" > /dev/null; do sleep 0.1; done
CLASSES=$(metric jvm.classes.loaded)
COMPILATION=$(metric jvm.compilation.time)
echo "[$PROFILE] ready"

for i in $(seq 1 10); do
  curl -s -o /dev/null -w "request #$i %{time_total}s\n" "$URL/products?category=BOOK"
done

awk -v c="$CLASSES" -v n="$(metric jvm.classes.loaded)" -v t="$COMPILATION" -v m="$(metric jvm.compilation.time)" \
  'BEGIN { printf "readiness 이후 클래스 로딩 +%d, JIT 컴파일 +%dms\n", n - c, m - t }'
