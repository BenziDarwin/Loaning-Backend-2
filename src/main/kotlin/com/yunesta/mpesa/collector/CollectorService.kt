package com.yunesta.mpesa.collector

import com.yunesta.mpesa.colletor.Collector
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class CollectorService @Autowired constructor(
    private val collectorRepository: CollectorRepository
) {

    // Create a new collector
    fun createCollector(collector: Collector): Collector {
        return collectorRepository.save(collector)
    }

    // Get all collectors
    fun getAllCollectors(): List<Collector> {
        return collectorRepository.findAll()
    }

    // Get a collector by ID
    fun getCollectorById(id: Long): Collector {
        return collectorRepository.findById(id).orElseThrow {
            IllegalArgumentException("Collector not found with ID: $id")
        }
    }

    // Update an existing collector
    fun updateCollector(id: Long, updatedCollector: Collector): Collector {
        val existingCollector = collectorRepository.findById(id).orElseThrow {
            IllegalArgumentException("Collector not found with ID: $id")
        }

        // Update fields
        existingCollector.firstName = updatedCollector.firstName
        existingCollector.secondName = updatedCollector.secondName
        existingCollector.thirdName = updatedCollector.thirdName
        existingCollector.dateOfBirth = updatedCollector.dateOfBirth
        existingCollector.gender = updatedCollector.gender
        existingCollector.email = updatedCollector.email
        existingCollector.phoneNumber = updatedCollector.phoneNumber
        existingCollector.profilePicture = updatedCollector.profilePicture
        existingCollector.idFrontImage = updatedCollector.idFrontImage
        existingCollector.idBackImage = updatedCollector.idBackImage
        existingCollector.password = updatedCollector.password

        return collectorRepository.save(existingCollector)
    }

    // Delete a collector by ID
    fun deleteCollector(id: Long) {
        val collector = collectorRepository.findById(id).orElseThrow {
            IllegalArgumentException("Collector not found with ID: $id")
        }
        collectorRepository.delete(collector)
    }
}
