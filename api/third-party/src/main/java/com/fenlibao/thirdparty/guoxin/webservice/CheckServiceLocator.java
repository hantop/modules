package com.fenlibao.thirdparty.guoxin.webservice;

/**
 * CheckServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */


public class CheckServiceLocator extends org.apache.axis.client.Service implements CheckService {

    public CheckServiceLocator() {
    }


    public CheckServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public CheckServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for CheckServiceHttpSoap11Endpoint
    private java.lang.String CheckServiceHttpSoap11Endpoint_address = "http://221.122.102.131/ceo/services/CheckService.CheckServiceHttpSoap11Endpoint/";

    public java.lang.String getCheckServiceHttpSoap11EndpointAddress() {
        return CheckServiceHttpSoap11Endpoint_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String CheckServiceHttpSoap11EndpointWSDDServiceName = "CheckServiceHttpSoap11Endpoint";

    public java.lang.String getCheckServiceHttpSoap11EndpointWSDDServiceName() {
        return CheckServiceHttpSoap11EndpointWSDDServiceName;
    }

    public void setCheckServiceHttpSoap11EndpointWSDDServiceName(java.lang.String name) {
        CheckServiceHttpSoap11EndpointWSDDServiceName = name;
    }

    public CheckServicePortType getCheckServiceHttpSoap11Endpoint() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(CheckServiceHttpSoap11Endpoint_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getCheckServiceHttpSoap11Endpoint(endpoint);
    }

    public CheckServicePortType getCheckServiceHttpSoap11Endpoint(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            CheckServiceSoap11BindingStub _stub = new CheckServiceSoap11BindingStub(portAddress, this);
            _stub.setPortName(getCheckServiceHttpSoap11EndpointWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setCheckServiceHttpSoap11EndpointEndpointAddress(java.lang.String address) {
        CheckServiceHttpSoap11Endpoint_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (CheckServicePortType.class.isAssignableFrom(serviceEndpointInterface)) {
                CheckServiceSoap11BindingStub _stub = new CheckServiceSoap11BindingStub(new java.net.URL(CheckServiceHttpSoap11Endpoint_address), this);
                _stub.setPortName(getCheckServiceHttpSoap11EndpointWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
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
        java.lang.String inputPortName = portName.getLocalPart();
        if ("CheckServiceHttpSoap11Endpoint".equals(inputPortName)) {
            return getCheckServiceHttpSoap11Endpoint();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://webservice.ws.check.ceo.cn", "CheckService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://webservice.ws.check.ceo.cn", "CheckServiceHttpSoap11Endpoint"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("CheckServiceHttpSoap11Endpoint".equals(portName)) {
            setCheckServiceHttpSoap11EndpointEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
