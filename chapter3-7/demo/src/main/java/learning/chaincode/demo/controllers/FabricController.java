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
 * Description          : fabric 控制类
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

    @RequestMapping(value = "/transaction", method = RequestMethod.GET)
    public String transaction(@RequestParam(name = "source") String source,
                              @RequestParam(name = "destination") String destination,
                              @RequestParam(name = "delta") String delta) {
        SampleOrg org = fabricService.getOrg("peerOrg1");
        fabricService.transaction(channel, hfClient, org, chaincodeID, source, destination, delta);
        return "{}";
    }


    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public Record query(@RequestParam(name = "userId") String userId) {
        return fabricService.queryByUserId(userId);
    }

//    @RequestMapping(value = "/queryBlocks", method = RequestMethod.GET)
//    public Test queryBlocks() {
//        return fabricService.queryBlock();
//    }


//    @RequestMapping(value = "/install", method = RequestMethod.GET)
//    public InstalledProposal install() {
//        return fabricService.installChainCode();
//    }

}
