package pl.poznan.put.dogloverservice.modules.locationrecommendation.dto

import pl.poznan.put.dogloverservice.modules.walk.dto.DogLoverInLocationDTO

data class LocationRecommendationDTO(

        val mapMarkerRecommendationDTO: MapMarkerRecommendationDTO,

        val dogLoversInLocationDTO: List<DogLoverInLocationDTO>,

        val rating: Int
)