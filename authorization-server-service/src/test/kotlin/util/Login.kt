package util

import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import java.nio.ByteBuffer
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.*
import kotlin.math.floor

class Login {
    private fun generateCodeVerifier():String {
        val secureRandom:SecureRandom = SecureRandom()
        val codeVerifier = ByteBuffer.allocate(32).array()
        secureRandom.nextBytes(codeVerifier)
        return Base64.getUrlEncoder().withoutPadding().encodeToString(codeVerifier)
    }
    private fun generateCodeChallenge(codeVerifier:String):String {
        val bytes = codeVerifier.toByteArray()
        val messageDigest = MessageDigest.getInstance("SHA-256")
        messageDigest.update(bytes, 0, bytes.size)
        val digest = messageDigest.digest()
        return Base64.getUrlEncoder().withoutPadding().encodeToString(digest)
    }
    private fun getHeader():HttpHeaders {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED

        headers.add("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7",)
        headers.add("accept-language", "es-US,es-419;q=0.9,es;q=0.8",)
        headers.add("sec-ch-ua", "\"Chromium\";v=\"122\", \"Not(A:Brand\";v=\"24\", \"Google Chrome\";v=\"122\"",)
        headers.add("sec-ch-ua-mobile", "?0",)
        headers.add("sec-ch-ua-platform", "\"Linux\"",)
        headers.add("sec-fetch-dest", "document",)
        headers.add("sec-fetch-mode", "navigate",)
        headers.add("sec-fetch-site", "same-origin",)
        headers.add("sec-fetch-user", "?1",)
        headers.add("upgrade-insecure-requests", "1")
        headers.add("referrer", "http://localhost/",)
        headers.add("referrerPolicy", "strict-origin-when-cross-origin",)
        headers.add("method", "GET",)
        headers.add("mode", "cors",)
        headers.add("credentials", "include")
        return headers
    }
    private fun generateState():String {
        var stateValue = ""
        val alphaNumericCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        val alphaNumericCharactersLength = alphaNumericCharacters.length
        for (i in 0 until alphaNumericCharactersLength) {
            val floor = floor(Math.random() * alphaNumericCharactersLength).toInt()
            stateValue += alphaNumericCharacters[floor]
        }
        return stateValue
    }
    fun getLogin():Triple<String, String, String> {
        val httpEntity = HttpEntity<String>("", getHeader())
        val login = "http://localhost/login"
        val restTemplateLogin = RestTemplate()
        val response = restTemplateLogin.exchange(login,
            HttpMethod.GET,
            httpEntity,
            String::class.java)
        return extraeVariables(response)
    }

    private fun extraeVariables(response: ResponseEntity<String>): Triple<String, String, String> {
        val body = response.body ?: ""
        val crfs = body.indexOf("value=").let {
            body.substring(it + 7, it + 103)
        }
        val cookie = response.headers.get("Set-Cookie").toString() ?: ""
        val location = response.headers.get("Location").toString() ?: ""
        return Triple(crfs, cookie.substring(1,cookie.length-19), location)
    }

    fun postLogin(cookie:String, crfs:String):ResponseEntity<String> {
        val restTemplate = RestTemplate()
        val login = "http://localhost/login"
        val headers = getHeader()
        headers.set("Cookie", cookie)
        val httpEntity = HttpEntity<String>("username=Leonardo&password=password&_csrf=${crfs}", headers)
        return restTemplate.exchange(login,
            HttpMethod.POST,
            httpEntity,
            String::class.java)
    }

    fun getAuthorize(): Triple<String, String, String> {
        val httpEntity = HttpEntity<String>("", getHeader())
        val restTemplate = RestTemplate()
        val generateCodeVerifier = generateCodeVerifier()
        val generateCodeChallenge = generateCodeChallenge(generateCodeVerifier)
        val state = generateState()
        var authorizationURL = "http://localhost/oauth2/authorize";
        authorizationURL += "?client_id=client1";
        authorizationURL += "&response_type=code";
        authorizationURL += "&scope=openid read";
        authorizationURL += "&redirect_uri=http://localhost:80/authorized";
        authorizationURL += "&state=" + state;
        authorizationURL += "&code_challenge=" + generateCodeChallenge;
        authorizationURL += "&code_challenge_method=S256";
        val response = restTemplate.exchange(authorizationURL,
            HttpMethod.GET,
            httpEntity,
            String::class.java)
        return extraeVariables(response)
    }
}