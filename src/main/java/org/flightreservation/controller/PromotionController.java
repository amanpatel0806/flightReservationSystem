package org.flightreservation.controller;

import org.flightreservation.data.PromotionDAO;
import org.flightreservation.entity.Promotion;

import java.util.List;

/**
 * Controller for promotion management.
 */
public class PromotionController {
    private PromotionDAO promotionDAO;

    public PromotionController() {
        this.promotionDAO = new PromotionDAO();
    }

    public boolean createPromotion(Promotion promotion) {
        return promotionDAO.save(promotion);
    }

    public boolean updatePromotion(Promotion promotion) {
        return promotionDAO.update(promotion);
    }

    public boolean deletePromotion(int id) {
        return promotionDAO.delete(id);
    }

    public Promotion getPromotionById(int id) {
        return promotionDAO.findById(id);
    }

    public Promotion getPromotionByCode(String code) {
        return promotionDAO.findByCode(code);
    }

    public List<Promotion> getAllPromotions() {
        return promotionDAO.findAll();
    }

    public List<Promotion> getActivePromotions() {
        return promotionDAO.findActive();
    }
}
