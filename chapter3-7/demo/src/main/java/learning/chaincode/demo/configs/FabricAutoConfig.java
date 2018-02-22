package learning.chaincode.demo.configs;

import learning.chaincode.demo.dtos.InstallProposalResponse;
import learning.chaincode.demo.dtos.SampleOrg;
import learning.chaincode.demo.dtos.SampleStore;
import learning.chaincode.demo.dtos.SampleUser;
import learning.chaincode.demo.utils.Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Create with IntelliJ IDEA
 * Author               : wangzhenpeng
 * Date                 : 2018/2/13
 * Time                 : 14:18
 * Description          :
 */
@Slf4j
@Configuration
public class FabricAutoConfig {

//    Log log = LogFactory.getLog(this.getClass());

    @Autowired
    @Bean(name = "chainCodeID")
    ChaincodeID chaincodeID(FabricConfig config) {
        return ChaincodeID.newBuilder().setName(config.getChainCodeName())
                .setVersion(config.getChainCodeVersion())
                .setPath(config.getChainCodePath()).build();
    }


    @Autowired
    @Bean(name = "hfClient")
    public HFClient hfClient(FabricConfigManager fabricConfigManager) throws Exception {
        HFClient client = HFClient.createNewInstance();

        try {
            client.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
        } catch (CryptoException e) {
            e.printStackTrace();
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
        }


        Collection<SampleOrg> sampleOrgs = fabricConfigManager.getIntegrationSampleOrgs();
        for (SampleOrg sampleOrg : sampleOrgs) {
            sampleOrg.setCAClient(HFCAClient.createNewInstance(sampleOrg.getCALocation(), sampleOrg.getCAProperties()));
        }
        File sampleStoreFile = new File(System.getProperty("java.io.tmpdir") + "/HFCSampletest.properties");
        if (sampleStoreFile.exists()) { //For testing start fresh
            sampleStoreFile.delete();
        }

        final SampleStore sampleStore = new SampleStore(sampleStoreFile);


        for (SampleOrg sampleOrg : sampleOrgs) {

            HFCAClient ca = sampleOrg.getCAClient();
            final String orgName = sampleOrg.getName();
            final String mspid = sampleOrg.getMSPID();
            ca.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
            SampleUser admin = sampleStore.getMember(fabricConfigManager.getFabricConfig().getAdminName(), orgName);
            if (!admin.isEnrolled()) {  //Preregistered admin only needs to be enrolled with Fabric caClient.
                admin.setEnrollment(ca.enroll(admin.getName(), "adminpw"));
                admin.setMspId(mspid);
            }

            sampleOrg.setAdmin(admin); // The admin of this org --

            SampleUser user = sampleStore.getMember(fabricConfigManager.getFabricConfig().getUser1Name(), sampleOrg.getName());
            if (!user.isRegistered()) {  // users need to be registered AND enrolled
                RegistrationRequest rr = new RegistrationRequest(user.getName(), "org1.department1");
                user.setEnrollmentSecret(ca.register(rr, admin));
            }
            if (!user.isEnrolled()) {
                user.setEnrollment(ca.enroll(user.getName(), user.getEnrollmentSecret()));
                user.setMspId(mspid);
            }
            sampleOrg.addUser(user); //Remember user belongs to this Org

            final String sampleOrgName = sampleOrg.getName();
            final String sampleOrgDomainName = sampleOrg.getDomainName();

            // src/test/fixture/sdkintegration/e2e-2Orgs/channel/crypto-config/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp/keystore/

            SampleUser peerOrgAdmin = sampleStore.getMember(sampleOrgName + "Admin", sampleOrgName, sampleOrg.getMSPID(),
                    Util.findFileSk(Paths.get("demo\\target\\classes", fabricConfigManager.getFabricConfig().getChannelPath(),
                            fabricConfigManager.getFabricConfig().getPeerOrganizations(),
                            sampleOrgDomainName, format("/users/Admin@%s/msp/keystore", sampleOrgDomainName)).toFile()),
                    Paths.get("demo\\target\\classes", fabricConfigManager.getFabricConfig().getChannelPath(),
                            fabricConfigManager.getFabricConfig().getPeerOrganizations(), sampleOrgDomainName,
                            format("/users/Admin@%s/msp/signcerts/Admin@%s-cert.pem", sampleOrgDomainName, sampleOrgDomainName)).toFile());

            sampleOrg.setPeerAdmin(peerOrgAdmin); //A special user that can create channels, join peers and install chaincode

        }

        return client;
    }


//    @ConditionalOnBean(learning.chaincode.demo.configs.FabricConfigManager.class)
    @Bean(name = "channel")
    @Autowired
    public Channel channel(FabricConfigManager fabricConfigManager, HFClient client, FabricConfig config) throws InvalidArgumentException, TransactionException, ProposalException, IOException {

        SampleOrg sampleOrg = fabricConfigManager.getIntegrationTestsSampleOrg("peerOrg1");
        String name = config.getFooChannelName();
        client.setUserContext(sampleOrg.getPeerAdmin());

        Collection<Orderer> orderers = new LinkedList<>();

        for (String orderName : sampleOrg.getOrdererNames()) {

            Properties ordererProperties = fabricConfigManager.getOrdererProperties(orderName);

            //example of setting keepAlive to avoid timeouts on inactive http2 connections.
            // Under 5 minutes would require changes to server side to accept faster ping rates.
            ordererProperties.put("grpc.NettyChannelBuilderOption.keepAliveTime", new Object[] {5L, TimeUnit.MINUTES});
            ordererProperties.put("grpc.NettyChannelBuilderOption.keepAliveTimeout", new Object[] {8L, TimeUnit.SECONDS});

            orderers.add(client.newOrderer(orderName, sampleOrg.getOrdererLocation(orderName),
                    ordererProperties));
        }

        //Just pick the first orderer in the list to create the channel.

        Orderer anOrderer = orderers.iterator().next();
        orderers.remove(anOrderer);

        ChannelConfiguration channelConfiguration = new ChannelConfiguration(new File(config.getFixturesPath() + "/e2e-2Orgs/channel/" + name + ".tx"));

        //Create channel that has only one signer that is this orgs peer admin. If channel creation policy needed more signature they would need to be added too.
        Channel newChannel = client.newChannel(name, anOrderer, channelConfiguration, client.getChannelConfigurationSignature(channelConfiguration, sampleOrg.getPeerAdmin()));


        for (String peerName : sampleOrg.getPeerNames()) {
            String peerLocation = sampleOrg.getPeerLocation(peerName);

            Properties peerProperties = fabricConfigManager.getPeerProperties(peerName); //test properties for peer.. if any.
            if (peerProperties == null) {
                peerProperties = new Properties();
            }
            //Example of setting specific options on grpc's NettyChannelBuilder
            peerProperties.put("grpc.NettyChannelBuilderOption.maxInboundMessageSize", 9000000);

            Peer peer = client.newPeer(peerName, peerLocation, peerProperties);
            newChannel.joinPeer(peer);
            sampleOrg.addPeer(peer);
        }

        for (Orderer orderer : orderers) { //add remaining orderers if any.
            newChannel.addOrderer(orderer);
        }

        for (String eventHubName : sampleOrg.getEventHubNames()) {

            final Properties eventHubProperties = fabricConfigManager.getEventHubProperties(eventHubName);

            eventHubProperties.put("grpc.NettyChannelBuilderOption.keepAliveTime", new Object[] {5L, TimeUnit.MINUTES});
            eventHubProperties.put("grpc.NettyChannconfigelBuilderOption.keepAliveTimeout", new Object[] {8L, TimeUnit.SECONDS});

            EventHub eventHub = client.newEventHub(eventHubName, sampleOrg.getEventHubLocation(eventHubName),
                    eventHubProperties);
            newChannel.addEventHub(eventHub);
        }

        newChannel.initialize();


        return newChannel;
    }


