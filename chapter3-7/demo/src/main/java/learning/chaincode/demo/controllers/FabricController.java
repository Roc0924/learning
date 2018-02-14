package learning.chaincode.demo.controllers;

import learning.chaincode.demo.configs.FabricConfig;
import learning.chaincode.demo.configs.FabricConfigManager;
import learning.chaincode.demo.configs.InstalledProposal;
import learning.chaincode.demo.dtos.InstallProposalResponse;
import learning.chaincode.demo.dtos.Record;
import learning.chaincode.demo.dtos.SampleOrg;
import learning.chaincode.demo.services.FabricService;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.ChaincodeEndorsementPolicyParseException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Collection;

/**
 * Create with IntelliJ IDEA
 * Author               : wangzhenpeng
 * Date                 : 2018/2/9
 * Time                 : 14:05
 * Description          :
 */
@RestController
@RequestMapping("/fabric")
public class FabricController {

    @Autowired
    HFClient hfClient;

    @Autowired
    FabricService fabricService;

    @Autowired
    FabricConfig config;

    @Autowired
    Channel channel;

    @Autowired
    FabricConfigManager fabricConfigManager;

    @Autowired
    InstalledProposal installedProposal;


    @Autowired
    ChaincodeID chaincodeID;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public HFClient testWeb() {

        try {
            return fabricService.setupClient();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/constructChannel", method = RequestMethod.GET)
    public String constructChannel() {
        HFClient client = null;
        try {
            client = fabricService.setupClient();
        } catch (Exception e) {
            e.printStackTrace();
        }
        fabricService.constractChannel(config, client,"peerOrg1");
        return "{}";

    }
    @RequestMapping(value = "/runChannel", method = RequestMethod.GET)
    public String runChannel() {
        HFClient client = null;
        try {
            client = fabricService.setupClient();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Channel channel = fabricService.constractChannel(config, client,"peerOrg1");

//        Collection<SampleOrg> sampleOrgs = fabricService.constructOrgs();
        SampleOrg org = fabricService.getOrg("peerOrg1");

        fabricService.runChannel(client, channel, true, org, 0, config);
        return "{}";

    }


    @RequestMapping(value = "/transaction", method = RequestMethod.GET)
    public String transaction() {

        HFClient client = null;
        try {
            client = fabricService.setupClient();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Channel channel = fabricService.constractChannel(config, client,"peerOrg1");

//        Collection<SampleOrg> sampleOrgs = fabricService.constructOrgs();
        SampleOrg org = fabricService.getOrg("peerOrg1");

        InstallProposalResponse response = fabricService.runChannel(client, channel, true, org, 0, config);
        ChaincodeID chaincodeID = ChaincodeID.newBuilder().setName(config.getChainCodeName())
                .setVersion(config.getChainCodeVersion())
                .setPath(config.getChainCodePath()).build();

        fabricService.transaction(channel, response.getSuccessful(), response.getFailed(), channel.getOrderers(), client, org, config, chaincodeID);

        return "{}";
    }


    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public Record query() {
        Record record = new Record();
        ChaincodeID chaincodeID = ChaincodeID.newBuilder().setName(config.getChainCodeName())
                .setVersion(config.getChainCodeVersion())
                .setPath(config.getChainCodePath()).build();
//        channel.
        Collection<ProposalResponse> responses = fabricService.query(channel, installedProposal.getSuccessful(), channel.getOrderers(), hfClient, chaincodeID, 0);


        for (ProposalResponse proposalResponse : responses) {
            record.setUserName("b");

            record.setBalance(Integer.parseInt(proposalResponse.getProposalResponse().getResponse().getPayload().toStringUtf8()));
        }
        return record;
    }


    @RequestMapping(value = "/getHfClient", method = RequestMethod.GET)
    public CryptoSuite getHfClient() {
        return hfClient.getCryptoSuite();
    }

    @RequestMapping(value = "/getChannel", method = RequestMethod.GET)
    public String getChannel() {
        return "{channelName:" + channel.getName() + "}";
    }

    @RequestMapping(value = "/testQuery", method = RequestMethod.GET)
    public Record testQuery() throws InvalidArgumentException, ProposalException, ChaincodeEndorsementPolicyParseException, IOException {
        return fabricService.testQuery(hfClient, channel, chaincodeID);
    }
}
