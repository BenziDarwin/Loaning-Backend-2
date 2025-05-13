package com.yunesta.mpesa.helpers

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import com.africastalking.AfricasTalking
import com.africastalking.SmsService
import com.africastalking.sms.Recipient
import org.slf4j.LoggerFactory
import java.io.IOException

@Component
class MessageNotifications(
    @Value("\${africastalking.api.username}") private val username: String,
    @Value("\${africastalking.api.key}") private val apiKey: String,
    @Value("\${africastalking.api.senderid}") private val senderId: String
) {
    private val logger = LoggerFactory.getLogger(MessageNotifications::class.java)
    private val smsService: SmsService


    init {
        // Initialize the Africa's Talking SDK
        AfricasTalking.initialize(username, apiKey)
        // Get the SMS service
        smsService = AfricasTalking.getService(AfricasTalking.SERVICE_SMS)
    }

    fun sendSms(phoneNumbers: List<String>, message: String): String {
        try {
            // Convert list to array
            val recipients = phoneNumbers.toTypedArray()
            // Send the message
            val response: List<Recipient> = smsService.send(message, senderId, recipients, true)
            // Log the response
            val statusReport = response.joinToString("\n") {
                "${it.number}: ${it.status}"
            }
            logger.info("SMS sending status: $statusReport")

            return "SMS sent successfully"
        } catch (e: IOException) {
            logger.error("Failed to send SMS: ${e.message}", e)
            return "Failed to send SMS: ${e.message}"
        } catch (e: Exception) {
            logger.error("Unexpected error sending SMS: ${e.message}", e)
            return "Unexpected error sending SMS: ${e.message}"
        }
    }
}