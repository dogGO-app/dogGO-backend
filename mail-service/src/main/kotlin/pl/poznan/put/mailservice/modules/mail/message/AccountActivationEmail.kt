package pl.poznan.put.mailservice.modules.mail.message

class AccountActivationEmail(
    override val receiverEmail: String,
    activationCode: String
) : EmailMessage {
    override val templateName = "account-activation"
    override val subject = "Account activation in the dogGO application"
    override val variables = mapOf(
        "usermail" to receiverEmail.substringBefore("@"),
        "code" to activationCode,
    )
}