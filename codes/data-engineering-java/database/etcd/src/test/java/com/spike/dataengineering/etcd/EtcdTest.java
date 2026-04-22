package com.spike.dataengineering.etcd;

import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Client;
import io.etcd.jetcd.KV;
import io.etcd.jetcd.Txn;
import io.etcd.jetcd.kv.DeleteResponse;
import io.etcd.jetcd.kv.GetResponse;
import io.etcd.jetcd.kv.PutResponse;
import io.etcd.jetcd.kv.TxnResponse;
import io.etcd.jetcd.op.Cmp;
import io.etcd.jetcd.op.CmpTarget;
import io.etcd.jetcd.op.Op;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;
import io.etcd.jetcd.options.TxnOption;
import io.etcd.jetcd.test.EtcdClusterExtension;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

// jetcd: https://github.com/etcd-io/jetcd
// etcdctl: https://github.com/etcd-io/etcd/tree/main/etcdctl
// compare to Apache ZooKeeper, Consul: https://www.baeldung.com/java-etcd-guide
public class EtcdTest {

    // see also: https://testcontainers.com/modules/etcd/
    @RegisterExtension
    public static final EtcdClusterExtension cluster = EtcdClusterExtension.builder()
            // quay.io/coreos/etcd:v3.5.14
            // .withImage("bitnami/etcd:3.5.21")
            .withNodes(1)
            .build();

    @Test
    public void testClient() {
        try (Client c = Client.builder()
                .endpoints(cluster.clientEndpoints())
                .build()) {
            Assertions.assertThat(c).isNotNull();
        }
    }

    private static Client client;

    @BeforeAll
    public static void beforeAll() {
        client = Client.builder()
                .endpoints(cluster.clientEndpoints())
                .namespace(ByteSequence.from("test/".getBytes())) // namespace
                .build();
    }

    @AfterAll
    public static void afterAll() {
        if (Objects.nonNull(client)) {
            client.close();
        }
    }

    /**
     * @see Client#getKVClient()
     */
    @Test
    public void CRUD() throws ExecutionException, InterruptedException {
        try (KV kvClient = client.getKVClient()) {
            ByteSequence key = ByteSequence.from("test_key".getBytes());
            ByteSequence value = ByteSequence.from("test_value".getBytes());

            // put
            PutResponse putResponse = kvClient.put(key, value).get();
            System.err.println("PUT: " + putResponse);

            // get
            kvClient.get(key).whenComplete(((getResponse, throwable) -> {
                if (Objects.nonNull(throwable)) {
                    throwable.printStackTrace();
                } else {
                    System.err.println("GET: " + getResponse);
                }
            })).get();
            kvClient.get(ByteSequence.EMPTY, GetOption.builder().isPrefix(true).build()).whenComplete(((getResponse, throwable) -> {
                if (Objects.nonNull(throwable)) {
                    throwable.printStackTrace();
                } else {
                    System.err.println("GET: " + getResponse);
                }
            })).get();

            // delete
            DeleteResponse deleteResponse = kvClient.delete(key).get();
            System.err.println("DELETE: " + deleteResponse);

            // get
            GetResponse getResponse = kvClient.get(key).get();
            System.err.println("GET: " + getResponse);
        }
    }

    //
    // etcd API: https://etcd.io/docs/v3.5/learning/api/
    // https://etcd.io/docs/v3.5/dev-guide/api_reference_v3/
    //
    // key space:
    // KV
    // Watch
    // Lease
    //
    // cluster:
    // Auth
    // Cluster
    // Maintenance


    // example: https://github.com/etcd-io/jetcd/blob/main/jetcd-core/src/test/java/io/etcd/jetcd/impl/KVTest.java
    @Test
    public void txn() throws ExecutionException, InterruptedException {
        try (KV kvClient = client.getKVClient()) {
            ByteSequence txnKey = ByteSequence.from("txn_key".getBytes());
            ByteSequence txnValue = ByteSequence.from("xyz".getBytes());

            ByteSequence cmpValue = ByteSequence.from("abc".getBytes());

            ByteSequence putValue = ByteSequence.from("XYZ".getBytes());
            ByteSequence putValueElse = ByteSequence.from("ABC".getBytes());

            // setup old value: xyz
            kvClient.put(txnKey, txnValue).get();

            Txn txn = kvClient.txn(TxnOption.DEFAULT);
            // > abc
            Cmp cmp = new Cmp(txnKey, Cmp.Op.GREATER, CmpTarget.value(cmpValue));
            // if: > abc
            // then: set to XYZ <-
            // else: set to ABC
            CompletableFuture<TxnResponse> cf = txn.If(cmp)
                    .Then(Op.put(txnKey, putValue, PutOption.DEFAULT))
                    .Else(Op.put(txnKey, putValueElse, PutOption.DEFAULT))
                    .commit();
            cf.get();

            GetResponse getResponse = kvClient.get(txnKey).get();

            Assertions.assertThat(getResponse.getKvs()).hasSize(1);
            Assertions.assertThat(getResponse.getKvs().get(0).getValue().toString()).isEqualTo(putValue.toString());
        }
    }


    @Test
    public void watch() {

    }

    @Test
    public void lease() {

    }
}
