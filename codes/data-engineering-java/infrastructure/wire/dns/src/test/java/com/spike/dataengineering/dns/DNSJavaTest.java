package com.spike.dataengineering.dns;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.xbill.DNS.Record;
import org.xbill.DNS.*;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.ExecutionException;

// examples: https://github.com/dnsjava/dnsjava/blob/master/EXAMPLES.md
public class DNSJavaTest {

    private final String DNS_GOOGLE = "8.8.8.8";
    // CoreDNS: https://github.com/jargonzhou/application-store/tree/main/ops/coredns
    private final String DNS_LOCAL = "192.168.3.178";

    /**
     * <pre>
     * @see Message
     *
     * @see Header
     *
     * @see Record
     * @see SRVRecord
     * @see ARecord
     * </pre>
     */
    @Test
    public void lookupWithResolver() throws TextParseException, UnknownHostException, ExecutionException, InterruptedException {
        // case 1: IPv4
        // Record queryRecord = Record.newRecord(Name.fromString("dnsjava.org."), Type.A, DClass.IN);
        // case 2: SRV
        Record queryRecord = Record.newRecord(Name.fromString("users.services.devops.org."), Type.SRV, DClass.IN);
        Message queryMessage = Message.newQuery(queryRecord);
        Resolver r = new SimpleResolver(new InetSocketAddress(DNS_LOCAL, 1053));
        r.sendAsync(queryMessage)
                .whenComplete(
                        (answer, ex) -> {
                            if (ex == null) {
                                System.out.println(answer);
                                Assertions.assertThat(answer).isNotNull();
                                // Count: 0-query/zone, 1-answer/prerequisite, 2-authority record/update, 3-additional information
                                Header header = answer.getHeader();
                                for (int i = 0; i < 4; i++) {
                                    int count = header.getCount(i);
                                    System.err.println(count);
                                    if (count > 0) {
                                        List<Record> list = answer.getSection(i);
                                        for (Record record : list) {
                                            System.err.println(record);
                                        }
                                    }
                                }
                            } else {
                                ex.printStackTrace();
                            }
                        })
                .toCompletableFuture()
                .get();
    }
}
