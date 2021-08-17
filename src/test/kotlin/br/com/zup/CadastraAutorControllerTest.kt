package br.com.zup

import br.com.zup.autores.EnderecoClient
import br.com.zup.autores.EnderecoResponse
import br.com.zup.autores.NovoAutorRequest
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import javax.inject.Inject

@MicronautTest
internal class CadastraAutorControllerTest {

    @field:Inject
    lateinit var enderecoClient: EnderecoClient

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    fun deveCadastrarUmNovoAutor() {
        // cenario
        val novoAutorRequest = NovoAutorRequest("Rafael Ponte", "rafael.ponte@zup.com.br", "O principe dos oceanos", "11222-333","37")

        val enderecoResponse = EnderecoResponse("Rua das Laranjeiras", "Rio de Janeiro", "RJ")

        Mockito.`when`(enderecoClient.consulta(novoAutorRequest.cep)).thenReturn(HttpResponse.ok(enderecoResponse))

        val request = HttpRequest.POST("/autores", novoAutorRequest)

        //acao
        val response = client.toBlocking().exchange(request, Any::class.java)


        //asserts
        assertEquals(HttpStatus.CREATED, response.status())
        assertTrue(response.headers.contains("Location"))
        assertTrue(response.header("Location")!!.matches("/autores/\\d".toRegex()))
    }

    @MockBean(EnderecoClient::class)
    fun enderecoMock(): EnderecoClient {
        return Mockito.mock(EnderecoClient::class.java)
    }
}