package pl.poznan.put.dogloverservice.modules.locationrecommendation.dto

import pl.poznan.put.dogloverservice.modules.mapmarker.dto.MapMarkerDistanceDTO
import pl.poznan.put.dogloverservice.modules.walk.dto.DogLoverInLocationDTO

data class LocationRecommendationDTO(

        val mapMarkerRecommendation: MapMarkerDistanceDTO,

        val dogLoversInLocation: List<DogLoverInLocationDTO>,

        val rating: Double
)