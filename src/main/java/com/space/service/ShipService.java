package com.space.service;


import com.space.exceptions.BadRequestExeption;
import com.space.exceptions.ShipNotExist;
import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;


@Service
@Transactional
public class ShipService {
    @Autowired
    ShipRepository repo;


// Getting of list of all ships with filration

    public Page<Ship> getAllShip(Specification<Ship> specification, Pageable page) {
        Page<Ship> ships;
        ships = repo.findAll(specification, page);
        return ships;
    }


    //  Setting of filters

    public Specification<Ship> filterByName (String name) {
        return (root, query, criteriaBuilder) -> name == null ? null : criteriaBuilder.like(root.get("name"), "%" + name + "%");
    }


    public Specification<Ship> filterByPlanet(String planet) {
        return (root, query, criteriaBuilder) -> planet == null ? null : criteriaBuilder.like(root.get("planet"), "%" + planet + "%");
    }


    public Specification<Ship> filterByShipType (ShipType shipType) {
        return (root, query, criteriaBuilder) -> shipType == null ? null : criteriaBuilder.equal(root.get("shipType"), shipType);
    }


    public Specification<Ship> filterByProdDate(Long yearAfter, Long yearBefore) {

        return (root, query, criteriaBuilder) -> {
            if (yearAfter != null && yearBefore != null) {
                return criteriaBuilder.between(root.get("prodDate"), new Date(yearAfter), new Date(yearBefore)); }
            else if(yearAfter!=null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("prodDate"), new Date(yearAfter));
            }
            else if (yearBefore!= null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("prodDate"), new Date(yearBefore));
            }
            else return null;
        };
    }

    public Specification<Ship> filterOfUsedShip(Boolean isUsed) {
        return (root, query, criteriaBuilder) -> isUsed == null ? null :
                isUsed ? criteriaBuilder.isTrue(root.get("isUsed")):
                         criteriaBuilder.isFalse(root.get("isUsed"));
    }

    public  Specification<Ship> filterBySpeed (Double minSpeed, Double maxSpeed) {
        return (root, query, criteriaBuilder) -> {
            if (minSpeed != null && maxSpeed != null) {
                return criteriaBuilder.between(root.get("speed"), minSpeed, maxSpeed); }
            else if(minSpeed !=null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("speed"), minSpeed);
            }
            else if (maxSpeed!= null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("speed"), maxSpeed);
            }
            else return null;
        };
    }

    public  Specification<Ship> filterByCrew (Integer minCrew, Integer maxCrew) {
        return (root, query, criteriaBuilder) -> {
            if (minCrew != null && maxCrew != null) {
                return criteriaBuilder.between(root.get("crewSize"), minCrew, maxCrew); }
            else if(minCrew !=null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("crewSize"), minCrew);
            }
            else if (maxCrew!= null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("crewSize"), maxCrew);
            }
            else return null;
        };
    }

    public  Specification<Ship> filterByRaiting (Double minRating , Double maxRating ) {
        return (root, query, criteriaBuilder) -> {
            if (minRating != null && maxRating != null) {
                return criteriaBuilder.between(root.get("rating"), minRating, maxRating); }
            else if(minRating !=null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("rating"), minRating);
            }
            else if (maxRating != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("rating"), maxRating);
            }
            else return null;
        };
    }



        // Getting of quantity of all ships
        public Long countShip(Specification<Ship> specification) {
            return repo.count(specification);
        }



         public Ship createNewShip(Ship ship) throws  BadRequestExeption{
             //Checking of missing arguments
             if (checkingMissingDatum(ship) )
             {
                 throw new BadRequestExeption();
             }
             //Checking if production date is in range
             if ( validationShipDatum(ship))
             {
                 throw new BadRequestExeption();
             }
             // Calculation of rating
             ship.setRating(shipRating(ship));

             return repo.saveAndFlush(ship);

         }


      // Updating of ship
      public Ship updateShip (Ship newShip, long id) throws  BadRequestExeption, ShipNotExist{
          if (id < 1)  throw new BadRequestExeption();
          Ship ship = findShipById(id);


          //Checking if production date is in range
          if ( validationShipDatum(newShip))
          {
              throw new BadRequestExeption();
          }
          if (newShip.getName()!=null) ship.setName(newShip.getName());
          if (newShip.getPlanet()!=null)ship.setPlanet(newShip.getPlanet());
          if (newShip.getShipType()!=null)ship.setShipType(newShip.getShipType());
          if (newShip.getProdDate()!=null)ship.setProdDate(newShip.getProdDate());
          ship.setUsed(newShip.isUsed());
          if (newShip.getSpeed()!=null)ship.setSpeed(newShip.getSpeed());
          if (newShip.getCrewSize()!=null)ship.setCrewSize(newShip.getCrewSize());
          ship.setRating(shipRating(ship));
          return repo.saveAndFlush(ship);

      }


      //Deleting of ship
     public void deleteShip (long id) throws ShipNotExist{
         if ( !repo.existsById(id)) {
             throw  new ShipNotExist();
         }
         repo.deleteById(id);
      }

         //Finding ship by ID
     public Ship findShipById(long id) throws ShipNotExist {
             if ( !repo.existsById(id)) {
                 throw  new ShipNotExist();
             }
             Optional<Ship> optional = repo.findById(id);
             return optional.orElse(null);


     }

     //Checking of missing ship's datum
    private boolean checkingMissingDatum (Ship ship) {
        boolean filter1 = false;
        if(ship.getName()==null || ship.getPlanet()==null) {
            filter1 = true;
        }
        else {
            filter1 = (ship.getName().equals("") || ship.getPlanet().equals(""));
        }
        boolean filter2 = (ship.getProdDate() == null || ship.getSpeed()==null||
                ship.getCrewSize() == null || ship.getShipType()==null);

        return (filter1 || filter2 );
    }


    //Validation of ship's datum and calculation
    private boolean validationShipDatum (Ship ship) {
        boolean filter3 =false;
        boolean filter4 = false;
        boolean filter5 = false;
        boolean filter6 = false;
        boolean filter7 = false;
        //Checking if production date is in range;
        if(ship.getProdDate() != null) {
            Calendar calendarBefore = Calendar.getInstance();
            calendarBefore.set(3019, Calendar.DECEMBER, 31);
            Date dateBefore = calendarBefore.getTime();
            Calendar calendarAfter = Calendar.getInstance();
            calendarAfter.set(2800, Calendar.JANUARY, 1);
            Date dateAfter = calendarAfter.getTime();
            filter3 = (ship.getProdDate().after(dateAfter) && ship.getProdDate().before(dateBefore));
        }
        //Checking if speed is in range
        if (ship.getSpeed() != null) {
            long speed = Math.round(ship.getSpeed() * 100);
            filter4 = (speed >= 1 && speed <= 99);
        }
        // Checking if crew quantity is in range
        if (ship.getCrewSize() != null ) {
            filter5 = (ship.getCrewSize() >= 1 && ship.getCrewSize() <= 9999);
        }
        //Checking of ship's name and planet name length
        if (ship.getName() != null) {
            filter6 = ship.getName().length() <= 50 ;
        }
        if (ship.getPlanet() != null) {
            filter7 =  ship.getPlanet().length() <= 50;
        }
         return (!filter3 || !filter4 || !filter5 || !filter6 || !filter7);
    }

     //Calculation of ship's rating
    private Double shipRating (Ship ship) {
        Calendar date = Calendar.getInstance();
        date.setTime(ship.getProdDate());
        int year = date.get(Calendar.YEAR);
        Double k = ship.isUsed() ? 0.5: 1;
        return (80 * ship.getSpeed()* k ) / (3019 - year + 1);
    }






    }






