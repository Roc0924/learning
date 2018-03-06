package learning.chaincode.demo.dtos;

import lombok.Data;

import java.io.Serializable;

/**
 * Create with IntelliJ IDEA
 * Author               : wangzhenpeng
 * Date                 : 2018/3/6
 * Time                 : 17:29
 * Description          :
 */
@Data
public class RecordJson implements Serializable{
    private static final long serialVersionUID = 1951349459328035084L;
    private String userId;
    private Integer cash;
    private Integer anticipated;
    private Integer frozen;

}
