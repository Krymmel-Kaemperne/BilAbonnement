package com.example.bilabonnement.Service;

import com.example.bilabonnement.Model.Damage;
import com.example.bilabonnement.Repository.DamageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// Serviceklasse for forretningslogik vedrørende skader
@Service
public class DamageService {
    @Autowired
    private DamageRepository damageRepository;

    public Damage create(Damage damage) {
        return damageRepository.create(damage);
    }

    public Damage findById(int id) {
        return damageRepository.findById(id);
    }

    public List<Damage> findByConditionReportId(int conditionReportId) {
        return damageRepository.findByConditionReportId(conditionReportId);
    }

    public List<Damage> findAll() {
        return damageRepository.findAll();
    }

    public int update(Damage damage) {
        return damageRepository.update(damage);
    }

    public int delete(int id) {
        return damageRepository.delete(id);
    }


} 