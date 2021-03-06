# spring cloud stream demo

## Start the demo  

Start all components:

```console
$ docker-compose up --build
```

This executes all configurations set forth by the `docker-compose.yaml` file.

It's important to note that sometimes the order or shipment service may fail to start if the dependent database takes longer than expected to initialize.  If that happens, simply re-execute the command again and it will start the remaining services. 

## Configure the Debezium connector

Register the connector that to stream outbox changes from the order service: 

```console
$ http PUT http://localhost:8083/connectors/outbox-connector/config < requests/register-postgres.json
HTTP/1.1 201 Created
```

## Call the various REST-based APIs

Place a "create order" request with the order service:

```console
$ http POST http://localhost:8080/orders < requests/create-order-request.json
```

Cancel one of the two order lines:

```console
$ http PUT http://localhost:8080/orders/{orderId}/lines/{orderLineId} < requests/cancel-order-line-request.json
```

## Review the outcome

Examine the events produced by the service using _kafkacat_:

```console
$ docker run --tty --rm \
    --network spring-cloud-stream-demo_default \
    debezium/tooling:1.0 \
    kafkacat -b kafka:9093 -C -o beginning -q \
    -f "{\"key\":%k, \"headers\":\"%h\"}\n%s\n" \
    -t order.events | jq .
```

Examine that the receiving service process the events:

```console
$ docker-compose logs shipment-service
```

(Look for "Processing '{OrderCreated|OrderLineUpdated}' event" messages in the logs)

## Useful Commands

Getting a session in the Postgres DB of the "order" service:

```console
$ docker run --tty --rm -i \
        --network spring-cloud-stream-demo_default \
        debezium/tooling:1.0 \
        bash -c 'pgcli postgresql://postgresuser:postgrespw@postgres:5432/order_service_db'
```
