package com.yunesta.mpesa.town

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v1/towns")
class TownController(private val townService: TownService) {

    // Get all towns
    @GetMapping
    fun getAllTowns(): List<Town> {
        return townService.getAllTowns()
    }

    // Get a specific town by its ID
    @GetMapping("/{id}")
    fun getTownById(@PathVariable id: Long): Town {
        return townService.getTownById(id)
    }

    // Create a new town
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createTown(@RequestBody town: Town): Town {
        return townService.createTown(town)
    }

    // Update an existing town
    @PutMapping("/{id}")
    fun updateTown(@RequestBody town :Town, @PathVariable("id") id: Long ): Town {
        return townService.updateTown(town, id)
    }

    // Delete a town by its ID
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteTown(@PathVariable id: Long) {
        townService.deleteTown(id)
    }
}
