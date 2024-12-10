package cl.microservice.frontend.service.Controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class ForwardingController {
        @RequestMapping(value = ["/{path:[^\\.]*}"])
        fun redirect():String {
            return "forward:/index.html";
        }
}