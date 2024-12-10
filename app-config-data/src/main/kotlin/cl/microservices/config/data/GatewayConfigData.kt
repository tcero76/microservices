package cl.microservices.config.data

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("gateway-router")
class GatewayConfigData() {
    var frontend: Frontend = Frontend()
    var authorizationServer: AuthorizationServer = AuthorizationServer()
    var resourceServer: ResourceServer = ResourceServer()
    class Frontend() {
        lateinit var path: ArrayList<String>
        lateinit var uri: String
    }
    class AuthorizationServer() {
        lateinit var path: ArrayList<String>
        lateinit var uri: String
    }
    class ResourceServer() {
        lateinit var path: ArrayList<String>
        lateinit var uri: String
    }
}