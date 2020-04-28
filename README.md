# my-zookeeper

一、项目介绍

    zookeeper是一个开源的，分布式的，为分布式应用**提供协调服务的**Apache项目。

二、工作机制

    基于观察者模式的服务管理框架。
    1、存储和管理大家都关心的数据；
    2、接收观察者应用的注册；
    3、当数据发现变化时，zookeeper通知这些观察者应用。
    zookeeper = 文件系统 + 通知机制
    
三、特点

    1、一个领导者（Leader），多个跟随者（Follower）
    2、集群中，只要有半数以上的服务存活，就能正常服务
    3、全局数据一致（每一个节点）
    4、更新请求顺序执行，同一个客户端的请求，按顺序执行
    5、数据更新的原子性（事务）
    6、实时性，同步副本的时间非常短 
    
四、数据结构

    zookeeper使用树形结构（和Linux文件系统类似），每个节点称作一个ZNode，每个ZNode默认存储1M的数据。
    每一个ZNode被路径唯一标识。
    
五、应用场景

    提供的服务包括：同一命名服务、同一配置管理、同一集群管理
                    服务器节点动态上下线、软负载均衡
    1）同一命名服务
        一个域名对应多个IP地址
    2）同一配置管理
        分布式，配置信息同步。配置信息写到ZNode里面，所有客户端订阅这个配置节点。
    3）同一集群管理
        分布式环境中，zookeeper实时监控所有节点的状态。记录在ZNode里。
        所有节点订阅这个ZNode。
    4）服务器动态上下线
        每台服务器把自己的状态写到Zookeeper上，
        客户端在调用之前，去zookeeper上找到一个合适的服务器，
        再发送请求信息
    5）软负载均衡
        zookeeper记录每台服务器的访问数，让客户端请求请求最少的服务器。
        
六、下载、安装
    
    官网镜像下载
    解压
    修改配置文件
    新建数据存储目录（dataDir）
    启动zookeeper服务
    
七、配置参数解读

    tickTime=2000   心跳（毫秒）
    initLimit=10    初始化最大心跳数（follower初始化时，超时时间为10×2000）
    syncLimit=5     同步时最大心跳数（集群启动之后，数据同步时，超时时间为5×2000）
    dataDir=E:\CP\zookeeper-3.4.14\data     数据存放地址
    clientPort=2181     客户端访问的端口号
    
八、**选举机制**

    1）半数机制，适合安装奇数服务器
    2）一个Leader，其他为Follower。
        每个服务先投票给自己，没有当选的话，再投票给id号较大的那个。
        一般当选的为中间号。
        
九、节点类型
    
    持久：在客户端和zookeeper数据节点断开连接之后，数据节点依旧保持
        简单持久节点、 自动编号持久节点
    短暂：在客户端和zookeeper数据节点断开连接之后，数据节点自动删除（可以看服务器在不在线）
        简单零时节点、 自动编号临时节点
    
十、实战-分布式安装
    
    1、规划 哪台服务器上安装zookeeper（奇数台）
    2、解压安装 同步目录到其他服务器
    3、配置    唯一编号 myid       zoo.cfg配置文件  集群配置
    4、启动服务  
    5、查看服务状态
    6、关闭服务
    
十一、客户端的命令行

    help 查看所有命令
    ls  ls2
    创建持久节点：create /sanguo "三国节点数据"  create /sanguo/shu "蜀国信息"
    获取节点信息：get /sanguo/shu   
    创建短暂节点：create -e /sanguo/wu "吴国信息"   重启客户端，吴国消失
    创建带序号的节点： create -s /sanguo/wei "魏国信息"   --> created /sanguo/wei
    赋值：set /sanguo/shu '新蜀国'
    节点数据变化监听：get /sanguo watch （仅一次有效）
    监听子节点的变化：ls /sanguo watch （仅一次有效）
    删除节点：delete /sanguo/shu
    递归删除节点： rmr /sanguo
    查看节点状态：stat /sanguo
    
十二、**监听器原理**

    1）main线程入口
    2）main创建zookeeper客户端对象。会创建两个线程（一个负责网络，一个负责监听）
    3）通过网络线程（connet）发送事件消息给zookeeper（服务器）
    4）zookeeper将监听事件添加到监听列表中
    5）zookeeper监听到任何数据或节点的变化，按要求发送给列表中的客户端
    6）调用业务服务器（客户端）的方法（回调）
    
十三、写数据的流程

    1、收到写请求
    2、看自己是不是Leader，如果不是，转发给Leader
    3、Leader广播到所有Follower，写
    4、收到半数以上的成功
    5、返回给请求来源
    
十四、实战练习（test.java.org.cp.myzookeeper.MyZookeeperApplicationTests）

十五、分布式主节点动态上下线

十六、面试真题

    1、选举机制
    2、监听原理
    3、部署方式，集群中的角色，集群台数
    4、常用命令