package pl.poznan.put.dogloverservice.modules.locationrecommendation.dto

import pl.poznan.put.dogloverservice.modules.walk.dto.DogLoverInLocationDTO

data class LocationRecommendationDTO(

        val mapMarkerRecommendation: MapMarkerRecommendationDTO,

        val dogLoversInLocation: List<DogLoverInLocationDTO>,

        val rating: Double
)