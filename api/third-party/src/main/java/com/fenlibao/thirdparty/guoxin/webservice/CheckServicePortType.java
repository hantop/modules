package com.fenlibao.thirdparty.guoxin.webservice;

/**
 * CheckServicePortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */


import com.fenlibao.thirdparty.guoxin.vo.xsd.IdentityRequest;
import com.fenlibao.thirdparty.guoxin.vo.xsd.IdentityResponse;

public interface CheckServicePortType extends java.rmi.Remote {
    public IdentityResponse identityCheck(IdentityRequest req) throws java.rmi.RemoteException;
}
