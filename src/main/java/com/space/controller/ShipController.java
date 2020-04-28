package com.space.controller;

import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/rest")
public class ShipController {
    private ShipService service;

    @Autowired
    public void setService(ShipService service) {
        this.service = service;
    }

    // Requesting of list of all ships
    @GetMapping("/ships")
    @ResponseStatus(HttpStatus.OK)
    public List<Ship> getAllExistingShipsList(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "planet", required = false) String planet,
            @RequestParam(value = "shipType", required = false) ShipType shipType,
            @RequestParam(value = "after", required = false) Long after,
            @RequestParam(value = "before", required = false) Long before,
            @RequestParam(value = "isUsed", required = false) Boolean isUsed,
            @RequestParam(value = "minSpeed", required = false) Double minSpeed,
            @RequestParam(value = "maxSpeed", required = false) Double maxSpeed,
            @RequestParam(value = "minCrewSize", required = false) Integer minCrewSize,
            @RequestParam(value = "maxCrewSize", required = false) Integer maxCrewSize,
            @RequestParam(value = "minRating", required = false) Double minRating,
            @RequestParam(value = "maxRating", required = false) Double maxRating,
            @RequestParam(value = "order", required = false, defaultValue = "ID") ShipOrder order,
            @RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(value = "pageSize", required = false, defaultValue = "3") Integer pageSize) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(order.getFieldName()));

        return service.getAllExistingShipsList(
                Specification.where(
                        service.nameFilter(name)
                        .and(service.planetFilter(planet)))
                        .and(service.shipTypeFilter(shipType))
                        .and(service.dateFilter(after, before))
                        .and(service.usageFilter(isUsed))
                        .and(service.speedFilter(minSpeed, maxSpeed))
                        .and(service.crewSizeFilter(minCrewSize, maxCrewSize))
                        .and(service.ratingFilter(minRating, maxRating)), pageable)
                .getContent();



    }

    //Requiesting of ship's quantity

    @RequestMapping(value = {"/rest/ships/count"}, method = {RequestMethod.GET})
    @ResponseBody
    public ResponseEntity<Integer> quantityOfShips () {



        return  new ResponseEntity<Integer>(1, HttpStatus.OK) ;
    }


}
