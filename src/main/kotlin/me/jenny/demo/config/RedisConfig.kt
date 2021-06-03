package me.jenny.demo.config

import me.jenny.demo.util.Log
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Configuration
import redis.embedded.RedisServer
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

@Configuration
@EnableCaching
class RedisConfig(@Value("\${spring.redis.port:6379}") private val port: Int) {
    companion object : Log

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

    // TODO: 작성중... logback 색깔 설정이 먹지 않는군.
}
