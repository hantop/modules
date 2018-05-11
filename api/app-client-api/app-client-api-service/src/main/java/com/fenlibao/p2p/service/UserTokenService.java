package com.fenlibao.p2p.service;

public interface UserTokenService {

    boolean checkToken(String token);

    boolean isInvalidToken(String token, String userId, String clientType);

}
