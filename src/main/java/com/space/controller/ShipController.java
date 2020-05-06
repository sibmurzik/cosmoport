package com.space.controller;

import com.space.exceptions.BadRequestExeption;
import com.space.exceptions.ShipNotExist;
import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
             Ship newShip = service.createNewShip(ship);
             return  new ResponseEntity<Ship>(newShip, HttpStatus.OK);
        }
        catch (BadRequestExeption e) {
            return new ResponseEntity<Ship>(HttpStatus.BAD_REQUEST);
        }

    }

    //Updating ship
    @RequestMapping(value = "/rest/ships/{id}", method = {RequestMethod.POST})
    @ResponseBody
    public ResponseEntity<Ship> updateShip(@RequestBody Ship ship,@PathVariable long id ) {
        // Checking for empty body
        if (ship.getName() == null && ship.getPlanet()== null && ship.getProdDate()==null &&
        ship.getShipType() == null && ship.getCrewSize()==null && ship.getSpeed()== null) {
            try {
                return new ResponseEntity<Ship>(service.findShipById(id), HttpStatus.OK);
            }
            catch ( ShipNotExist e) {
                return  new ResponseEntity<Ship>(HttpStatus.NOT_FOUND);
            }
            catch (BadRequestExeption e) {
                return new ResponseEntity<Ship>(HttpStatus.BAD_REQUEST);
            }

        }

        try {
            Ship newShip = service.updateShip(ship, id);
            return  new ResponseEntity<Ship>(newShip, HttpStatus.OK);
        }
        catch (BadRequestExeption e) {
            return new ResponseEntity<Ship>(HttpStatus.BAD_REQUEST);
        }
        catch (ShipNotExist e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }


    }




    //Deleting of ship
    @RequestMapping(value = "/rest/ships/{id}", method = {RequestMethod.DELETE})
    @ResponseBody
    public ResponseEntity<Ship> deleteShip(@PathVariable long id) {
        if (id <=0) return new ResponseEntity<Ship>(HttpStatus.BAD_REQUEST);
        try {
            service.deleteShip(id);
            return new ResponseEntity<Ship>(HttpStatus.OK);
        }
        catch ( ShipNotExist e) {
            return  new ResponseEntity<Ship>(HttpStatus.NOT_FOUND);
        }

    }

    //Finding ship by ID
    @RequestMapping(value = "/rest/ships/{id}", method = {RequestMethod.GET})
     @ResponseBody
    public ResponseEntity<Ship> findShip(@PathVariable long id) {
        try {
            Ship ship = service.findShipById(id);
            return new ResponseEntity<Ship>(ship, HttpStatus.OK);
        }
        catch (ShipNotExist e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (BadRequestExeption e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }



}
