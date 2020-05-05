package com.space.controller;

import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.service.ShipService;
import com.sun.deploy.net.HttpResponse;
import com.sun.deploy.net.MessageHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.io.BufferedInputStream;
import java.net.URL;
import java.util.Collections;
import java.util.List;

@RestController

public class ShipController {
    @Autowired
    private ShipService service;


    // Requesting of list of all ships with filtration
    @RequestMapping(value = "/rest/ships", method = {RequestMethod.GET})
    @ResponseStatus(HttpStatus.OK)
    public List<Ship> allShipList(
            @RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(value = "pageSize", required = false, defaultValue = "3") Integer pageSize,
            @RequestParam(value = "order", required = false, defaultValue = "ID") ShipOrder order,

            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "planet", required = false) String planet,
            @RequestParam(value = "shipType", required = false) ShipType shipType,

            @RequestParam(value = "after", required = false) Long yearAfter,
            @RequestParam(value = "before", required = false) Long yearBefore,

            @RequestParam(value = "isUsed", required = false) Boolean isUsed,

            @RequestParam(value = "minSpeed", required = false) Double minSpeed,
            @RequestParam(value = "maxSpeed", required = false) Double maxSpeed,

            @RequestParam(value = "minCrewSize", required = false) Integer minCrew,
            @RequestParam(value = "maxCrewSize", required = false) Integer maxCrew,

            @RequestParam(value = "minRating", required = false) Double minRating,
            @RequestParam(value = "maxRating", required = false) Double maxRating
            ) {

        PageRequest page = PageRequest.of(pageNumber, pageSize, Sort.by(order.getFieldName()));

        return service.getAllShip(Specification.where(service.filterByName(name).
                                  and(service.filterByPlanet(planet)).
                                  and(service.filterByShipType(shipType)).
                                  and(service.filterByProdDate(yearAfter, yearBefore))).
                                  and(service.filterOfUsedShip(isUsed)).
                                  and(service.filterBySpeed(minSpeed, maxSpeed)).
                                  and(service.filterByCrew(minCrew, maxCrew)).
                                  and(service.filterByRaiting(minRating, maxRating))
                                , page).getContent();

    }


    //Requiesting of ship's quantity

    @RequestMapping(value = "/rest/ships/count", method = {RequestMethod.GET})
    @ResponseStatus(HttpStatus.OK)
    public Long quantityOfShips (
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "planet", required = false) String planet,
            @RequestParam(value = "shipType", required = false) ShipType shipType,
            @RequestParam(value = "after", required = false) Long yearAfter,
            @RequestParam(value = "before", required = false) Long yearBefore,
            @RequestParam(value = "isUsed", required = false) Boolean isUsed,
            @RequestParam(value = "minSpeed", required = false) Double minSpeed,
            @RequestParam(value = "maxSpeed", required = false) Double maxSpeed,
            @RequestParam(value = "minCrewSize", required = false) Integer minCrew,
            @RequestParam(value = "maxCrewSize", required = false) Integer maxCrew,
            @RequestParam(value = "minRating", required = false) Double minRating,
            @RequestParam(value = "maxRating", required = false) Double maxRating) {

             return  service.countShip(
            Specification.where(service.filterByName(name).
                             and(service.filterByPlanet(planet)).
                             and(service.filterByShipType(shipType)).
                             and(service.filterByProdDate(yearAfter, yearBefore))).
                             and(service.filterOfUsedShip(isUsed)).
                             and(service.filterBySpeed(minSpeed, maxSpeed)).
                             and(service.filterByCrew(minCrew, maxCrew)).
                             and(service.filterByRaiting(minRating, maxRating))
             ) ;
    }


    //Creating new ship
    @RequestMapping(value = "/rest/ships", method = {RequestMethod.POST})
    @ResponseBody
    public ResponseEntity<Ship> createNewShip(@RequestBody Ship ship) {
        try {
             service.createNewShip(ship);
             return  new ResponseEntity<Ship>(HttpStatus.OK);
        }
        catch (BadRequestExeption e) {
            return new ResponseEntity<Ship>(HttpStatus.BAD_REQUEST);
        }

    }

    //Updating ship
    @RequestMapping(value = "/rest/ships/{id}", method = {RequestMethod.POST})
    @ResponseBody
    public ResponseEntity<Ship> createNewShip(@RequestBody Ship ship,@PathVariable long id ) {
        try {
            service.updateShip(ship, id);
            return  new ResponseEntity<Ship>(HttpStatus.OK);
        }
        catch (BadRequestExeption e) {
            return new ResponseEntity<Ship>(HttpStatus.BAD_REQUEST);
        }

    }




    //Deleting of ship
    @RequestMapping(value = "/rest/ships/{id}", method = {RequestMethod.DELETE})
    @ResponseStatus(HttpStatus.OK)
    public void deleteShip(@PathVariable long id) {
        service.deleteShip(id);

    }

    //Finding ship by ID
    @RequestMapping(value = "/rest/ships/{id}", method = {RequestMethod.GET})
    @ResponseStatus(HttpStatus.OK)
    public Ship findShip(@PathVariable long id) {
        Ship ship = service.findShipById(id);
        return ship;
    }





}
