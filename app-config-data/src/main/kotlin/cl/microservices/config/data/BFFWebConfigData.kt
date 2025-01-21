package cl.microservices.config.data

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("bffweb-config")
class BFFWebConfigData() {
    var webClient = WebClient()
    var queryPayment = QueryPayment()
    var postPayment = PostPayment()
    class QueryPayment {
        lateinit var method: String
        lateinit var uri: String
        lateinit var accept: String
    }
    class PostPayment {
        lateinit var method: String
        lateinit var uri: String
        lateinit var accept: String
    }
    class WebClient() {
        lateinit var baseUrl: String
        lateinit var contentType: String
        lateinit var acceptType: String
        var maxInMemorySize: Int = 0
        var connectTimeoutMs: Int = 0
        var readTimeoutMs: Long = 0
        var writeTimeoutMs: Long = 0
    }
}