package com.yunesta.mpesa.town

import com.yunesta.mpesa.collector.CollectorRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.yunesta.mpesa.colletor.Collector
import java.util.*

@Service
@Transactional
class TownService @Autowired constructor(
    private val townRepository: TownRepository,
    private val collectorRepository: CollectorRepository
) {

    // Get all towns
    fun getAllTowns(): List<Town> {
        return townRepository.findAll()
    }

    // Get a specific town by its ID
    fun getTownById(id: Long): Town {
        return townRepository.findById(id).orElseThrow { IllegalArgumentException("Town not found") }
    }

    // Create a new town
    fun createTown(town: Town): Town {
        val collector = collectorRepository.findById(town.collector.id).orElseThrow { IllegalArgumentException("Collector not found") }
        val newTown = Town(
            id = 0L, // ID will be auto-generated
            name = town.name,
            nickname = town.nickname,
            enrollmentDate = Date(),
            collector = collector,
            loanPortfolio = town.loanPortfolio,
            profilePicture = town.profilePicture
        )
        return townRepository.save(town)
    }

    // Update an existing town
    fun updateTown(updateTown: Town, id: Long): Town {
        val town = townRepository.findById(id).orElseThrow { IllegalArgumentException("Town not found") }
        val collector = collectorRepository.findById(updateTown.collector.id).orElseThrow { IllegalArgumentException("Collector not found") }
        // Reassign values to mutable fields of the town
        town.apply {
            this.name = updateTown.name
            this.nickname = updateTown.nickname
            this.collector = collector
            this.loanPortfolio = updateTown.loanPortfolio
            this.profilePicture = updateTown.profilePicture
        }
        return townRepository.save(town)
    }


    // Delete a town by its ID
    fun deleteTown(id: Long) {
        if (!townRepository.existsById(id)) {
            throw IllegalArgumentException("Town not found")
        }
        townRepository.deleteById(id)
    }
}
