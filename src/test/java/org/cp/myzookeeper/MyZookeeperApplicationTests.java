package org.cp.myzookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.event.annotation.BeforeTestExecution;

import java.io.IOException;
import java.lang.annotation.Target;
import java.util.List;

@SpringBootTest
class MyZookeeperApplicationTests {

    private String urls = "192.168.0.8:2181,192.168.0.9:2181";//集群
    private String url = "192.168.0.8:2181";

    ZooKeeper zooKeeper;

    void init() throws IOException {
        zooKeeper = new ZooKeeper(url, 15000, watchedEvent -> {
            //监听
            try {
                List<String> children = zooKeeper.getChildren("/cp", true);
                System.out.println(children);
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println("zookeeper初始化成功");
    }

    @Test
    void zkTest() throws KeeperException, InterruptedException, IOException {
        init();//初始化
        Thread.sleep(1000);
        //创建
        String path = zooKeeper.create("/cp", "程鹏的信息".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println(path);
        //获取子节点
        List<String> children = zooKeeper.getChildren("/cp", false);
        //判断是否存在
        Stat exists = zooKeeper.exists("/cp/zj", false);

    }

}
