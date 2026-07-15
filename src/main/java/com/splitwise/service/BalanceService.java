package com.splitwise.service;

import java.util.List;
import com.splitwise.dto.BalanceResponse;

public interface BalanceService {

    List<BalanceResponse> calculateBalance(Long groupId);

}