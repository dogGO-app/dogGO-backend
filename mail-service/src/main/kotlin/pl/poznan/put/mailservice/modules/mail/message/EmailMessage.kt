package pl.poznan.put.mailservice.modules.mail.message

interface EmailMessage {
    val templateName: String
    val subject: String
    val receiverEmail: String
    val variables: Map<String, Any>
}