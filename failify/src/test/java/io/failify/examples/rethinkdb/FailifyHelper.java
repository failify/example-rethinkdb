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
        return Deployment.builder("example-rethinkdb")
            .withService("rethinkdb")
                .appPath("../build-2.3.6-jessie/release", "/rethinkdb")
                .startCmd("/rethinkdb/rethinkdb --join n1:29015 --bind all --log-file /rethinkdb.log")
                .dockerImgName("failify/example-rethinkdb").dockerFileAddr("docker/Dockerfile", false)
                .logFile("/rethinkdb.log").and().nodeInstances(numOfNodes, "n", "rethinkdb", false)
            .node("n1").tcpPort(28015).startCmd("/rethinkdb/rethinkdb --bind all --log-file /rethinkdb.log").and().build();
    }

    public static Connection getConnection(FailifyRunner runner) {
        return r.connection().hostname(runner.runtime().ip("n1")).port(runner.runtime()
                .portMapping("n1", 28015, PortType.TCP)).connect();
    }
}
