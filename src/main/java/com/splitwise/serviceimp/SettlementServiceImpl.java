package com.splitwise.serviceimp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.splitwise.dto.SettlementRequest;
import com.splitwise.dto.SettlementResponse;
import com.splitwise.entity.ExpenseGroup;
import com.splitwise.entity.GroupMember;
import com.splitwise.entity.Settlement;
import com.splitwise.entity.User;
import com.splitwise.exception.GroupNotFoundException;
import com.splitwise.exception.UnauthorizedException;
import com.splitwise.exception.UserNotFoundException;
import com.splitwise.repository.ExpenseGroupRepository;
import com.splitwise.repository.GroupMemberRepository;
import com.splitwise.repository.SettlementRepository;
import com.splitwise.repository.UserRepository;
import com.splitwise.service.SettlementService;

@Service
public class SettlementServiceImpl implements SettlementService {

    @Autowired
    private SettlementRepository settlementRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExpenseGroupRepository groupRepository;

    @Autowired
    private GroupMemberRepository memberRepository;

    @Override
    public SettlementResponse settleUp(SettlementRequest request, String loggedInEmail) {

        // Logged-in user (payer)
        User payer = userRepository.findByEmail(loggedInEmail)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));

        // Receiver
        User receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow(() ->
                        new UserNotFoundException("Receiver not found"));

        // Group
        ExpenseGroup group = groupRepository.findById(request.getGroupId())
                .orElseThrow(() ->
                        new GroupNotFoundException("Group not found"));

        // Check payer is member
        boolean payerMember = memberRepository.findByExpenseGroup(group)
                .stream()
                .map(GroupMember::getUser)
                .anyMatch(user -> user.getId().equals(payer.getId()));

        if (!payerMember) {
            throw new UnauthorizedException("You are not a member of this group.");
        }

        // Check receiver is member
        boolean receiverMember = memberRepository.findByExpenseGroup(group)
                .stream()
                .map(GroupMember::getUser)
                .anyMatch(user -> user.getId().equals(receiver.getId()));

        if (!receiverMember) {
            throw new UnauthorizedException("Receiver is not a member of this group.");
        }

        // yaha Save settlement hoga
        Settlement settlement = new Settlement();

        settlement.setPayer(payer);
        settlement.setReceiver(receiver);
        settlement.setExpenseGroup(group);
        settlement.setAmount(request.getAmount());

        Settlement saved = settlementRepository.save(settlement);

        // Response aayega yaha se
        SettlementResponse response = new SettlementResponse();

        response.setSettlementId(saved.getId());
        response.setPayer(payer.getName());
        response.setReceiver(receiver.getName());
        response.setAmount(saved.getAmount());
        response.setSettledAt(saved.getSettledAt());

        return response;
    }
}