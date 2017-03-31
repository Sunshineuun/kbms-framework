package com.winning.domain.kbms.engine.data;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;


/**
 * 
 * @标题: OBaseDaomain.java
 * @包名: net.newwinning.knowledgeBase.ckb.domain
 * @描述: 
 * @作者: s_lei
 * @时间: Apr 21, 2014 2:33:56 PM
 * @版权: (c) 2014, 卫宁软件科技有限公司
 */
@MappedSuperclass
public class BaseDomain
    implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;


    @Id
    @GeneratedValue
    @Column(name = "ID", nullable = false)
    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }
}