    @Bean(name = "installedProposal")
    @Autowired
    InstalledProposal installedProposal(FabricConfigManager fabricConfigManager, HFClient client, Channel channel,
                                        FabricConfig config, ChaincodeID chaincodeID) {
        boolean installChaincode = true;
        int delta = 0;
        InstalledProposal installedProposal = null;
        SampleOrg sampleOrg = fabricConfigManager.getIntegrationTestsSampleOrg("peerOrg1");
        try {

            final String channelName = channel.getName();
            boolean isFooChain = config.getFooChannelName().equals(channelName);
            channel.setTransactionWaitTime(fabricConfigManager.getTransactionWaitTime());
            channel.setDeployWaitTime(Integer.parseInt(config.getDeployWaitTime()));

            Collection<Peer> channelPeers = channel.getPeers();
            Collection<Orderer> orderers = channel.getOrderers();
            Collection<ProposalResponse> responses;
            Collection<ProposalResponse> successful = new LinkedList<>();
            Collection<ProposalResponse> failed = new LinkedList<>();


            if (installChaincode) {
                ////////////////////////////
                // Install Proposal Request
                //

                client.setUserContext(sampleOrg.getPeerAdmin());


                InstallProposalRequest installProposalRequest = client.newInstallProposalRequest();
                installProposalRequest.setChaincodeID(chaincodeID);

                if (isFooChain) {
                    // on foo chain install from directory.

                    ////For GO language and serving just a single user, chaincodeSource is mostly likely the users GOPATH
//                    installProposalRequest.setChaincodeSourceLocation(new File(config.getFixturesPath() + "/sdkintegration/gocc/sample1"));
                    installProposalRequest.setChaincodeSourceLocation(new File(config.getFixturesPath()));
                } else {
                    // On bar chain install from an input stream.

                    installProposalRequest.setChaincodeInputStream(Util.generateTarGzInputStream(
//                            (Paths.get(config.getFixturesPath(), "/sdkintegration/gocc/sample1", "src", config.getChainCodePath()).toFile()),
                            (Paths.get(config.getFixturesPath(), config.getChainCodePath()).toFile()),
                            Paths.get(config.getChainCodePath()).toString()));

                }

                installProposalRequest.setChaincodeVersion(config.getChainCodeVersion());


                ////////////////////////////
                // only a client from the same org as the peer can issue an install request
                int numInstallProposal = 0;
                //    Set<String> orgs = orgPeers.keySet();
                //   for (SampleOrg org : testSampleOrgs) {

                Set<Peer> peersFromOrg = sampleOrg.getPeers();
                numInstallProposal = numInstallProposal + peersFromOrg.size();
                responses = client.sendInstallProposal(installProposalRequest, peersFromOrg);

                for (ProposalResponse response : responses) {
                    if (response.getStatus() == ProposalResponse.Status.SUCCESS) {
                        log.info("Successful install proposal response Txid: {} from peer {}", response.getTransactionID(), response.getPeer().getName());
                        successful.add(response);
                    } else {
                        failed.add(response);
                    }
                }



                SDKUtils.getProposalConsistencySets(responses);
                //   }

                log.info("Received %d install proposal responses. Successful+verified: {} . Failed: {}", numInstallProposal, successful.size(), failed.size());

                if (failed.size() > 0) {
                    ProposalResponse first = failed.iterator().next();
                    log.info("Not enough endorsers for install :" + successful.size() + ".  " + first.getMessage());
                }
            }

            //   client.setUserContext(sampleOrg.getUser(TEST_ADMIN_NAME));
            //  final ChaincodeID chaincodeID = firstInstallProposalResponse.getChaincodeID();
            // Note installing chaincode does not require transaction no need to
            // send to Orderers

            ///////////////
            //// Instantiate chaincode.
            InstantiateProposalRequest instantiateProposalRequest = client.newInstantiationProposalRequest();
            instantiateProposalRequest.setProposalWaitTime(Integer.parseInt(config.getProposalWaitTime()));
            instantiateProposalRequest.setChaincodeID(chaincodeID);
            instantiateProposalRequest.setFcn("init");
            instantiateProposalRequest.setArgs(new String[] {"a", "500", "b", "" + (200 + delta)});
            Map<String, byte[]> tm = new HashMap<>();
            tm.put("HyperLedgerFabric", "InstantiateProposalRequest:JavaSDK".getBytes(UTF_8));
            tm.put("method", "InstantiateProposalRequest".getBytes(UTF_8));
            instantiateProposalRequest.setTransientMap(tm);

            /*
              policy OR(Org1MSP.member, Org2MSP.member) meaning 1 signature from someone in either Org1 or Org2
              See README.md Chaincode endorsement policies section for more details.
            */
            ChaincodeEndorsementPolicy chaincodeEndorsementPolicy = new ChaincodeEndorsementPolicy();
            chaincodeEndorsementPolicy.fromYamlFile(new File(config.getFixturesPath() + "/chaincodeendorsementpolicy.yaml"));
            instantiateProposalRequest.setChaincodeEndorsementPolicy(chaincodeEndorsementPolicy);

            log.info("Sending instantiateProposalRequest to all peers with arguments: a and b set to 100 and {} respectively", "" + (200 + delta));
            successful.clear();
            failed.clear();

            if (isFooChain) {  //Send responses both ways with specifying peers and by using those on the channel.
                responses = channel.sendInstantiationProposal(instantiateProposalRequest, channel.getPeers());
            } else {
                responses = channel.sendInstantiationProposal(instantiateProposalRequest);
            }
            for (ProposalResponse response : responses) {
                if (response.isVerified() && response.getStatus() == ProposalResponse.Status.SUCCESS) {
                    successful.add(response);
                    log.info("Succesful instantiate proposal response Txid: {} from peer {}", response.getTransactionID(), response.getPeer().getName());
                } else {
                    failed.add(response);
                }
            }

            installedProposal = new InstalledProposal();
            installedProposal.setFailed(failed);
            installedProposal.setSuccessful(successful);
            log.info("Received %d instantiate proposal responses. Successful+verified: {} . Failed: {}", responses.size(), successful.size(), failed.size());
            if (failed.size() > 0) {
                ProposalResponse first = failed.iterator().next();
                log.error("Not enough endorsers for instantiate :" + successful.size() + "endorser failed with " + first.getMessage() + ". Was verified:" + first.isVerified());
            }

            ///////////////
            /// Send instantiate transaction to orderer
            log.info("Sending instantiateTransaction to orderer with a and b set to 100 and {} respectively", "" + (200 + delta));
            channel.sendTransaction(successful, orderers);

        } catch (Exception e) {
            log.info("Caught an exception running channel {}", channel.getName());
            e.printStackTrace();
            log.error("Test failed with error : " + e.getMessage());
        }



        return installedProposal;

    }



}
