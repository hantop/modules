/**
 * QueryServicesLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fenlibao.thirdparty.guoxin.webservice;

public class QueryServicesLocator extends org.apache.axis.client.Service implements QueryServices {

    public QueryServicesLocator() {
    }


    public QueryServicesLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public QueryServicesLocator(String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for QueryServicesHttpSoap11Endpoint
    private String QueryServicesHttpSoap11Endpoint_address = "http://116.90.85.122/ceo/services/QueryServices.QueryServicesHttpSoap11Endpoint/";

    public String getQueryServicesHttpSoap11EndpointAddress() {
        return QueryServicesHttpSoap11Endpoint_address;
    }

    // The WSDD service name defaults to the port name.
    private String QueryServicesHttpSoap11EndpointWSDDServiceName = "QueryServicesHttpSoap11Endpoint";

    public String getQueryServicesHttpSoap11EndpointWSDDServiceName() {
        return QueryServicesHttpSoap11EndpointWSDDServiceName;
    }

    public void setQueryServicesHttpSoap11EndpointWSDDServiceName(String name) {
        QueryServicesHttpSoap11EndpointWSDDServiceName = name;
    }

    public QueryServicesPortType getQueryServicesHttpSoap11Endpoint() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(QueryServicesHttpSoap11Endpoint_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getQueryServicesHttpSoap11Endpoint(endpoint);
    }

    public QueryServicesPortType getQueryServicesHttpSoap11Endpoint(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            QueryServicesSoap11BindingStub _stub = new QueryServicesSoap11BindingStub(portAddress, this);
            _stub.setPortName(getQueryServicesHttpSoap11EndpointWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setQueryServicesHttpSoap11EndpointEndpointAddress(String address) {
        QueryServicesHttpSoap11Endpoint_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (QueryServicesPortType.class.isAssignableFrom(serviceEndpointInterface)) {
                QueryServicesSoap11BindingStub _stub = new QueryServicesSoap11BindingStub(new java.net.URL(QueryServicesHttpSoap11Endpoint_address), this);
                _stub.setPortName(getQueryServicesHttpSoap11EndpointWSDDServiceName());
                return _stub;
            }
        }
        catch (Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        String inputPortName = portName.getLocalPart();
        if ("QueryServicesHttpSoap11Endpoint".equals(inputPortName)) {
            return getQueryServicesHttpSoap11Endpoint();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://webservice.ws.check.ceo.cn", "QueryServices");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://webservice.ws.check.ceo.cn", "QueryServicesHttpSoap11Endpoint"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(String portName, String address) throws javax.xml.rpc.ServiceException {
        
if ("QueryServicesHttpSoap11Endpoint".equals(portName)) {
            setQueryServicesHttpSoap11EndpointEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
