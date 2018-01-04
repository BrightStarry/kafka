#### Kafka入门


#### 介绍 

* Topic And Logs (主题和日志)  

        主题是分配消息发送的一种类别划分.一个主题可以有任意个消费者.
        对于每个主题,Kafka可以有若干个分区,每个分区维护一个分区日志.  
        每个分区是一个有序的，不可变的记录序列，不断追加到结构化的提交日志中。
        分区中的每条消息有连续的id号，称为偏移量，用于唯一标识分区内的每条记录。
        
        kafka会保留消费者在日志中的偏移量.该偏移量可以由消费者控制.所以可以随机读写.
        
        Kafka集群可以持久化所有消息.并可以设置过期时间,到期后删除持久化消息.
    ![](img/1.png)

* Distribution(分区)
        
        kafka的分区分布在集群的各台机器上,共享.并可配置指定数量的服务器进行复制,以便容错.
        每个分区有一个leader服务器,0个或多个followers服务器.即主从集群,并且主节点宕机后会自动选举从节点为新主节点.
        
* Producers(生产者)
        
        发送消息到指定主题.负责选择将消息分配给主题的哪个分区.通常为轮询.或者根据某些语义分区(例如消息中的某个key)

* Consumers(消费者)
        
        每个消费者都属于某个Consumer Grouop(消费者组),发送到主题的每条消息被传递到每个订阅了该主题的 消费者组的一个消费者实例中.  
        如果所有消费者实例具有相同的消费者组，则消息将有效地在消费者实例上负载平衡。 
        如果所有消费者实例具有不同的消费者组，则每个消息将被广播给所有消费者进程。

#### 安装
1. 解压
2. 配置./conf/server.properties  如下是默认值
>
    #每个节点唯一的id
    broker.id=0
    #日志位置
    log.dirs=/tmp/kafka-logs
    #zookeeper连接地址,多个地址用','分割   
    zookeeper.connect=localhost:2181
    
    # 监听(就是该节点的ip或hostname 和 port)
    # 如下配置,PLAINTEXT(明文)表示不对该ip和port加密.
    listeners=PLAINTEXT://0.0.0.0:9092
>
3. 运行自己的zookeeper或kafka自带的zookeeper
>
    如果是自带的
    ./bin/zookeeper-server-start.sh config/zookeeper.properties
    
    如果是自己的..
    zkServer.sh start
>
4. 启动kafka,必须指定配置文件位置,默认端口 9092
> 
    ./bin/kafka-server-start.sh config/server.properties & 
    
    停止
    ./bin/kafka-server-stop.sh
>
5. 测试
>
    创建一个 1个副本的 一个分区的 名为test 的主题
    bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic test
    
    查看所有主题
    bin/kafka-topics.sh --list --zookeeper localhost:2181
    
    运行生产者,在控制台发送一些消息到test主题 CTRL+C退出
    bin/kafka-console-producer.sh --broker-list localhost:9092 --topic test
    
    运行消费者,该消费者只是将消息输出到控制台
    bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test --from-beginning
>
6. 集群配置
>
    需要配置各台机器 
    broker.id=0 1 2
    这个就只配自己的就行了,是通过zk通信的
    listeners=PLAINTEXT://0.0.0.0:9092 
    
    查看每个节点的主从身份
    bin/kafka-topics.sh --describe --zookeeper localhost:2181 --topic my-replicated-topic
>

#### Kafka Connect 导入/导出数据
* 如下.生成测试文件,将'zx\xx\dc\df'输出到test.txt中
> echo -e 'zx\xx\dc\df' > test.txt
* 开启连接器  
该命令会创建两个连接器.第一个是源连接器，用于读取输入文件中的行，并将每个连接生成为Kafka主题，第二个为连接器它从Kafka主题读取消息，并在输出文件中产生每行消息。
connect-file-source.properties这个文件配置了读取test.txt文件中的数据.
connect-file-sink.properties配置了将数据输出到test.sink.txt文件
> bin/connect-standalone.sh config/connect-standalone.properties config/connect-file-source.properties config/connect-file-sink.properties
* 另起Linux窗口查看输出文件
> more test.sink.txt
* 还可以查看根据文件新建出来的主题
> bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic connect-test --from-beginning
* 如下语句可以将"Another line"追加到文件
> echo Another line>> test.txt
* 然后就可以再次在输出文件中查看到追加的内容


 

                
        


    
