package com.fenlibao.thirdparty.guoxin.webservice;

import com.fenlibao.thirdparty.guoxin.vo.xsd.IdentityRequest;
import com.fenlibao.thirdparty.guoxin.vo.xsd.IdentityResponse;


public class CheckServicePortTypeProxy implements CheckServicePortType {
  private String _endpoint = null;
  private CheckServicePortType checkServicePortType = null;
  
  public CheckServicePortTypeProxy() {
    _initCheckServicePortTypeProxy();
  }
  
  public CheckServicePortTypeProxy(String endpoint) {
    _endpoint = endpoint;
    _initCheckServicePortTypeProxy();
  }
  
  private void _initCheckServicePortTypeProxy() {
    try {
      checkServicePortType = (new CheckServiceLocator()).getCheckServiceHttpSoap11Endpoint();
      if (checkServicePortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)checkServicePortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)checkServicePortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (checkServicePortType != null)
      ((javax.xml.rpc.Stub)checkServicePortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public CheckServicePortType getCheckServicePortType() {
    if (checkServicePortType == null)
      _initCheckServicePortTypeProxy();
    return checkServicePortType;
  }
  
  public IdentityResponse identityCheck(IdentityRequest req) throws java.rmi.RemoteException{
    if (checkServicePortType == null)
      _initCheckServicePortTypeProxy();
    return checkServicePortType.identityCheck(req);
  }
  
  
}