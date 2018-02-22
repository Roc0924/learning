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
 * Description          :
 */
@Slf4j
@Service("fabricService")
public class FabricService {
    private final HFClient hfClient;

    private final FabricConfig config;

    private final Channel channel;

    private final FabricConfigManager fabricConfigManager;


    private final ChaincodeID chaincodeID;


//    String testTxID;


//    private static final Map<String, String> TX_EXPECTED = null;

    @Autowired
    public FabricService(HFClient hfClient, FabricConfig config, Channel channel, FabricConfigManager fabricConfigManager, ChaincodeID chaincodeID) {
        this.hfClient = hfClient;
        this.config = config;
        this.channel = channel;
        this.fabricConfigManager = fabricConfigManager;
        this.chaincodeID = chaincodeID;
    }

//    public HFClient setupClient() throws Exception {
//        Collection<SampleOrg> sampleOrgs = fabricConfigManager.getIntegrationSampleOrgs();
//        for (SampleOrg sampleOrg : sampleOrgs) {
//            sampleOrg.setCAClient(HFCAClient.createNewInstance(sampleOrg.getCALocation(), sampleOrg.getCAProperties()));
//        }
//
//
//        HFClient client = HFClient.createNewInstance();
//
//        client.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
//
//
//        File sampleStoreFile = new File(System.getProperty("java.io.tmpdir") + "/HFCSampletest.properties");
//        if (sampleStoreFile.exists()) { //For testing start fresh
//            sampleStoreFile.delete();
//        }
//
//        final SampleStore sampleStore = new SampleStore(sampleStoreFile);
//
//
//        for (SampleOrg sampleOrg : sampleOrgs) {
//
//            HFCAClient ca = sampleOrg.getCAClient();
//            final String orgName = sampleOrg.getName();
//            final String mspid = sampleOrg.getMSPID();
//            ca.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
//            SampleUser admin = sampleStore.getMember(fabricConfigManager.getFabricConfig().getAdminName(), orgName);
//            if (!admin.isEnrolled()) {  //Preregistered admin only needs to be enrolled with Fabric caClient.
//                admin.setEnrollment(ca.enroll(admin.getName(), "adminpw"));
//                admin.setMspId(mspid);
//            }
//
//            sampleOrg.setAdmin(admin); // The admin of this org --
//
//            SampleUser user = sampleStore.getMember(fabricConfigManager.getFabricConfig().getUser1Name(), sampleOrg.getName());
//            if (!user.isRegistered()) {  // users need to be registered AND enrolled
//                RegistrationRequest rr = new RegistrationRequest(user.getName(), "org1.department1");
//                user.setEnrollmentSecret(ca.register(rr, admin));
//            }
//            if (!user.isEnrolled()) {
//                user.setEnrollment(ca.enroll(user.getName(), user.getEnrollmentSecret()));
//                user.setMspId(mspid);
//            }
//            sampleOrg.addUser(user); //Remember user belongs to this Org
//
//            final String sampleOrgName = sampleOrg.getName();
//            final String sampleOrgDomainName = sampleOrg.getDomainName();
//
//            // src/test/fixture/sdkintegration/e2e-2Orgs/channel/crypto-config/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp/keystore/
//
//            SampleUser peerOrgAdmin = sampleStore.getMember(sampleOrgName + "Admin", sampleOrgName, sampleOrg.getMSPID(),
//                    Util.findFileSk(Paths.get("demo\\target\\classes", fabricConfigManager.getFabricConfig().getChannelPath(),
//                            fabricConfigManager.getFabricConfig().getPeerOrganizations(),
//                            sampleOrgDomainName, format("/users/Admin@%s/msp/keystore", sampleOrgDomainName)).toFile()),
//                    Paths.get("demo\\target\\classes", fabricConfigManager.getFabricConfig().getChannelPath(),
//                            fabricConfigManager.getFabricConfig().getPeerOrganizations(), sampleOrgDomainName,
//                            format("/users/Admin@%s/msp/signcerts/Admin@%s-cert.pem", sampleOrgDomainName, sampleOrgDomainName)).toFile());
//
//            sampleOrg.setPeerAdmin(peerOrgAdmin); //A special user that can create channels, join peers and install chaincode
//
//        }
//        return client;
//    }


//    public Channel constractChannel(FabricConfig config, HFClient client, String orgName) {
////        SampleOrg sampleOrg = fabricConfigManager.getIntegrationTestsSampleOrg("peerOrg1");
//        SampleOrg sampleOrg = fabricConfigManager.getIntegrationTestsSampleOrg(orgName);
//        Channel channel = null;
//        try {
//            channel = constructChannel(config.getFooChannelName(), client, sampleOrg, config);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return channel;
//    }

//    public void constractAndRunChannels(FabricConfig config, HFClient client) throws ProposalException, IOException, InvalidArgumentException {
//
//        SampleOrg sampleOrg = fabricConfigManager.getIntegrationTestsSampleOrg("peerOrg1");
//        Channel fooChannel = null;
//        try {
//            fooChannel = constructChannel(config.getFooChannelName(), client, sampleOrg, config);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        runChannel(client, fooChannel, true, sampleOrg, 0, config);
//        fooChannel.shutdown(true); // Force foo channel to shutdown clean up resources.
//
//        sampleOrg = fabricConfigManager.getIntegrationTestsSampleOrg("peerOrg2");
//        Channel barChannel = null;
//        try {
//            barChannel = constructChannel(config.getBarChannelName(), client, sampleOrg, config);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        runChannel(client, barChannel, true, sampleOrg, 100, config); //run a newly constructed bar channel with different b value!
//        //let bar channel just shutdown so we have both scenarios.
//
//        blockWalker(barChannel);
//    }

//
//    /**
//     * 构建channel
//     * @param name
//     * @param client
//     * @param sampleOrg
//     * @param config
//     * @return
//     * @throws Exception
//     */
//    private Channel constructChannel(String name, HFClient client, SampleOrg sampleOrg, FabricConfig config) throws Exception {
//        ////////////////////////////
//        //Construct the channel
//        //
//
//
//        //Only peer Admin org
//        client.setUserContext(sampleOrg.getPeerAdmin());
//
//        Collection<Orderer> orderers = new LinkedList<>();
//
//        for (String orderName : sampleOrg.getOrdererNames()) {
//
//            Properties ordererProperties = fabricConfigManager.getOrdererProperties(orderName);
//
//            //example of setting keepAlive to avoid timeouts on inactive http2 connections.
//            // Under 5 minutes would require changes to server side to accept faster ping rates.
//            ordererProperties.put("grpc.NettyChannelBuilderOption.keepAliveTime", new Object[] {5L, TimeUnit.MINUTES});
//            ordererProperties.put("grpc.NettyChannelBuilderOption.keepAliveTimeout", new Object[] {8L, TimeUnit.SECONDS});
//
//            orderers.add(client.newOrderer(orderName, sampleOrg.getOrdererLocation(orderName),
//                    ordererProperties));
//        }
//
//        //Just pick the first orderer in the list to create the channel.
//
//        Orderer anOrderer = orderers.iterator().next();
//        orderers.remove(anOrderer);
//
//        ChannelConfiguration channelConfiguration = new ChannelConfiguration(new File(config.getFixturesPath() + "/e2e-2Orgs/channel/" + name + ".tx"));
//
//        //Create channel that has only one signer that is this orgs peer admin. If channel creation policy needed more signature they would need to be added too.
//        Channel newChannel = client.newChannel(name, anOrderer, channelConfiguration, client.getChannelConfigurationSignature(channelConfiguration, sampleOrg.getPeerAdmin()));
//
//
//        for (String peerName : sampleOrg.getPeerNames()) {
//            String peerLocation = sampleOrg.getPeerLocation(peerName);
//
//            Properties peerProperties = fabricConfigManager.getPeerProperties(peerName); //test properties for peer.. if any.
//            if (peerProperties == null) {
//                peerProperties = new Properties();
//            }
//            //Example of setting specific options on grpc's NettyChannelBuilder
//            peerProperties.put("grpc.NettyChannelBuilderOption.maxInboundMessageSize", 9000000);
//
//            Peer peer = client.newPeer(peerName, peerLocation, peerProperties);
//            newChannel.joinPeer(peer);
//            sampleOrg.addPeer(peer);
//        }
//
//        for (Orderer orderer : orderers) { //add remaining orderers if any.
//            newChannel.addOrderer(orderer);
//        }
//
//        for (String eventHubName : sampleOrg.getEventHubNames()) {
//
//            final Properties eventHubProperties = fabricConfigManager.getEventHubProperties(eventHubName);
//
//            eventHubProperties.put("grpc.NettyChannelBuilderOption.keepAliveTime", new Object[] {5L, TimeUnit.MINUTES});
//            eventHubProperties.put("grpc.NettyChannconfigelBuilderOption.keepAliveTimeout", new Object[] {8L, TimeUnit.SECONDS});
//
//            EventHub eventHub = client.newEventHub(eventHubName, sampleOrg.getEventHubLocation(eventHubName),
//                    eventHubProperties);
//            newChannel.addEventHub(eventHub);
//        }
//
//        newChannel.initialize();
//
//
//        return newChannel;
//
//    }

//
//    public InstallProposalResponse runChannel(HFClient client, Channel channel, boolean installChaincode, SampleOrg sampleOrg, int delta, FabricConfig config) {
//        InstallProposalResponse installProposalResponse = null;
//        try {
//
//            final String channelName = channel.getName();
//            boolean isFooChain = config.getFooChannelName().equals(channelName);
//            channel.setTransactionWaitTime(fabricConfigManager.getTransactionWaitTime());
//            channel.setDeployWaitTime(Integer.parseInt(config.getDeployWaitTime()));
//
//            Collection<Peer> channelPeers = channel.getPeers();
//            Collection<Orderer> orderers = channel.getOrderers();
//            final ChaincodeID chaincodeID;
//            Collection<ProposalResponse> responses;
//            Collection<ProposalResponse> successful = new LinkedList<>();
//            Collection<ProposalResponse> failed = new LinkedList<>();
//
//            chaincodeID = ChaincodeID.newBuilder().setName(config.getChainCodeName())
//                    .setVersion(config.getChainCodeVersion())
//                    .setPath(config.getChainCodePath()).build();
//
//            if (installChaincode) {
//                ////////////////////////////
//                // Install Proposal Request
//                //
//
//                client.setUserContext(sampleOrg.getPeerAdmin());
//
//
//                InstallProposalRequest installProposalRequest = client.newInstallProposalRequest();
//                installProposalRequest.setChaincodeID(chaincodeID);
//
//                if (isFooChain) {
//                    // on foo chain install from directory.
//
//                    ////For GO language and serving just a single user, chaincodeSource is mostly likely the users GOPATH
////                    installProposalRequest.setChaincodeSourceLocation(new File(config.getFixturesPath() + "/sdkintegration/gocc/sample1"));
//                    installProposalRequest.setChaincodeSourceLocation(new File(config.getFixturesPath()));
//                } else {
//                    // On bar chain install from an input stream.
//
//                    installProposalRequest.setChaincodeInputStream(Util.generateTarGzInputStream(
////                            (Paths.get(config.getFixturesPath(), "/sdkintegration/gocc/sample1", "src", config.getChainCodePath()).toFile()),
//                            (Paths.get(config.getFixturesPath(), config.getChainCodePath()).toFile()),
//                            Paths.get(config.getChainCodePath()).toString()));
//
//                }
//
//                installProposalRequest.setChaincodeVersion(config.getChainCodeVersion());
//
//
//                ////////////////////////////
//                // only a client from the same org as the peer can issue an install request
//                int numInstallProposal = 0;
//                //    Set<String> orgs = orgPeers.keySet();
//                //   for (SampleOrg org : testSampleOrgs) {
//
//                Set<Peer> peersFromOrg = sampleOrg.getPeers();
//                numInstallProposal = numInstallProposal + peersFromOrg.size();
//                responses = client.sendInstallProposal(installProposalRequest, peersFromOrg);
//
//                for (ProposalResponse response : responses) {
//                    if (response.getStatus() == ProposalResponse.Status.SUCCESS) {
//                        out("Successful install proposal response Txid: %s from peer %s", response.getTransactionID(), response.getPeer().getName());
//                        successful.add(response);
//                    } else {
//                        failed.add(response);
//                    }
//                }
//
//                installProposalResponse = new InstallProposalResponse();
//                installProposalResponse.setFailed(failed);
//                installProposalResponse.setSuccessful(successful);
//
//                SDKUtils.getProposalConsistencySets(responses);
//                //   }
//                out("Received %d install proposal responses. Successful+verified: %d . Failed: %d", numInstallProposal, successful.size(), failed.size());
//
//                if (failed.size() > 0) {
//                    ProposalResponse first = failed.iterator().next();
//                    fail("Not enough endorsers for install :" + successful.size() + ".  " + first.getMessage());
//                }
//            }
//
//            //   client.setUserContext(sampleOrg.getUser(TEST_ADMIN_NAME));
//            //  final ChaincodeID chaincodeID = firstInstallProposalResponse.getChaincodeID();
//            // Note installing chaincode does not require transaction no need to
//            // send to Orderers
//
//            ///////////////
//            //// Instantiate chaincode.
//            InstantiateProposalRequest instantiateProposalRequest = client.newInstantiationProposalRequest();
//            instantiateProposalRequest.setProposalWaitTime(Integer.parseInt(config.getProposalWaitTime()));
//            instantiateProposalRequest.setChaincodeID(chaincodeID);
//            instantiateProposalRequest.setFcn("init");
//            instantiateProposalRequest.setArgs(new String[] {"a", "500", "b", "" + (200 + delta)});
//            Map<String, byte[]> tm = new HashMap<>();
//            tm.put("HyperLedgerFabric", "InstantiateProposalRequest:JavaSDK".getBytes(UTF_8));
//            tm.put("method", "InstantiateProposalRequest".getBytes(UTF_8));
//            instantiateProposalRequest.setTransientMap(tm);
//
//            /*
//              policy OR(Org1MSP.member, Org2MSP.member) meaning 1 signature from someone in either Org1 or Org2
//              See README.md Chaincode endorsement policies section for more details.
//            */
//            ChaincodeEndorsementPolicy chaincodeEndorsementPolicy = new ChaincodeEndorsementPolicy();
//            chaincodeEndorsementPolicy.fromYamlFile(new File(config.getFixturesPath() + "/chaincodeendorsementpolicy.yaml"));
//            instantiateProposalRequest.setChaincodeEndorsementPolicy(chaincodeEndorsementPolicy);
//
//            out("Sending instantiateProposalRequest to all peers with arguments: a and b set to 100 and %s respectively", "" + (200 + delta));
//            successful.clear();
//            failed.clear();
//
//            if (isFooChain) {  //Send responses both ways with specifying peers and by using those on the channel.
//                responses = channel.sendInstantiationProposal(instantiateProposalRequest, channel.getPeers());
//            } else {
//                responses = channel.sendInstantiationProposal(instantiateProposalRequest);
//
//            }
//            for (ProposalResponse response : responses) {
//                if (response.isVerified() && response.getStatus() == ProposalResponse.Status.SUCCESS) {
//                    successful.add(response);
//                    out("Succesful instantiate proposal response Txid: %s from peer %s", response.getTransactionID(), response.getPeer().getName());
//                } else {
//                    failed.add(response);
//                }
//            }
//            out("Received %d instantiate proposal responses. Successful+verified: %d . Failed: %d", responses.size(), successful.size(), failed.size());
//            if (failed.size() > 0) {
//                ProposalResponse first = failed.iterator().next();
//                fail("Not enough endorsers for instantiate :" + successful.size() + "endorser failed with " + first.getMessage() + ". Was verified:" + first.isVerified());
//            }
//
//            ///////////////
//            /// Send instantiate transaction to orderer
//            out("Sending instantiateTransaction to orderer with a and b set to 100 and %s respectively", "" + (200 + delta));



//            transaction(channel, successful, failed, orderers, client, sampleOrg, config, chaincodeID);
//
//            Thread.sleep(10000);
//
//            query(channel, successful, orderers, client, chaincodeID, delta);



//            channel.sendTransaction(successful, orderers).thenApply(transactionEvent -> {
//
//
////                assertTrue(transactionEvent.isValid()); // must be valid to be here.
//                out("Finished instantiate transaction with transaction id %s", transactionEvent.getTransactionID());
//
//                try {
//                    successful.clear();
//                    failed.clear();
//
//                    client.setUserContext(sampleOrg.getUser(config.getUser1Name()));
//
//                    ///////////////
//                    /// Send transaction proposal to all peers
//                    TransactionProposalRequest transactionProposalRequest = client.newTransactionProposalRequest();
//                    transactionProposalRequest.setChaincodeID(chaincodeID);
//                    transactionProposalRequest.setFcn("invoke");
//                    transactionProposalRequest.setProposalWaitTime(Integer.parseInt(config.getProposalWaitTime()));
//                    transactionProposalRequest.setArgs(new String[] {"move", "a", "b", "100"});
//
//                    Map<String, byte[]> tm2 = new HashMap<>();
//                    tm2.put("HyperLedgerFabric", "TransactionProposalRequest:JavaSDK".getBytes(UTF_8));
//                    tm2.put("method", "TransactionProposalRequest".getBytes(UTF_8));
//                    tm2.put("result", ":)".getBytes(UTF_8));  /// This should be returned see chaincode.
//                    transactionProposalRequest.setTransientMap(tm2);
//
//                    out("sending transactionProposal to all peers with arguments: move(a,b,100)");
//
//                    Collection<ProposalResponse> transactionPropResp = channel.sendTransactionProposal(transactionProposalRequest, channel.getPeers());
//                    for (ProposalResponse response : transactionPropResp) {
//                        if (response.getStatus() == ProposalResponse.Status.SUCCESS) {
//                            out("Successful transaction proposal response Txid: %s from peer %s", response.getTransactionID(), response.getPeer().getName());
//                            successful.add(response);
//                        } else {
//                            failed.add(response);
//                        }
//                    }
//
//                    // Check that all the proposals are consistent with each other. We should have only one set
//                    // where all the proposals above are consistent.
//                    Collection<Set<ProposalResponse>> proposalConsistencySets = SDKUtils.getProposalConsistencySets(transactionPropResp);
//                    if (proposalConsistencySets.size() != 1) {
//                        fail(format("Expected only one set of consistent proposal responses but got %d", proposalConsistencySets.size()));
//                    }
//
//                    out("Received %d transaction proposal responses. Successful+verified: %d . Failed: %d",
//                            transactionPropResp.size(), successful.size(), failed.size());
//                    if (failed.size() > 0) {
//                        ProposalResponse firstTransactionProposalResponse = failed.iterator().next();
//                        fail("Not enough endorsers for invoke(move a,b,100):" + failed.size() + " endorser error: " +
//                                firstTransactionProposalResponse.getMessage() +
//                                ". Was verified: " + firstTransactionProposalResponse.isVerified());
//                    }
//                    out("Successfully received transaction proposal responses.");
//
//                    ProposalResponse resp = transactionPropResp.iterator().next();
//                    byte[] x = resp.getChaincodeActionResponsePayload(); // This is the data returned by the chaincode.
//                    String resultAsString = null;
//                    if (x != null) {
//                        resultAsString = new String(x, "UTF-8");
//                    }
////                    assertEquals(":)", resultAsString);
//
////                    assertEquals(200, resp.getChaincodeActionResponseStatus()); //Chaincode's status.
//
//                    TxReadWriteSetInfo readWriteSetInfo = resp.getChaincodeActionResponseReadWriteSetInfo();
//                    //See blockwalker below how to transverse this
////                    assertNotNull(readWriteSetInfo);
////                    assertTrue(readWriteSetInfo.getNsRwsetCount() > 0);
//
//                    ChaincodeID cid = resp.getChaincodeID();
////                    assertNotNull(cid);
////                    assertEquals(CHAIN_CODE_PATH, cid.getPath());
////                    assertEquals(CHAIN_CODE_NAME, cid.getName());
////                    assertEquals(CHAIN_CODE_VERSION, cid.getVersion());
//
//                    ////////////////////////////
//                    // Send Transaction Transaction to orderer
//                    out("Sending chaincode transaction(move a,b,100) to orderer.");
//                    return channel.sendTransaction(successful).get(fabricConfigManager.getTransactionWaitTime(), TimeUnit.SECONDS);
//
//                } catch (Exception e) {
//                    out("Caught an exception while invoking chaincode");
//                    e.printStackTrace();
//                    fail("Failed invoking chaincode with error : " + e.getMessage());
//                }
//
//                return null;
//
//            }).thenApply(transactionEvent -> {
//                try {
//
////                    waitOnFabric(0);
//
////                    assertTrue(transactionEvent.isValid()); // must be valid to be here.
//                    out("Finished transaction with transaction id %s", transactionEvent.getTransactionID());
//                    testTxID = transactionEvent.getTransactionID(); // used in the channel queries later
//
//                    ////////////////////////////
//                    // Send Query Proposal to all peers
//                    //
//                    String expect = "" + (300 + delta);
//                    out("Now query chaincode for the value of b.");
//                    QueryByChaincodeRequest queryByChaincodeRequest = client.newQueryProposalRequest();
//                    queryByChaincodeRequest.setArgs(new String[] {"query", "b"});
//                    queryByChaincodeRequest.setFcn("invoke");
//                    queryByChaincodeRequest.setChaincodeID(chaincodeID);
//
//                    Map<String, byte[]> tm2 = new HashMap<>();
//                    tm2.put("HyperLedgerFabric", "QueryByChaincodeRequest:JavaSDK".getBytes(UTF_8));
//                    tm2.put("method", "QueryByChaincodeRequest".getBytes(UTF_8));
//                    queryByChaincodeRequest.setTransientMap(tm2);
//
//                    Collection<ProposalResponse> queryProposals = channel.queryByChaincode(queryByChaincodeRequest, channel.getPeers());
//                    for (ProposalResponse proposalResponse : queryProposals) {
//                        if (!proposalResponse.isVerified() || proposalResponse.getStatus() != ProposalResponse.Status.SUCCESS) {
//                            fail("Failed query proposal from peer " + proposalResponse.getPeer().getName() + " status: " + proposalResponse.getStatus() +
//                                    ". Messages: " + proposalResponse.getMessage()
//                                    + ". Was verified : " + proposalResponse.isVerified());
//                        } else {
//                            String payload = proposalResponse.getProposalResponse().getResponse().getPayload().toStringUtf8();
//                            out("Query payload of b from peer %s returned %s", proposalResponse.getPeer().getName(), payload);
////                            assertEquals(payload, expect);
//                        }
//                    }
//
//                    return null;
//                } catch (Exception e) {
//                    out("Caught exception while running query");
//                    e.printStackTrace();
//                    fail("Failed during chaincode query with error : " + e.getMessage());
//                }
//
//                return null;
//            }).exceptionally(e -> {
//                if (e instanceof TransactionEventException) {
//                    BlockEvent.TransactionEvent te = ((TransactionEventException) e).getTransactionEvent();
//                    if (te != null) {
//                        fail(format("Transaction with txid %s failed. %s", te.getTransactionID(), e.getMessage()));
//                    }
//                }
//                fail(format("Test failed with %s exception %s", e.getClass().getName(), e.getMessage()));
//
//                return null;
//            }).get(fabricConfigManager.getTransactionWaitTime(), TimeUnit.SECONDS);
//
//            // Channel queries
//
//            // We can only send channel queries to peers that are in the same org as the SDK user context
//            // Get the peers from the current org being used and pick one randomly to send the queries to.
//            Set<Peer> peerSet = sampleOrg.getPeers();
//            //  Peer queryPeer = peerSet.iterator().next();
//            //   out("Using peer %s for channel queries", queryPeer.getName());
//
//            BlockchainInfo channelInfo = channel.queryBlockchainInfo();
//            out("Channel info for : " + channelName);
//            out("Channel height: " + channelInfo.getHeight());
//            String chainCurrentHash = Hex.encodeHexString(channelInfo.getCurrentBlockHash());
//            String chainPreviousHash = Hex.encodeHexString(channelInfo.getPreviousBlockHash());
//            out("Chain current block hash: " + chainCurrentHash);
//            out("Chainl previous block hash: " + chainPreviousHash);
//
//            // Query by block number. Should return latest block, i.e. block number 2
//            BlockInfo returnedBlock = channel.queryBlockByNumber(channelInfo.getHeight() - 1);
//            String previousHash = Hex.encodeHexString(returnedBlock.getPreviousHash());
//            out("queryBlockByNumber returned correct block with blockNumber " + returnedBlock.getBlockNumber()
//                    + " \n previous_hash " + previousHash);
////            assertEquals(channelInfo.getHeight() - 1, returnedBlock.getBlockNumber());
////            assertEquals(chainPreviousHash, previousHash);
//
//            // Query by block hash. Using latest block's previous hash so should return block number 1
//            byte[] hashQuery = returnedBlock.getPreviousHash();
//            returnedBlock = channel.queryBlockByHash(hashQuery);
//            out("queryBlockByHash returned block with blockNumber " + returnedBlock.getBlockNumber());
////            assertEquals(channelInfo.getHeight() - 2, returnedBlock.getBlockNumber());
//
//            // Query block by TxID. Since it's the last TxID, should be block 2
//            returnedBlock = channel.queryBlockByTransactionID(testTxID);
//            out("queryBlockByTxID returned block with blockNumber " + returnedBlock.getBlockNumber());
////            assertEquals(channelInfo.getHeight() - 1, returnedBlock.getBlockNumber());
//
//            // query transaction by ID
//            TransactionInfo txInfo = channel.queryTransactionByID(testTxID);
//            out("QueryTransactionByID returned TransactionInfo: txID " + txInfo.getTransactionID()
//                    + "\n     validation code " + txInfo.getValidationCode().getNumber());
//
//            out("Running for Channel %s done", channelName);
//
//        } catch (Exception e) {
//            out("Caught an exception running channel %s", channel.getName());
//            e.printStackTrace();
//            fail("Test failed with error : " + e.getMessage());
//        }
//        return installProposalResponse;
//    }



//    static void out(String format, Object... args) {
//
//        System.err.flush();
//        System.out.flush();
//
//        System.out.println(format(format, args));
//        System.err.flush();
//        System.out.flush();
//
//    }
//
//    public static void fail(String message) {
//        if (message == null) {
//            throw new AssertionError();
//        } else {
//            throw new AssertionError(message);
//        }
//    }

//    static String printableString(final String string) {
//        int maxLogStringLength = 64;
//        if (string == null || string.length() == 0) {
//            return string;
//        }
//
//        String ret = string.replaceAll("[^\\p{Print}]", "?");
//
//        ret = ret.substring(0, Math.min(ret.length(), maxLogStringLength)) + (ret.length() > maxLogStringLength ? "..." : "");
//
//        return ret;
//
//    }


