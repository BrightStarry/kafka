#### Kafka入门

#### 奇淫巧技
* IDEA自动提示快捷键(CTRL + 句号).. 我之所以现在才去记录它..是因为IDEA太智能..大部分时候不需要主动使用.
#### 介绍 
* Kafka是一个分布式流媒体平台.
        
        流媒体平台的功能:
            1. 发布和订阅记录流.这方面,它类似于MQ
            2. 可以以容错的方式存储记录流
            3. 可以在发生记录时处理记录流.
        它被应用于两大类的应用程序:
            1. 构建在应用间,获取数据的实时流数据通道.
            2. 构建实时流数据应用.可以转换或响应数据流.

* Topic And Logs (主题和日志)  

        主题是分配消息发送的一种类别划分.一个主题可以有任意个消费者.
        对于每个主题,Kafka可以有若干个分区,每个分区维护一个分区日志.  
        每个分区是一个有序的，不可变的记录序列，不断追加到结构化的提交日志中。
        分区中的每条消息有连续的id号，称为偏移量，用于唯一标识分区内的每条记录。
        分区无法并发.所以.消费者数量不要大于分区数.
        最好分区数是消费者数的整数倍,否则轮询时,每个消费者的负载可能不均匀
        
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


#### Spring Kafka API 基础
不和spring整合的最基础的配置
* 导入依赖
>
		<dependency>
			<groupId>org.springframework.kafka</groupId>
			<artifactId>spring-kafka</artifactId>
			<version>2.1.0.RELEASE</version>
		</dependency>    
>
* 具体代码 见base

#### Spring Kafka API   with  Java Configuration
和spring整合,使用java configuration配置
* 代码见spring包

* 此处遇到一些问题,主要在于我用的是spring boot.kafka已经做了一些整合.  
例如在没有某些bean的时候创建某些bean.有的时候使用用户自定的等等.  
暂时不处理了.

#### Spring Boot Kafka API 
* 看了篇博客. 笔者遇到的问题是: kafka并发过大,消费者拉取一批记录,处理时常超过sessionTimeoutMsConfig,导致消费者被服务端剔除.  
此时因为开启了自动提交offset.然后因为消费者被提出了,所以提交失败.  
重新连接后,获取到了重复数据,处理再次超时,如此循环.  
他不关闭自动提交,使用spring-kafka提供的listener的ack-Mode.  
https://www.jianshu.com/p/4e00dff97f39  
再细看了下.简单来说.用spring listener处理提交offset的话.spring kafka在消费者和kafka之间多了一个阻塞队列.  
当队列满了.停止获取.可以理解为RabbitMQ的Qos策略.

