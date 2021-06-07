package me.jenny.demo.config

import me.jenny.demo.util.Log
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories
import org.springframework.data.redis.serializer.GenericToStringSerializer
import redis.embedded.RedisServer
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

@Configuration
@EnableRedisRepositories
class RedisConfig(
        @Value("\${spring.redis.host}") private val host: String,
        @Value("\${spring.redis.port:6379}") private val port: Int
) {

    lateinit var redisServer: RedisServer

    @PostConstruct
    fun startRedisServer() {
        log.info("starting redis server...")

        redisServer = RedisServer(port)
        redisServer.start()
    }

    @PreDestroy
    fun stopRedisServer() {
        log.info("shutting down redis server...")

        redisServer.stop()
    }

    @Bean
    fun redisConnectionFactory(): RedisConnectionFactory = LettuceConnectionFactory(host, port)

    @Bean
    fun stringRedisTemplate(redisConnectionFactory: RedisConnectionFactory): RedisTemplate<String, Any> {
        return RedisTemplate<String, Any>().apply {
            setConnectionFactory(redisConnectionFactory)
            valueSerializer = GenericToStringSerializer(Any::class.java)
        }
    }

    companion object : Log
}
