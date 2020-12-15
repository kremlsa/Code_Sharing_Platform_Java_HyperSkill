package platform;

import javax.persistence.*;

@Entity
@SequenceGenerator(name="sequence", initialValue=1)
public class CodeDB {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="sequence")
    private Long id;

    private String uuid;

    private Long views = 0L;

    private Long time = 0L;

    private String date;

    private Long creationTime;

    private String code;

    public void setCreationTime(Long creationTime) {
        this.creationTime = creationTime;
    }

    public Long getCreationTime() {
        return creationTime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Long getTime() {
        return time;
    }

    public void setViews(Long views) {
        this.views = views;
    }

    public Long getViews() {
        return views;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }
}
