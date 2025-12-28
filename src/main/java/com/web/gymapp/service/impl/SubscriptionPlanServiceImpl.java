package com.web.gymapp.service.impl;

import com.web.gymapp.model.SubscriptionPlan;
import com.web.gymapp.repository.SubscriptionPlanRepository;
import com.web.gymapp.service.SubscriptionPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionPlanServiceImpl implements SubscriptionPlanService {

    private final SubscriptionPlanRepository planRepository;

    @Override
    @Transactional
    public SubscriptionPlan createPlan(SubscriptionPlan plan) {
        // Validate plan name uniqueness
        if (planRepository.existsByName(plan.getName())) {
            throw new RuntimeException("Plan with name '" + plan.getName() + "' already exists");
        }

        // Validate required fields
        if (plan.getPrice() == null || plan.getPrice() <= 0) {
            throw new RuntimeException("Plan price must be greater than 0");
        }

        if (plan.getDurationMonths() == null || plan.getDurationMonths() <= 0) {
            throw new RuntimeException("Plan duration must be greater than 0");
        }

        return planRepository.save(plan);
    }

    @Override
    @Transactional
    public SubscriptionPlan updatePlan(Long id, SubscriptionPlan plan) {
        SubscriptionPlan existing = planRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plan not found with id: " + id));

        // Check if name is being changed and if new name is unique
        if (!existing.getName().equals(plan.getName())) {
            if (planRepository.existsByNameAndIdNot(plan.getName(), id)) {
                throw new RuntimeException("Plan with name '" + plan.getName() + "' already exists");
            }
        }

        // Validate price and duration
        if (plan.getPrice() != null && plan.getPrice() <= 0) {
            throw new RuntimeException("Plan price must be greater than 0");
        }

        if (plan.getDurationMonths() != null && plan.getDurationMonths() <= 0) {
            throw new RuntimeException("Plan duration must be greater than 0");
        }

        // Update fields
        existing.setName(plan.getName());
        existing.setPrice(plan.getPrice());
        existing.setDurationMonths(plan.getDurationMonths());
        existing.setDescription(plan.getDescription());
        existing.setIsActive(plan.getIsActive());

        return planRepository.save(existing);
    }

    @Override
    @Transactional
    public void deletePlan(Long id) {
        if (!planRepository.existsById(id)) {
            throw new RuntimeException("Plan not found with id: " + id);
        }
        // Note: Consider soft delete or checking if plan has active subscriptions
        planRepository.deleteById(id);
    }

    @Override
    public Optional<SubscriptionPlan> getPlanById(Long id) {
        return planRepository.findById(id);
    }

    @Override
    public Optional<SubscriptionPlan> getPlanByName(String name) {
        return planRepository.findByName(name);
    }

    @Override
    public List<SubscriptionPlan> getAllPlans() {
        return planRepository.findAll();
    }

    @Override
    public List<SubscriptionPlan> getActivePlans() {
        return planRepository.findByIsActiveTrue();
    }

    @Override
    public List<SubscriptionPlan> getInactivePlans() {
        return planRepository.findByIsActiveFalse();
    }

    @Override
    public List<SubscriptionPlan> getPlansByDuration(Integer durationMonths) {
        return planRepository.findByDurationMonths(durationMonths);
    }

    @Override
    public List<SubscriptionPlan> getPlansByPriceRange(Double minPrice, Double maxPrice) {
        return planRepository.findByPriceRange(minPrice, maxPrice);
    }

    @Override
    public List<SubscriptionPlan> getActivePlansOrderedByPrice() {
        return planRepository.findActivePlansOrderedByPrice();
    }

    @Override
    public List<SubscriptionPlan> getActivePlansOrderedByDuration() {
        return planRepository.findActivePlansOrderedByDuration();
    }

    @Override
    @Transactional
    public void togglePlanStatus(Long id, boolean isActive) {
        SubscriptionPlan plan = planRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plan not found with id: " + id));
        plan.setIsActive(isActive);
        planRepository.save(plan);
    }

    @Override
    @Transactional
    public void activatePlan(Long id) {
        togglePlanStatus(id, true);
    }

    @Override
    @Transactional
    public void deactivatePlan(Long id) {
        togglePlanStatus(id, false);
    }

    @Override
    public List<SubscriptionPlan> searchPlansByName(String keyword) {
        return planRepository.searchByName(keyword);
    }

    @Override
    public List<SubscriptionPlan> getMostPopularPlans() {
        return planRepository.findMostPopularPlans();
    }

    @Override
    public Long countActivePlans() {
        return planRepository.countActivePlans();
    }

    @Override
    public Long countInactivePlans() {
        return planRepository.countInactivePlans();
    }

    @Override
    public boolean isPlanNameAvailable(String name) {
        return !planRepository.existsByName(name);
    }

    @Override
    public boolean isPlanNameAvailableForUpdate(String name, Long planId) {
        return !planRepository.existsByNameAndIdNot(name, planId);
    }
}