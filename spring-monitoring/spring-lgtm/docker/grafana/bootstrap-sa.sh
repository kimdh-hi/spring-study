#!/bin/sh
set -eu

TOKEN_FILE=/shared/token
AUTH="$ADMIN_USER:$ADMIN_PASSWORD"

echo "waiting for grafana at $GRAFANA_URL ..."
until curl -sf "$GRAFANA_URL/api/health" >/dev/null 2>&1; do
  sleep 2
done

SA_ID=$(curl -s -u "$AUTH" -H "Content-Type: application/json" \
  -X POST "$GRAFANA_URL/api/serviceaccounts" \
  -d '{"name":"mcp","role":"Admin","isDisabled":false}' \
  | sed -n 's/.*"id":\([0-9]*\).*/\1/p')

if [ -z "$SA_ID" ]; then
  SA_ID=$(curl -s -u "$AUTH" "$GRAFANA_URL/api/serviceaccounts/search?query=mcp" \
    | sed -n 's/.*"id":\([0-9]*\).*/\1/p' | head -n1)
fi

if [ -z "$SA_ID" ]; then
  echo "failed to resolve service account id"
  exit 1
fi

TOKEN=$(curl -s -u "$AUTH" -H "Content-Type: application/json" \
  -X POST "$GRAFANA_URL/api/serviceaccounts/$SA_ID/tokens" \
  -d "{\"name\":\"mcp-$(date +%s)\"}" \
  | sed -n 's/.*"key":"\([^"]*\)".*/\1/p')

if [ -z "$TOKEN" ]; then
  echo "failed to create service account token"
  exit 1
fi

printf '%s' "$TOKEN" > "$TOKEN_FILE"
echo "service account token written to $TOKEN_FILE (sa_id=$SA_ID)"