* 配置yml
>
    spring:
      kafka:
        bootstrap-servers: 106.14.7.29:9092 #连接地址(也可给生产者/消费者单独配置)
        #发送模版配置
        template:
          default-topic: test1 #默认发送的主题(可以在发送方法中发送给其他主题)
        #监听器配置
        listener:
          #偏移量提交模式(AckMode类) RECORD:每条记录发送后提交;  BATCH(默认):每批记录(每次poll提交);
          # TIME:每间隔ack-time,提交; COUNT:累积到ack-count条记录提交一次; COUNT_TIME:COUNT或TIME满足其一就提交;
          # MANUAL:手动提交(但也是批量的); MANUAL_IMMEDIATE:立即手动提交(每手动提交一次,就马上提交)
          ack-mode: BATCH
          ack-count: 100 # COUNT或COUNT_TIME模式.累计条数
          ack-time: 1000 # TIME或OUNT_TIME模式.间隔时间.
          concurrency: 10 # 监听容器中运行的线程数
          poll-timeout: 3000 # 每次poll()的超时时间(ack-time应该小于这个值)
        #消费者配置
        consumer:
          enable-auto-commit: true #是否自动提交消费者的offset(偏移量),也就是该消费者目前在分区日志中的位置
          auto-commit-interval: 100 #自动提交消费者偏移量的间隔,毫秒. 如果ENABLE_AUTO_COMMIT_CONFIG属性开启,它就会生效
          client-id: 1 #客户端id,发送请求时,会将该值传送到服务端.记录到日志中去
          # 消费者没有初始化的偏移量,或者当前偏移量不存在,几种策略
          # latest:最新的偏移量; earliest:最早的偏移量; exception:抛出异常; none:什么都不做.
          auto-offset-reset: earliest
          fetch-max-wait: 1000 # 每次获取请求,最大等待时间
          fetch-min-size: 1 # 每次获取请求.服务端应返回的最小数据量(字节)
          group-id: consumer-group-1 # 消费者所属的消费者组,唯一
          key-deserializer: org.apache.kafka.common.serialization.IntegerDeserializer # key反序列化器(此处自动提示要等待会才出来)
          value-deserializer: org.apache.kafka.common.serialization.StringDeserializer # value 反序列化器
          max-poll-records: 99999 #单次调用 poll()方法能获取的最大记录数
          heartbeat-interval: 20000 # 心跳检测间隔,需要小于session.timeout.ms
        #生产者配置
        producer:
          acks: 1 #生产者确认模式  发送消息时,  0:不等待服务端确认收到消息; 1:等待服务端确认收到消息; -1:等待服务器确认收到消息并转发到从服务器完成;
          batch-size: 100  # 批量发送时,每批数量
          buffer-memory: 10240000 #生产者可以缓存的.等待被发送的记录的总字节数
          client-id: 2 #客户端id,发送请求时,会将该值传送到服务端.记录到日志中去
          retries: 3 #发送失败时,可重试次数,为0时不重试
          compression-type: none  # 压缩类型， none(默认):不压缩;   可选: gzip snappy lz4
          key-serializer: org.apache.kafka.common.serialization.IntegerSerializer  # key序列化器
          value-serializer: org.apache.kafka.common.serialization.StringSerializer # value 反序列化器
        #可传入Map类型参数,配置其他属性(例如这个session.timeout.ms)
        properties:
          # 这些属性的key,可以参照ConsumerConfig和ProducerConfig类的 字段值
          session.timeout.ms: 30000 #客户端响应服务端的心跳检测的超时时间,超时后被提出
>

* 生产者和消费者
>
    @Slf4j
    @Component
    public class Kafka {
    	//发送模版
    	@Autowired
    	private KafkaTemplate<Integer,String> kafkaTemplate;
    
    	//发送
    	public void send() throws InterruptedException {
    		for (int i = 0; i < 300; i++) {
    			kafkaTemplate.send("topic1", "这是一条消息." + i);
    
    		}
    	}
    
    	//监听该主题的消息
    	@KafkaListener(topics = "topic1")
    	public void listen(ConsumerRecord<Integer,String> consumerRecord) throws InterruptedException {
    		log.info("收到消息:{}",consumerRecord);
    		Thread.sleep(500);
    	}
    }
>

* @KafkaListener 该注解可以配置   (详见该类注解) topics/topicPattern/topicPartitions 三者互斥
containerFactory: 哪个消息监听器容器生成该监听器
topics: 要监听的若干主题.
topicPattern: 可以是主题名/属性占位符或表达式
topicPartitions: 要监听的若干主题分区,并可初始化偏移量
group: 如果提供了.这些监听器都会被添加到以该值为name的bean中.MessageListenerContainer. 可批量删除等.

* 如下代码,发送时指定了发送到哪个分区,消费监听时也指定了分区
>
    //1:主题 2:分区索引 3.消息
    kafkaTemplate.send("topic1", 0,"这是一条消息." + i);

    //监听该主题的消息
    @KafkaListener(topicPartitions = {@TopicPartition(topic = "topic1",partitions = "1")})
    public void listen(ConsumerRecord<Integer,String> consumerRecord) throws InterruptedException {
    	log.info("1分区 - 收到消息:{}",consumerRecord);
    	Thread.sleep(500);
    }
   
    //监听该主题的消息
    @KafkaListener(topicPartitions = {@TopicPartition(topic = "topic1",partitions = "0")})
    public void listen2(ConsumerRecord<Integer,String> consumerRecord) throws InterruptedException {
    	log.info("0分区 - 收到消息:{}",consumerRecord);
    	Thread.sleep(500);
    }
>

                
        
3、kafkaTemplate发送消息时如果topic不存在，则默认创建server.properties配置的partition的个数，可以指定发送到那个partition，没有指定则采用轮询方式

    
