package com.dorka.fifaapp.service;

import org.springframework.stereotype.Service;

@Service
public class ChampionshipService {

    public Boolean checkIfPowerOfTwo(Integer num) {
        if (num == 2) {
            return true;
        }
        if (num % 2 == 1 || num <= 0) {
            return false;
        }
        return checkIfPowerOfTwo(num / 2);
    }
}