    public SampleOrg getOrg(String orgName) {
        return fabricConfigManager.getIntegrationTestsSampleOrg(orgName);
    }

//    public void transaction(Channel channel, Collection<ProposalResponse> successful, Collection<ProposalResponse> failed,
//                            Collection<Orderer> orderers, HFClient client, SampleOrg sampleOrg, FabricConfig config,
//                            ChaincodeID chaincodeID) {
//
//        Object obj = null;
//        try {
//            obj = channel.sendTransaction(successful, orderers).thenApply(transactionEvent -> {
//
//
////                assertTrue(transactionEvent.isValid()); // must be valid to be here.
//                out("Finished instantiate transaction with transaction id %s", transactionEvent.getTransactionID());
//
//                try {
//                    successful.clear();
//                    failed.clear();
//
//
//                    client.setUserContext(sampleOrg.getUser(config.getUser1Name()));
//
//                    ///////////////
//                    /// Send transaction proposal to all peers
//                    TransactionProposalRequest transactionProposalRequest = client.newTransactionProposalRequest();
//                    transactionProposalRequest.setChaincodeID(chaincodeID);
//                    transactionProposalRequest.setFcn("invoke");
//                    transactionProposalRequest.setProposalWaitTime(Integer.parseInt(config.getProposalWaitTime()));
//                    transactionProposalRequest.setArgs(new String[] {"move", "a", "b", "100"});
//
//                    Map<String, byte[]> tm2 = new HashMap<>();
//                    tm2.put("HyperLedgerFabric", "TransactionProposalRequest:JavaSDK".getBytes(UTF_8));
//                    tm2.put("method", "TransactionProposalRequest".getBytes(UTF_8));
//                    tm2.put("result", ":)".getBytes(UTF_8));  /// This should be returned see chaincode.
//                    transactionProposalRequest.setTransientMap(tm2);
//
//                    out("sending transactionProposal to all peers with arguments: move(a,b,100)");
//
//                    Collection<ProposalResponse> transactionPropResp = channel.sendTransactionProposal(transactionProposalRequest, channel.getPeers());
//                    for (ProposalResponse response : transactionPropResp) {
//                        if (response.getStatus() == ProposalResponse.Status.SUCCESS) {
//                            out("Successful transaction proposal response Txid: %s from peer %s", response.getTransactionID(), response.getPeer().getName());
//                            successful.add(response);
//                        } else {
//                            failed.add(response);
//                        }
//                    }
//
//                    // Check that all the proposals are consistent with each other. We should have only one set
//                    // where all the proposals above are consistent.
//                    Collection<Set<ProposalResponse>> proposalConsistencySets = SDKUtils.getProposalConsistencySets(transactionPropResp);
//                    if (proposalConsistencySets.size() != 1) {
//                        fail(format("Expected only one set of consistent proposal responses but got %d", proposalConsistencySets.size()));
//                    }
//
//                    out("Received %d transaction proposal responses. Successful+verified: %d . Failed: %d",
//                            transactionPropResp.size(), successful.size(), failed.size());
//                    if (failed.size() > 0) {
//                        ProposalResponse firstTransactionProposalResponse = failed.iterator().next();
//                        fail("Not enough endorsers for invoke(move a,b,100):" + failed.size() + " endorser error: " +
//                                firstTransactionProposalResponse.getMessage() +
//                                ". Was verified: " + firstTransactionProposalResponse.isVerified());
//                    }
//                    out("Successfully received transaction proposal responses.");
//
//
//                    ////////////////////////////
//                    // Send Transaction Transaction to orderer
//                    out("Sending chaincode transaction(move a,b,100) to orderer.");
//                    return channel.sendTransaction(successful).get(fabricConfigManager.getTransactionWaitTime(), TimeUnit.SECONDS);
//
//                } catch (Exception e) {
//                    out("Caught an exception while invoking chaincode");
//                    e.printStackTrace();
//                    fail("Failed invoking chaincode with error : " + e.getMessage());
//                }
//
//                return null;
//
//            }).exceptionally(e -> {
//                if (e instanceof TransactionEventException) {
//                    BlockEvent.TransactionEvent te = ((TransactionEventException) e).getTransactionEvent();
//                    if (te != null) {
//                        fail(format("Transaction with txid %s failed. %s", te.getTransactionID(), e.getMessage()));
//                    }
//                }
//                fail(format("Test failed with %s exception %s", e.getClass().getName(), e.getMessage()));
//
//                return null;
//            }).get(fabricConfigManager.getTransactionWaitTime(), TimeUnit.SECONDS);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (TimeoutException e) {
//            e.printStackTrace();
//        }
//
//
//        System.out.println(obj);
//
//    }







