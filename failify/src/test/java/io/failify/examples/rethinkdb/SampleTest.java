package io.failify.examples.rethinkdb;

import com.rethinkdb.RethinkDB;
import com.rethinkdb.net.Connection;
import com.rethinkdb.net.Cursor;
import io.failify.FailifyRunner;
import io.failify.dsl.entities.Deployment;
import io.failify.dsl.entities.PortType;
import io.failify.exceptions.RuntimeEngineException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.Assert.*;

import java.util.Map;

public class SampleTest {
    private static final Logger logger = LoggerFactory.getLogger(SampleTest.class);
    public static final RethinkDB r = RethinkDB.r;

    protected static FailifyRunner runner;

    @BeforeClass
    public static void before() throws InterruptedException {
        Deployment deployment = FailifyHelper.getDeployment(3);
        runner = FailifyRunner.run(deployment);
        // Wait for the cluster to start up
        Thread.sleep(1000);
        logger.info("The cluster is UP!");
    }

    @AfterClass
    public static void after() {
        if (runner != null) {
            runner.stop();
        }
    }

    @Test
    public void sampleTest() throws RuntimeEngineException {
        Connection conn = r.connection().hostname(runner.runtime().ip("n1")).port(runner.runtime()
                .portMapping("n1", 28015, PortType.TCP)).connect();

//        Example of how to impose a network partition
//        NetPart netPart = NetPart.partitions("n1", "n2,n3").build();
//        runner.runtime().networkPartition(netPart);

        r.db("test").tableCreate("table1").run(conn);
        r.table("table1").insert(r.hashMap("key", "value")).run(conn);

//        Example of how to remove a network partition
//        runner.runtime().removeNetworkPartition(netPart);

        Cursor<Map> cursor = r.table("table1").run(conn);
        assertEquals(cursor.next().getOrDefault("key", "not_test"), "value");
    }
}
