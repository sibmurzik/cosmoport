package com.space.service;


import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ShipService {
    @Autowired
    ShipRepository repo;




    public Page<Ship> getAllExistingShipsList(Specification<Ship> specification, Pageable sortedByName) {

        Page<Ship> result = repo.findAll(specification, sortedByName);
        return result;
    }

    public Specification<Ship> nameFilter(String name) {
        return (root, query, criteriaBuilder) -> name == null ? null : criteriaBuilder.like(root.get("name"), "%" + name + "%");
    }


    public Specification<Ship> planetFilter(String planet) {
        return (root, query, criteriaBuilder) -> planet == null ? null : criteriaBuilder.like(root.get("planet"), "%" + planet + "%");
    }


    public Specification<Ship> shipTypeFilter(ShipType shipType) {
        return (root, query, criteriaBuilder) -> shipType == null ? null : criteriaBuilder.equal(root.get("shipType"), shipType);
    }


    public Specification<Ship> dateFilter(Long after, Long before) {
        return (root, query, criteriaBuilder) -> {
            if (after == null && before == null) {
                return null;
            }
            if (after == null) {
                Date before1 = new Date(before);
                return criteriaBuilder.lessThanOrEqualTo(root.get("prodDate"), before1);
            }
            if (before == null) {
                Date after1 = new Date(after);
                return criteriaBuilder.greaterThanOrEqualTo(root.get("prodDate"), after1);
            }
            //time difference
            Date before1 = new Date(before - 3600001);
            Date after1 = new Date(after);
            return criteriaBuilder.between(root.get("prodDate"), after1, before1);
        };
    }


    public Specification<Ship> usageFilter(Boolean isUsed) {
        return (root, query, criteriaBuilder) -> {
            if (isUsed == null) {
                return null;
            }
            if (isUsed) {
                return criteriaBuilder.isTrue(root.get("isUsed"));
            } else {
                return criteriaBuilder.isFalse(root.get("isUsed"));
            }
        };
    }


    public Specification<Ship> speedFilter(Double min, Double max) {
        return (root, query, criteriaBuilder) -> {
            if (min == null && max == null) {
                return null;
            }
            if (min == null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("speed"), max);
            }
            if (max == null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("speed"), min);
            }
            return criteriaBuilder.between(root.get("speed"), min, max);
        };
    }


    public Specification<Ship> crewSizeFilter(Integer min, Integer max) {
        return (root, query, criteriaBuilder) -> {
            if (min == null && max == null) {
                return null;
            }
            if (min == null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("crewSize"), max);
            }
            if (max == null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("crewSize"), min);
            }
            return criteriaBuilder.between(root.get("crewSize"), min, max);
        };
    }


    public Specification<Ship> ratingFilter(Double min, Double max) {
        return (root, query, criteriaBuilder) -> {
            if (min == null && max == null) {
                return null;
            }
            if (min == null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("rating"), max);
            }
            if (max == null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("rating"), min);
            }
            return criteriaBuilder.between(root.get("rating"), min, max);
        };
    }
}




