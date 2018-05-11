package com.fenlibao.thirdparty.guoxin.webservice;

public class QueryServicesPortTypeProxy implements QueryServicesPortType {
  private String _endpoint = null;
  private QueryServicesPortType queryServicesPortType = null;
  
  public QueryServicesPortTypeProxy() {
    _initQueryServicesPortTypeProxy();
  }
  
  public QueryServicesPortTypeProxy(String endpoint) {
    _endpoint = endpoint;
    _initQueryServicesPortTypeProxy();
  }
  
  private void _initQueryServicesPortTypeProxy() {
    try {
      queryServicesPortType = (new QueryServicesLocator()).getQueryServicesHttpSoap11Endpoint();
      if (queryServicesPortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)queryServicesPortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)queryServicesPortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (queryServicesPortType != null)
      ((javax.xml.rpc.Stub)queryServicesPortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public QueryServicesPortType getQueryServicesPortType() {
    if (queryServicesPortType == null)
      _initQueryServicesPortTypeProxy();
    return queryServicesPortType;
  }
  
  public String querySingle(String userName_, String password_, String type_, String param_) throws java.rmi.RemoteException{
    if (queryServicesPortType == null)
      _initQueryServicesPortTypeProxy();
    return queryServicesPortType.querySingle(userName_, password_, type_, param_);
  }
  
  public String queryBatch(String userName_, String password_, String type_, String param_) throws java.rmi.RemoteException{
    if (queryServicesPortType == null)
      _initQueryServicesPortTypeProxy();
    return queryServicesPortType.queryBatch(userName_, password_, type_, param_);
  }
  
  
}