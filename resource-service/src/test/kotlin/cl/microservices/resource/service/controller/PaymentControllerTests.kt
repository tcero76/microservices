package cl.microservices.resource.service.controller

import cl.microservices.resource.service.common.dto.CommandPayment
import cl.microservices.resource.service.common.dto.OrderItemDto
import cl.microservices.resource.service.common.dto.PaymentsResponse
import cl.microservices.resource.service.service.PaymentService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.RequestPostProcessor
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.util.*


@WebMvcTest
class PaymentControllerTests(@Autowired private val mockMvc:MockMvc,
                             @Autowired private val objectMapper: ObjectMapper) {
    @MockBean
    private lateinit var paymentService: PaymentService
    private var total:Int = 0
    private lateinit var authorities: RequestPostProcessor
    private lateinit var commandPayment: CommandPayment
    private lateinit var customerId:UUID
    @BeforeEach
    fun setup2() {
        total=50
        customerId = UUID.randomUUID()
        authorities = SecurityMockMvcRequestPostProcessors.jwt()
            .authorities(SimpleGrantedAuthority("ROLE_USER"))
            .jwt { it.claims { it.put("id_user",customerId) } }

        commandPayment = CommandPayment(500,
            arrayListOf(
                OrderItemDto(UUID.randomUUID().toString(), 10, 20,200),
                OrderItemDto(UUID.randomUUID().toString(), 30, 10, 300)
            )
        )
    }
    @Test
    fun givenCommandPayment_whenPersistPayment_thenReturnSavedPayment() {
        val paymentsResponse = PaymentsResponse(customerId, total)
        BDDMockito.given<Optional<PaymentsResponse>>(paymentService!!.paymentPersist(any<CommandPayment>(), any<String>()))
            .willReturn(Optional.of(paymentsResponse))

        //when
        val response = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .with(authorities)
                .content(objectMapper.writeValueAsString(commandPayment))
        )
        //then
        response.andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isCreated())
    }

    @Test
    fun givenHealth_when_thenReturnHealth() {

        //when
        val response = mockMvc.perform(
            MockMvcRequestBuilders.get("/api/health")
                .contentType(MediaType.APPLICATION_JSON)
                .with(authorities)
        )
        //then
        response.andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
    }
    @Test
    fun givenCommandPaymentError_whenPersist_thenReturnError() {
        val paymentsResponse = PaymentsResponse(customerId, 600)
        BDDMockito.given<Optional<PaymentsResponse>>(paymentService!!.paymentPersist(any<CommandPayment>(), any<String>()))
            .willReturn(Optional.empty())
        //when
        val response = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .with(authorities)
                .content(objectMapper.writeValueAsString(commandPayment))
        )
        //then
        response.andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isInternalServerError)
    }
}