    public void transaction(Channel channel, HFClient client, SampleOrg sampleOrg,
                            ChaincodeID chaincodeID) {
        Collection<ProposalResponse> successful = new ArrayList<>();

        try {
            client.setUserContext(sampleOrg.getUser(config.getUser1Name()));

            ///////////////
            /// Send transaction proposal to all peers
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












//    public Collection<ProposalResponse> query(Channel channel, Collection<ProposalResponse> successful,
//                      Collection<Orderer> orderers, HFClient client,
//                      ChaincodeID chaincodeID, int delta) {
//        Collection<ProposalResponse> proposals = null;
//        try {
//            proposals = channel.sendTransaction(successful, orderers).thenApply(transactionEvent -> {
//                try {
//                    out("Finished transaction with transaction id %s", transactionEvent.getTransactionID());
//                    testTxID = transactionEvent.getTransactionID(); // used in the channel queries later
//
//                    log.info("testTxID:{}", testTxID);
//                    ////////////////////////////
//                    // Send Query Proposal to all peers
//                    //
//                    String expect = "" + (300 + delta);
//                    out("Now query chaincode for the value of b.");
//                    QueryByChaincodeRequest queryByChaincodeRequest = client.newQueryProposalRequest();
//                    queryByChaincodeRequest.setArgs(new String[] {"query", "b"});
//                    queryByChaincodeRequest.setFcn("invoke");
//                    queryByChaincodeRequest.setChaincodeID(chaincodeID);
//
//                    Map<String, byte[]> tm2 = new HashMap<>();
//                    tm2.put("HyperLedgerFabric", "QueryByChaincodeRequest:JavaSDK".getBytes(UTF_8));
//                    tm2.put("method", "QueryByChaincodeRequest".getBytes(UTF_8));
//                    queryByChaincodeRequest.setTransientMap(tm2);
//
//
//                    Collection<ProposalResponse> queryProposals = channel.queryByChaincode(queryByChaincodeRequest, channel.getPeers());
//                    for (ProposalResponse proposalResponse : queryProposals) {
//                        if (!proposalResponse.isVerified() || proposalResponse.getStatus() != ProposalResponse.Status.SUCCESS) {
//                            fail("Failed query proposal from peer " + proposalResponse.getPeer().getName() + " status: " + proposalResponse.getStatus() +
//                                    ". Messages: " + proposalResponse.getMessage()
//                                    + ". Was verified : " + proposalResponse.isVerified());
//                        } else {
//                            String payload = proposalResponse.getProposalResponse().getResponse().getPayload().toStringUtf8();
//                            out("Query payload of b from peer %s returned %s", proposalResponse.getPeer().getName(), payload);
//                        }
//                    }
//                    return queryProposals;
//                } catch (Exception e) {
//                    out("Caught exception while running query");
//                    e.printStackTrace();
//                    fail("Failed during chaincode query with error : " + e.getMessage());
//                }
//
//                return null;
//            }).exceptionally(e -> {
//                if (e instanceof TransactionEventException) {
//                    BlockEvent.TransactionEvent te = ((TransactionEventException) e).getTransactionEvent();
//                    if (te != null) {
//                        fail(format("Transaction with txid %s failed. %s", te.getTransactionID(), e.getMessage()));
//                    }
//                }
//                fail(format("Test failed with %s exception %s", e.getClass().getName(), e.getMessage()));
//
//                return null;
//            }).get(fabricConfigManager.getTransactionWaitTime(), TimeUnit.SECONDS);
//            return proposals;
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (TimeoutException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }











//    public Query.ChaincodeInfo query1(Channel channel, HFClient hfClient, ChaincodeID chaincodeID) {
//        Collection<Peer> peers = channel.getPeers();
//        List<Query.ChaincodeInfo> chaincodeInfos = null;
//
//        if(null != peers && !peers.isEmpty()) {
//
//            try {
////                channel.
//                chaincodeInfos = channel.queryInstantiatedChaincodes((Peer)peers.toArray()[0]);
//            } catch (InvalidArgumentException | ProposalException e) {
//                e.printStackTrace();
//            }
//        }
//
////        out("Now query chaincode on channel %s for the value of b expecting to see: %s", channel.getName(), expect);
//        QueryByChaincodeRequest queryByChaincodeRequest = hfClient.newQueryProposalRequest();
//        queryByChaincodeRequest.setArgs(new String[] {"query", "b"});
//        queryByChaincodeRequest.setFcn("invoke");
//        queryByChaincodeRequest.setChaincodeID(chaincodeID);
//
//        Collection<ProposalResponse> queryProposals;
//
//        try {
//            queryProposals = channel.queryByChaincode(queryByChaincodeRequest);
//        } catch (Exception e) {
//            throw new CompletionException(e);
//        }
//
//        return chaincodeInfos.get(0);
//    }
//
//
//
//
//    public Query.ChaincodeInfo query2(Channel channel) {
//        Collection<ProposalResponse> proposals = null;
//
////        channel.send
//
//        Collection<Peer> peers = channel.getPeers();
//        List<Query.ChaincodeInfo> chaincodeInfos = null;
//
//        if(null != peers && !peers.isEmpty()) {
//
//            try {
//                chaincodeInfos = channel.queryInstantiatedChaincodes((Peer)peers.toArray()[0]);
//            } catch (InvalidArgumentException | ProposalException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return chaincodeInfos.get(0);
//
//
//    }










//    void blockWalker(Channel channel) throws InvalidArgumentException, ProposalException, IOException {
//        try {
//            BlockchainInfo channelInfo = channel.queryBlockchainInfo();
//
//            for (long current = channelInfo.getHeight() - 1; current > -1; --current) {
//                BlockInfo returnedBlock = channel.queryBlockByNumber(current);
//                final long blockNumber = returnedBlock.getBlockNumber();
//
//                out("current block number %d has data hash: %s", blockNumber, Hex.encodeHexString(returnedBlock.getDataHash()));
//                out("current block number %d has previous hash id: %s", blockNumber, Hex.encodeHexString(returnedBlock.getPreviousHash()));
//                out("current block number %d has calculated block hash is %s", blockNumber, Hex.encodeHexString(SDKUtils.calculateBlockHash(blockNumber, returnedBlock.getPreviousHash(), returnedBlock.getDataHash())));
//
//                final int envelopeCount = returnedBlock.getEnvelopeCount();
////                assertEquals(1, envelopeCount);
//                out("current block number %d has %d envelope count:", blockNumber, returnedBlock.getEnvelopeCount());
//                int i = 0;
//                for (BlockInfo.EnvelopeInfo envelopeInfo : returnedBlock.getEnvelopeInfos()) {
//                    ++i;
//
//                    out("  Transaction number %d has transaction id: %s", i, envelopeInfo.getTransactionID());
//                    final String channelId = envelopeInfo.getChannelId();
////                    assertTrue("foo".equals(channelId) || "bar".equals(channelId));
//
//                    out("  Transaction number %d has channel id: %s", i, channelId);
//                    out("  Transaction number %d has epoch: %d", i, envelopeInfo.getEpoch());
//                    out("  Transaction number %d has transaction timestamp: %tB %<te,  %<tY  %<tT %<Tp", i, envelopeInfo.getTimestamp());
//                    out("  Transaction number %d has type id: %s", i, "" + envelopeInfo.getType());
//
//                    if (envelopeInfo.getType() == TRANSACTION_ENVELOPE) {
//                        BlockInfo.TransactionEnvelopeInfo transactionEnvelopeInfo = (BlockInfo.TransactionEnvelopeInfo) envelopeInfo;
//
//                        out("  Transaction number %d has %d actions", i, transactionEnvelopeInfo.getTransactionActionInfoCount());
////                        assertEquals(1, transactionEnvelopeInfo.getTransactionActionInfoCount()); // for now there is only 1 action per transaction.
//                        out("  Transaction number %d isValid %b", i, transactionEnvelopeInfo.isValid());
////                        assertEquals(transactionEnvelopeInfo.isValid(), true);
//                        out("  Transaction number %d validation code %d", i, transactionEnvelopeInfo.getValidationCode());
////                        assertEquals(0, transactionEnvelopeInfo.getValidationCode());
//
//                        int j = 0;
//                        for (BlockInfo.TransactionEnvelopeInfo.TransactionActionInfo transactionActionInfo : transactionEnvelopeInfo.getTransactionActionInfos()) {
//                            ++j;
//                            out("   Transaction action %d has response status %d", j, transactionActionInfo.getResponseStatus());
////                            assertEquals(200, transactionActionInfo.getResponseStatus());
//                            out("   Transaction action %d has response message bytes as string: %s", j,
//                                    printableString(new String(transactionActionInfo.getResponseMessageBytes(), "UTF-8")));
//                            out("   Transaction action %d has %d endorsements", j, transactionActionInfo.getEndorsementsCount());
////                            assertEquals(2, transactionActionInfo.getEndorsementsCount());
//
//                            for (int n = 0; n < transactionActionInfo.getEndorsementsCount(); ++n) {
//                                BlockInfo.EndorserInfo endorserInfo = transactionActionInfo.getEndorsementInfo(n);
//                                out("Endorser %d signature: %s", n, Hex.encodeHexString(endorserInfo.getSignature()));
//                                out("Endorser %d endorser: %s", n, new String(endorserInfo.getEndorser(), "UTF-8"));
//                            }
//                            out("   Transaction action %d has %d chaincode input arguments", j, transactionActionInfo.getChaincodeInputArgsCount());
//                            for (int z = 0; z < transactionActionInfo.getChaincodeInputArgsCount(); ++z) {
//                                out("     Transaction action %d has chaincode input argument %d is: %s", j, z,
//                                        printableString(new String(transactionActionInfo.getChaincodeInputArgs(z), "UTF-8")));
//                            }
//
//                            out("   Transaction action %d proposal response status: %d", j,
//                                    transactionActionInfo.getProposalResponseStatus());
//                            out("   Transaction action %d proposal response payload: %s", j,
//                                    printableString(new String(transactionActionInfo.getProposalResponsePayload())));
//
//                            TxReadWriteSetInfo rwsetInfo = transactionActionInfo.getTxReadWriteSet();
//                            if (null != rwsetInfo) {
//                                out("   Transaction action %d has %d name space read write sets", j, rwsetInfo.getNsRwsetCount());
//
//                                for (TxReadWriteSetInfo.NsRwsetInfo nsRwsetInfo : rwsetInfo.getNsRwsetInfos()) {
//                                    final String namespace = nsRwsetInfo.getNamespace();
//                                    KvRwset.KVRWSet rws = nsRwsetInfo.getRwset();
//
//                                    int rs = -1;
//                                    for (KvRwset.KVRead readList : rws.getReadsList()) {
//                                        rs++;
//
//                                        out("     Namespace %s read set %d key %s  version [%d:%d]", namespace, rs, readList.getKey(),
//                                                readList.getVersion().getBlockNum(), readList.getVersion().getTxNum());
//
//                                        if ("bar".equals(channelId) && blockNumber == 2) {
//                                            if ("example_cc_go".equals(namespace)) {
//                                                if (rs == 0) {
////                                                    assertEquals("a", readList.getKey());
////                                                    assertEquals(1, readList.getVersion().getBlockNum());
////                                                    assertEquals(0, readList.getVersion().getTxNum());
//                                                } else if (rs == 1) {
////                                                    assertEquals("b", readList.getKey());
////                                                    assertEquals(1, readList.getVersion().getBlockNum());
////                                                    assertEquals(0, readList.getVersion().getTxNum());
//                                                } else {
//                                                    fail(format("unexpected readset %d", rs));
//                                                }
//
//                                                TX_EXPECTED.remove("readset1");
//                                            }
//                                        }
//                                    }
//
//                                    rs = -1;
//                                    for (KvRwset.KVWrite writeList : rws.getWritesList()) {
//                                        rs++;
//                                        String valAsString = printableString(new String(writeList.getValue().toByteArray(), "UTF-8"));
//
//                                        out("     Namespace %s write set %d key %s has value '%s' ", namespace, rs,
//                                                writeList.getKey(),
//                                                valAsString);
//
//                                        if ("bar".equals(channelId) && blockNumber == 2) {
//                                            if (rs == 0) {
////                                                assertEquals("a", writeList.getKey());
////                                                assertEquals("400", valAsString);
//                                            } else if (rs == 1) {
////                                                assertEquals("b", writeList.getKey());
////                                                assertEquals("400", valAsString);
//                                            } else {
//                                                fail(format("unexpected writeset %d", rs));
//                                            }
//
//                                            TX_EXPECTED.remove("writeset1");
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//            if (!TX_EXPECTED.isEmpty()) {
//                fail(TX_EXPECTED.get(0));
//            }
//        } catch (InvalidProtocolBufferRuntimeException e) {
//            throw e.getCause();
//        }
//    }



//    public InstalledProposal generateInstalledProposal() throws InvalidArgumentException, ProposalException {
//
//        InstalledProposal installedProposal = new InstalledProposal();
//        SampleOrg sampleOrg = this.getOrg("peerOrg1");
//        Collection<ProposalResponse> responses;
//        Collection<ProposalResponse> successful = new LinkedList<>();
//        Collection<ProposalResponse> failed = new LinkedList<>();
//
//        hfClient.setUserContext(sampleOrg.getPeerAdmin());
//        ChaincodeID chaincodeID = ChaincodeID.newBuilder().setName(config.getChainCodeName())
//                .setVersion(config.getChainCodeVersion())
//                .setPath(config.getChainCodePath()).build();
//
//        InstallProposalRequest installProposalRequest = hfClient.newInstallProposalRequest();
//        installProposalRequest.setChaincodeID(chaincodeID);
//
//
//        installProposalRequest.setChaincodeSourceLocation(new File(config.getFixturesPath()));
//
//
//        installProposalRequest.setChaincodeVersion(config.getChainCodeVersion());
//
//
//        ////////////////////////////
//        // only a client from the same org as the peer can issue an install request
//        int numInstallProposal = 0;
//        //    Set<String> orgs = orgPeers.keySet();
//        //   for (SampleOrg org : testSampleOrgs) {
//
//        Set<Peer> peersFromOrg = sampleOrg.getPeers();
//        numInstallProposal = numInstallProposal + peersFromOrg.size();
//
//        responses = hfClient.sendInstallProposal(installProposalRequest, peersFromOrg);
//
//        for (ProposalResponse response : responses) {
//            if (response.getStatus() == ProposalResponse.Status.SUCCESS) {
//                log.info("Successful install proposal response Txid: {} from peer {}", response.getTransactionID(), response.getPeer().getName());
//                successful.add(response);
//            } else {
//                failed.add(response);
//            }
//        }
//
//        installedProposal = new InstalledProposal();
//        installedProposal.setFailed(failed);
//        installedProposal.setSuccessful(successful);
//        SDKUtils.getProposalConsistencySets(responses);
//
//        return installedProposal;
//    }

//    public void instantiateChainCode(InstalledProposal installedProposal, int delta) throws InvalidArgumentException, IOException, ChaincodeEndorsementPolicyParseException, ProposalException {
//
//        Collection<ProposalResponse> responses;
//
//        Collection<ProposalResponse> successful = new LinkedList<>();
//        Collection<ProposalResponse> failed = new LinkedList<>();
//        ChaincodeID chaincodeID = ChaincodeID.newBuilder().setName(config.getChainCodeName())
//                .setVersion(config.getChainCodeVersion())
//                .setPath(config.getChainCodePath()).build();
//        InstantiateProposalRequest instantiateProposalRequest = hfClient.newInstantiationProposalRequest();
//        instantiateProposalRequest.setProposalWaitTime(Integer.parseInt(config.getProposalWaitTime()));
//        instantiateProposalRequest.setChaincodeID(chaincodeID);
//        instantiateProposalRequest.setFcn("init");
//        instantiateProposalRequest.setArgs(new String[] {"a", "500", "b", "" + (200 + delta)});
//        Map<String, byte[]> tm = new HashMap<>();
//        tm.put("HyperLedgerFabric", "InstantiateProposalRequest:JavaSDK".getBytes(UTF_8));
//        tm.put("method", "InstantiateProposalRequest".getBytes(UTF_8));
//        instantiateProposalRequest.setTransientMap(tm);
//
//            /*
//              policy OR(Org1MSP.member, Org2MSP.member) meaning 1 signature from someone in either Org1 or Org2
//              See README.md Chaincode endorsement policies section for more details.
//            */
//        ChaincodeEndorsementPolicy chaincodeEndorsementPolicy = new ChaincodeEndorsementPolicy();
//        chaincodeEndorsementPolicy.fromYamlFile(new File(config.getFixturesPath() + "/chaincodeendorsementpolicy.yaml"));
//        instantiateProposalRequest.setChaincodeEndorsementPolicy(chaincodeEndorsementPolicy);
//
//        log.info("Sending instantiateProposalRequest to all peers with arguments: a and b set to 100 and {} respectively", "" + (200 + delta));
//        installedProposal.getSuccessful().clear();
//        installedProposal.getFailed().clear();
//
//        responses = channel.sendInstantiationProposal(instantiateProposalRequest, channel.getPeers());
//
//        for (ProposalResponse response : responses) {
//            if (response.isVerified() && response.getStatus() == ProposalResponse.Status.SUCCESS) {
//                successful.add(response);
//                log.info("Succesful instantiate proposal response Txid: {} from peer {}", response.getTransactionID(), response.getPeer().getName());
//            } else {
//                failed.add(response);
//            }
//        }
//        log.info("Received %d instantiate proposal responses. Successful+verified: {} . Failed: {}", responses.size(), successful.size(), failed.size());
//        if (failed.size() > 0) {
//            ProposalResponse first = failed.iterator().next();
//            log.error("Not enough endorsers for instantiate :" + successful.size() + "endorser failed with " + first.getMessage() + ". Was verified:" + first.isVerified());
//        }
//
//        ///////////////
//        /// Send instantiate transaction to orderer
//        log.info("Sending instantiateTransaction to orderer with a and b set to 100 and {} respectively", "" + (200 + delta));
//    }



//    public InstalledProposal generateInstalledProposal() throws InvalidArgumentException, ProposalException {
//
//        InstalledProposal installedProposal = new InstalledProposal();
//        SampleOrg sampleOrg = this.getOrg("peerOrg1");
//        Collection<ProposalResponse> responses;
//        Collection<ProposalResponse> successful = new LinkedList<>();
//        Collection<ProposalResponse> failed = new LinkedList<>();
//
//        hfClient.setUserContext(sampleOrg.getPeerAdmin());
//        ChaincodeID chaincodeID = ChaincodeID.newBuilder().setName(config.getChainCodeName())
//                .setVersion(config.getChainCodeVersion())
//                .setPath(config.getChainCodePath()).build();
//
//        InstallProposalRequest installProposalRequest = hfClient.newInstallProposalRequest();
//        installProposalRequest.setChaincodeID(chaincodeID);
//
//
//        installProposalRequest.setChaincodeSourceLocation(new File(config.getFixturesPath()));
//
//
//        installProposalRequest.setChaincodeVersion(config.getChainCodeVersion());
//
//
//        ////////////////////////////
//        // only a client from the same org as the peer can issue an install request
//        int numInstallProposal = 0;
//        //    Set<String> orgs = orgPeers.keySet();
//        //   for (SampleOrg org : testSampleOrgs) {
//
//        Set<Peer> peersFromOrg = sampleOrg.getPeers();
//        numInstallProposal = numInstallProposal + peersFromOrg.size();
//
//        responses = hfClient.sendInstallProposal(installProposalRequest, peersFromOrg);
//
//        for (ProposalResponse response : responses) {
//            if (response.getStatus() == ProposalResponse.Status.SUCCESS) {
//                log.info("Successful install proposal response Txid: {} from peer {}", response.getTransactionID(), response.getPeer().getName());
//                successful.add(response);
//            } else {
//                failed.add(response);
//            }
//        }
//
//        installedProposal = new InstalledProposal();
//        installedProposal.setFailed(failed);
//        installedProposal.setSuccessful(successful);
//        SDKUtils.getProposalConsistencySets(responses);
//
//        return installedProposal;
//    }



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
            record.setUserName(userId);
            record.setBalance(balance);
        }


        return record;
    }


//    public Record testQuery(HFClient client, ChaincodeID chaincodeID) {
//        Collection<Peer> peers = channel.getPeers();
//        List<Query.ChaincodeInfo> chaincodeInfos = null;

//        if(null != peers && !peers.isEmpty()) {
//
//            try {
////                channel.
//                chaincodeInfos = channel.queryInstantiatedChaincodes((Peer)peers.toArray()[0]);
//            } catch (InvalidArgumentException | ProposalException e) {
//                e.printStackTrace();
//            }
//        }

//        Channel newChannel = null;
//        try {
//            newChannel = reconstructChannel(fabricConfigManager.getFabricConfig().getFooChannelName(), hfClient, getOrg("peerOrg1"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


//        if(null != peers && !peers.isEmpty()) {
//
//            try {
//                chaincodeInfos = newChannel.queryInstantiatedChaincodes((Peer)peers.toArray()[0]);
//            } catch (InvalidArgumentException | ProposalException e) {
//                e.printStackTrace();
//            }
//        }

//        if(null != peers && !peers.isEmpty()) {
//
//            try {
//                chaincodeInfos = channel.queryInstantiatedChaincodes((Peer)peers.toArray()[0]);
//            } catch (InvalidArgumentException | ProposalException e) {
//                e.printStackTrace();
//            }
//        }


//
//        QueryByChaincodeRequest queryByChaincodeRequest = client.newQueryProposalRequest();
//        queryByChaincodeRequest.setArgs(new String[] {"query", "b"});
//        queryByChaincodeRequest.setFcn("invoke");
//        queryByChaincodeRequest.setChaincodeID(chaincodeID);
//
//        Collection<ProposalResponse> queryProposals;
//
//        try {
//            queryProposals = channel.queryByChaincode(queryByChaincodeRequest);
//        } catch (Exception e) {
//            throw new CompletionException(e);
//        }

//        try {
//            queryProposals = newChannel.queryByChaincode(queryByChaincodeRequest);
//        } catch (Exception e) {
//            throw new CompletionException(e);
//        }
//
//        for (ProposalResponse proposalResponse : queryProposals) {
//            if (!proposalResponse.isVerified() || proposalResponse.getStatus() != ChaincodeResponse.Status.SUCCESS) {
//                fail("Failed query proposal from peer " + proposalResponse.getPeer().getName() + " status: " + proposalResponse.getStatus() +
//                        ". Messages: " + proposalResponse.getMessage()
//                        + ". Was verified : " + proposalResponse.isVerified());
//            } else {
//                String payload = proposalResponse.getProposalResponse().getResponse().getPayload().toStringUtf8();
//                out("Query payload of b from peer %s returned %s", proposalResponse.getPeer().getName(), payload);
//            }
//        }
//
//
//        QueryByChaincodeRequest queryByChaincodeRequesta = client.newQueryProposalRequest();
//        queryByChaincodeRequesta.setArgs(new String[] {"query", "a"});
//        queryByChaincodeRequesta.setFcn("invoke");
//        queryByChaincodeRequesta.setChaincodeID(chaincodeID);
//
//        Collection<ProposalResponse> queryProposalsa;
//
//        try {
//            queryProposalsa = channel.queryByChaincode(queryByChaincodeRequesta);
//        } catch (Exception e) {
//            throw new CompletionException(e);
//        }
//
//        for (ProposalResponse proposalResponse : queryProposalsa) {
//            if (!proposalResponse.isVerified() || proposalResponse.getStatus() != ChaincodeResponse.Status.SUCCESS) {
//                fail("Failed query proposal from peer " + proposalResponse.getPeer().getName() + " status: " + proposalResponse.getStatus() +
//                        ". Messages: " + proposalResponse.getMessage()
//                        + ". Was verified : " + proposalResponse.isVerified());
//            } else {
//                String payload = proposalResponse.getProposalResponse().getResponse().getPayload().toStringUtf8();
//                out("Query payload of a from peer %s returned %s", proposalResponse.getPeer().getName(), payload);
//            }
//        }
//
//        return null;
//    }
//    private Channel reconstructChannel(String name, HFClient client, SampleOrg sampleOrg) throws Exception {
//
//        client.setUserContext(sampleOrg.getPeerAdmin());
//        Channel newChannel = client.newChannel(name);
//
//        for (String orderName : sampleOrg.getOrdererNames()) {
//            newChannel.addOrderer(client.newOrderer(orderName, sampleOrg.getOrdererLocation(orderName),
//                    fabricConfigManager.getOrdererProperties(orderName)));
//        }
//
//        for (String peerName : sampleOrg.getPeerNames()) {
//            String peerLocation = sampleOrg.getPeerLocation(peerName);
//            Peer peer = client.newPeer(peerName, peerLocation, fabricConfigManager.getPeerProperties(peerName));
//
//            //Query the actual peer for which channels it belongs to and check it belongs to this channel
//            Set<String> channels = client.queryChannels(peer);
//            if (!channels.contains(name)) {
//                throw new AssertionError(format("Peer %s does not appear to belong to channel %s", peerName, name));
//            }
//
//            newChannel.addPeer(peer);
//            sampleOrg.addPeer(peer);
//        }
//
//        for (String eventHubName : sampleOrg.getEventHubNames()) {
//            EventHub eventHub = client.newEventHub(eventHubName, sampleOrg.getEventHubLocation(eventHubName),
//                    fabricConfigManager.getEventHubProperties(eventHubName));
//            newChannel.addEventHub(eventHub);
//        }
//
//        newChannel.initialize();
//
//        return newChannel;
//    }



}
