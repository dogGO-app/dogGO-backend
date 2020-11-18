package pl.poznan.put.dogloverservice

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class TestClass : BehaviorSpec({
    Given("foo") {
        When("bar") {
            Then("baz") {
                true shouldBe false
            }
        }
    }
})