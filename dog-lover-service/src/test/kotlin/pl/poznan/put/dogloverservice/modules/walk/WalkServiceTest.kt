package pl.poznan.put.dogloverservice.modules.walk

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import pl.poznan.put.dogloverservice.infrastructure.exceptions.ActiveWalkNotFoundException
import pl.poznan.put.dogloverservice.infrastructure.exceptions.ArrivedAtDestinationWalkNotFoundException
import pl.poznan.put.dogloverservice.infrastructure.exceptions.WalkNotFoundException
import pl.poznan.put.dogloverservice.infrastructure.exceptions.WalkUpdateException
import pl.poznan.put.dogloverservice.modules.dog.DogData
import pl.poznan.put.dogloverservice.modules.dog.DogService
import pl.poznan.put.dogloverservice.modules.doglover.DogLoverData
import pl.poznan.put.dogloverservice.modules.doglover.DogLoverService
import pl.poznan.put.dogloverservice.modules.dogloverrelationship.DogLoverRelationshipData
import pl.poznan.put.dogloverservice.modules.dogloverrelationship.DogLoverRelationshipService
import pl.poznan.put.dogloverservice.modules.mapmarker.MapMarkerData
import pl.poznan.put.dogloverservice.modules.mapmarker.MapMarkerService
import pl.poznan.put.dogloverservice.modules.walk.dto.CreateWalkDTO
import pl.poznan.put.dogloverservice.modules.walk.dto.DogLoverInLocationDTO
import pl.poznan.put.dogloverservice.modules.walk.dto.WalkDTO
import java.util.*
import pl.poznan.put.dogloverservice.modules.walk.WalkData.getArrivedAtDestination

