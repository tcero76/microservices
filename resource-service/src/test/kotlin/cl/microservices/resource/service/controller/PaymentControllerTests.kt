package cl.microservices.resource.service.controller

import cl.microservices.postgres.model.Payments
import cl.microservices.resource.service.dto.CommandPayment
import cl.microservices.resource.service.dto.OrderItemDto
import cl.microservices.postgres.services.services.PostgresPaymentService
import cl.microservices.resource.service.util.AccessToken
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.web.client.RestTemplate
import java.util.*


@WebMvcTest
class PaymentControllerTests(@Autowired private val mockMvc:MockMvc,
                             @Autowired private val objectMapper: ObjectMapper) {
    @MockBean
    private var postgresPaymentServiceImpl: PostgresPaymentService? = null

    private var jwt:Jwt? = null

    fun setup() {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED
        val httpEntity =
            HttpEntity<String>("grant_type=password&username=app_user&password=Reaktor6_&client_id=frontend-service", headers)
        val restTemplate = RestTemplate()
        val response = restTemplate.exchange("http://localhost:9091/auth/realms/master/protocol/openid-connect/token",
            HttpMethod.POST,
            httpEntity,
            AccessToken::class.java)
        val accessToken = "Bearer " + response.body?.access_token ?: ""
    }
    @BeforeEach
    fun setup2() {
    jwt = Jwt.withTokenValue("token")
        .header("alg", "none")
        .claim("sub", "user")
        .claim("scope", "read")
        .claim("roles", "user")
        .build()
    }

    @Test
    @WithMockUser(username="admin",roles=["USER"])
    fun givenCommandPayment_whenPersistPayment_thenReturnSavedPayment() {
        val customerid1 = UUID.randomUUID()
        val commandPayment = CommandPayment(customerid1
            ,500,
            arrayListOf(
                OrderItemDto(UUID.randomUUID().toString(), 10, 20,200),
                OrderItemDto(UUID.randomUUID().toString(), 30, 10, 300)
            )
        )
        BDDMockito.given<Optional<Payments>>(postgresPaymentServiceImpl!!.paymentPersist(any<CommandPayment>()))
            .willReturn(Optional.of(Payments()))
        //when

        val response = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commandPayment))
                .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt))
        )
        //then
        response.andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isCreated())
    }

    @Test
    @WithMockUser(username="admin",roles=["USER"])
    fun givenHealth_when_thenReturnHealth() {

        //when
        val response = mockMvc.perform(
            MockMvcRequestBuilders.get("/api/health")
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt))
        )
        //then
        response.andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
    }
}