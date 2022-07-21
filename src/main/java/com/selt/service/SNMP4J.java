package com.selt.service;

import com.selt.repository.OIDRepo;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Data
@Service
@RequiredArgsConstructor
public class SNMP4J {

    public static final int DEFAULT_VERSION = SnmpConstants.version2c;
    public static final String DEFAULT_PROTOCOL = "udp";
    public static final int DEFAULT_PORT = 161;
    public static final long DEFAULT_TIMEOUT = 3 * 1000L;
    public static final int DEFAULT_RETRY = 3;
    @Autowired
    private static OIDRepo oidRepo;


    public static CommunityTarget createDefault(String ip, String community) {
        Address address = GenericAddress.parse(DEFAULT_PROTOCOL + ":" + ip
                + "/" + DEFAULT_PORT);
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString(community));
        target.setAddress(address);
        target.setVersion(DEFAULT_VERSION);
        target.setTimeout(DEFAULT_TIMEOUT); // milliseconds
        target.setRetries(DEFAULT_RETRY);
        return target;
    }

    public static String snmpGet(String ip, String community, String oidValue, String oidName) {
        //String oidName=oidRepo.findOIDByOidValue(oidValue).getOidName();

        String info = new String();

        CommunityTarget target = createDefault(ip, community);
        Snmp snmp = null;
        try {
            PDU pdu = new PDU();
            // pdu.add(new VariableBinding(new OID(new int[]
            // {1,3,6,1,2,1,1,2})));
            pdu.add(new VariableBinding(new OID(oidValue)));

            DefaultUdpTransportMapping transport = new DefaultUdpTransportMapping();
            snmp = new Snmp(transport);
            snmp.listen();

            pdu.setType(PDU.GET);
            ResponseEvent respEvent = snmp.send(pdu, target);

            PDU response = respEvent.getResponse();

            if (response == null) {
                info = "Minął czas oczekiwania, odczyt nie powiódł się.";
            } else {

                // Vector<VariableBinding> vbVect =
                // response.getVariableBindings();
                // System.out.println("vb size:" + vbVect.size());
                // if (vbVect.size() == 0) {
                // System.out.println("response vb size is 0 ");
                // } else {
                // VariableBinding vb = vbVect.firstElement();
                // System.out.println(vb.getOid() + " = " + vb.getVariable());
                // }

                //info = "response pdu size is " + response.size();
                for (int i = 0; i < response.size(); i++) {
                    VariableBinding vb = response.get(i);
                    info = oidName + " " + vb.getVariable();

                }

            }
            //System.out.println("SNMP GET one OID value finished !");
        } catch (Exception e) {
            e.printStackTrace();
            //System.out.println("SNMP Get Exception:" + e);
        } finally {
            if (snmp != null) {
                try {
                    snmp.close();
                } catch (IOException ex1) {
                    snmp = null;
                }
            }

        }
        return info;
    }


    public static Long snmpGet(String ip, String community, String oidValue) {


        String info = null;

        CommunityTarget target = createDefault(ip, community);
        Snmp snmp = null;
        try {
            PDU pdu = new PDU();

            pdu.add(new VariableBinding(new OID(oidValue)));

            DefaultUdpTransportMapping transport = new DefaultUdpTransportMapping();
            snmp = new Snmp(transport);
            snmp.listen();

            pdu.setType(PDU.GET);
            ResponseEvent respEvent = snmp.send(pdu, target);

            PDU response = respEvent.getResponse();

            if (response == null) {
                info = "0";
            } else {

                for (int i = 0; i < response.size(); i++) {
                    VariableBinding vb = response.get(i);
                    info = String.valueOf(vb.getVariable());

                }

            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (snmp != null) {
                try {
                    snmp.close();
                } catch (IOException ex1) {
                    snmp = null;
                }
            }

        }
        return Long.parseLong(info);
    }
}
