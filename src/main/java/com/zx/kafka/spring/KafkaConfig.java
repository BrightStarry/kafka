package com.zx.kafka.spring;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import org.springframework.kafka.listener.config.ContainerProperties;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * author:ZhengXing
 * datetime:2018/1/5 0005 14:17
 * 配置类
 */
//@Configuration
//@EnableKafka
//@Slf4j
public class KafkaConfig {

//	//主题
//	private static final String topic1 = "topic1";
//	private static final String topic2 = "topic2";
//
//	/**
//	 * 消费者监听器容器 属性
//	 */
//	@Order(1)
//	@Bean
//	public ContainerProperties containerProperties() {
//		return new ContainerProperties(topic1, topic2);
//	}
//
//
//	/**
//	 * 消费者监听器容器工厂
//	 */
//	@Order(1)
//	@Bean
//	public ConsumerFactory<Integer,String> consumerFactory() {
//		return new DefaultKafkaConsumerFactory<>(consumePropMap());
//	}
//
//	/**
//	 * 这个bean的名字必须是这个.因为
//	 * See{@link KafkaAnnotationDrivenConfiguration#kafkaListenerContainerFactory}
//	 * 消费者监听器容器 该容器和 KafkaMessageListenerContainer的区别是
//	 * 他是并发的
//	 */
//	@Order(2)
//	@Bean
//	public ConcurrentMessageListenerContainer<Integer, String> kafkaListenerContainerFactory(
//			ConsumerFactory<Integer,String> consumerFactory,
//			ContainerProperties containerProperties) {
//		return new ConcurrentMessageListenerContainer<>(consumerFactory, containerProperties);
//	}
//
//	/**
//	 * 生产者工厂
//	 */
//	@Bean
//	public DefaultKafkaProducerFactory<Integer, String> producerFactory() {
//		return new DefaultKafkaProducerFactory<>(producePropMap());
//	}
//
//	/**
//	 * 发送模版
//	 */
//	@Order(3)
//	@Bean
//	public KafkaTemplate<Integer, String> kafkaTemplate(DefaultKafkaProducerFactory<Integer, String> producerFactory) {
//		return new KafkaTemplate<>(producerFactory);
//	}
//
//
//
//
//
//
//
//
//	/**
//	 * 生产者配置map
//	 */
//	public Map<String, Object> producePropMap() {
//		Map<String, Object> props = new HashMap<>();
//		//kafka连接地址
//		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "106.14.7.29:9092");
//		//重试次数
//		props.put(ProducerConfig.RETRIES_CONFIG, 0);
//		//当有多个记录被发送到同一个分区,生产者尝试将多个记录合并到一个请求中,该配置就是默认批量提交的每一批的最大字节大小.. 过小可能还会降低吞吐量,过大会占用内存
//		props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
//		//对于上面的批量发送,给配置可以设置主动等待x毫秒,以便让每次请求,发送的消息尽可能的多.以提高性能. 默认为0,也就是不等待
//		props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
//		//生产者可以缓存的.等待被发送的记录的总字节数
//		props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
//		//消息的key的序列花器, 使用了kafka自带的Integer序列化器
//		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
//		//消息的value的序列化器,使用了kafka自带的String序列化器
//		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//		return props;
//	}
//
//	/**
//	 * 消费者配置map
//	 */
//	@Bean
//	public Map<String, Object> consumePropMap() {
//		Map<String, Object> props = new HashMap<>();
//		//连接地址
//		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "106.14.7.29:9092");
//		//消费者组,标识这个消费者所属的消费者组,一个唯一的字符串
//		props.put(ConsumerConfig.GROUP_ID_CONFIG, "consumer-group-1");
//		//是否自动提交消费者的offset(偏移量),也就是该消费者目前在分区日志中的位置
//		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
//		//自动提交消费者偏移量的间隔,毫秒. 如果ENABLE_AUTO_COMMIT_CONFIG属性开启,它就会生效
//		props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
//		//服务端会对消费者心跳检测.如果该消费者超过该时间未响应.则剔除
//		props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
//		//key反序列化
//		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
//		//value反序列化
//		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//		return props;
//	}
}
