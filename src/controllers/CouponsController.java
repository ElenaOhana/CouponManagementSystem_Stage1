/*
package com.couponsystemstage3.controllers;

import com.couponsystemstage3.entity_beans.Coupon;
import com.couponsystemstage3.exceptions.CouponSystemException;
import com.couponsystemstage3.services.CompanyServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;


//TODO OLD EXAMPLE FROM TAVOR
@RestController
@RequestMapping({"coupons"})
@Slf4j
public class CouponsController {
    @Autowired
    CompanyServiceImpl companyServiceImpl;

    @GetMapping({"{id}"})
    public Coupon getCoupon(@PathVariable long id) throws CouponSystemException {
        log.info("Getting coupon with couponId {}", id);
        //return companyServiceImpl.getCouponById(id);
        return companyServiceImpl.getCouponByIdOfConnectedCompany(id); // TODO THE check because change the method
    }

    */
/*@PostMapping
    public Coupon addCoupon(@RequestBody Coupon coupon) throws CouponSystemException {
        log.info("Adding coupon {}", coupon);
        return companyServiceImpl.createCoupon(coupon);
    }*//*


    @PostMapping //TODO all checks must to be only in service. TODO RestControllerExceptionHandler class and put ResponseEntity<> there!
    public ResponseEntity<?> addCoupon(@RequestBody Coupon coupon) throws CouponSystemException {//fixme because not adding a companyId & categoryId
        if (coupon.getAmount() <= 0 || coupon.getPrice() <= 0) {
            return new ResponseEntity<String>("Coupon amount or price must be positive!", HttpStatus.BAD_REQUEST);
        } else {
            log.info("Adding coupon {}", coupon);
            companyServiceImpl.addCoupon(coupon);
            return new ResponseEntity<Coupon>(coupon, HttpStatus.CREATED);
        }
    }

  */
/*  @DeleteMapping("{id}")
    public void deleteCoupon(@PathVariable Long id) {
        log.info("Deleting coupon with couponId {}", id);
        companyServiceImpl.removeCoupon(id);
    }*//*


    @DeleteMapping("{id}") // Here I catch the exception and hide it from the client(Postman), but StackTrace is printed at Idea. Google received everything(((
    //TODO how to hide StackTrace from client??
    public ResponseEntity<?> deleteCoupon(@PathVariable Long id) {
        Coupon coupon;
        try {
            //coupon = companyServiceImpl.getCouponById(id);
            coupon = companyServiceImpl.getCouponByIdOfConnectedCompany(id); // TODO THE check because change the method
            log.info("Deleting coupon with couponId {}", id);
            companyServiceImpl.removeCoupon(id);
            return new ResponseEntity<Coupon>(coupon, HttpStatus.OK);
        } catch (CouponSystemException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<Coupon>((Coupon) null, HttpStatus.EXPECTATION_FAILED);
    }

    @GetMapping
    public Collection<Coupon> getAll() {
        Collection<Coupon> coupons = new ArrayList<Coupon>();
        log.info("Getting all coupons ");
        coupons = companyServiceImpl.getAllCompanyCoupons();
        return coupons;
    }

    @GetMapping("get-by-id-title") //DONE!!!
    public ResponseEntity<?> getCouponByIdAndName(@RequestParam("id") Long id, @RequestParam("title") String title) {
        Coupon coupon;
        try {
            coupon = companyServiceImpl.getCouponByIdAndTitle(id, title);
            log.info("Getting coupon by id {} and title {}", id, title);
            return new  ResponseEntity<Coupon>(coupon, HttpStatus.OK);
        } catch (CouponSystemException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<Coupon>((Coupon) null, HttpStatus.EXPECTATION_FAILED);
    }

///TODO all checks to amount, price positive and eg. Bring it from CouponController - addCoupon(). ??? может в purchase???
}
*/
