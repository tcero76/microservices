package cl.microservices.resource.service.integration

import cl.microservices.resource.service.dto.CommandPayment
import cl.microservices.resource.service.dto.OrderItemDto
import cl.microservices.resource.service.util.AccessToken
import cl.microservices.postgres.services.repo.PaymentsRepo
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.web.client.RestTemplate
import java.util.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class PaymentControllerITests(
    @Autowired val mockMvc: MockMvc,
    @Autowired val objectMapper:ObjectMapper) {
    private var accessToken:String = ""
//    @BeforeEach
//    fun setup() {
//        val headers = HttpHeaders()
//        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED
//        val httpEntity =
//            HttpEntity<String>("grant_type=password&username=app_user&password=Reaktor6_&scope=openid&client_id=frontend-service", headers)
//        val restTemplate = RestTemplate()
//        val response = restTemplate.exchange("http://localhost:9091/auth/realms/master/protocol/openid-connect/token",
//            HttpMethod.POST,
//            httpEntity,
//            AccessToken::class.java)
//        accessToken = "Bearer " + response.body?.access_token ?: ""
//    }
    @Test
    fun givenCommandPayment_whenPersistPayment_thenReturnSavedPayment() {
        val customerid1 = UUID.fromString("d215b5f8-0249-4dc5-89a3-51fd148cfb41")
        val commandPayment = CommandPayment(customerid1
            ,500,
            arrayListOf(
                OrderItemDto(UUID.randomUUID().toString(), 10, 20,200),
                OrderItemDto(UUID.randomUUID().toString(), 30, 10, 300)
            )
        )
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