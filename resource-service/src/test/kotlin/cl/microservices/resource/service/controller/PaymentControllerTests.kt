package cl.microservices.resource.service.controller

import cl.microservices.resource.service.dto.CommandPayment
import cl.microservices.resource.service.dto.OrderItemDto
import cl.microservices.resource.service.service.PaymentService
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
    private var paymentServiceImpl: PaymentService? = null

    private var accessToken:String = ""

    @BeforeEach
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
        accessToken = "Bearer " + response.body?.access_token ?: ""
    }

    @Test
    fun givenCommandPayment_whenPersistPayment_thenReturnSavedPayment() {
        val customerid1 = UUID.randomUUID()
        val commandPayment = CommandPayment(customerid1
            ,500,
            arrayListOf(
                OrderItemDto(UUID.randomUUID().toString(), 10, 20,200),
                OrderItemDto(UUID.randomUUID().toString(), 30, 10, 300)
            )
        )
        BDDMockito.given<Boolean>(paymentServiceImpl!!.paymentPersist(any<CommandPayment>()))
            .willReturn(true)
        //when
        val response = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/payments")
                .header("Authorization", accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commandPayment))
        )
        //then
        response.andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isCreated())
    }
}