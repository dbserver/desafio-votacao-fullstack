#!/bin/bash
if [ -z "$BASH_VERSION" ]; then
  echo "Este script deve ser executado com bash, e não com sh!"
  exit 1
fi

send_vote() {
  INDEX=$1
  CPF=$(sed -n "${INDEX}p" cpfs.txt)

  if [ -z "$CPF" ]; then
    TOTAL_CPFS=$(wc -l < cpfs.txt)
    INDEX=$(( (INDEX - 1) % TOTAL_CPFS + 1 ))
    CPF=$(sed -n "${INDEX}p" cpfs.txt)
  fi

  DISCUSS_ID=23
  VOTE_OPTION=$((RANDOM % 2))

  if [ "$VOTE_OPTION" -eq 0 ]; then
    VOTE="sim"
  else
    VOTE="não"
  fi

  RESPONSE=$(curl -s -w "HTTPSTATUS:%{http_code}" -X POST "http://localhost:8080/api/vote" \
    -H "accept: */*" \
    -H "Content-Type: application/json" \
    -d "{\"associatedCpf\": \"${CPF}\", \"discussId\": ${DISCUSS_ID}, \"vote\": \"${VOTE}\"}")

  BODY=$(echo "$RESPONSE" | sed -e 's/HTTPSTATUS\:.*//g')
  STATUS=$(echo "$RESPONSE" | tr -d '\n' | sed -e 's/.*HTTPSTATUS://')

  echo -e "\n[$INDEX] CPF: ${CPF} - Status: $STATUS"
  echo "[$INDEX] Body: $BODY"
}

export -f send_vote

TOTAL_REQUESTS=400
MAX_PARALLEL=50

seq 1 $TOTAL_REQUESTS | xargs -n1 -P$MAX_PARALLEL -I{} bash -c "send_vote {}"

