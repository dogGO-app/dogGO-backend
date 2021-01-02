package pl.poznan.put.dogloverservice.infrastructure.response

import org.springframework.http.ContentDisposition
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity

object FileResponseEntityBuilder {
    fun build(
            httpStatus: HttpStatus,
            filename: String,
            contentType: String,
            body: ByteArray?
    ): ResponseEntity<ByteArray> =
            ResponseEntity
                    .status(httpStatus)
                    .headers {
                        it.contentDisposition = ContentDisposition.parse("attachment; filename=\"$filename\"")
                    }
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(body)
}