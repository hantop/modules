package com.fenlibao.p2p.service.xinwang.sign;

import com.fenlibao.p2p.model.xinwang.entity.sign.ElectronicSignature;

import java.io.File;

public interface AgreementService {

    void createAgreement(ElectronicSignature electronicSignature) throws Exception;

    File[] fillAgreement(ElectronicSignature electronicSignature) throws Exception;
}
