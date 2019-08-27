#!bash -x

set -e

tok=$(curl -X POST --data-ascii "thisismykey" http://127.0.0.1:9000/add/w@e.com)
curl "http://127.0.0.1:9000/add!/$tok"

tok=$(curl -X POST --data-ascii "yekymsisiht" http://127.0.0.1:9000/add/q@b.eu)
curl "http://127.0.0.1:9000/add!/$tok"

curl "http://127.0.0.1:9000/get/w@e.com"
curl "http://127.0.0.1:9000/get/q@b.eu"

tok=$(curl -X POST --data-ascii "thisismyotherkey" http://127.0.0.1:9000/add/w@e.com)
curl "http://127.0.0.1:9000/add!/$tok"
curl "http://127.0.0.1:9000/get/q@b.eu"

tok=$(curl -X POST --data-ascii "thisismykey" http://127.0.0.1:9000/del/w@e.com)

tok=$(curl -X POST --data-ascii "yekymsisiht" http://127.0.0.1:9000/del/q@b.eu)
curl "http://127.0.0.1:9000/del!/$tok"
