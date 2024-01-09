package microservices.schedule

import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.Scheduled

@Configuration
class ScheduleMsg {
    @Scheduled(initialDelay = 1000, fixedDelay = 5000)
    fun getMsg() {
        println("Mensaje secreto")
    }
}