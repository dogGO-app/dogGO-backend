package pl.poznan.put.mailservice.modules.mail.message

abstract class EmailMessage(val receiverEmail: String) {
    abstract val templateName: String
    abstract val subject: String
    abstract val variables: Map<String, Any>
}