class WalkServiceTest : BehaviorSpec({
    isolationMode = IsolationMode.InstancePerLeaf

    val timeZone = "Europe/Warsaw"
    val walkRepository = mockk<WalkRepository>(relaxed = true)
    val activeDogLoverWalkCache = mockk<ActiveDogLoverWalkCache>(relaxed = true)
    val dogLoverService = mockk<DogLoverService>(relaxed = true)
    val dogService = mockk<DogService>(relaxed = true)
    val mapMarkerService = mockk<MapMarkerService>(relaxed = true)
    val dogLoverRelationshipService = mockk<DogLoverRelationshipService>(relaxed = true)

    val walkService = WalkService(
            timeZone,
            walkRepository,
            activeDogLoverWalkCache,
            dogLoverService,
            dogService,
            mapMarkerService,
            dogLoverRelationshipService
    )

    Given("dog lover id") {
        val dogLover = DogLoverData.john
        val dogLoverId = dogLover.id

        every {
            dogLoverService.getDogLover(dogLoverId)
        } returns dogLover

        And("completed walks") {
            val completedWalks = WalkData.historicalWalks
            val expectedCompletedWalks = completedWalks.map { WalkDTO(it, timeZone) }

            every {
                walkRepository.findAllByDogLoverAndWalkStatusOrderByCreatedAtAsc(dogLover, WalkStatus.LEFT_DESTINATION)
            } returns completedWalks

            When("getting completed walks history") {
                val actualCompletedWalks = walkService.getCompletedWalksHistory(dogLoverId)

                Then("actual completed walks should be equal to expected") {
                    actualCompletedWalks shouldBe expectedCompletedWalks
                }
            }
        }

        And("active walk") {
            val activeWalk = WalkData.ongoing

            every {
                walkRepository.findFirstByDogLoverIdAndWalkStatusInOrderByCreatedAtDesc(dogLoverId, WalkStatus.activeWalkStatuses())
            } returns activeWalk

            When("keeping walk active") {
                walkService.keepWalkActive(dogLoverId)

                Then("ActiveDogLoverWalkCache::put should be called") {
                    verify(exactly = 1) {
                        activeDogLoverWalkCache.put(dogLoverId, activeWalk.id)
                    }
                }
            }
        }

        And("no active walk") {
            every {
                walkRepository.findFirstByDogLoverIdAndWalkStatusInOrderByCreatedAtDesc(dogLoverId, WalkStatus.activeWalkStatuses())
            } returns null

            When("keeping walk active") {
                val exception = shouldThrow<ActiveWalkNotFoundException> {
                    walkService.keepWalkActive(dogLoverId)
                }

                Then("exception should have correct message") {
                    exception shouldHaveMessage ActiveWalkNotFoundException().message
                }
            }
        }

        And("arrived at destination walk") {
            val arrivedAtDestinationWalk = getArrivedAtDestination(dogLover)

            every {
                walkRepository.findFirstByDogLoverIdAndWalkStatusInOrderByCreatedAtDesc(dogLoverId, setOf(WalkStatus.ARRIVED_AT_DESTINATION))
            } returns arrivedAtDestinationWalk

            When("getting arrived at destination walk") {
                val actualWalk = walkService.getArrivedAtDestinationWalkByDogLoverId(dogLoverId)

                Then("actual walk should be equal to expected") {
                    actualWalk shouldBe arrivedAtDestinationWalk
                }
            }
        }

        And("no arrived at destination walk") {
            every {
                walkRepository.findFirstByDogLoverIdAndWalkStatusInOrderByCreatedAtDesc(dogLoverId, setOf(WalkStatus.ARRIVED_AT_DESTINATION))
            } returns null

            When("getting arrived at destination walk") {
                val exception = shouldThrow<ArrivedAtDestinationWalkNotFoundException> {
                    walkService.getArrivedAtDestinationWalkByDogLoverId(dogLoverId)
                }

                Then("actual walk should be equal to expected") {
                    exception shouldHaveMessage ArrivedAtDestinationWalkNotFoundException().message
                }
            }
        }

        And("CreateWalkDTO and dogs and map marker") {
            val walk = WalkData.ongoing
            val dogs = walk.dogs
            val mapMarker = walk.mapMarker
            val createWalkDTO = CreateWalkDTO(
                    dogNames = dogs.map { it.name },
                    mapMarker = mapMarker.id
            )

            val expectedWalkDTO = WalkDTO(walk, timeZone)

            every {
                dogService.getDog(createWalkDTO.dogNames.first(), dogLoverId)
            } returns DogData.azor
            every {
                mapMarkerService.getMapMarker(createWalkDTO.mapMarker)
            } returns mapMarker
            every {
                walkRepository.save(any())
            } returns walk

            When("saving walk") {
                val actualWalkDTO = walkService.saveWalk(createWalkDTO, dogLoverId)

                Then("actual WalkDTO should be equal to expected") {
                    actualWalkDTO shouldBe expectedWalkDTO
                }

                Then("started walks should be completed and ActiveDogLoverWalkCache::put should be invoked") {
                    verify(exactly = 1) {
                        walkRepository.completeDogLoverStartedWalks(dogLoverId)
                        activeDogLoverWalkCache.put(dogLoverId, walk.id)
                    }
                }
            }
        }


        And("existing walk id") {
            val walk = WalkData.ongoing
            val walkId = walk.id

            every {
                walkRepository.findByIdAndDogLoverId(walkId, dogLoverId)
            } returns walk

            And("new permissible walk status") {
                val newWalkStatus = WalkStatus.ARRIVED_AT_DESTINATION
                val updatedWalk = Walk(walk, newWalkStatus)
                val expectedWalkDTO = WalkDTO(updatedWalk, timeZone)

                every {
                    walkRepository.save(any())
                } returns updatedWalk

                When("updating walk status") {
                    val actualWalkDTO = walkService.updateWalkStatus(walkId, newWalkStatus, dogLoverId)

                    Then("actual updated walk DTO should be equal to expected") {
                        actualWalkDTO shouldBe expectedWalkDTO
                    }

                    Then("ActiveDogLoverWalkCache::put should be invoked") {
                        verify(exactly = 1) {
                            activeDogLoverWalkCache.put(dogLoverId, walkId)
                        }
                    }
                }
            }

            And("new non-permissible walk status") {
                val newWalkStatus = WalkStatus.LEFT_DESTINATION

                When("updating walk status") {
                    val exception = shouldThrow<WalkUpdateException> {
                        walkService.updateWalkStatus(walkId, newWalkStatus, dogLoverId)
                    }

                    Then("exception should have correct message") {
                        exception shouldHaveMessage WalkUpdateException().message
                    }

                    Then("ActiveDogLoverWalkCache::put should not be invoked") {
                        verify(exactly = 0) {
                            activeDogLoverWalkCache.put(any(), any())
                        }
                    }
                }
            }
        }

        And("non-existing walk id and new walk status") {
            val walkId = UUID.fromString("05fe3fc6-fe95-424b-91b2-9c8746f4825b")
            val newWalkStatus = WalkStatus.ARRIVED_AT_DESTINATION

            every {
                walkRepository.findByIdAndDogLoverId(walkId, dogLoverId)
            } returns null

            When("updating walk status") {
                val exception = shouldThrow<WalkNotFoundException> {
                    walkService.updateWalkStatus(walkId, newWalkStatus, dogLoverId)
                }

                Then("exception should have correct message") {
                    exception shouldHaveMessage WalkNotFoundException().message
                }
            }
        }

        And("map marker id") {
            val mapMarker = MapMarkerData.parkMostowa
            val mapMarkerId = mapMarker.id
            val otherDogLoversWalks = WalkData.dogLoversArrivedAtDestinationWalks
            val dogLoverRelationships = DogLoverRelationshipData.johnRelationships

            val expectedDogLoverInLocationDTOs = otherDogLoversWalks.map {
                DogLoverInLocationDTO(it, dogLoverRelationships)
            }

            every {
                walkRepository.findAllByMapMarkerIdAndWalkStatusAndDogLoverIdIsNot(
                        mapMarkerId,
                        WalkStatus.ARRIVED_AT_DESTINATION,
                        dogLoverId
                )
            } returns otherDogLoversWalks
            every {
                dogLoverRelationshipService.getDogLoverRelationships(dogLoverId)
            } returns dogLoverRelationships

            When("getting other dog lovers in location") {
                val actualDogLoverInLocationDTOs = walkService.getOtherDogLoversInLocation(mapMarkerId, dogLoverId)

                Then("actual list of DogLoverInLocationDTO should be equal to expected") {
                    actualDogLoverInLocationDTOs shouldBe expectedDogLoverInLocationDTOs
                }
            }
        }

        And("map marker ids and dog lover relationships") {
            val mapMarkers = MapMarkerData.parks
            val mapMarkerIds = mapMarkers.map { it.id }
            val otherDogLoversWalks = WalkData.dogLoversArrivedAtDestinationWalks
            val dogLoverRelationships = DogLoverRelationshipData.johnRelationships

            val expected = otherDogLoversWalks
                    .groupBy { it.mapMarker.id }
                    .mapValues { (_, walks) ->
                        walks.map { walk ->
                            DogLoverInLocationDTO(
                                    walk,
                                    dogLoverRelationships
                            )
                        }
                    }

            every {
                walkRepository.findAllByMapMarkerIdInAndWalkStatusInAndDogLoverIdIn(
                        mapMarkerIds,
                        listOf(WalkStatus.ONGOING, WalkStatus.ARRIVED_AT_DESTINATION),
                        dogLoverRelationships.map { it.id.receiverDogLover.id })
            } returns otherDogLoversWalks

            When("getting other dog lovers in locations") {
                val actual = walkService.getDogLoversInLocations(mapMarkerIds, dogLoverRelationships)

                Then("actual dog lovers in locations should be equal to expected") {
                    actual shouldBe expected
                }
            }
        }
    }
})