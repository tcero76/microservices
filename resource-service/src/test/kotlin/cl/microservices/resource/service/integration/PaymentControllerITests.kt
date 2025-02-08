package cl.microservices.resource.service.integration

import cl.microservices.resource.service.common.dto.CommandPayment
import cl.microservices.resource.service.common.dto.OrderItemDto
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.RequestPostProcessor
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.util.*


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class PaymentControllerITests(
    @Autowired val mockMvc: MockMvc,
    @Autowired val objectMapper:ObjectMapper) {
    private lateinit var authorities:RequestPostProcessor
    private val customerid = UUID.fromString("d215b5f8-0249-4dc5-89a3-51fd148cfb41")
    @BeforeEach
    fun setup() {
        authorities = SecurityMockMvcRequestPostProcessors.jwt()
            .authorities(SimpleGrantedAuthority("ROLE_USER"))
            .jwt { it.claims { it.put("id_user",customerid) } }
    }
    @Test
    fun givenCommandPayment_whenPersistPayment_thenReturnSavedPayment() {
        val commandPayment = CommandPayment(500,
            arrayListOf(
                OrderItemDto(UUID.randomUUID().toString(), 10, 20,200),
                OrderItemDto(UUID.randomUUID().toString(), 30, 10, 300)
            )
        )
        //when
        val response = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/payments")
                .with(this.authorities)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(commandPayment))
        )
        //then
        response.andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isCreated())
    }
}