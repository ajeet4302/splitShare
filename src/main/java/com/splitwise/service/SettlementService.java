package com.splitwise.service;

import com.splitwise.dto.SettlementRequest;
import com.splitwise.dto.SettlementResponse;

public interface SettlementService {

    SettlementResponse settleUp(SettlementRequest request, String loggedInEmail);

}