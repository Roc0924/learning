package learning.chaincode.demo.controllers;

import learning.chaincode.demo.dtos.Record;
import learning.chaincode.demo.dtos.SampleOrg;
import learning.chaincode.demo.services.FabricService;
import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.HFClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    private final HFClient hfClient;

    private final FabricService fabricService;

    private final Channel channel;

    private final ChaincodeID chaincodeID;

    @Autowired
    public FabricController(HFClient hfClient, FabricService fabricService, Channel channel, ChaincodeID chaincodeID) {
        this.hfClient = hfClient;
        this.fabricService = fabricService;
        this.channel = channel;
        this.chaincodeID = chaincodeID;
    }

//    @RequestMapping(value = "/test", method = RequestMethod.GET)
//    public HFClient testWeb() {
//
//        try {
//            return fabricService.setupClient();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

//    @RequestMapping(value = "/constructChannel", method = RequestMethod.GET)
//    public String constructChannel() {
//        HFClient client = null;
//        try {
//            client = fabricService.setupClient();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        fabricService.constractChannel(config, client,"peerOrg1");
//        return "{}";
//
//    }
//    @RequestMapping(value = "/runChannel", method = RequestMethod.GET)
//    public String runChannel() {
//        HFClient client = null;
//        try {
//            client = fabricService.setupClient();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        Channel channel = fabricService.constractChannel(config, client,"peerOrg1");
//
////        Collection<SampleOrg> sampleOrgs = fabricService.constructOrgs();
//        SampleOrg org = fabricService.getOrg("peerOrg1");
//
//        fabricService.runChannel(client, channel, true, org, 0, config);
//        return "{}";
//
//    }


//    @RequestMapping(value = "/transaction", method = RequestMethod.GET)
//    public String transaction() {
//        SampleOrg org = fabricService.getOrg("peerOrg1");
//        Collection<ProposalResponse> successful = installedProposal.getSuccessful();
//        Collection<ProposalResponse> failed = installedProposal.getFailed();
//
//
//
//
//        fabricService.transaction(channel, successful, failed, channel.getOrderers(), hfClient, org, config, chaincodeID);
//
//        return "{}";
//    }


    @RequestMapping(value = "/transaction", method = RequestMethod.GET)
    public String transaction() {
        SampleOrg org = fabricService.getOrg("peerOrg1");


        fabricService.transaction(channel, hfClient, org, chaincodeID);

        return "{}";
    }


    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public Record query(@RequestParam(name = "userId") String userId) {



        return fabricService.queryByUserId(userId);
    }


//    @RequestMapping(value = "/getHfClient", method = RequestMethod.GET)
//    public CryptoSuite getHfClient() {
//        return hfClient.getCryptoSuite();
//    }
//
//    @RequestMapping(value = "/getChannel", method = RequestMethod.GET)
//    public String getChannel() {
//        return "{channelName:" + channel.getName() + "}";
//    }
//
//    @RequestMapping(value = "/testQuery", method = RequestMethod.GET)
//    public Record testQuery() throws InvalidArgumentException, ProposalException, ChaincodeEndorsementPolicyParseException, IOException {
//        return fabricService.testQuery(hfClient, chaincodeID);
//    }
//
//
//    @RequestMapping(value = "/query1", method = RequestMethod.GET)
//    public Query.ChaincodeInfo query1() {
//        return fabricService.query1(channel, hfClient, chaincodeID);
//    }
}
