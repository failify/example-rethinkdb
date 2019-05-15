package io.failify.examples.rethinkdb;

import com.rethinkdb.RethinkDB;
import com.rethinkdb.net.Connection;
import io.failify.FailifyRunner;
import io.failify.dsl.entities.Deployment;
import io.failify.dsl.entities.PortType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FailifyHelper {
    public static final Logger logger = LoggerFactory.getLogger(FailifyHelper.class);
    public static final RethinkDB r = RethinkDB.r;

    public static Deployment getDeployment(int numOfNodes) {
        Deployment.Builder builder = Deployment.builder("example-rethinkdb")
                .withService("rethinkdb")
                    .applicationPath("../build-2.3.6-jessie/release", "/rethinkdb")
                    .startCommand("/rethinkdb/rethinkdb --bind all --log-file /rethinkdb.log")
                    .dockerImageName("failify/example-rethinkdb").dockerFileAddress("docker/Dockerfile", false)
                    .logFile("/rethinkdb.log").and()
                .withNode("n1", "rethinkdb").tcpPort(28015).and();
        for (int i = 2; i <= numOfNodes; i++) builder.withNode("n" + i, "rethinkdb")
                .startCommand("/rethinkdb/rethinkdb --join n1:29015 --bind all --log-file /rethinkdb.log").and();
        return builder.build();
    }

    public static Connection getConnection(FailifyRunner runner) {
        return r.connection().hostname(runner.runtime().ip("n1")).port(runner.runtime()
                .portMapping("n1", 28015, PortType.TCP)).connect();
    }
}
