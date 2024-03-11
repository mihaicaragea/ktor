cd $(dirname $0)
docker run -i --rm -v .:/app -w /app grafana/k6 run script.js