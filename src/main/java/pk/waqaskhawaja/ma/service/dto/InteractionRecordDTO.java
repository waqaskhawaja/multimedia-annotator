package pk.waqaskhawaja.ma.service.dto;
import pk.waqaskhawaja.ma.domain.InteractionType;

import java.io.Serializable;


public class InteractionRecordDTO implements Serializable {

    private Long id;

    private Integer duration;

    private String text;

    private String sourceId;

    private Integer time;

    private InteractionType interactionType;

    public InteractionRecordDTO() {
    }

    public InteractionRecordDTO(Long id, Integer duration, String text, String sourceId, Integer time, InteractionType interactionType) {
        this.id = id;
        this.duration = duration;
        this.text = text;
        this.sourceId = sourceId;
        this.time = time;
        this.interactionType = interactionType;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public InteractionType getInteractionType() {
        return interactionType;
    }

    public void setInteractionType(InteractionType interactionType) {
        this.interactionType = interactionType;
    }

    @Override
    public String toString() {
        return "InteractionRecordDTO{" +
            "id=" + id +
            ", duration=" + duration +
            ", text='" + text + '\'' +
            ", sourceId='" + sourceId + '\'' +
            ", time=" + time +
            ", interactionType=" + interactionType +
            '}';
    }
}
