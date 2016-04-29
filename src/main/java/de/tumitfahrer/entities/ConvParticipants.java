package de.tumitfahrer.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "CONV_PARTICIPANTS")
public class ConvParticipants implements EntityInterface {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @SequenceGenerator(name = "seq", sequenceName = "conv_participants_id_seq", allocationSize = 1)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @PrimaryKeyJoinColumn
    @Column(name = "CONV_ID", nullable = false)
    private Integer convId;

    @PrimaryKeyJoinColumn
    @Column(name = "USER_ID", nullable = false)
    private Integer userId;

    @Column(name = "CREATED_AT", nullable = false)
    private Date createdAt;

    @Column(name = "UPDATED_AT", nullable = false)
    private Date updatedAt;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getConvId() {
        return convId;
    }

    public void setConvId(Integer convId) {
        this.convId = convId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
