package com.yunesta.mpesa.collector


import com.yunesta.mpesa.colletor.Collector
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v1/collectors")
class CollectorController @Autowired constructor(
    private val collectorService: CollectorService
) {

    // Create a new collector
    @PostMapping
    fun createCollector(@RequestBody collector: Collector): ResponseEntity<Collector> {
        val createdCollector = collectorService.createCollector(collector)
        return ResponseEntity(createdCollector, HttpStatus.CREATED)
    }

    // Get all collectors
    @GetMapping
    fun getAllCollectors(): ResponseEntity<List<Collector>> {
        val collectors = collectorService.getAllCollectors()
        return ResponseEntity(collectors, HttpStatus.OK)
    }

    // Get a collector by ID
    @GetMapping("/{id}")
    fun getCollectorById(@PathVariable id: Long): ResponseEntity<Collector> {
        val collector = collectorService.getCollectorById(id)
        return ResponseEntity(collector, HttpStatus.OK)
    }

    // Update a collector
    @PutMapping("/{id}")
    fun updateCollector(
        @PathVariable id: Long,
        @RequestBody updatedCollector: Collector
    ): ResponseEntity<Collector> {
        val updated = collectorService.updateCollector(id, updatedCollector)
        return ResponseEntity(updated, HttpStatus.OK)
    }

    // Delete a collector
    @DeleteMapping("/{id}")
    fun deleteCollector(@PathVariable id: Long): ResponseEntity<Void> {
        collectorService.deleteCollector(id)
        return ResponseEntity<Void>(HttpStatus.NO_CONTENT)
    }
}
