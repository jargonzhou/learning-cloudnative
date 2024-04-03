FROM golang:1.22 as build
COPY . /app
WORKDIR /app
RUN CGO_ENABLED=0 GOOS=linux go build -o key-value-store

FROM scratch
COPY --from=build /app/key-value-store .
COPY --from=build /app/kvs.yaml .
EXPOSE 8888
CMD ["/key-value-store"]

