package com.github.madz0.jerseytest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@MappedSuperclass
@Audited
@EntityListeners(BaseModel.AuditListener.class)
public abstract class BaseModel implements Serializable {
    public final static int MAX_SUPPORTED_MONEY_FRACTION = 4;
    public final static int MAX_SUPPORTED_MONEY = 19;
    public final static int SUPPORTED_MONEY_SAFE_GUARD = 4;

    @JsonIgnore
    @Transient
    private transient boolean roundMoney = false;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    protected Long id;

    @Version
    @NotNull(groups = Update.class, message = "version is mandatory for update")
    protected Long version;

    @Temporal(TemporalType.TIMESTAMP)
    protected Date createdDate;
    protected Time createdTime;

    @Temporal(TemporalType.TIMESTAMP)
    protected Date modifiedDate;
    protected Time modifiedTime;

    public Date getCreatedDate() {
        return createdDate != null ? new Date(createdDate.getTime()) : null;
    }

    public Date getModifiedDate() {
        return modifiedDate != null ? new Date(modifiedDate.getTime()) : null;
    }

    public void setCreatedDate(Date date) {
        if (date != null) {
            this.createdDate = new Date(date.getTime());
        }
    }

    public void setModifiedDate(Date date) {
        if (date != null) {
            this.modifiedDate = new Date(date.getTime());
        }
    }

    public interface Update {
    }

    public static class AuditListener {
        @PrePersist
        void onPrePersist(Object o) {
            if (o instanceof BaseModel) {
                Date d = new Date();
                BaseModel obj = (BaseModel) o;
                if (obj.getCreatedDate() == null) {
                    obj.setCreatedDate(d);
                    obj.setModifiedDate(d);
                }
                if (obj.getCreatedTime() == null) {
                    obj.setCreatedTime(new Time(d.getTime()));
                    obj.setModifiedTime(new Time(d.getTime()));
                }
            }
        }

        @PreUpdate
        void onPreUpdate(Object o) {
            if (o instanceof BaseModel) {
                BaseModel obj = (BaseModel) o;
                Date d = new Date();
                if (obj.getModifiedDate() == null) {
                    obj.setModifiedDate(d);
                }
                if (obj.getModifiedTime() == null) {
                    obj.setModifiedTime(new Time(d.getTime()));
                }
            }
        }
    }

}
