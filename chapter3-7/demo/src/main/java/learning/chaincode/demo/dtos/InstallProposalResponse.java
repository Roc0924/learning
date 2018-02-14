package learning.chaincode.demo.dtos;

import lombok.Data;
import org.hyperledger.fabric.sdk.ProposalResponse;

import java.util.Collection;

/**
 * Create with IntelliJ IDEA
 * Author               : wangzhenpeng
 * Date                 : 2018/2/13
 * Time                 : 12:20
 * Description          :
 */
@Data
public class InstallProposalResponse {
    private Collection<ProposalResponse> successful;
    private Collection<ProposalResponse> failed;

}
