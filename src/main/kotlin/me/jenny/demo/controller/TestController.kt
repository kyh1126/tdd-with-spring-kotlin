package me.jenny.demo.controller

import io.swagger.annotations.Api
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("test")
@Api(tags = ["Test API"])
class TestController {

    @GetMapping("ping")
    fun ping(): String {
        return "OK"
    }
}
