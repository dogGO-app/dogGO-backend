package pl.poznan.put.dogloverservice.modules.usercalendarevent

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDateTime
import java.time.ZoneId
import pl.poznan.put.dogloverservice.infrastructure.exceptions.UserCalendarEventDateTimeException
import pl.poznan.put.dogloverservice.modules.dog.DogData
import pl.poznan.put.dogloverservice.modules.dog.DogService
import pl.poznan.put.dogloverservice.modules.doglover.DogLoverData
import pl.poznan.put.dogloverservice.modules.doglover.DogLoverService
import pl.poznan.put.dogloverservice.modules.usercalendarevent.dto.UserCalendarEventDTO

class UserCalendarEventServiceTest : BehaviorSpec({
    isolationMode = IsolationMode.InstancePerLeaf

    val timeZone = "Europe/Warsaw"
    val userCalendarEventRepository = mockk<UserCalendarEventRepository>(relaxed = true)
    val dogService = mockk<DogService>(relaxed = true)
    val dogLoverService = mockk<DogLoverService>(relaxed = true)

    val userCalendarEventService = UserCalendarEventService(
            timeZone,
            userCalendarEventRepository,
            dogService,
            dogLoverService
    )

    Given("dog lover id") {
        val dogLover = DogLoverData.john
        val dogLoverId = dogLover.id
        val johnEvents = UserCalendarEventData.johnEvents
        val expectedEvents = johnEvents.map { UserCalendarEventDTO(it, timeZone) }

        every {
            dogLoverService.getDogLover(dogLoverId)
        } returns dogLover
        every {
            userCalendarEventRepository.findAllByDogLoverOrderByDateTimeAsc(dogLover)
        } returns johnEvents

        When("getting all dog lover events") {
            val returnedEvents = userCalendarEventService.getUserCalendar(dogLoverId)

            Then("returned events should be equal to expected") {
                returnedEvents shouldBe expectedEvents
            }
        }

        val event = UserCalendarEventData.johnAndAzorEvent
        val eventId = event.id
        val expectedEvent = UserCalendarEventDTO(event, timeZone)

        every {
            userCalendarEventRepository.findByIdAndDogLover(eventId, dogLover)
        } returns event

        When("getting dog lover event") {
            val returnedEvent = userCalendarEventService.getCalendarEvent(eventId, dogLoverId)

            Then("returned event should be equal to expected") {
                returnedEvent shouldBe expectedEvent
            }
        }
    }

    Given("dog lover id and calendar event DTO") {
        val dogLover = DogLoverData.john
        val dogLoverId = dogLover.id
        val dog = DogData.azor

        And("correct calendar event DTO") {
            val event = UserCalendarEventData.johnAndAzorEvent
            val eventDTO = UserCalendarEventDTO(event, timeZone)
            val dateTime = LocalDateTime.of(eventDTO.date, eventDTO.time).atZone(ZoneId.of(timeZone)).toInstant()

            every {
                dogLoverService.getDogLover(dogLoverId)
            } returns dogLover
            every {
                dogService.getDog(eventDTO.dogName, dogLoverId)
            } returns dog
            every {
                userCalendarEventRepository.existsByDateTimeAndDogLoverAndDog(dateTime, dogLover, dog)
            } returns false
            every {
                userCalendarEventRepository.save(any())
            } returns event

            When("saving dog lover calendar event") {
                val savedEvent = userCalendarEventService.saveCalendarEvent(eventDTO, dogLoverId)

                Then("saved event should be equal to expected") {
                    savedEvent shouldBe eventDTO
                }
            }
        }

        And("calendar event from the past") {
            val event = UserCalendarEventData.johnAndBurekEvent
            val eventDTO = UserCalendarEventDTO(event, timeZone)

            When("saving dog lover calendar event from the past") {
                val exception = shouldThrow<UserCalendarEventDateTimeException> {
                    userCalendarEventService.saveCalendarEvent(eventDTO, dogLoverId)
                }

                Then("UserCalendarEventDateTimeException should have correct message") {
                    exception.message shouldBe UserCalendarEventDateTimeException().message
                }
            }
        }
    }

    Given("existing user calendar event DTO and dog lover id") {
        val dogLover = DogLoverData.john
        val dogLoverId = dogLover.id
        val dog = DogData.azor
        val event = UserCalendarEventData.johnAndAzorEvent
        val updatedEvent = UserCalendarEventData.updatedJohnAndAzorEvent
        val updatedEventDTO = UserCalendarEventDTO(updatedEvent, timeZone)
        val updatedDateTime = LocalDateTime.of(updatedEventDTO.date, updatedEventDTO.time).atZone(ZoneId.of(timeZone)).toInstant()

        every {
            userCalendarEventRepository.existsByDateTimeAndDogLoverAndDogAndIdIsNot(updatedDateTime, dogLover, dog, updatedEvent.id)
        } returns false
        every {
            userCalendarEventRepository.findByIdAndDogLover(updatedEvent.id, dogLover)
        } returns event
        every {
            userCalendarEventRepository.save(any())
        } returns updatedEvent

        When("updating dog lover calendar event") {
            val returnedUpdatedEvent = userCalendarEventService.updateCalendarEvent(updatedEventDTO, dogLoverId)

            Then("updated event should be equal to expected") {
                returnedUpdatedEvent shouldBe updatedEventDTO
            }
        }

        every {
            userCalendarEventRepository.existsByDateTimeAndDogLoverAndDogAndIdIsNot(updatedDateTime, dogLover, dog, updatedEvent.id)
        } returns true

        When("updating dog lover calendar event with existing data") {
            val returnedUpdatedEvent = userCalendarEventService.updateCalendarEvent(updatedEventDTO, dogLoverId)

            Then("updated event should be equal to expected") {
                returnedUpdatedEvent shouldBe updatedEventDTO
            }
        }
    }
})