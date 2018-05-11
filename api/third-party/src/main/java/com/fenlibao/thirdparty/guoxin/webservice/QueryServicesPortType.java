/**
 * QueryServicesPortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fenlibao.thirdparty.guoxin.webservice;

public interface QueryServicesPortType extends java.rmi.Remote {
    public String querySingle(String userName_, String password_, String type_, String param_) throws java.rmi.RemoteException;
    public String queryBatch(String userName_, String password_, String type_, String param_) throws java.rmi.RemoteException;
}
