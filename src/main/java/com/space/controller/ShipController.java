package com.space.controller;


import com.space.model.Ship;
import com.space.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ShipController {
    @Autowired
    private ShipService shipService;
    // Requesting of list of all ships
    @RequestMapping(value = {"/rest/ships"}, method = {RequestMethod.GET})
    @ResponseBody
    public ResponseEntity<Ship[]> allShips () {
        List<Ship> listShip = shipService.listAll();


        return  new ResponseEntity<Ship[]>(listShip.toArray(new Ship[0]), HttpStatus.OK) ;

    }

    //Requiesting of ship's quantity

    @RequestMapping(value = {"/rest/ships/count"}, method = {RequestMethod.GET})
    @ResponseBody
    public ResponseEntity<Integer> quantityOfShips () {



        return  new ResponseEntity<Integer>(1, HttpStatus.OK) ;
    }


}
