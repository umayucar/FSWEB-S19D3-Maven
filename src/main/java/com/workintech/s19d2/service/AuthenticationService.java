package com.workintech.s19d2.service;

import com.workintech.s19d2.dao.MemberRepository;
import com.workintech.s19d2.dao.RoleRepository;
import com.workintech.s19d2.entity.Member;
import com.workintech.s19d2.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Member register(String email, String password) {

        if (memberRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException(
                    "User with given email already exist"
            );
        }

        Role role = roleRepository.findByAuthority("ADMIN")
                .orElseThrow(() ->
                        new RuntimeException("Role not found"));

        Member member = new Member();

        member.setEmail(email);
        member.setPassword(passwordEncoder.encode(password));
        member.setRoles(new ArrayList<>(List.of(role)));

        return memberRepository.save(member);
    }
}