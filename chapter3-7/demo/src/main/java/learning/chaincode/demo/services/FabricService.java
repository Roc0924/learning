package learning.chaincode.demo.services;

import learning.chaincode.demo.configs.FabricConfig;
import learning.chaincode.demo.configs.FabricConfigManager;
import learning.chaincode.demo.dtos.Record;
import learning.chaincode.demo.dtos.SampleOrg;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Create with IntelliJ IDEA
 * Author               : wangzhenpeng
 * Date                 : 2018/2/9
 * Time                 : 14:04
 * Description          : fabric 服务类
 */
@Slf4j
@Service("fabricService")
public class FabricService {
    private final HFClient hfClient;

    private final FabricConfig config;

    private final Channel channel;

    private final FabricConfigManager fabricConfigManager;


    private final ChaincodeID chaincodeID;

    @Autowired
    public FabricService(HFClient hfClient, FabricConfig config, Channel channel, FabricConfigManager fabricConfigManager, ChaincodeID chaincodeID) {
        this.hfClient = hfClient;
        this.config = config;
        this.channel = channel;
        this.fabricConfigManager = fabricConfigManager;
        this.chaincodeID = chaincodeID;
    }


    public SampleOrg getOrg(String orgName) {
        return fabricConfigManager.getIntegrationTestsSampleOrg(orgName);
    }



    public void transaction(Channel channel, HFClient client, SampleOrg sampleOrg,
                            ChaincodeID chaincodeID) {
        Collection<ProposalResponse> successful = new ArrayList<>();

        try {
            client.setUserContext(sampleOrg.getUser(config.getUser1Name()));

            // Send transaction proposal to all peers
            TransactionProposalRequest transactionProposalRequest = client.newTransactionProposalRequest();
            transactionProposalRequest.setChaincodeID(chaincodeID);
            transactionProposalRequest.setFcn("invoke");
            transactionProposalRequest.setProposalWaitTime(Integer.parseInt(config.getProposalWaitTime()));
            transactionProposalRequest.setArgs(new String[] {"move", "a", "b", "100"});

            Map<String, byte[]> tm2 = new HashMap<>();
            tm2.put("HyperLedgerFabric", "TransactionProposalRequest:JavaSDK".getBytes(UTF_8));
            tm2.put("method", "TransactionProposalRequest".getBytes(UTF_8));
            tm2.put("result", ":)".getBytes(UTF_8));  /// This should be returned see chaincode.
            transactionProposalRequest.setTransientMap(tm2);

            log.info("sending transactionProposal to all peers with arguments: move(a,b,100)");

            Collection<ProposalResponse> transactionPropResp = channel.sendTransactionProposal(transactionProposalRequest, channel.getPeers());

            for (ProposalResponse response : transactionPropResp) {
                if (response.getStatus() == ProposalResponse.Status.SUCCESS) {
                    log.info("Successful  transaction proposal response Txid: %s from peer %s", response.getTransactionID(), response.getPeer().getName());
                    successful.add(response);
                }
            }



            channel.sendTransaction(successful).get(fabricConfigManager.getTransactionWaitTime(), TimeUnit.SECONDS);
        } catch (ProposalException | InvalidArgumentException | ExecutionException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
        }

        System.out.println();

    }




    public Record queryByUserId(String userId) {
        QueryByChaincodeRequest queryByChaincodeRequest = hfClient.newQueryProposalRequest();
        queryByChaincodeRequest.setArgs(new String[] {"query", userId});
        queryByChaincodeRequest.setFcn("invoke");
        queryByChaincodeRequest.setChaincodeID(chaincodeID);

        Collection<ProposalResponse> queryProposals;

        try {
            queryProposals = channel.queryByChaincode(queryByChaincodeRequest);
        } catch (Exception e) {
            throw new CompletionException(e);
        }


        boolean isEqual = false;
        Integer balance = null;

        for (ProposalResponse response : queryProposals) {
            Integer tempBalance = Integer.parseInt(response.getProposalResponse().getResponse().getPayload().toStringUtf8());
            if (null !=  balance) {
                if ( tempBalance.intValue() == balance.intValue()) {
                    isEqual = true;
                }
            } else {
                balance = tempBalance;
            }
        }

        Record record = new Record();

        if (isEqual) {
            record.setUserId(userId);
            record.setBalance(balance);
        }
        return record;
    }




}
